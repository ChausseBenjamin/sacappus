package app6;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {
    Terminal value;

    /**Constructeur pour l'initialisation d'attribut(s)
     */
    public FeuilleAST(Terminal lexicalUnit) {  // avec arguments
        value = lexicalUnit;

        if (value.type == TypeUniteLexicale.invalid) {
            System.out.println("Build Err: Leaf: Instanciated with an invalid lexical unit: " + lexicalUnit.toString());
        }
    }

    /** Evaluation de feuille d'AST
    */
    public int EvalAST( ) {

        switch (value.type) {
            case invalid:
                System.out.println("EVAL ERR: Leaf: Lexical unit is invalid: " + value.toString());
                return 0;

            case identifier:
                System.out.println("EVAL ERR: Leaf: Tried to evaluate an identificator: " + value.toString());
                return 0;

            case operator:
                System.out.println("EVAL ERR: Leaf: Operators cannot be a leaf of AST. Critical system error: " + value.toString());
                return 0;

            case delimiter:
                System.out.println("EVAL ERR: Leaf: Delimiter cannot be a leaf of AST: " + value.toString());
                return 0;

            case literal:
                try {
                    int result = Integer.parseInt(value.lexeme);
                    return result;
                } catch(NumberFormatException e){
                    System.out.println("EVAL ERR: Leaf: Could not parse literal lexeme to int: " + value.toString());
                    return 0;
                }

            default:
                System.out.println("EVAL ERR: Leaf: Implementation error. Switch case defaulted. Fix the code.");
                return 0;
        }
    }
    
    /** Lecture de chaine de caracteres correspondant a la feuille d'AST
    */
    public String LectAST( ) {
        return LectAST(0);
    }

    public String LectAST(int depth) {
        return "{\"nodeType\":\"leaf\",\"type\":\"" + value.type + "\",\"lexeme\":\"" + value.lexeme + "\"}";
    }
}
