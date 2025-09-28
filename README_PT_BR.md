# UNIPDS - M02.A01 - Introdução ao Java 25 com Gradle e Projeto Cardápio

## Contexto Geral

- Aula sobre ferramentas de build no ecossistema Java
- Foco em alternativas ao Maven: Ant e Gradle
- Demonstração prática de criação de projeto usando Gradle na IntelliJ IDEA
- Exemplo baseado no restaurante da Dona Florinda (Chaves) para ilustrar conceitos
- A demonstração também serviu para apresentar o novo Java 25

## Principais Funcionalidades Demonstradas em `Main.java`

- **Gestão de Itens do Cardápio:**
  - Carrega itens do cardápio a partir de uma abstração de banco de dados (`Database`, `DatabaseHashMap`).
  - Imprime o total de itens, exibe detalhes e remove itens do cardápio.
- **Serialização:**
  - Serializa itens do cardápio para JSON usando Gson e imprime-os.
- **Análise de Categorias:**
  - Conta e exibe categorias distintas usando sets e streams do Java.
  - Imprime categorias em diferentes ordens (inserção, ordenado por nome).
  - Conta itens por categoria usando loops e streams, com diferentes tipos de mapas para preservar a ordem.
- **Filtragem e Promoção:**
  - Filtra itens por categorias promocionais usando `TreeSet`, `Set.of` e `EnumSet`.
  - Imprime itens em promoção e suas categorias.
- **Descrições de Categorias:**
  - Usa `EnumMap` para associar descrições às categorias e imprimi-las para categorias promocionais.
- **Histórico de Visualização do Cardápio:**
  - Registra e imprime o histórico de visualização dos itens do cardápio usando a classe `HistoricoVisualizacao`.
  - Mostra o total de visualizações e lista todos os eventos de visualização.
- **Busca e Modificação de Itens:**
  - Busca itens por ID e imprime resultados.
  - Remove itens por ID e confirma a remoção.
  - Altera preços de itens e audita mudanças de preço, exibindo logs de auditoria.
- **Gerenciamento de Memória:**
  - Demonstra coleta de lixo explícita e seu efeito sobre os itens do cardápio.

## Exemplos de Operações na Demonstração

- Imprime todos os itens do cardápio e suas representações em JSON.
- Mostra e conta todas as categorias únicas, usando diferentes tipos de coleções.
- Conta e imprime o número de itens por categoria, preservando a ordem.
- Filtra e imprime itens em promoção por categoria.
- Mostra descrições de categorias para categorias promocionais.
- Registra e imprime o histórico de visualização do cardápio, incluindo o total de visualizações.
- Remove e atualiza itens do cardápio, com confirmação e logs de auditoria.

## Tecnologias Utilizadas

- Java 25 (para fins de demonstração)
- Gradle (ferramenta de build)
- Gson (serialização JSON)

## Como Executar

1. Certifique-se de ter o Java 25 e o Gradle instalados (ou use o Gradle Wrapper fornecido).
2. Construa o projeto:
   ```sh
   ./gradlew build
   ```
3. Execute a classe principal:
   ```sh
   ./gradlew run
   ```

## Observações

- O projeto demonstra recursos modernos do Java (streams, records, coleções, text blocks).
- O código está organizado para fins educacionais, mostrando estilos de programação imperativa e funcional.

## Ant e Maven

- Ant lançado em 2000, usa XML, formato programático baseado em tarefas
- Ant não gerencia dependências nativamente (precisa do Ivy)
- Maven lançado em 2004, também usa XML, mas é declarativo
- Maven traz gerenciamento de dependências integrado
- Exemplo real: necessidade de lógica customizada (ex: nomes de bancos de dados de teste)
- Limitação do Maven/Ant: difícil adicionar lógica complexa, precisa criar plugins Java (Mojo)
- O criador do Ant reconheceu que XML não foi a melhor escolha, preferiria uma linguagem de script

## Gradle: Visão Geral

- Gradle lançado em 2007, mais moderno
- Scripts podem ser escritos em Groovy ou Kotlin
- Combina abordagem declarativa (fases predefinidas) e programática (customização fácil)
- Reutiliza o repositório central do Maven para dependências
- Build descrito no arquivo build.gradle
- Mais flexível para lógica customizada dentro do próprio script

## Instalação e Uso do Gradle

- Duas formas principais de instalar: diretamente na máquina ou via Gradle Wrapper (gradlew)
- Gradle Wrapper recomendado, fornece scripts para Windows (.bat) e Mac/Linux (shell script)
- Exemplo de criação de projeto na IntelliJ usando Gradle
- Projeto de exemplo: cardápio do restaurante da Dona Florinda
- Java 20 escolhido, Groovy como linguagem do build

## Estrutura do Projeto Gradle

- Arquivos principais: build.gradle (configuração principal), settings.gradle (nome do projeto)
- Gradle cria uma estrutura de pastas semelhante ao Maven: src/main/java, src/test/java
- Plugins adicionam tarefas (compilar, testar, gerar JAR, JavaDoc)
- group e version definidos, equivalente ao groupId e version do Maven

## Dependências e Tasks

- Adição de dependências via implementation no build.gradle
- Exemplo de dependência: JUnit para testes
- Bibliotecas buscadas no Maven Repository, exemplo com Gson (Google)
- implementation para dependências de produção, testImplementation para testes
- O comando gradlew dependencies mostra todas as dependências do projeto

## Demonstração Prática: Criação de Classes

- Criação da classe ItemCardapio com atributos: id, nome, descrição, categoria, preço, desconto
- Uso de enum para categorias (Entradas, Pratos Principais, Bebidas, Sobremesa)
- Discussão sobre imutabilidade, uso de final e construtor
- Sugestão de converter para record (Java moderno)
- Organização em pacotes (ex: mx.florinda.cardapio)
- O build gera automaticamente as classes e o JAR

## Serialização com Gson

- Adição da dependência Gson ao projeto
- Criação da classe Main para instanciar ItemCardapio e serializar para JSON
- Uso de text blocks do Java para descrição
- Execução do projeto via Gradle na IDE
- Impressão do objeto em formato JSON

## Considerações Finais

- O Gradle será utilizado durante o módulo extra do curso
- Ferramenta destacada pela flexibilidade e integração com o ecossistema Java
- Encerramento da aula e convite para próximos vídeos
