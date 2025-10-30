package mx.florinda.cardapio;

/**
 * Classe de demonstração conceitual do protocolo TCP
 *
 * Esta classe contém documentação e exemplos conceituais sobre:
 * - ServerSocket vs Socket
 * - Comunicação bidirecional TCP
 * - OutputStream e InputStream
 * - Diferenças entre TCP e UDP
 */
public class DemonstracaoProtocoloTCP {

    /**
     * DIAGRAMA CONCEITUAL TCP
     *
     * ┌─────────────────────────────────────────────────────────────────┐
     * │                    COMUNICAÇÃO TCP                              │
     * └─────────────────────────────────────────────────────────────────┘
     *
     *     SERVIDOR                              CLIENTE
     * ┌──────────────────┐                  ┌──────────────────┐
     * │                  │                  │                  │
     * │  ServerSocket    │                  │     Socket       │
     * │   (porta 9000)   │                  │  (conecta ao     │
     * │                  │                  │   servidor)      │
     * │      accept()    │◄─────────────────┤    connect()     │
     * │        ↓         │   Estabelece     │                  │
     * │     Socket       │    Conexão       │                  │
     * │                  │                  │                  │
     * └──────────────────┘                  └──────────────────┘
     *
     *
     * COMUNICAÇÃO BIDIRECIONAL:
     *
     * ┌──────────────────────────────────────────────────────────────────┐
     * │                                                                  │
     * │  SERVIDOR                                           CLIENTE      │
     * │  ┌─────────────┐                                 ┌────────────┐  │
     * │  │             │                                 │            │  │
     * │  │ OutputStream├────────────────────────────────►│ InputStream│  │
     * │  │             │  Dados fluem do servidor        │            │  │
     * │  │             │  para o cliente                 │            │  │
     * │  └─────────────┘                                 └────────────┘  │
     * │                                                                  │
     * │  ┌─────────────┐                                 ┌────────────┐  │
     * │  │             │                                 │            │  │
     * │  │ InputStream │◄────────────────────────────────┤OutputStream│  │
     * │  │             │  Dados fluem do cliente         │            │  │
     * │  │             │  para o servidor                │            │  │
     * │  └─────────────┘                                 └────────────┘  │
     * │                                                                  │
     * └──────────────────────────────────────────────────────────────────┘
     */

    public static void main(String[] args) {
        demonstrarConceitosTCP();
    }

    public static void demonstrarConceitosTCP() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║        DEMONSTRAÇÃO: PROTOCOLO TCP EM JAVA                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // 1. Classes principais TCP
        System.out.println("📌 CLASSES JAVA PARA TCP:");
        System.out.println("   • ServerSocket - Lado do SERVIDOR");
        System.out.println("     - Aguarda conexões de clientes");
        System.out.println("     - Método accept() bloqueia até receber conexão");
        System.out.println("     - Retorna um Socket quando cliente conecta\n");

        System.out.println("   • Socket - Lado do CLIENTE (e usado no servidor após accept)");
        System.out.println("     - Estabelece conexão com servidor");
        System.out.println("     - Fornece OutputStream e InputStream");
        System.out.println("     - Representa uma conexão TCP estabelecida\n");

        // 2. Comunicação bidirecional
        System.out.println("📌 COMUNICAÇÃO BIDIRECIONAL:");
        System.out.println("   ┌─────────────────────────────────────────────────┐");
        System.out.println("   │  OutputStream (Servidor) → InputStream (Cliente) │");
        System.out.println("   │  OutputStream (Cliente) → InputStream (Servidor) │");
        System.out.println("   └─────────────────────────────────────────────────┘\n");

        System.out.println("   ✓ Entrega CONFIÁVEL dos dados");
        System.out.println("   ✓ Ordem PRESERVADA dos pacotes");
        System.out.println("   ✓ Controle de fluxo e congestionamento");
        System.out.println("   ✓ Verificação de erros (checksum)\n");

        // 3. Fluxo de dados
        System.out.println("📌 FLUXO DE DADOS TCP:");
        System.out.println("   1. Servidor cria ServerSocket e aguarda (accept)");
        System.out.println("   2. Cliente cria Socket e conecta ao servidor");
        System.out.println("   3. Servidor aceita conexão → retorna Socket");
        System.out.println("   4. Ambos obtêm OutputStream e InputStream");
        System.out.println("   5. Dados fluem bidirecionalmente");
        System.out.println("   6. Conexão é fechada por qualquer lado\n");

        // 4. Código exemplo
        System.out.println("📌 EXEMPLO DE CÓDIGO:");
        System.out.println("   Servidor:");
        System.out.println("   ┌────────────────────────────────────────────┐");
        System.out.println("   │ ServerSocket server = new ServerSocket(9000); │");
        System.out.println("   │ Socket cliente = server.accept();          │");
        System.out.println("   │ OutputStream out = cliente.getOutputStream(); │");
        System.out.println("   │ InputStream in = cliente.getInputStream(); │");
        System.out.println("   └────────────────────────────────────────────┘\n");

        System.out.println("   Cliente:");
        System.out.println("   ┌────────────────────────────────────────────┐");
        System.out.println("   │ Socket socket = new Socket(\"localhost\", 9000); │");
        System.out.println("   │ OutputStream out = socket.getOutputStream(); │");
        System.out.println("   │ InputStream in = socket.getInputStream();  │");
        System.out.println("   └────────────────────────────────────────────┘\n");

        // 5. TCP vs UDP
        System.out.println("📌 COMPARAÇÃO: TCP vs UDP");
        System.out.println("   ┌──────────────┬─────────────────┬──────────────────┐");
        System.out.println("   │ Característica│      TCP        │       UDP        │");
        System.out.println("   ├──────────────┼─────────────────┼──────────────────┤");
        System.out.println("   │ Classe Java  │ ServerSocket/   │ DatagramSocket   │");
        System.out.println("   │              │ Socket          │                  │");
        System.out.println("   ├──────────────┼─────────────────┼──────────────────┤");
        System.out.println("   │ Conexão      │ Orientado       │ Sem conexão      │");
        System.out.println("   ├──────────────┼─────────────────┼──────────────────┤");
        System.out.println("   │ Confiabilidade│ Garantida      │ Não garantida    │");
        System.out.println("   ├──────────────┼─────────────────┼──────────────────┤");
        System.out.println("   │ Ordem        │ Preservada      │ Não preservada   │");
        System.out.println("   ├──────────────┼─────────────────┼──────────────────┤");
        System.out.println("   │ Velocidade   │ Mais lento      │ Mais rápido      │");
        System.out.println("   ├──────────────┼─────────────────┼──────────────────┤");
        System.out.println("   │ Uso comum    │ HTTP, FTP, SSH  │ DNS, Streaming   │");
        System.out.println("   └──────────────┴─────────────────┴──────────────────┘\n");

        // 6. Instruções para testar
        System.out.println("📌 COMO TESTAR:");
        System.out.println("   1. Execute ServidorTCPManual primeiro");
        System.out.println("      ./gradlew runServidorTCP");
        System.out.println();
        System.out.println("   2. Em outro terminal, execute ClienteTCPManual");
        System.out.println("      ./gradlew runClienteTCP");
        System.out.println();
        System.out.println("   3. Digite mensagens no cliente e veja a comunicação\n");

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  Para entender melhor, execute as classes de demonstração ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}

