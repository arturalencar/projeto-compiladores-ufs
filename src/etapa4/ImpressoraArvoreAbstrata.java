package etapa4;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import quinas.analysis.DepthFirstAdapter;
import quinas.node.EOF;
import quinas.node.Node;
import quinas.node.Start;
import quinas.node.Token;

/** Imprime a AST gerada pelo SableCC em formato de árvore. */
public final class ImpressoraArvoreAbstrata extends DepthFirstAdapter {
    private static final class NoArvore {
        private final String rotulo;
        private final List<NoArvore> filhos = new ArrayList<>();

        private NoArvore(String rotulo) {
            this.rotulo = rotulo;
        }
    }

    private final Deque<NoArvore> pilha = new ArrayDeque<>();
    private NoArvore raiz;

    @Override
    public void defaultIn(Node node) {
        // Start é apenas o invólucro criado pelo parser, não um nó da AST.
        if (node instanceof Start) {
            return;
        }

        NoArvore atual = new NoArvore(node.getClass().getSimpleName());
        adicionar(atual);
        pilha.push(atual);
    }

    @Override
    public void defaultOut(Node node) {
        if (!(node instanceof Start)) {
            pilha.pop();
        }
    }

    @Override
    public void defaultCase(Node node) {
        // EOF também é infraestrutura do parser, não pertence à AST.
        if (node instanceof EOF) {
            return;
        }

        if (node instanceof Token) {
            Token token = (Token) node;
            adicionar(new NoArvore(node.getClass().getSimpleName()
                    + " \"" + escapar(token.getText()) + "\""));
        } else {
            adicionar(new NoArvore(node.getClass().getSimpleName()));
        }
    }

    public void imprimir() {
        if (raiz != null) {
            imprimirNo(raiz, "", true, true);
        }
    }

    private void adicionar(NoArvore no) {
        if (pilha.isEmpty()) {
            raiz = no;
        } else {
            pilha.peek().filhos.add(no);
        }
    }

    private void imprimirNo(NoArvore no, String prefixo,
                            boolean ultimo, boolean raizNo) {
        if (raizNo) {
            System.out.println(no.rotulo);
        } else {
            System.out.println(prefixo + (ultimo ? "└── " : "├── ") + no.rotulo);
        }

        for (int i = 0; i < no.filhos.size(); i++) {
            boolean ultimoFilho = i == no.filhos.size() - 1;
            String proximoPrefixo = raizNo
                    ? ""
                    : prefixo + (ultimo ? "    " : "│   ");
            imprimirNo(no.filhos.get(i), proximoPrefixo, ultimoFilho, false);
        }
    }

    private String escapar(String texto) {
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }
}
