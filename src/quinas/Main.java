package quinas;

import quinas.lexer.*;
import quinas.node.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java quinas.Main <arquivo.qui>");
            System.exit(2);
        }

        try {
            String arquivo = args[0];
            Lexer lexer = new Lexer(
                    new PushbackReader(
                            new FileReader(arquivo), 1024
                    )
            );

            Token token;

            while (!((token = lexer.next()) instanceof EOF)) {
                System.out.println(token.getClass());
                System.out.println(" ( " + token + " ) ");
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
