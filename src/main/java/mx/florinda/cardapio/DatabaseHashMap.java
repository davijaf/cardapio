package mx.florinda.cardapio;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.ArrayList;

import static mx.florinda.cardapio.ItemCardapio.CategoriaCardapio.*;

public class DatabaseHashMap {
    private static final Map<Long, ItemCardapio> itensPorId = new WeakHashMap<>();//HashMap<>();
    private final Map<ItemCardapio, PrecoAuditoria> auditoriaPrecos = new IdentityHashMap<>(); //HashMap<>();

    public DatabaseHashMap() {
        // Inicializa o mapa com itens do cardápio
        itensPorId.put(1L, new ItemCardapio(1L, "Sanduíche de Presunto", "Sanduíche feito com pão, presunto, queijo, alface e tomate",
                PRATOS_PRINCIPAIS, new BigDecimal("30.00"), new BigDecimal("25.00")));
        itensPorId.put(2L, new ItemCardapio(2L, "Refresco do Chaves", "Bebida refrescante de limão com hortelã",
                BEBIDA, new BigDecimal("10.00"), new BigDecimal("8.00")));
        itensPorId.put(3L, new ItemCardapio(3L, "Brigadeiro", "Doce brasileiro feito com leite condensado, chocolate e granulado",
                SOBREMESA, new BigDecimal("8.00"), new BigDecimal("7.00")));
        itensPorId.put(4L, new ItemCardapio(4L, "Guacamole", "Molho mexicano feito com abacate, tomate, cebola e limão",
                ENTRADA, new BigDecimal("15.00"), new BigDecimal("12.00")));
        itensPorId.put(5L, new ItemCardapio(5L, "Cafezinho", "Café preto forte, servido em xícara pequena",
                BEBIDA, new BigDecimal("5.00"), new BigDecimal("4.00")));
    }

    public List<ItemCardapio> listaDeItensCardapio() {
        return new ArrayList<>(itensPorId.values());
    }

    public Optional<ItemCardapio> buscarPorId(Long id) {
        ItemCardapio item = itensPorId.get(id);
        return Optional.ofNullable(item); // retorna Optional vazio se não encontrar
    }

    public boolean removerPorId(Long idParaRemover) {
        ItemCardapio itemParaRemover = itensPorId.remove(idParaRemover);
        if (itemParaRemover != null) {
            System.out.println("Removendo item: " + itemParaRemover);
            System.out.println("Itens restantes após remoção: " + itensPorId.size());
            return true;
        } else {
            System.out.println("Item para remoção não encontrado com ID: " + idParaRemover);
            return false;
        }
    }

    //alterar preco do item
    public boolean alterarPrecoItem(Long id, BigDecimal novoPreco, BigDecimal novoPrecoComDesconto) {
        ItemCardapio itemAntigo = itensPorId.get(id);
        if (itemAntigo != null) {
            ItemCardapio itemAtualizado = new ItemCardapio(
                    itemAntigo.id(), itemAntigo.nome(), itemAntigo.descricao(), itemAntigo.categoria(), novoPreco, novoPrecoComDesconto);
            itensPorId.put(id, itemAtualizado);
            System.out.println("Preço do item atualizado: " + itemAtualizado);
            auditoriaPrecos.put(itemAntigo, new PrecoAuditoria(itemAntigo.preco(), new Timestamp(System.currentTimeMillis())));
            return true;
        } else {
            System.out.println("Item não encontrado com ID: " + id);
            return false;
        }
    }

    // Classe para armazenar auditoria de preço
    public static class PrecoAuditoria {
        private final BigDecimal precoAnterior;
        private final Timestamp dataAlteracao;

        public PrecoAuditoria(BigDecimal precoAnterior, Timestamp dataAlteracao) {
            this.precoAnterior = precoAnterior;
            this.dataAlteracao = dataAlteracao;
        }

        public BigDecimal getPrecoAnterior() {
            return precoAnterior;
        }

        public Timestamp getDataAlteracao() {
            return dataAlteracao;
        }
    }

    //rastro de auditoria
    public void exibirAuditoriaPrecos() {
        if (auditoriaPrecos.isEmpty()) {
            System.out.println("Nenhuma alteração de preço registrada.");
        } else {
            System.out.println("Rastro de Auditoria de Preços:");
            for (Map.Entry<ItemCardapio, PrecoAuditoria> entry : auditoriaPrecos.entrySet()) {
                ItemCardapio item = entry.getKey();
                PrecoAuditoria auditoria = entry.getValue();
                System.out.printf("Item: %s | Preço Anterior: %.2f | Data Alteração: %s | Novo Preço: %.2f%n",
                        item.nome(), auditoria.getPrecoAnterior(), auditoria.getDataAlteracao(), item.preco());
            }
        }
    }
}
