# Projeto de Compiladores - UFS

Este repositório foi criado para o desenvolvimento do projeto da disciplina de Compiladores da Universidade Federal de Sergipe (UFS). O objetivo é aplicar os conceitos teóricos estudados em sala de aula na construção de um compilador funcional, em etapas, seguindo as boas práticas de análise léxica, sintática e semântica.

### Etapa 1
A pasta `src/etapa1/` representa a primeira fase do projeto, consiste na implementação de 3 códigos-fonte utilizando todos os recursos da linguagem QUINAS (funcional):

- C1: Estrutura básica, tipos, blocos e operações;
- C2: Booleanos, guardas e recursão;
- C3: Funções de alta ordem e assinaturas.

### Etapa 2
A segunda fase do projeto consiste na implementação do **Analisador Léxico (Lexer)** para a linguagem QUINAS, utilizando a ferramenta geradora de compiladores **SableCC**:

- **Especificação Léxica (`src/etapa2/grupo_08.sable`)**:
  - **Helpers**: Definições auxiliares para caracteres Unicode, dígitos decimais e binários (`0b...`), letras maiúsculas/minúsculas e regras de composição para comentários;
  - **Tokens**:
    - **Tipos de dados**: `numero` e `booleano`;
    - **Literais**: Números inteiros (decimais e binários), números reais (utilizando vírgula `,`) e valores booleanos (`V` e `F`);
    - **Operadores**: Aritméticos (`+`, `-`, `.`, `/`, `//`, `\`, `´`), relacionais (`<<`, `>>`, `==`) e lógicos (`^`, `v`, `¬`);
    - **Identificadores**: Regras léxicas específicas para funções (`id_funcao`), constantes (`id_constante`) e parâmetros (`id_parametro`);
    - **Delimitadores e pontuações**: Precedência (`<`, `>`), blocos (`[`, `]`), guardas (`||`, `#`, `=`), separadores (`|`), declaração de retorno (`:`) e ponto de entrada (`++`);
  - **Ignored Tokens**: Descarte automático de caracteres em branco/espaçamento (`vazio_tok`), comentários de linha (`--`) e comentários de bloco (`/-- ... --/`).

- **Código Gerado e Testes (`src/quinas/`)**:
  - Pacotes gerados pelo SableCC (`quinas.lexer`, `quinas.node`, `quinas.analysis`);
  - **`Main.java`**: Classe de teste responsável por instanciar o `Lexer` a partir de um arquivo-fonte da linguagem QUINAS (como os exemplos de `src/etapa1/`) através de um `PushbackReader`, iterando sobre o fluxo de tokens e imprimindo na saída padrão a classe do token e o lexema correspondente até o fim do arquivo (`EOF`).

---

Desenvolvido para a disciplina de Compiladores.
