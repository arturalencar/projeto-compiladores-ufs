package etapa3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import quinas.analysis.DepthFirstAdapter;
import quinas.node.Node;
import quinas.node.Token;

/** Imprime a árvore concreta produzida pelo parser em formato de árvore. */
public final class ImpressoraArvore extends DepthFirstAdapter {
    private static final class NoArvore {
        private final String rotulo;
        private final List<NoArvore> filhos = new ArrayList<>();

        private NoArvore(String rotulo) {
            this.rotulo = rotulo;
        }
    }

    private final Deque<NoArvore> pilha = new ArrayDeque<>();
    private final boolean completa;
    private NoArvore raiz;

    /** Por padrão, oculta apenas os nós de passagem das expressões. */
    public ImpressoraArvore() {
        this(false);
    }

    public ImpressoraArvore(boolean completa) {
        this.completa = completa;
    }

    @Override
    public void defaultIn(Node node) {
        String nome = node.getClass().getSimpleName();
        if (nome.startsWith("A")) {
            nome = nome.substring(1);
        }
        NoArvore atual = new NoArvore(nome);
        adicionar(atual);
        pilha.push(atual);
    }

    @Override
    public void defaultOut(Node node) {
        pilha.pop();
    }

    @Override
    public void defaultCase(Node node) {
        if (node instanceof Token) {
            Token token = (Token) node;
            adicionar(new NoArvore(token.getClass().getSimpleName()
                    + " \"" + escapar(token.getText()) + "\""));
        } else {
            adicionar(new NoArvore(node.getClass().getSimpleName()));
        }
    }

    /** Deve ser chamado depois de a raiz aplicar este visitor. */
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

        List<NoArvore> filhos = filhosVisiveis(no);
        for (int i = 0; i < filhos.size(); i++) {
            boolean ultimoFilho = i == filhos.size() - 1;
            String proximoPrefixo = raizNo
                    ? ""
                    : prefixo + (ultimo ? "    " : "│   ");
            imprimirNo(filhos.get(i), proximoPrefixo, ultimoFilho, false);
        }
    }

    private List<NoArvore> filhosVisiveis(NoArvore no) {
        List<NoArvore> visiveis = new ArrayList<>();
        for (NoArvore filho : no.filhos) {
            if (!completa && ehNoDePassagem(filho)) {
                visiveis.addAll(filhosVisiveis(filho));
            } else {
                visiveis.add(filho);
            }
        }
        return visiveis;
    }

    /** Produções que apenas encaminham para o próximo nível de precedência. */
    private boolean ehNoDePassagem(NoArvore no) {
        return no.filhos.size() == 1
                && (no.rotulo.equals("Exp") || no.rotulo.startsWith("BaseExp"));
    }

    private String escapar(String texto) {
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }
}
