package mx.florinda.cardapio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class ClientLocalhostCardapio {
    private static final String CARDAPIO_URL = "http://localhost:8000/itens-cardapio";

    public ClientLocalhostCardapio() {
    }

    public String consultarCardapio() throws IOException {
        String urlString = CARDAPIO_URL;

        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        // Ler a resposta
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    public static void main(String[] args) {
        ClientLocalhostCardapio client = new ClientLocalhostCardapio();
        try {
            String resultado = client.consultarCardapio();
            System.out.println(resultado);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}