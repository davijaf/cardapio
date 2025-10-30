package mx.florinda.cardapio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public static void main(String[] args) {
        System.out.println("=== Servidor HTTP com ServerSocket ===");
        System.out.println("Iniciando servidor na porta " + PORTA + "...\n");

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor aguardando conexões em http://localhost:" + PORTA);
            System.out.println("Acesse http://localhost:8000/itens-cardapio no navegador");
            System.out.println("Pressione Ctrl+C para parar o servidor\n");

            int numeroRequisicao = 0;

            // Loop infinito para aceitar múltiplas requisições
            while (true) {
                numeroRequisicao++;
                System.out.println("\n========================================");
                System.out.println("Aguardando requisição #" + numeroRequisicao + "...");
                System.out.println("========================================");

                // Aceita conexão do cliente (bloqueia até receber uma conexão)
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Processa a requisição
                processarRequisicao(clientSocket);

                // Fecha o socket do cliente após processar
                clientSocket.close();
                System.out.println("Conexão fechada. Pronto para próxima requisição.");
            }

        } catch (IOException e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Processa a requisição HTTP lendo bytes do InputStream
     * e enviando resposta HTTP manual através do OutputStream
     */
    private static void processarRequisicao(Socket clientSocket) {
        try (
            // Obtém o InputStream do cliente
            InputStream inputStream = clientSocket.getInputStream();

            // Obtém o OutputStream para enviar resposta ao cliente
            OutputStream outputStream = clientSocket.getOutputStream();

            // PrintStream para facilitar o envio de dados
            PrintStream printStream = new PrintStream(outputStream)
        ) {
            System.out.println("\n--- Lendo dados do cliente ---");

            // StringBuilder para armazenar os dados lidos
            StringBuilder dados = new StringBuilder();

            // Lê os bytes manualmente
            int byteLido;
            while (inputStream.available() > 0 || dados.isEmpty()) {
                byteLido = inputStream.read();

                if (byteLido == -1) {
                    break; // Fim do stream
                }

                // Armazena o byte lido no StringBuilder
                dados.append(byteLido).append(" ");

                // Verifica se não há mais bytes disponíveis
                if (inputStream.available() == 0) {
                    // Pequena pausa para garantir que todos os dados foram recebidos
                    Thread.sleep(100);
                    if (inputStream.available() == 0) {
                        break;
                    }
                }
            }

            // Converte para string e exibe
            String conteudo = dados.toString();
            System.out.println("\nBytes lidos (como números):");
            System.out.println(conteudo);

            System.out.println("\nTotal de bytes lidos: " + conteudo.split(" ").length);
            System.out.println("\nNota: Os dados foram lidos como bytes (números),");
            System.out.println("mas ainda não foram convertidos corretamente em caracteres.");

            // --- ENVIO DA RESPOSTA HTTP MANUAL ---
            System.out.println("\n--- Enviando resposta HTTP manual ---");

            // 1. Linha de status HTTP (Status Line)
            printStream.println("HTTP/1.1 200 OK");
            System.out.println("Status line enviada: HTTP/1.1 200 OK");

            // 2. Headers HTTP
            printStream.println("Content-Type: application/json; charset=utf-8");
            printStream.println("Connection: close");

            // 3. Linha em branco separando headers do body
            printStream.println();

            // 4. Body (conteúdo JSON do cardápio)
            String jsonCardapio = lerArquivoCardapio();
            printStream.println(jsonCardapio);

            System.out.println("Headers enviados:");
            System.out.println("  - Content-Type: application/json; charset=utf-8");
            System.out.println("  - Connection: close");
            System.out.println("\nBody (JSON) enviado com sucesso!");
            System.out.println("Tamanho do conteúdo: " + jsonCardapio.length() + " caracteres");

            printStream.flush();
            System.out.println("\nResposta HTTP completa enviada ao cliente.");

        } catch (IOException e) {
            System.err.println("Erro ao processar requisição: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Thread interrompida: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Lê o arquivo cardapio.json e retorna seu conteúdo
     */
    private static String lerArquivoCardapio() {
        try {
            return Files.readString(Paths.get("cardapio.json"));
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo cardapio.json: " + e.getMessage());
            // Retorna JSON vazio em caso de erro
            return "[]";
        }
    }
}

