package etapa4;

import java.io.IOException;
import java.io.PushbackReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import quinas.lexer.Lexer;
import quinas.lexer.LexerException;
import quinas.node.Start;
import quinas.parser.Parser;
import quinas.parser.ParserException;

/** Executa a análise sintática e imprime a árvore sintática abstrata da Quinas. */
public final class Main {
    private Main() {
        // Classe utilitária.
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java etapa4.Main <arquivo.qui>");
            System.exit(2);
        }

        String arquivo = args[0];

        try (PushbackReader reader = new PushbackReader(
                Files.newBufferedReader(Paths.get(arquivo), StandardCharsets.UTF_8), 1024)) {
            Lexer lexer = new Lexer(reader);
            Start arvore = new Parser(lexer).parse();

            ImpressoraArvoreAbstrata impressora = new ImpressoraArvoreAbstrata();
            arvore.apply(impressora);
            impressora.imprimir();
        } catch (LexerException | ParserException e) {
            System.err.println("Erro na análise: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
            System.exit(1);
        }
    }
}
