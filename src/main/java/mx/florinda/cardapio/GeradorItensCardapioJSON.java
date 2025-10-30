package mx.florinda.cardapio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeradorItensCardapioJSON {

    public void gerarArquivoJSON(String nomeArquivo) {
        // Instanciar o Database
        Database database = new Database();

        // Obter a lista de itens
        List<ItemCardapio> itens = database.listaDeItensCardapio();

        // Usar o Gson para converter a lista em JSON com o método toJson()
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(itens);

        // Escrever o JSON em um arquivo
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.write(json);
            System.out.println("Arquivo JSON gerado com sucesso: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao gerar arquivo JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GeradorItensCardapioJSON gerador = new GeradorItensCardapioJSON();
        gerador.gerarArquivoJSON("cardapio.json");
    }
}

