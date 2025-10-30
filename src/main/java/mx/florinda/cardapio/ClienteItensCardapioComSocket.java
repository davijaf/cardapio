package mx.florinda.cardapio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Cliente HTTP manual usando Socket
 *
 * Esta classe demonstra como criar um cliente HTTP básico usando Socket,
 * enviando requisições HTTP manualmente e lendo a resposta byte a byte.
 *
 * O cliente:
 * - Conecta ao servidor na porta 8000
 * - Envia uma requisição HTTP GET manual
 * - Lê a resposta do servidor através do InputStream
 * - Exibe a resposta HTTP completa
 */
public class ClienteItensCardapioComSocket {

    private static final String HOST = "localhost";
    private static final int PORTA = 8000;

    public static void main(String[] args) {
        System.out.println("=== Cliente HTTP com Socket ===");
        System.out.println("Conectando ao servidor em " + HOST + ":" + PORTA + "...\n");

        try (
            // Cria conexão Socket com o servidor
            Socket socket = new Socket(HOST, PORTA);

            // OutputStream para enviar requisição ao servidor
            OutputStream outputStream = socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);

            // InputStream para receber resposta do servidor
            InputStream inputStream = socket.getInputStream()
        ) {
            System.out.println("Conectado ao servidor!");

            // --- ENVIO DA REQUISIÇÃO HTTP MANUAL ---
            System.out.println("\n--- Enviando requisição HTTP GET ---");

            // 1. Linha de requisição (Request Line)
            printStream.println("GET /itens-cardapio HTTP/1.1");
            System.out.println("Request line: GET /itens-cardapio HTTP/1.1");

            // 2. Headers HTTP
            printStream.println("Host: " + HOST);
            printStream.println("User-Agent: ClienteItensCardapioComSocket/1.0");
            printStream.println("Accept: application/json");
            printStream.println("Connection: close");

            // 3. Linha em branco indicando fim dos headers
            printStream.println();
            printStream.flush();

            System.out.println("Headers enviados:");
            System.out.println("  - Host: " + HOST);
            System.out.println("  - User-Agent: ClienteItensCardapioComSocket/1.0");
            System.out.println("  - Accept: application/json");
            System.out.println("  - Connection: close");
            System.out.println("\nRequisição HTTP enviada!");

            // --- LEITURA DA RESPOSTA HTTP ---
            System.out.println("\n--- Lendo resposta do servidor ---\n");

            StringBuilder resposta = new StringBuilder();
            int byteLido;
            int totalBytes = 0;

            // Lê a resposta byte a byte
            while ((byteLido = inputStream.read()) != -1) {
                resposta.append((char) byteLido);
                totalBytes++;
            }

            // Exibe a resposta completa
            System.out.println("========================================");
            System.out.println("RESPOSTA HTTP COMPLETA:");
            System.out.println("========================================");
            System.out.println(resposta.toString());
            System.out.println("========================================");
            System.out.println("Total de bytes recebidos: " + totalBytes);
            System.out.println("========================================");

            // Separa e exibe partes da resposta
            String respostaStr = resposta.toString();
            String[] partes = respostaStr.split("\r\n\r\n|\n\n", 2);

            if (partes.length >= 1) {
                System.out.println("\n--- HEADERS DA RESPOSTA ---");
                System.out.println(partes[0]);
            }

            if (partes.length >= 2) {
                System.out.println("\n--- BODY DA RESPOSTA (JSON) ---");
                System.out.println(partes[1]);
            }

            System.out.println("\nConexão encerrada com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            System.err.println("\nCertifique-se de que o ServidorItensCardapioComSocket está rodando!");
            System.err.println("Execute: ./gradlew runServidorSocket");
            e.printStackTrace();
        }
    }
}

