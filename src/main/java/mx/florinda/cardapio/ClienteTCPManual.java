package mx.florinda.cardapio;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente TCP para demonstrar comunicação bidirecional com ServidorTCPManual
 *
 * Demonstra como:
 * - OutputStream do cliente se conecta ao InputStream do servidor
 * - InputStream do cliente recebe dados do OutputStream do servidor
 */
public class ClienteTCPManual {

    private static final String HOST = "localhost";
    private static final int PORTA = 9000;

    public static void main(String[] args) {
        System.out.println("=== Cliente TCP Manual ===\n");

        try (
            // Estabelece conexão TCP com o servidor
            Socket socket = new Socket(HOST, PORTA);

            // OutputStream: envia dados PARA o servidor
            PrintWriter saida = new PrintWriter(
                socket.getOutputStream(),
                true  // autoFlush habilitado
            );

            // InputStream: recebe dados DO servidor
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            // Scanner para ler entrada do usuário
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Conectado ao servidor TCP em " + HOST + ":" + PORTA);
            System.out.println("\n--- Comunicação Bidirecional Estabelecida ---");
            System.out.println("OutputStream (cliente) → InputStream (servidor)");
            System.out.println("OutputStream (servidor) → InputStream (cliente)\n");

            // 1. ENVIAR primeira mensagem ao servidor (OutputStream)
            System.out.print("Digite sua mensagem para o servidor: ");
            String mensagemInicial = scanner.nextLine();
            saida.println(mensagemInicial);
            System.out.println("Mensagem enviada ao servidor.");

            // 2. RECEBER resposta do servidor (InputStream)
            String respostaServidor = entrada.readLine();
            System.out.println("Resposta do servidor: " + respostaServidor);

            // 3. Continuar comunicação interativa
            System.out.println("\n--- Modo Interativo (digite 'SAIR' para encerrar) ---");

            while (true) {
                System.out.print("\nVocê: ");
                String mensagem = scanner.nextLine();

                // Envia mensagem ao servidor
                saida.println(mensagem);

                // Recebe resposta
                String resposta = entrada.readLine();
                System.out.println("Servidor: " + resposta);

                if (mensagem.equalsIgnoreCase("SAIR")) {
                    System.out.println("\nConexão encerrada.");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Erro na conexão TCP: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

