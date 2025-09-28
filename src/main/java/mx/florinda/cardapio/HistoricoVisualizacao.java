package mx.florinda.cardapio;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class HistoricoVisualizacao {
    private final DatabaseHashMap database;

    // ItemCardapio => Data e hora da visualização
    private final Map<ItemCardapio, LocalDateTime> historicoVisualizacao = new WeakHashMap<>();//new HashMap<>();

    public HistoricoVisualizacao(DatabaseHashMap database) {
        this.database = database;
    }

    // cria comando tostring
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Histórico de Visualizações:\n");
        for (Map.Entry<ItemCardapio, LocalDateTime> entry : historicoVisualizacao.entrySet()) {
            ItemCardapio item = entry.getKey();
            // Only show if item still exists in the database
            if (database.buscarPorId(item.id()).isPresent()) {
                sb.append("Item: ").append(item.nome())
                  .append(" | Visualizado em: ").append(entry.getValue())
                  .append("\n");
            }
        }
        return sb.toString();
    }

    //Lista as visualizações
    public void listarVisualizacoes() {
        if (historicoVisualizacao.isEmpty()) {
            System.out.println("Nenhuma visualização registrada.");
        } else {
            System.out.println(this.toString());
        }
    }

    //total de itens visualizados
    public int totalVisualizacoes() {
        return historicoVisualizacao.size();
    }

    public void registrarVisualizacao(Long itemId) {
        Optional<ItemCardapio> itemOpt = database.buscarPorId(itemId);
        if (itemOpt.isPresent()) {
            ItemCardapio item = itemOpt.get();
            LocalDateTime agora = LocalDateTime.now();
            historicoVisualizacao.put(item, agora);
            System.out.println("Visualizado: " + item.nome() + " em " + agora);
        } else {
            System.out.println("Item não encontrado para o ID: " + itemId);
        }
    }
}
