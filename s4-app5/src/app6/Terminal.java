package app6;

/** @author Ahmed Khoumsi */

/** Cette classe identifie les terminaux reconnus et retournes par
 *  l'analyseur lexical
 */
public class Terminal {

  /**
   * Contenue / valeur de l'unité lexical.
   * Un lexeme c'est la chaine de charactères de l'Unité lexicale
   */
  public String lexeme = "";

  /**
   * Indique le type de l'unité lexical.
   * Operateur / nombre / etc.
   */
  TypeUniteLexicale type = TypeUniteLexicale.invalid;


  public boolean isNumeric() {
    try {
      Double.parseDouble(lexeme);
      return true;
    } catch(NumberFormatException e){
      return false;
    }
  }


  /** Un ou deux constructeurs (ou plus, si vous voulez)
   *  pour l'initalisation d'attributs
   */
  public Terminal( String unit, TypeUniteLexicale typeUniteLexicale ) {   // arguments possibles
     lexeme = unit;
     type = typeUniteLexicale;
  }

  public String toString() {
    return "type:\t" + type + "\tlexeme:\t" + lexeme;
  }

}
