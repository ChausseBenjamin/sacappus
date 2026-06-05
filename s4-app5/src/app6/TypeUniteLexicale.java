package app6;

public enum TypeUniteLexicale {

    /**
     * Not recognized by the allowed alphabet of the lexical unit.
     */
    invalid,

    /**
     * +, -, *, /
     */
    operator,

    /**
     * Numbers
     */
    literal,

    /**
     * variables starting with capitalized letter
     */
    identifier,

    /**
     * Parenthesis
     */
    delimiter
}
