package mx.florinda.cardapio;

import java.math.BigDecimal;

public record ItemCardapio(Long id, String nome, String descricao, CategoriaCardapio categoria, BigDecimal preco,
                           BigDecimal precoComDesconto) {

    public enum CategoriaCardapio {
        ENTRADA,
        BEBIDA,
        PRATOS_PRINCIPAIS,
        SOBREMESA
    }

    @Override
    public String toString() {
        // Usando String.format para um resultado limpo e organizado
        return String.format(
                "ID: %d\n" +
                        "Nome: %s\n" +
                        "Descrição: %s\n" +
                        "Categoria: %s\n" +
                        "Preço: %.2f\n" +
                        "Preço com desconto: %.2f",
                id, nome, descricao, categoria, preco, precoComDesconto
        );
    }

}
