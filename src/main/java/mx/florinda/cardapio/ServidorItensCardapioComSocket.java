package mx.florinda.cardapio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Servidor HTTP manual usando ServerSocket
 *
 * Esta classe demonstra como criar um servidor HTTP básico usando ServerSocket,
 * lendo os dados brutos do cliente byte a byte através do InputStream.
 *
 * O servidor:
 * - Abre a porta 8000
 * - Aceita conexões de clientes
 * - Lê os bytes do InputStream
 * - Armazena em um StringBuilder
 * - Exibe os dados recebidos
 */
public class ServidorItensCardapioComSocket {

    private static final int PORTA = 8000;
    private static final Database database = new Database();
    private static final Map<Long, ItemCardapio> itensCardapio = inicializarCardapio();
    private static final AtomicLong proximoId = new AtomicLong(6L); // Próximo ID disponível

    /**
     * Inicializa o mapa de cardápio com os itens da Database
     */
    private static Map<Long, ItemCardapio> inicializarCardapio() {
        mx.florinda.cardapio.ArrayList arrayListHelper = new mx.florinda.cardapio.ArrayList();
        Map<Long, ItemCardapio> mapa = arrayListHelper.mapaDeItensCardapio();

        // Popula o mapa com os itens iniciais da database
        List<ItemCardapio> itensIniciais = database.listaDeItensCardapio();
        for (ItemCardapio item : itensIniciais) {
            mapa.put(item.id(), item);
        }

        return mapa;
    }

    public static void main(String[] args) {
        System.out.println("=== Servidor HTTP com ServerSocket ===");
        System.out.println("Iniciando servidor na porta " + PORTA + "...\n");

        // limitar o numero de threads para 50
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORTA);
            // shutdown hook para fechar o server socket e parar o executor quando o processo finalizar
            final ServerSocket finalServerSocket = serverSocket;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown hook: fechando servidor e interrompendo executor...");
                try {
                    finalServerSocket.close();
                } catch (IOException e) {
                    // ignore
                }
                executorService.shutdownNow();
            }));

            System.out.println("Servidor aguardando conexões em http://localhost:" + PORTA);
            System.out.println("Acesse http://localhost:8000/itens-cardapio no navegador");
            System.out.println("Pressione Ctrl+C para parar o servidor\n");

            // Use AtomicInteger para contar requisições de forma thread-safe
            AtomicInteger numeroRequisicao = new AtomicInteger(0);

            // Loop para aceitar múltiplas requisições enquanto o serverSocket estiver aberto
            while (!serverSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
                try {
                    // Aceita conexão do cliente (bloqueia até receber uma conexão)
                    Socket clientSocket = serverSocket.accept();

                    // Incrementa o contador e passa o número da requisição para a thread
                    int reqNum = numeroRequisicao.incrementAndGet();

                    try {
                        // Submete a tarefa ao executor. Se estiver em shutdown, receberemos RejectedExecutionException
                        executorService.execute(() -> trataRequisicao(reqNum, clientSocket));
                    } catch (RejectedExecutionException rex) {
                        System.err.println("Executor rejeitou a tarefa: " + rex.getMessage() + ". Fechando socket do cliente.");
                        try {
                            clientSocket.close();
                        } catch (IOException ignore) {
                        }
                    }

                } catch (SocketException se) {
                    // Pode acontecer quando o ServerSocket for fechado (ex: shutdown hook)
                    if (serverSocket.isClosed()) {
                        System.out.println("Server socket fechado, saindo do loop de aceitação.");
                        break;
                    } else {
                        System.err.println("SocketException durante accept(): " + se.getMessage());
                        break;
                    }
                } catch (IOException e) {
                    System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Não foi possível iniciar o ServerSocket na porta " + PORTA + ": " + e.getMessage());
        } finally {
            // Garante que o ServerSocket esteja fechado
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException ignore) {
                }
            }

            // tenta um shutdown gracioso do executor
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println("Executor não terminou em 5s; forçando shutdown...");
                    executorService.shutdownNow();
                }
            } catch (InterruptedException ie) {
                // restaura o estado de interrupção e força shutdown
                Thread.currentThread().interrupt();
                executorService.shutdownNow();
            }
        }
    }

    // Alterado para void pois o valor retornado não é usado pelo chamador
    private static void trataRequisicao(int numeroRequisicao, Socket clientSocket) {
        // Usa try-with-resources para garantir fechamento do socket após processamento
        try (Socket socket = clientSocket) {
            System.out.println("\n========================================");
            System.out.println("Processando requisição #" + numeroRequisicao + "...");
            System.out.println("========================================");

            System.out.println("Cliente conectado: " + socket.getInetAddress());

            // Define um timeout curto para leitura para evitar bloqueio indefinido
            try {
                socket.setSoTimeout(5000); // 5 segundos
            } catch (IOException ignored) {
                // Se não for possível configurar, continuamos sem timeout
            }

            // Processa a requisição
            processarRequisicao(socket);

            // socket será fechado automaticamente pelo try-with-resources
            System.out.println("Conexão fechada. Pronto para próxima requisição.");
        } catch (IOException e) {
            System.err.println("Erro ao tratar requisição #" + numeroRequisicao + ": " + e.getMessage());
        }
    }

    /**
     * Processa a requisição HTTP lendo os headers via BufferedReader
     * e enviando resposta HTTP manual através do OutputStream
     */
    private static void processarRequisicao(Socket clientSocket) {
        try (
            // Obtém o InputStream do cliente e um reader para ler linhas
            InputStream inputStream = clientSocket.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);

            // Obtém o OutputStream para enviar resposta ao cliente
            OutputStream outputStream = clientSocket.getOutputStream();

            // PrintStream para facilitar o envio de dados (usa Charset overload)
            PrintStream printStream = new PrintStream(outputStream, true, StandardCharsets.UTF_8)
        ) {
            System.out.println("\n--- Lendo requisição (cabecalhos) ---");

            // Lê a primeira linha (request line) que contém método, path e versão HTTP
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                System.out.println("Nenhuma requisição recebida ou conexão fechada pelo cliente.");
                enviarResposta(printStream, 400, "Bad Request", "{}");
                return;
            }

            System.out.println("Request line: " + requestLine);

            // Parse da request line: "GET /itens-cardapio HTTP/1.1"
            String[] parts = requestLine.split(" ");
            if (parts.length < 3) {
                enviarResposta(printStream, 400, "Bad Request", "{\"erro\": \"Request line inválida\"}");
                return;
            }

            String metodo = parts[0];
            String path = parts[1];

            // Lê o restante dos headers
            StringBuilder headersBuilder = new StringBuilder();
            String line;
            int contentLength = 0;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                headersBuilder.append(line).append("\n");
                System.out.println(line);

                // Captura Content-Length para requisições POST
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.substring(15).trim());
                }
            }

            // Lê o body se for POST e tiver Content-Length
            String body = "";
            if ("POST".equalsIgnoreCase(metodo) && contentLength > 0) {
                char[] bodyChars = new char[contentLength];
                int read = reader.read(bodyChars, 0, contentLength);
                body = new String(bodyChars, 0, read);
                System.out.println("Body recebido: " + body);
            }

            // --- ROTEAMENTO DE ENDPOINTS ---
            System.out.println("\n--- Processando endpoint: " + metodo + " " + path + " ---");

            if ("GET".equalsIgnoreCase(metodo) && "/itens-cardapio".equals(path)) {
                handleGetItensCardapio(printStream);
            } else if ("GET".equalsIgnoreCase(metodo) && "/itens-cardapio/total".equals(path)) {
                handleGetTotal(printStream);
            } else if ("GET".equalsIgnoreCase(metodo) && path.startsWith("/itens-cardapio/") && !path.equals("/itens-cardapio/total")) {
                // GET /itens-cardapio/{id} - Busca item específico por ID
                String idStr = path.substring("/itens-cardapio/".length());
                handleGetItemPorId(printStream, idStr);
            } else if ("POST".equalsIgnoreCase(metodo) && "/itens-cardapio".equals(path)) {
                handlePostItemCardapio(printStream, body);
            } else if ("DELETE".equalsIgnoreCase(metodo) && path.startsWith("/itens-cardapio/")) {
                // Extrai o ID do path: /itens-cardapio/{id}
                String idStr = path.substring("/itens-cardapio/".length());
                handleDeleteItemCardapio(printStream, idStr);
            } else {
                // Endpoint não encontrado
                enviarResposta(printStream, 404, "Not Found",
                    "{\"erro\": \"Endpoint não encontrado\", \"path\": \"" + path + "\"}");
            }

        } catch (SocketTimeoutException e) {
            System.err.println("Timeout ao ler requisição: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao processar requisição: " + e.getMessage());
        }
    }

    /**
     * GET /itens-cardapio - Lista todos os itens do cardápio
     */
    private static void handleGetItensCardapio(PrintStream printStream) {
        // Converte os valores do mapa para lista e serializa como JSON
        List<ItemCardapio> itens = new ArrayList<>(itensCardapio.values());
        String json = itensCardapioParaJson(itens);
        enviarResposta(printStream, 200, "OK", json);
        System.out.println("Retornados " + itensCardapio.size() + " itens do cardápio");
    }

    /**
     * GET /itens-cardapio/total - Retorna a quantidade de itens do cardápio
     */
    private static void handleGetTotal(PrintStream printStream) {
        // ConcurrentSkipListMap.size() é thread-safe
        int total = itensCardapio.size();
        String json = "{\"total\": " + total + "}";
        enviarResposta(printStream, 200, "OK", json);
        System.out.println("Total de itens: " + total);
    }

    /**
     * GET /itens-cardapio/{id} - Busca um item específico por ID
     * Aproveita a busca O(log n) do ConcurrentSkipListMap
     */
    private static void handleGetItemPorId(PrintStream printStream, String idStr) {
        try {
            // Converte o ID de String para Long
            Long id = Long.parseLong(idStr);

            // Busca direta no mapa - O(log n) - muito mais rápido que lista!
            ItemCardapio item = itensCardapio.get(id);

            if (item != null) {
                String json = itemCardapioParaJson(item);
                enviarResposta(printStream, 200, "OK", json);
                System.out.println("Item encontrado: " + item.nome() + " (ID: " + id + ")");
            } else {
                enviarResposta(printStream, 404, "Not Found",
                    "{\"erro\": \"Item não encontrado\", \"id\": " + id + "}");
                System.out.println("Item não encontrado com ID: " + id);
            }

        } catch (NumberFormatException e) {
            System.err.println("ID inválido: " + idStr);
            enviarResposta(printStream, 400, "Bad Request",
                "{\"erro\": \"ID inválido\", \"valor\": \"" + idStr + "\"}");
        }
    }

    /**
     * POST /itens-cardapio - Adiciona um item do cardápio recebido em JSON
     */
    private static void handlePostItemCardapio(PrintStream printStream, String body) {
        try {
            // Parse simples do JSON recebido
            ItemCardapio novoItem = parseJsonParaItemCardapio(body);

            // Adiciona no mapa usando o ID como chave - ConcurrentSkipListMap é thread-safe
            itensCardapio.put(novoItem.id(), novoItem);
            System.out.println("Item adicionado com sucesso: " + novoItem.nome() + " (ID: " + novoItem.id() + ")");

            // Retorna o item criado
            String json = itemCardapioParaJson(novoItem);
            enviarResposta(printStream, 201, "Created", json);

        } catch (Exception e) {
            System.err.println("Erro ao processar POST: " + e.getMessage());
            enviarResposta(printStream, 400, "Bad Request",
                "{\"erro\": \"JSON inválido ou campos obrigatórios faltando\", \"mensagem\": \"" +
                e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    /**
     * DELETE /itens-cardapio/{id} - Remove um item do cardápio por ID
     */
    private static void handleDeleteItemCardapio(PrintStream printStream, String idStr) {
        try {
            // Converte o ID de String para Long
            Long id = Long.parseLong(idStr);

            // Remove diretamente do mapa - O(log n) - muito mais rápido que lista!
            ItemCardapio itemRemovido = itensCardapio.remove(id);

            // Verifica se o item foi encontrado e removido
            if (itemRemovido != null) {
                System.out.println("Item removido com sucesso: " + itemRemovido.nome() + " (ID: " + id + ")");
                String json = "{\"mensagem\": \"Item removido com sucesso\", \"id\": " + id + "}";
                enviarResposta(printStream, 200, "OK", json);
            } else {
                System.out.println("Item não encontrado com ID: " + id);
                enviarResposta(printStream, 404, "Not Found",
                    "{\"erro\": \"Item não encontrado\", \"id\": " + id + "}");
            }

        } catch (NumberFormatException e) {
            System.err.println("ID inválido: " + idStr);
            enviarResposta(printStream, 400, "Bad Request",
                "{\"erro\": \"ID inválido\", \"valor\": \"" + idStr + "\"}");
        }
    }

    /**
     * Envia uma resposta HTTP com o código de status e body JSON
     */
    private static void enviarResposta(PrintStream printStream, int statusCode, String statusMessage, String jsonBody) {
        System.out.println("\n--- Enviando resposta HTTP ---");
        System.out.println("Status: " + statusCode + " " + statusMessage);

        printStream.println("HTTP/1.1 " + statusCode + " " + statusMessage);
        printStream.println("Content-Type: application/json; charset=utf-8");
        printStream.println("Content-Length: " + jsonBody.getBytes(StandardCharsets.UTF_8).length);
        printStream.println("Connection: close");
        printStream.println();
        printStream.println(jsonBody);
        printStream.flush();

        System.out.println("Body (JSON) enviado: " + jsonBody.length() + " caracteres");
    }

    /**
     * Converte uma lista de ItemCardapio para JSON manualmente
     */
    private static String itensCardapioParaJson(List<ItemCardapio> itens) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < itens.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            json.append(itemCardapioParaJson(itens.get(i)));
        }
        json.append("]");
        return json.toString();
    }

    /**
     * Converte um ItemCardapio para JSON manualmente
     */
    private static String itemCardapioParaJson(ItemCardapio item) {
        return String.format(
            "{\"id\":%d,\"nome\":\"%s\",\"descricao\":\"%s\",\"categoria\":\"%s\",\"preco\":%s,\"precoComDesconto\":%s}",
            item.id(),
            escaparJson(item.nome()),
            escaparJson(item.descricao()),
            item.categoria().name(),
            item.preco().toString(),
            item.precoComDesconto().toString()
        );
    }

    /**
     * Parse simples de JSON para ItemCardapio (sem biblioteca externa)
     */
    private static ItemCardapio parseJsonParaItemCardapio(String json) {
        // Remove espaços em branco, chaves externas
        json = json.trim();
        if (json.startsWith("{")) {
            json = json.substring(1);
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }

        // Extrai campos do JSON
        Long id = extrairLong(json, "id");
        String nome = extrairString(json, "nome");
        String descricao = extrairString(json, "descricao");
        String categoriaStr = extrairString(json, "categoria");
        BigDecimal preco = extrairBigDecimal(json, "preco");
        BigDecimal precoComDesconto = extrairBigDecimal(json, "precoComDesconto");

        // Se ID não foi fornecido, gera um novo
        if (id == null || id == 0) {
            id = proximoId.getAndIncrement();
        }

        // Valida campos obrigatórios
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Campo 'nome' é obrigatório");
        }
        if (descricao == null) {
            descricao = "";
        }
        if (preco == null) {
            throw new IllegalArgumentException("Campo 'preco' é obrigatório");
        }
        if (precoComDesconto == null) {
            precoComDesconto = preco;
        }

        // Parse da categoria
        ItemCardapio.CategoriaCardapio categoria;
        try {
            categoria = categoriaStr != null ?
                ItemCardapio.CategoriaCardapio.valueOf(categoriaStr.toUpperCase()) :
                ItemCardapio.CategoriaCardapio.PRATOS_PRINCIPAIS;
        } catch (IllegalArgumentException e) {
            categoria = ItemCardapio.CategoriaCardapio.PRATOS_PRINCIPAIS;
        }

        return new ItemCardapio(id, nome, descricao, categoria, preco, precoComDesconto);
    }

    /**
     * Extrai um valor Long de um campo JSON
     */
    private static Long extrairLong(String json, String campo) {
        String valor = extrairValor(json, campo);
        if (valor == null || valor.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Extrai um valor String de um campo JSON
     */
    private static String extrairString(String json, String campo) {
        String valor = extrairValor(json, campo);
        if (valor == null) {
            return null;
        }
        // Remove aspas se houver
        if (valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1);
        }
        return valor;
    }

    /**
     * Extrai um valor BigDecimal de um campo JSON
     */
    private static BigDecimal extrairBigDecimal(String json, String campo) {
        String valor = extrairValor(json, campo);
        if (valor == null || valor.isEmpty()) {
            return null;
        }
        // Remove aspas se houver
        if (valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1);
        }
        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Extrai o valor de um campo JSON de forma simples
     */
    private static String extrairValor(String json, String campo) {
        String pattern = "\"" + campo + "\"";
        int index = json.indexOf(pattern);
        if (index == -1) {
            return null;
        }

        // Pula o campo e procura o ':'
        int colonIndex = json.indexOf(':', index);
        if (colonIndex == -1) {
            return null;
        }

        // Pula espaços após ':'
        int startIndex = colonIndex + 1;
        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }

        // Determina o fim do valor
        int endIndex;
        if (json.charAt(startIndex) == '"') {
            // Valor string - procura a próxima aspas não escapada
            endIndex = startIndex + 1;
            while (endIndex < json.length()) {
                if (json.charAt(endIndex) == '"' && json.charAt(endIndex - 1) != '\\') {
                    endIndex++;
                    break;
                }
                endIndex++;
            }
        } else {
            // Valor numérico ou booleano - procura vírgula ou fim
            endIndex = startIndex;
            while (endIndex < json.length() && json.charAt(endIndex) != ',' && json.charAt(endIndex) != '}') {
                endIndex++;
            }
        }

        return json.substring(startIndex, endIndex).trim();
    }

    /**
     * Escapa caracteres especiais para JSON
     */
    private static String escaparJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
