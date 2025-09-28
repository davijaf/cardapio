package mx.florinda.cardapio;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static mx.florinda.cardapio.ItemCardapio.CategoriaCardapio.*;

public class Database {

    public List<ItemCardapio> listaDeItensCardapio() {
        final List<ItemCardapio> itens = new ArrayList<>();
        ItemCardapio sanduichePresunto = new ItemCardapio(1L, "Sanduíche de Presunto", "Sanduíche feito com pão, presunto, queijo, alface e tomate",
                PRATOS_PRINCIPAIS, new BigDecimal("30.00"), new BigDecimal("25.00"));
        itens.add(sanduichePresunto);
        ItemCardapio refrescoDoChaves = new ItemCardapio(2L, "Refresco do Chaves", "Bebida refrescante de limão com hortelã",
                BEBIDA, new BigDecimal("10.00"), new BigDecimal("8.00"));
        itens.add(refrescoDoChaves);
        ItemCardapio brigadeiro = new ItemCardapio(3L, "Brigadeiro", "Doce brasileiro feito com leite condensado, chocolate e granulado",
                SOBREMESA, new BigDecimal("8.00"), new BigDecimal("7.00"));
        itens.add(brigadeiro);
        ItemCardapio guacamole = new ItemCardapio(4L, "Guacamole", "Molho mexicano feito com abacate, tomate, cebola e limão",
                ENTRADA, new BigDecimal("15.00"), new BigDecimal("12.00"));
        itens.add(guacamole);
        ItemCardapio cafezinho = new ItemCardapio(5L, "Cafezinho", "Café preto forte, servido em xícara pequena",
                BEBIDA, new BigDecimal("5.00"), new BigDecimal("4.00"));
        itens.add(cafezinho);
        return itens;
    }

}
