
# UNIPDS - M02.A01 - Introduction to Java 25 with Gradle and Cardapio Project

## General Context

- Lesson about build tools in the Java ecosystem
- Focus on alternatives to Maven: Ant and Gradle
- Practical demonstration of project creation using Gradle in IntelliJ IDEA
- Example based on Dona Florinda's restaurant (from Chaves) to illustrate concepts
- The demonstration also served to introduce the new Java 25 version

## Main Features Demonstrated in `Main.java`

- **Menu Item Management:**
	- Load menu items from a database abstraction (`Database`, `DatabaseHashMap`).
	- Print total items, display item details, and remove items from the menu.
- **Serialization:**
	````markdown

	# UNIPDS - M02.A01 - Introdução ao Java 25 com Gradle e Projeto Cardápio

	## Contexto Geral

	- Aula sobre ferramentas de build no ecossistema Java
	- Foco em alternativas ao Maven: Ant e Gradle
	- Demonstração prática de criação de projeto usando Gradle no IntelliJ IDEA
	- Exemplo baseado no restaurante da Dona Florinda (do seriado Chaves) para ilustrar os conceitos
	- A demonstração também serviu para apresentar a nova versão Java 25

	## Principais Funcionalidades Demonstradas em `Main.java`

	- **Gerenciamento de Itens do Cardápio:**
		- Carrega itens do cardápio a partir de uma abstração de banco (`Database`, `DatabaseHashMap`).
		- Imprime total de itens, exibe detalhes dos itens e remove itens do cardápio.
	- **Serialização:**
		- Serializa itens do cardápio para JSON usando Gson e imprime o resultado.
	- **Análise de Categorias:**
		- Conta e exibe categorias distintas usando conjuntos e streams do Java.
		- Imprime categorias em diferentes ordens (inserção, ordenadas por nome).
		- Conta itens por categoria usando loops e streams, com diferentes tipos de mapa para preservar ordem.
	- **Filtragem e Promoções:**
		- Filtra itens por categorias promocionais usando `TreeSet`, `Set.of` e `EnumSet`.
		- Imprime itens em promoção e suas categorias.
	- **Descrições de Categoria:**
		- Usa `EnumMap` para associar descrições às categorias e imprime-as para categorias em promoção.
	- **Histórico de Visualização do Cardápio:**
		- Registra e imprime o histórico de visualizações de itens com a classe `HistoricoVisualizacao`.
		- Mostra total de visualizações e lista todos os eventos de visualização.
	- **Busca e Modificação de Itens:**
		- Busca itens por ID e imprime os resultados.
		- Remove itens por ID e confirma a remoção.
		- Altera preços de itens e audita mudanças de preço, exibindo logs de auditoria.
	- **Gerência de Memória:**
		- Demonstra chamada explícita ao coletor de lixo e seu efeito sobre objetos do cardápio.

	## Operações de Exemplo na Demonstração

	- Imprimir todos os itens do cardápio e suas representações em JSON.
	- Mostrar e contar todas as categorias únicas, usando diferentes tipos de coleções.
	- Contar e imprimir o número de itens por categoria, preservando a ordem.
	- Filtrar e imprimir itens em promoção por categoria.
	- Mostrar descrições de categorias para categorias em promoção.
	- Registrar e imprimir o histórico de visualizações do cardápio, incluindo o total de visualizações.
	- Remover e atualizar itens do cardápio, com confirmação e logs de auditoria.

	## Tecnologias Utilizadas

	- Java 25 (para fins de demonstração)
	- Gradle (ferramenta de build)
	- Gson (serialização JSON)
	- HttpServer (com.sun.net.httpserver) - servidor HTTP simples
	- ServerSocket/Socket - programação TCP

	## Funcionalidades de Programação de Rede

	### Servidor HTTP (`ServidorItensCardapio`)
	- Servidor HTTP simples usando `com.sun.net.httpserver.HttpServer`
	- Serve itens do cardápio como JSON via endpoint REST
	- Executa na porta 8000 em `/itens-cardapio`
	- Lê e serve o arquivo `cardapio.json`
	- Retorna os cabeçalhos corretos `Content-Type: application/json`

	### Servidor HTTP com ServerSocket (`ServidorItensCardapioComSocket`)
	- Implementação manual de servidor HTTP usando baixo nível com `ServerSocket`
	- Demonstra programação de socket TCP em bruto
	- Abre a porta 8000 e aceita conexões de clientes
	- Lê bytes diretamente de `InputStream`
	- Armazena dados em um `StringBuilder` byte a byte
	- Mostra dados recebidos como valores numéricos de bytes
	- Exemplo educacional que evidencia a necessidade de codificação de caracteres adequada
	- Ilustra a diferença entre bytes e caracteres na comunicação de rede

	### Cliente HTTP (`ClientLocalhostCardapio`)
	- Consome o endpoint do servidor HTTP
	- Busca itens do cardápio em `http://localhost:8000/itens-cardapio`
	- Demonstra requisições HTTP GET em Java

	### Demonstração do Protocolo TCP
	Três classes para ilustrar programação TCP em baixo nível:

	1. **`ServidorTCPManual`** - Implementação de Servidor TCP
	   - Usa `ServerSocket` para escutar na porta 9000
	   - Aceita conexões de clientes com `accept()`
	   - Demonstra comunicação bidirecional:
	     - OutputStream (servidor) → InputStream (cliente)
	     - InputStream (servidor) ← OutputStream (cliente)
	   - Modo echo (servidor de eco) com mensagens interativas
	   - Entrega de dados confiável e ordenada

	2. **`ClienteTCPManual`** - Implementação de Cliente TCP
	   - Conecta-se ao servidor usando `Socket`
	   - Envia mensagens via OutputStream
	   - Recebe respostas via InputStream
	   - Modo interativo para testar comunicação bidirecional
	   - Digite "SAIR" para desconectar

	3. **`DemonstracaoProtocoloTCP`** - Conceitos Educacionais de TCP
	   - Diagramas ASCII mostrando a arquitetura ServerSocket ↔ Socket
	   - Explica o fluxo de comunicação bidirecional do TCP
	   - Compara TCP vs UDP (DatagramSocket)
	   - Mostra exemplos de código e boas práticas
	   - Destaca recursos do TCP: confiabilidade, ordenação e controle de fluxo

	### Geração de JSON (`GeradorItensCardapioJSON`)
	- Gera o arquivo `cardapio.json` a partir do banco de dados
	- Usa Gson com pretty printing
	- Exporta todos os itens do cardápio para o formato JSON

	## Como Executar

	1. Verifique se você tem Java 25 e Gradle instalados (ou use o Gradle Wrapper fornecido).
	2. Build do projeto:
		 ```sh
		 ./gradlew build
		 ```
	3. Execute a classe main:
		 ```sh
		 ./gradlew run
		 ```

	### Executando o Servidor HTTP
	Inicie o servidor HTTP para servir os itens do cardápio:
	```sh
	./gradlew runServidor
	```
	Acesse o cardápio em: `http://localhost:8000/itens-cardapio`

	### Executando o Servidor HTTP com ServerSocket (Implementação Manual)
	Inicie o servidor de socket em baixo nível para ver a comunicação em bytes brutos:
	```sh
	./gradlew runServidorSocket
	```
	Então acesse `http://localhost:8000` no seu navegador para observar como os bytes são lidos do InputStream.

	### Executando as Demonstrações TCP

	**Ver conceitos e diagramas TCP:**
	```sh
	./gradlew runDemonstracaoTCP
	```

	**Iniciar servidor TCP (Terminal 1):**
	```sh
	./gradlew runServidorTCP
	```

	**Conectar cliente TCP (Terminal 2):**
	```sh
	./gradlew runClienteTCP
	```

	### Gerar JSON do Cardápio
	```sh
	./gradlew runGerador
	```

	## Observações

	- O projeto demonstra recursos modernos do Java (streams, records, collections, text blocks).
	- O código está organizado para fins educacionais, mostrando estilos imperativo e funcional.

	## Ant e Maven

	- Ant lançado em 2000, usa XML, formato programático baseado em tarefas
	- Ant não gerencia dependências nativamente (requer Ivy)
	- Maven lançado em 2004, também usa XML, mas é declarativo
	- Maven fornece gerenciamento de dependências integrado
	- Exemplo em projetos reais: necessidade de lógica customizada (por exemplo, nomes de bancos de teste)
	- Limitação do Maven/Ant: difícil adicionar lógica complexa, exigindo criação de plugins Java (Mojo)
	- O criador do Ant reconheceu que XML não foi a melhor escolha; teria preferido uma linguagem de script

	## Gradle: Visão Geral

	- Gradle lançado em 2007, mais moderno
	- Scripts podem ser escritos em Groovy ou Kotlin
	- Combina abordagem declarativa (fases pré-definidas) e programática (fácil customização)
	- Reaproveita o repositório Maven Central para dependências
	- O build é descrito no arquivo `build.gradle`
	- Mais flexível para lógica customizada dentro do próprio script

	## Instalação e Uso do Gradle

	- Duas formas principais de instalação: diretamente na máquina ou via Gradle Wrapper (gradlew)
	- Recomenda-se o Gradle Wrapper, que fornece scripts para Windows (.bat) e Mac/Linux (shell script)
	- Exemplo de criação de projeto no IntelliJ usando Gradle
	- Projeto de exemplo: cardápio da Dona Florinda
	- Java 20 foi escolhido no exemplo original; Groovy como linguagem do script de build

	## Estrutura de Projeto Gradle

	- Arquivos principais: `build.gradle` (configuração principal), `settings.gradle` (nome do projeto)
	- O Gradle cria uma estrutura de pastas semelhante ao Maven: `src/main/java`, `src/test/java`
	- Plugins adicionam tarefas (compile, test, gerar JAR, JavaDoc)
	- `group` e `version` definidos, equivalentes a `groupId` e `version` do Maven

	## Dependências e Tarefas

	- Adicione dependências via `implementation` no `build.gradle`
	- Dependência de exemplo: JUnit para testes
	- Bibliotecas são buscadas no Maven Repository; exemplo com Gson (Google)
	- `implementation` para dependências de produção, `testImplementation` para testes
	- `./gradlew dependencies` mostra todas as dependências do projeto

	## Demonstração Prática: Criação de Classes

	- Criação da classe `ItemCardapio` com atributos: id, nome, descrição, categoria, preço, desconto
	- Uso de `enum` para categorias (Entradas, Pratos Principais, Bebidas, Sobremesa)
	- Discussão sobre imutabilidade, uso de `final` e construtor
	- Sugestão de conversão para `record` (Java moderno)
	- Organização em pacotes (ex.: `mx.florinda.cardapio`)
	- O build gera as classes e o JAR automaticamente

	## Serialização com Gson

	- Adição da dependência Gson ao projeto
	- Criação da classe `Main` para instanciar `ItemCardapio` e serializar para JSON
	- Uso de text blocks do Java para descrições
	- Execução do projeto via Gradle na IDE
	- Impressão do objeto em formato JSON

	## Considerações Finais

	- O Gradle será usado ao longo do módulo extra do curso
	- Ferramenta destacada por sua flexibilidade e integração com o ecossistema Java
	- Fim da aula e convite para vídeos/atividades futuras

	````
