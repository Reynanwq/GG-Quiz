# GG Quiz – Especificação Funcional 

## 1. Visão Geral

O **GG Quiz** é uma plataforma digital de quizzes voltada exclusivamente ao cenário competitivo de League of Legends, abrangendo todas as regiões que possuem circuito profissional ativo e inativo.

A plataforma tem como objetivo:

- Testar conhecimento competitivo dos usuários.
- Promover competição por meio de rankings regionais e global.
- Incentivar contribuição da comunidade.
- Garantir justiça estatística por meio de sistema de pontuação ponderado por dificuldade.

---

## 2. Estrutura dos Modos de Quiz

A plataforma possui apenas duas subdivisões oficiais:

### 2.1 Ranking por Região

Cada região competitiva possui:

- Pool próprio de perguntas.
- Ranking independente.
- Classificação filtrável por período.

### 2.2 Ranking Global

- Pool composto por perguntas de todas as regiões.
- Ranking unificado.
- Classificação filtrável por período.

> Não existem outros modos ou categorias.

---

## 3. Sistema de Ranking

### 3.1 Filtros Temporais

Os rankings podem ser visualizados nos seguintes períodos:

- Diário
- Semanal
- Mensal
- All Time

O filtro é aplicável tanto aos rankings regionais quanto ao ranking global.

### 3.2 Estrutura de Exibição

Cada ranking exibe:

| Campo | Descrição |
|---|---|
| Posição | Classificação do usuário |
| Nome do usuário | Identificação |
| Rating | Pontuação calculada |
| Quantidade de tentativas | Total de partidas |

---

## 4. Estrutura da Partida

### 4.1 Regras Gerais

- Perguntas são selecionadas aleatoriamente do pool correspondente.
- Cada pergunta possui **4 alternativas**.
- Ao **errar uma pergunta**, a partida é encerrada imediatamente.
- Cada pergunta possui **tempo mínimo de 20 segundos**.
- O tempo total da sessão é utilizado no cálculo do rating.

---

## 5. Sistema de Perguntas

### 5.1 Estrutura da Pergunta

Cada pergunta contém:

| Campo | Detalhe |
|---|---|
| Região | Regional ou Global |
| Enunciado | Texto da pergunta |
| Alternativas | 4 opções |
| Alternativa correta | Resposta certa |
| Nível de dificuldade | 1 a 10 |
| Tempo mínimo | 20 segundos |
| Autor | Sistema ou Usuário |
| Status | Pendente / Aprovada / Rejeitada |
| Data de criação | — |
| Data de aprovação | — |

### 5.2 Nível de Dificuldade

Cada pergunta possui nível de dificuldade entre **1** e **10**:

- `1` → Pergunta muito fácil.
- `10` → Pergunta extremamente difícil.

A dificuldade influencia diretamente o cálculo do rating.

---

## 6. Sistema de Rating

### 6.1 Componentes do Cálculo

O rating considera:

- Quantidade de acertos.
- Dificuldade das perguntas acertadas.
- Tempo total da sessão.
- Dificuldade da pergunta incorreta.

### 6.2 Pontuação Base

```
rating_base = (Σ (100 × dificuldade_da_pergunta_acertada)) / tempo_total_em_segundos
```

### 6.3 Penalização por Erro

O fator de penalização é proporcional à dificuldade da pergunta errada:

```
fator_penalizacao = (dificuldade_da_pergunta_errada - 1) / 10
```

Exemplos:

| Dificuldade | Fator de Penalização |
|---|---|
| 1 | 0.0 |
| 5 | 0.4 |
| 10 | 0.9 |

### 6.4 Rating Final

```
rating_final = rating_base × fator_penalizacao
```

### 6.5 Regras Complementares

- Se o usuário errar a **primeira pergunta**, o rating é `0`.
- Não existe **rating negativo**.
- O sistema penaliza fortemente erros básicos.
- O sistema preserva parcialmente a pontuação em erros de alta dificuldade.
- Perguntas fáceis geram menor pontuação base.

---

## 7. Sistema de Usuários

### 7.1 Usuário Comum

**Permissões:**

- Jogar quizzes.
- Visualizar rankings.
- Sugerir perguntas.
- Visualizar mural de colaboradores.

**Login:**

- Email e senha.
- Conta Google.

### 7.2 Sistema de Sugestão de Perguntas

- Usuários podem enviar perguntas para avaliação.
- Status possíveis: `Pendente` | `Aprovada` | `Rejeitada`
- Perguntas aprovadas passam a integrar o pool oficial.

### 7.3 Administrador

**Permissões:**

- Adicionar perguntas.
- Editar perguntas.
- Remover perguntas.
- Aprovar perguntas enviadas por usuários.
- Rejeitar perguntas enviadas por usuários.
- Gerenciar regiões.

---

## 8. Mural de Colaboradores

### 8.1 Finalidade

Reconhecer publicamente os usuários que contribuíram com perguntas aprovadas.

### 8.2 Critério de Inclusão

O usuário deve possuir **pelo menos uma pergunta aprovada**.

### 8.3 Informações Exibidas

| Campo | Descrição |
|---|---|
| Nome do usuário | Identificação |
| Perguntas aprovadas | Quantidade total |
| Primeira contribuição | Data da primeira aprovação |
| Regiões contribuídas | Quando aplicável |

---

## 9. Princípios do Sistema

O GG Quiz é estruturado sobre os seguintes princípios:

- **Justiça competitiva**
- **Meritocracia** baseada em conhecimento
- **Penalização proporcional**
- **Transparência** no ranking
- **Incentivo** à contribuição comunitária
- **Sustentabilidade estatística** do sistema

---

## 10. Estrutura Consolidada

O sistema completo contempla:

- Modo Regional
- Modo Global
- Ranking por período
- Sistema de dificuldade
- Penalização proporcional ao erro
- Login simplificado
- Sistema de moderação
- Mural público de colaboradores
