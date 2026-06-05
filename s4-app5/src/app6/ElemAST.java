package app6;

/** @author Ahmed Khoumsi */

/** Classe Abstraite dont heriteront les classes FeuilleAST et NoeudAST
 */
public abstract class ElemAST {

  
  /** Evaluation d'AST
   */
  public abstract int EvalAST();


  /** Lecture d'AST
   */
  public abstract String LectAST();


/** ErreurEvalAST() envoie un message d'erreur lors de la construction d'AST
 */  
  public void ErreurEvalAST(String s) {	
    // 
  }

  public abstract String LectAST(int depth);

  public String AsPostFix() {
    StringBuilder b = new StringBuilder();
    if (this instanceof NoeudAST e) { // spaces avoids "12 3" becoming "123"...
      b.append(e.left.AsPostFix()).append(" ")
       .append(e.right.AsPostFix()).append(" ")
       .append(e.pivot.lexeme).append(" ");
    } else if ( this instanceof FeuilleAST e) {
      b.append(e.value.lexeme).append(" ");
    }
    return b.toString();
  }

}
