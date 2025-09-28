package mx.florinda.cardapio;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Obter lista de itens do cardápio
        Database database = new Database();
        List<ItemCardapio> itens = database.listaDeItensCardapio();

        System.out.println("Total de itens no cardápio: " + itens.size());

        // Converter itens para JSON e imprimir
        for (ItemCardapio item : itens) {
            Gson gson = new Gson();
            String json = gson.toJson(item);
            System.out.println(json);
        }

        ItemCardapio itemCardapio = itens.get(0);

        System.out.println("Nome: " + itemCardapio.nome());

        // Remover item do cardápio
        /*itens.remove(itemCardapio);
        ItemCardapio itemCardapio2 = itens.get(0);
        itens.remove(itemCardapio2);
        System.out.println("Itens restantes: " + itens.size());*/

        itens.forEach(System.out::println);

        // Contar categorias distintas
        //Set<ItemCardapio.CategoriaCardapio> categorias = new HashSet<>();
        Set<ItemCardapio.CategoriaCardapio> categorias = new LinkedHashSet<>();
        for (ItemCardapio item : itens) {
            categorias.add(item.categoria());
        }
        System.out.println("Categorias: " + categorias);

        // Contar Categorias Usando streams
        //Set<ItemCardapio.CategoriaCardapio> categoriasStream = new HashSet<>();
        //Set<ItemCardapio.CategoriaCardapio> categoriasStream = new LinkedHashSet<>();
        Comparator<ItemCardapio.CategoriaCardapio> comparadorPorNome = Comparator.comparing(ItemCardapio.CategoriaCardapio::name);
        Set<ItemCardapio.CategoriaCardapio> categoriasStream = new TreeSet<>(comparadorPorNome);
        itens.stream()
                .map(ItemCardapio::categoria)
                .distinct()
                .forEach(categoria -> categoriasStream.add(categoria));
        System.out.println("Categorias (stream): " + categoriasStream);

        // Imprimir categorias usando streams e collect
        itens.stream()
                .map(ItemCardapio::categoria)
                .collect(Collectors.toSet())
                .forEach(System.out::println);
        System.out.println("-----");

        // Imprimir categorias usando streams e collect com LinkedHashSet para manter a ordem
        itens.stream()
                .map(ItemCardapio::categoria)
                //.collect(Collectors.toCollection(LinkedHashSet::new))
                .forEach(System.out::println);
        System.out.println("-----");

        // Imprimir categorias usando streams e collect com LinkedHashSet para manter a ordem
        itens.stream()
                .map(ItemCardapio::categoria)
                //.collect(Collectors.toCollection(TreeSet::new))
                .collect(Collectors.toCollection(() -> new TreeSet<>(comparadorPorNome)))
                .forEach(System.out::println);
        System.out.println("----");
        // Quantificar itens por categoria usando loop
        Map<ItemCardapio.CategoriaCardapio, Integer> qtdPorCategoriaHash = new HashMap<>();
        for (ItemCardapio item : itens) {
            ItemCardapio.CategoriaCardapio categoria = item.categoria();
            qtdPorCategoriaHash.put(categoria, qtdPorCategoriaHash.getOrDefault(categoria, 0) + 1);
        }
        System.out.println("Quantidade por categoria: " + qtdPorCategoriaHash);
        System.out.println("----");
        // Quantificar itens por categoria
        Map<ItemCardapio.CategoriaCardapio, Long> quantidadePorCategoria = itens.stream()
                .collect(Collectors.groupingBy(ItemCardapio::categoria, Collectors.counting()));
        System.out.println("Quantidade por categoria: " + quantidadePorCategoria);
        System.out.println("----");
        // Quantificar itens por categoria usando LinkedHashMap para manter a ordem
        Map<ItemCardapio.CategoriaCardapio, Integer> qtdPorCategoriaLinkedHash = new LinkedHashMap<>();
        for (ItemCardapio item : itens) {
            ItemCardapio.CategoriaCardapio categoria = item.categoria();
            qtdPorCategoriaLinkedHash.put(categoria, qtdPorCategoriaLinkedHash.getOrDefault(categoria, 0) + 1);
        }
        System.out.println("Quantidade por categoria: " + qtdPorCategoriaLinkedHash);
        System.out.println("----");
        // Quantificar itens por categoria usando streams e LinkedHashMap para manter a ordem
        itens.stream()
            .collect(Collectors.groupingBy(
                    ItemCardapio::categoria,
                    LinkedHashMap::new,
                    Collectors.counting()
            ))
                .forEach((  chave,  valor) -> System.out. println (chave + "=>" + valor));

        System.out.println("----");
        // Quantificar itens por categoria usando streams e LinkedHashMap para manter a ordem
        itens.stream()
                .collect(Collectors.groupingBy(
                        ItemCardapio::categoria,
                        TreeMap::new,
                        Collectors.counting()
                ))
                .forEach((  chave,  valor) -> System.out. println (chave + "=>" + valor));
        System.out.println("----");
        // Obter lista de itens do cardápio
        DatabaseHashMap dbHashMap = new DatabaseHashMap();
        List<ItemCardapio> itensHashMap = dbHashMap.listaDeItensCardapio();
        System.out.println("Total de itens no cardápio (HashMap): " + itensHashMap.size());
        System.out.println("----");
        // Buscar item por ID
        //ItemCardapio itemBuscado = dbHashMap.buscarPorId(3L);
        Optional<ItemCardapio> itemBuscadoOpt = dbHashMap.buscarPorId(1L);
        ItemCardapio itemBuscado = itemBuscadoOpt.orElse(null);
        System.out.println("Busca por ID: " + itemBuscado);
        System.out.println("----");
        if (itemBuscado != null) {
            System.out.println("Item encontrado: " + itemBuscado);
            System.out.println("----");
            // Converter item para JSON e imprimir
            Gson gson = new Gson();
            String json = gson.toJson(itemBuscado);
            System.out.println(json);
        } else {
            System.out.println("Item não encontrado");
        }
        System.out.println("----");

        if (itemBuscadoOpt.isPresent()) {
            System.out.println("Item encontrado (isPresent): " + itemBuscadoOpt.get());
        } else {
            System.out.println("Item não encontrado (isPresent)");
        }
        System.out.println("----");
        String mensagem = itemBuscadoOpt.map(ItemCardapio::toString).orElse("Item não encontrado (map+orElse)");
        System.out.println(mensagem);

        System.out.println("----");
        // Precisa manter as categorias que estão em promoção
        Set<ItemCardapio.CategoriaCardapio> categoriasEmPromocao = new TreeSet<>();
        categoriasEmPromocao.add(ItemCardapio.CategoriaCardapio.SOBREMESA);
        categoriasEmPromocao.forEach(item -> System.out.println(item));
        // Filtrar itens em promoção
        System.out.println("----");
        List<ItemCardapio> itensEmPromocao = itens.stream()
                .filter(item -> categoriasEmPromocao.contains(item.categoria()))
                .collect(Collectors.toList());
        System.out.println("Itens em promoção: " + itensEmPromocao.toString());

        System.out.println("----");
        // Filtrar itens em promoção 2
        Set<ItemCardapio.CategoriaCardapio> categoriaCardapios2 = Set.of(ItemCardapio.CategoriaCardapio.BEBIDA);
        System.out.println("Itens em promoção 2: " + categoriaCardapios2);
        // Filtrar itens em promoção 3 usando EnumSet
        System.out.println("----");
        EnumSet<ItemCardapio.CategoriaCardapio> categoriaCardapios3 = EnumSet.of(ItemCardapio.CategoriaCardapio.ENTRADA);
        categoriaCardapios3.add(ItemCardapio.CategoriaCardapio.BEBIDA);
        System.out.println("Itens em promoção 3: " + categoriaCardapios3);

        // Informar as descriçoes das categorias em promoção com EnumMap
        System.out.println("----");
        EnumMap<ItemCardapio.CategoriaCardapio, String> descricoesCategorias = new EnumMap<>(ItemCardapio.CategoriaCardapio.class);
        descricoesCategorias.put(ItemCardapio.CategoriaCardapio.ENTRADA, "Pratos para começar a refeição");
        descricoesCategorias.put(ItemCardapio.CategoriaCardapio.BEBIDA, "Bebidas refrescantes e energéticas");
        descricoesCategorias.put(ItemCardapio.CategoriaCardapio.PRATOS_PRINCIPAIS, "Pratos principais variados");
        descricoesCategorias.put(ItemCardapio.CategoriaCardapio.SOBREMESA, "Doces e sobremesas deliciosas");
        categoriaCardapios3.forEach(categoria -> {
            String descricao = descricoesCategorias.get(categoria);
            System.out.println(categoria + ": " + descricao);
        });

        //Historico de visualizacão do cardapio
        System.out.println("----");
        HistoricoVisualizacao historico = new HistoricoVisualizacao(dbHashMap);
        historico.registrarVisualizacao(1L);

        // tempo de 2 s
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();}

        historico.registrarVisualizacao(3L);
        //historico.registrarVisualizacao(10L); // ID inexistente
        System.out.println("----");
        System.out.println(historico);

        // Total de visualizações
        System.out.println("----");
        System.out.println("Total de visualizações: " + historico.totalVisualizacoes());
        historico.listarVisualizacoes();

        // Remover um item do cardápio
        System.out.println("----");
        System.out.println("Itens existentes antes da remoção: " + dbHashMap.listaDeItensCardapio().size());
        Long idParaRemover = 3L;
        boolean removido = dbHashMap.removerPorId(idParaRemover);
        if (removido) {
            System.out.println("Item com ID " + idParaRemover + " removido com sucesso.");
        } else {
            System.out.println("Falha ao remover item com ID " + idParaRemover + ".");
        }

        System.out.println("----");
        System.out.println("Solicitando coleta de lixo...");
        System.gc();
        // tempo de 2 s
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();}
        dbHashMap.listaDeItensCardapio().forEach(System.out::println);
        System.out.println("----");
        System.out.println(historico);
        System.out.println("Total de visualizações: " + historico.totalVisualizacoes());
        historico.listarVisualizacoes();

        // Alterar o preço de um item do cardápio
        System.out.println("----");
        Long idParaAlterar = 1L;
        System.out.println("Item antes da alteração: " + dbHashMap.buscarPorId(idParaAlterar).orElse(null));
        System.out.println("----");
        boolean alterado = dbHashMap.alterarPrecoItem(idParaAlterar, new java.math.BigDecimal("35.00"), new java.math.BigDecimal("30.00"));
        if (alterado) {
            System.out.println("----");
            System.out.println("Item com ID " + idParaAlterar + " alterado com sucesso.");
        } else {
            System.out.println("Falha ao alterar item com ID " + idParaAlterar + ".");
        }
        System.out.println("----");
        boolean alterado2 = dbHashMap.alterarPrecoItem(idParaAlterar, new java.math.BigDecimal("30.00"), new java.math.BigDecimal("25.00"));
        System.out.println("----");
        boolean alterado3 = dbHashMap.alterarPrecoItem(idParaAlterar, new java.math.BigDecimal("35.00"), new java.math.BigDecimal("30.00"));


        // Auditar a mudança de preço dos itens do cardápio
        System.out.println("----");
        dbHashMap.exibirAuditoriaPrecos();
        System.out.println("----");




    }
}