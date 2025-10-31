package mx.florinda.cardapio;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Classe que fornece um mapa thread-safe de ItemCardapio usando ConcurrentSkipListMap.
 *
 * ConcurrentSkipListMap é ideal para cenários com:
 * - Busca rápida por ID (O(log n))
 * - Remoção eficiente por ID (O(log n))
 * - Muitas operações de escrita/remoção
 * - Necessidade de itens ordenados por ID
 *
 * Vantagens:
 * - Thread-safe nativo (não precisa de synchronized)
 * - Busca por ID muito rápida: O(log n) vs O(n) da lista
 * - Remoção por ID muito rápida: O(log n) vs O(n) da lista
 * - Não copia estrutura inteira em escritas
 * - Itens sempre ordenados por ID
 * - Melhor para muitas operações de escrita
 *
 * Desvantagens:
 * - Listagem completa um pouco mais lenta (precisa converter values())
 * - Usa mais memória (estrutura de árvore)
 * - Overhead de sincronização em cada operação
 */
public class ArrayList {

    /**
     * Retorna um mapa thread-safe de ItemCardapio, usando ID como chave.
     *
     * @return Mapa vazio thread-safe usando ConcurrentSkipListMap
     */
    public Map<Long, ItemCardapio> mapaDeItensCardapio() {
        // ConcurrentSkipListMap é thread-safe e perfeita para cenários onde
        // há necessidade de busca e remoção eficientes por ID
        return new ConcurrentSkipListMap<>();
    }

}
