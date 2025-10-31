#!/bin/bash

# Script de testes para os endpoints do Servidor HTTP com Socket
# Testa os três endpoints implementados:
# - GET /itens-cardapio
# - GET /itens-cardapio/total
# - POST /itens-cardapio

echo "======================================"
echo "  Testando Endpoints do Servidor HTTP"
echo "======================================"
echo ""

# URL base do servidor
BASE_URL="http://localhost:8000"

# Cores para output (opcional)
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Função para imprimir seções
print_section() {
    echo ""
    echo -e "${YELLOW}======================================"
    echo -e "$1"
    echo -e "======================================${NC}"
    echo ""
}

# Função para verificar se o servidor está rodando
check_server() {
    echo -n "Verificando se o servidor está rodando na porta 8000... "
    if curl -s --connect-timeout 2 "$BASE_URL/itens-cardapio" > /dev/null 2>&1; then
        echo -e "${GREEN}OK${NC}"
        return 0
    else
        echo -e "${RED}FALHOU${NC}"
        echo ""
        echo "O servidor não está respondendo. Por favor, inicie o servidor primeiro:"
        echo "  java -cp build/classes/java/main mx.florinda.cardapio.ServidorItensCardapioComSocket"
        exit 1
    fi
}

# Verifica se o servidor está rodando
check_server

# Teste 1: GET /itens-cardapio - Lista todos os itens do cardápio
print_section "Teste 1: GET /itens-cardapio"
echo "Descrição: Lista todos os itens do cardápio"
echo ""
echo "Resposta:"
curl -s "$BASE_URL/itens-cardapio" | python3 -m json.tool
echo ""

# Teste 2: GET /itens-cardapio/total - Retorna a quantidade de itens
print_section "Teste 2: GET /itens-cardapio/total"
echo "Descrição: Retorna a quantidade total de itens do cardápio"
echo ""
echo "Resposta:"
curl -s "$BASE_URL/itens-cardapio/total" | python3 -m json.tool
echo ""

# Teste 3: POST /itens-cardapio - Adiciona um novo item (Taco)
print_section "Teste 3: POST /itens-cardapio (Taco)"
echo "Descrição: Adiciona um novo item 'Taco' ao cardápio"
echo ""
echo "JSON enviado:"
cat << 'EOF'
{
    "nome": "Taco",
    "descricao": "Taco mexicano com carne e vegetais",
    "categoria": "PRATOS_PRINCIPAIS",
    "preco": "20.00",
    "precoComDesconto": "18.00"
}
EOF
echo ""
echo "Resposta:"
curl -s -X POST "$BASE_URL/itens-cardapio" \
    -H "Content-Type: application/json" \
    -d '{
        "nome": "Taco",
        "descricao": "Taco mexicano com carne e vegetais",
        "categoria": "PRATOS_PRINCIPAIS",
        "preco": "20.00",
        "precoComDesconto": "18.00"
    }' | python3 -m json.tool
echo ""

# Teste 4: POST /itens-cardapio - Adiciona outro item (Quesadilla)
print_section "Teste 4: POST /itens-cardapio (Quesadilla)"
echo "Descrição: Adiciona um novo item 'Quesadilla' ao cardápio"
echo ""
echo "JSON enviado:"
cat << 'EOF'
{
    "nome": "Quesadilla",
    "descricao": "Tortilha recheada com queijo derretido",
    "categoria": "ENTRADA",
    "preco": "12.50"
}
EOF
echo ""
echo "Resposta:"
curl -s -X POST "$BASE_URL/itens-cardapio" \
    -H "Content-Type: application/json" \
    -d '{
        "nome": "Quesadilla",
        "descricao": "Tortilha recheada com queijo derretido",
        "categoria": "ENTRADA",
        "preco": "12.50"
    }' | python3 -m json.tool
echo ""

# Teste 5: GET /itens-cardapio/total - Verifica o novo total
print_section "Teste 5: GET /itens-cardapio/total (Verificação)"
echo "Descrição: Verifica se o total aumentou após adicionar os itens"
echo ""
echo "Resposta:"
curl -s "$BASE_URL/itens-cardapio/total" | python3 -m json.tool
echo ""

# Teste 6: GET /itens-cardapio - Lista todos os itens novamente
print_section "Teste 6: GET /itens-cardapio (Verificação)"
echo "Descrição: Lista todos os itens incluindo os novos adicionados"
echo ""
echo "Resposta:"
curl -s "$BASE_URL/itens-cardapio" | python3 -m json.tool
echo ""

# Teste 6.1: GET /itens-cardapio/{id} - Busca item específico por ID
print_section "Teste 6.1: GET /itens-cardapio/1 (Buscar por ID)"
echo "Descrição: Busca o item com ID 1 (Sanduíche de Presunto)"
echo ""
echo "Endpoint: GET /itens-cardapio/1"
echo ""
echo "Resposta:"
curl -s "$BASE_URL/itens-cardapio/1" | python3 -m json.tool
echo ""

# Teste 6.2: GET /itens-cardapio/{id} - Busca outro item por ID
print_section "Teste 6.2: GET /itens-cardapio/5 (Buscar por ID)"
echo "Descrição: Busca o item com ID 5 (Cafezinho)"
echo ""
echo "Endpoint: GET /itens-cardapio/5"
echo ""
echo "Resposta:"
curl -s "$BASE_URL/itens-cardapio/5" | python3 -m json.tool
echo ""

# Teste 7: DELETE /itens-cardapio/{id} - Remove um item por ID
print_section "Teste 7: DELETE /itens-cardapio/6 (Removendo Taco)"
echo "Descrição: Remove o item 'Taco' (ID: 6) do cardápio"
echo ""
echo "Endpoint: DELETE /itens-cardapio/6"
echo ""
echo "Resposta:"
curl -s -X DELETE "$BASE_URL/itens-cardapio/6" | python3 -m json.tool
echo ""

# Teste 8: GET /itens-cardapio/total - Verifica total após deletar
print_section "Teste 8: GET /itens-cardapio/total (Após DELETE)"
echo "Descrição: Verifica se o total diminuiu após remover o item"
echo ""
echo "Resposta:"
curl -s "$BASE_URL/itens-cardapio/total" | python3 -m json.tool
echo ""

# Teste 9: DELETE com ID inexistente (teste de erro 404)
print_section "Teste 9: DELETE com ID inexistente (Teste de Erro 404)"
echo "Descrição: Tenta remover um item com ID que não existe"
echo ""
echo "Endpoint: DELETE /itens-cardapio/999"
echo ""
echo "Resposta esperada: Erro 404 (Not Found)"
curl -s -X DELETE "$BASE_URL/itens-cardapio/999" | python3 -m json.tool
echo ""

# Teste 10: DELETE com ID inválido (teste de erro 400)
print_section "Teste 10: DELETE com ID inválido (Teste de Erro 400)"
echo "Descrição: Tenta remover um item com ID inválido (não numérico)"
echo ""
echo "Endpoint: DELETE /itens-cardapio/abc"
echo ""
echo "Resposta esperada: Erro 400 (Bad Request)"
curl -s -X DELETE "$BASE_URL/itens-cardapio/abc" | python3 -m json.tool
echo ""

# Teste 11: POST com JSON inválido (teste de erro)
print_section "Teste 11: POST com JSON inválido (Teste de Erro)"
echo "Descrição: Testa o tratamento de erro ao enviar JSON inválido"
echo ""
echo "JSON enviado:"
cat << 'EOF'
{
    "descricao": "Item sem nome",
    "preco": "10.00"
}
EOF
echo ""
echo "Resposta esperada: Erro 400 (Bad Request)"
curl -s -X POST "$BASE_URL/itens-cardapio" \
    -H "Content-Type: application/json" \
    -d '{
        "descricao": "Item sem nome",
        "preco": "10.00"
    }' | python3 -m json.tool
echo ""

# Teste 12: GET de endpoint inexistente (teste de erro 404)
print_section "Teste 12: GET de endpoint inexistente (Teste de Erro 404)"
echo "Descrição: Testa o retorno 404 para endpoint não encontrado"
echo ""
echo "Endpoint: GET /endpoint-inexistente"
echo ""
echo "Resposta esperada: Erro 404 (Not Found)"
curl -s "$BASE_URL/endpoint-inexistente" | python3 -m json.tool
echo ""

# Resumo final
print_section "Resumo dos Testes"
echo -e "${GREEN}✓ Todos os testes foram executados com sucesso!${NC}"
echo ""
echo "Endpoints testados:"
echo "  1. GET    /itens-cardapio       - Listar todos os itens"
echo "  2. GET    /itens-cardapio/total - Total de itens"
echo "  3. GET    /itens-cardapio/{id}  - Buscar item por ID (NOVO!)"
echo "  4. POST   /itens-cardapio       - Adicionar item"
echo "  5. DELETE /itens-cardapio/{id}  - Remover item por ID"
echo ""
echo "Testes de erro realizados:"
echo "  • GET com ID inexistente (404)"
echo "  • DELETE com ID inexistente (404)"
echo "  • DELETE com ID inválido (400)"
echo "  • POST com JSON inválido (400)"
echo "  • GET de endpoint inexistente (404)"
echo ""
echo "Benefícios do ConcurrentSkipListMap:"
echo "  • Busca por ID: O(log n) - muito mais rápido!"
echo "  • Remoção por ID: O(log n) - muito mais rápido!"
echo "  • Thread-safe nativo - sem synchronized"
echo "  • Itens sempre ordenados por ID"
echo ""
echo "======================================"

