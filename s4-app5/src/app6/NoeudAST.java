package app6;

/** @author Ahmed Khoumsi */

import java.lang.reflect.Type;

/** Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

  // Attributs
  ElemAST left;
  ElemAST right;
  Terminal pivot;

  /** Constructeur pour l'initialisation d'attributs
   */
  public NoeudAST(Terminal input, ElemAST left, ElemAST right ) { // avec arguments
    this.right = right;
    this.left = left;    pivot = input;

    if (pivot.type == TypeUniteLexicale.invalid) {
      System.out.println("Build Err: Node:  Lexical unit is of type invalid: " + pivot.toString());
    }
  }

 
  /** Evaluation de noeud d'AST
   */
  public int EvalAST( ) {

    switch (pivot.type) {
      case operator:
        switch (pivot.lexeme) {
          case "+":
            return left.EvalAST() + right.EvalAST();
          case "*":
            return left.EvalAST() * right.EvalAST();
          case "-":
            return left.EvalAST() - right.EvalAST();
          case "/":
            return left.EvalAST() / right.EvalAST();
          default:
            System.out.println("EVAL ERR: Node: Recognized an operator but did not have the tools to perform the evaluation:" + pivot.toString());
            return 0;
        }

      case literal:
        System.out.println("EVAL WARN: Node: You have a literal in the AST as a node... Bruh?: " + pivot.toString());
        return 0;

      case identifier:
        System.out.println("EVAL ERR: Node: A node has an identifier. It shouldn't happen and cannot be evaluated.");
        return 0;

      case delimiter:
        System.out.println("EVAL ERR: Node: A delimiter was found. The AST should not have delimiters: " + pivot.toString());
        return 0;

      default:
        System.out.println("EVAL ERR: Node: Implementation error: Pivot type switch case defaulted. You forgot to change this code.");
        return 0;
    }
  }


  /** Lecture de noeud d'AST
   */
  public String LectAST( ) {
     return LectAST(0);
  }

  public String LectAST(int depth) {
    return "{\"nodeType\":\"node\",\"type\":\"" + pivot.type + "\",\"lexeme\":\"" + pivot.lexeme + "\",\"left\":" + left.LectAST(depth + 1) + ",\"right\":" + right.LectAST(depth + 1) + "}";
  }
}


