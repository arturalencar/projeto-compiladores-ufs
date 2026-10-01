package quinas;

import quinas.lexer.*;
import quinas.node.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {
            String arquivo = "src/etapa1/codigo1.qui";

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
            System.out.println(e.getMessage());
        }
    }
}
