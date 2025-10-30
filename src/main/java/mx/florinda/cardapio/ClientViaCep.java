package mx.florinda.cardapio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class ClientViaCep {
    private static final String VIACEP_URL = "https://viacep.com.br/ws/";

    public ClientViaCep() {
    }

    public String consultarCep(String cep) throws IOException {
        String urlString = VIACEP_URL + cep + "/json/";

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
        ClientViaCep client = new ClientViaCep();
        try {
            String resultado = client.consultarCep("01001000");
            System.out.println(resultado);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}