package mx.florinda.cardapio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Demonstração do protocolo TCP usando ServerSocket
 *
 * O TCP permite comunicação bidirecional confiável:
 * - OutputStream do servidor → InputStream do cliente
 * - OutputStream do cliente → InputStream do servidor
 *
 * Esta classe implementa um servidor TCP manual para entender
 * o funcionamento interno das conexões antes de usar abstrações
 * como HttpServer.
 */
public class ServidorTCPManual {

    private static final int PORTA = 9000;

    public static void main(String[] args) {
        System.out.println("=== Demonstração de Servidor TCP Manual ===\n");

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor TCP iniciado na porta " + PORTA);
            System.out.println("Aguardando conexões de clientes...\n");

            // Aceita conexão do cliente (fica bloqueado até receber uma conexão)
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

            // Comunicação bidirecional
            demonstrarComunicacaoBidirecional(clientSocket);

        } catch (IOException e) {
            System.err.println("Erro no servidor TCP: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstra a comunicação bidirecional TCP:
     * - InputStream: recebe dados do cliente
     * - OutputStream: envia dados para o cliente
     */
    private static void demonstrarComunicacaoBidirecional(Socket clientSocket) {
        try (
            // InputStream: recebe dados DO cliente
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
            );

            // OutputStream: envia dados PARA o cliente
            PrintWriter saida = new PrintWriter(
                clientSocket.getOutputStream(),
                true  // autoFlush habilitado
            )
        ) {
            System.out.println("\n--- Comunicação Bidirecional Estabelecida ---");
            System.out.println("OutputStream (servidor) → InputStream (cliente)");
            System.out.println("OutputStream (cliente) → InputStream (servidor)\n");

            // 1. RECEBER mensagem do cliente (InputStream)
            System.out.println("Aguardando mensagem do cliente...");
            String mensagemRecebida = entrada.readLine();
            System.out.println("Mensagem recebida do cliente: " + mensagemRecebida);

            // 2. ENVIAR resposta ao cliente (OutputStream)
            String resposta = "Servidor recebeu: '" + mensagemRecebida + "' - Conexão TCP estabelecida com sucesso!";
            System.out.println("Enviando resposta ao cliente...");
            saida.println(resposta);
            System.out.println("Resposta enviada: " + resposta);

            // 3. Demonstrar múltiplas trocas de mensagens
            System.out.println("\n--- Continuando comunicação bidirecional ---");

            String linha;
            while ((linha = entrada.readLine()) != null) {
                System.out.println("Cliente: " + linha);

                if (linha.equalsIgnoreCase("SAIR")) {
                    System.out.println("Cliente solicitou encerramento.");
                    saida.println("Encerrando conexão. Até logo!");
                    break;
                }

                // Envia echo de volta
                String echo = "ECHO: " + linha;
                saida.println(echo);
                System.out.println("Servidor: " + echo);
            }

            System.out.println("\nConexão encerrada com sucesso.");

        } catch (IOException e) {
            System.err.println("Erro na comunicação: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Comparação: TCP vs UDP
     *
     * TCP (ServerSocket/Socket):
     * - Orientado a conexão
     * - Entrega confiável e ordenada
     * - Comunicação bidirecional
     * - Controle de fluxo e congestionamento
     *
     * UDP (DatagramSocket):
     * - Sem conexão
     * - Entrega não confiável
     * - Sem garantia de ordem
     * - Mais rápido, menor overhead
     */
}

