package app6;

import java.util.Set;

/**
 * Contient la définition des charactères permis pour chaque type d'unité lexical.
 */
public class Alphabet {
    private static final Set<Character> operators = Set.of('+', '-', '/', '*');
    private static final Set<Character> delimiter = Set.of('(', ')');
    private static final Set<Character> identifierStart = Set.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
    private static final Set<Character> identifier = Set.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '_');
    private static final Set<Character> literal = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

    /**
     * Check si un charactere fait partie de l'alphabet de l'analyseur lexical.
     * Vas toujours prendre le premier char de la string donnee.
     * @param input
     * @return
     */
    public static boolean isInAlphabet(String input) {
        return Alphabet.isInAlphabet(input.charAt(0));
    }

    /**
     * Check si un charactere fait partie de l'alphabet de l'analyseur lexical.
     * @param input
     * @return
     */
    public static boolean isInAlphabet(char input) {
        if (Alphabet.isIdentifier(input)) {
            return true;
        }

        if (Alphabet.isLiteral(input)) {
            return true;
        }

        if (Alphabet.isOperator(input)) {
            return true;
        }

        if (Alphabet.isDelimiter(input)) {
            return true;
        }

        return false;
    }

    public static boolean isLiteral(String input) {
        return Alphabet.isLiteral(input.charAt(0));
    }

    public static boolean isLiteral(char input) {
        return literal.contains(input);
    }

    public static boolean isOperator(String input) {
        return Alphabet.isOperator(input.charAt(0));
    }

    public static boolean isOperator(char input) {
        return operators.contains(input);
    }

    public static boolean isDelimiter(String input) {
        return Alphabet.isDelimiter(input.charAt(0));
    }

    public static boolean isDelimiter(char input) {
        return delimiter.contains(input);
    }

    public static boolean isIdentifier(String input) {
        return Alphabet.isDelimiter(input.charAt(0));
    }

    public static boolean isIdentifier(char input) {
        return identifier.contains(input);
    }

    public static boolean isIdentifierStart(String input) {
        return Alphabet.isIdentifierStart(input.charAt(0));
    }

    public static boolean isIdentifierStart(char input) {
        return identifierStart.contains(input);
    }
}
