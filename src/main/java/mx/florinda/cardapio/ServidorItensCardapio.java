package mx.florinda.cardapio;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServidorItensCardapio {

    public static void main(String[] args) {
        try {
            ServidorItensCardapio servidor = new ServidorItensCardapio();
            servidor.iniciarServidor();
        } catch (IOException e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void iniciarServidor() throws IOException {
        // Criar o servidor na porta 8000
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);

        // Definir o contexto /itens-cardapio
        httpServer.createContext("/itens-cardapio", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Ler o conteúdo do arquivo JSON
                String conteudoJSON = Files.readString(Paths.get("cardapio.json"));

                // Configurar headers da resposta
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");

                // Enviar resposta
                byte[] resposta = conteudoJSON.getBytes();
                exchange.sendResponseHeaders(200, resposta.length);

                OutputStream os = exchange.getResponseBody();
                os.write(resposta);
                os.close();
            }
        });

        // Iniciar o servidor
        httpServer.start();

        // Imprimir mensagem no console
        System.out.println("Servidor rodando em http://localhost:8000/itens-cardapio");
    }
}

