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
    - **Identificadores**: Regras léxicas específicas para funções (`id_funcao`), constantes (`id_constante`) e parâmetros (`id` genérico; a regra de minúsculas para parâmetros primitivos é semântica);
    - **Delimitadores e pontuações**: Precedência (`<`, `>`), blocos (`[`, `]`), guardas (`||`, `#`, `=`), separadores (`|`), declaração de retorno (`:`) e ponto de entrada (`++`);
  - **Ignored Tokens**: Descarte automático de caracteres em branco/espaçamento (`vazio_tok`), comentários de linha (`--`) e comentários de bloco (`/-- ... --/`).

- **Código Gerado e Testes (`src/quinas/`)**:
  - Pacotes gerados pelo SableCC (`quinas.lexer`, `quinas.node`, `quinas.analysis`);
  - **`Main.java`**: Classe de teste responsável por instanciar o `Lexer` a partir de um arquivo-fonte da linguagem QUINAS (como os exemplos de `src/etapa1/`) através de um `PushbackReader`, iterando sobre o fluxo de tokens e imprimindo na saída padrão a classe do token e o lexema correspondente até o fim do arquivo (`EOF`).

### Etapa 3

O analisador sintático está em `src/etapa3/grupo_08.sable`. A gramática inclui precedência de operadores, guardas, constantes e assinaturas de funções. `Main.java` imprime a árvore sintática concreta após uma análise bem-sucedida; erros incluem a posição informada pelo SableCC e encerram a execução com código 1.

A árvore é exibida em formato compacto por padrão, com conectores entre os nós e sem as produções intermediárias de precedência. Para mostrar todos os nós da árvore concreta, passe `--completa` antes do caminho do arquivo, por exemplo: `etapa3.Main --completa src/etapa3/exemplo.qui`.

Com JDK e SableCC 3.7, execute na raiz do projeto (PowerShell), ajustando o caminho do JAR:

```powershell
New-Item -ItemType Directory -Force out/etapa3/gerado, out/etapa3/classes | Out-Null
java -jar "caminho/para/sablecc.jar" -d out/etapa3/gerado src/etapa3/grupo_08.sable
$fontes = @((Get-ChildItem out/etapa3/gerado -Recurse -Filter *.java).FullName) + @((Get-ChildItem src/etapa3 -Filter *.java).FullName)
javac -encoding UTF-8 -d out/etapa3/classes $fontes
java -cp "out/etapa3/classes;out/etapa3/gerado" etapa3.Main src/etapa3/exemplo.qui
```

A geração fica separada dos arquivos da etapa 2; o classpath inclui as tabelas `.dat` geradas pelo SableCC. As entradas `.qui` devem estar em UTF-8.

O exemplo da etapa 3 segue a gramática da v2: constantes têm um único nome em maiúsculas (`[ numero TOTAL [10] ]`), conforme esclarecido para esta implementação. Guardas e blocos de expressão possuem seus próprios colchetes, além dos colchetes do corpo da função. Os exemplos originais da etapa 1 foram preservados e contêm divergências em relação a essas regras.

---

Desenvolvido para a disciplina de Compiladores.
