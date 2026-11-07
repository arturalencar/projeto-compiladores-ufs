package etapa3;

import java.io.IOException;
import java.io.PushbackReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import quinas.lexer.Lexer;
import quinas.lexer.LexerException;
import quinas.node.InvalidToken;
import quinas.node.TComentarioIncompleto;
import quinas.parser.Parser;
import quinas.parser.ParserException;

public final class Main {
    public static void main(String[] args) {
        boolean completa = args.length == 2 && "--completa".equals(args[0]);
        if (args.length != 1 && !completa) {
            System.err.println("Uso: java etapa3.Main [--completa] <arquivo.qui>");
            System.exit(2);
        }
        String arquivo = completa ? args[1] : args[0];

        try (PushbackReader reader = new PushbackReader(
                Files.newBufferedReader(Paths.get(arquivo), StandardCharsets.UTF_8), 1024)) {
            Lexer lexer = new Lexer(reader) {
                @Override
                protected void filter() throws LexerException {
                    if (token instanceof TComentarioIncompleto) {
                        throw new LexerException(
                                new InvalidToken(token.getText(), token.getLine(), token.getPos()),
                                "[" + token.getLine() + "," + token.getPos()
                                        + "] Comentário de bloco não terminado.");
                    }
                }
            };
            ImpressoraArvore impressora = new ImpressoraArvore(completa);
            new Parser(lexer).parse().apply(impressora);
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
