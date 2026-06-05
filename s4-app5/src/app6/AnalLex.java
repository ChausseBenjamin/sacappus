package app6;

/** @author Ahmed Khoumsi */

import java.util.ArrayList;

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

  // Attributs
  //  ...
  static int state = 0;
  String expression = "";
  String currentLexeme = "";
  int charPosition = 0;

  boolean isWrong = false;
  ArrayList<Terminal> terminalArray = new ArrayList<Terminal>();

	
  /** Constructeur pour l'initialisation d'attribut(s)
   */
  public AnalLex( ) {
    //
  }

  public AnalLex(String fileContents ) {
    expression = fileContents;

    // Remove all whitespaces from the string.
    expression = expression.replaceAll("\\s+","");

    System.out.println("Cleaned expression: " + expression);

    Analyze();
  }

  /**
   * Méthode qui performe l'automate à états finis.
   * Créé le tableau de Terminal.
   */
  public void Analyze() {
    if (expression.isEmpty()) {
      ErreurLex("Fichier vide");
      return;
    }

    int executionTracker = 0;
    while (Automaton()) {
      executionTracker++;
      if (executionTracker > 100) {
        ErreurLex("Too many expressions OR your program sucks");
        return;
      }
    }
    System.out.println();
  }

  /**
   * Machine à état d'analyze lexical.
   * Doit être executé jusqu'à ce que ça return false.
   * @return false si c'est terminé, true si y'en reste à faire.
   */
  private boolean Automaton() {

    char currentCharacter = expression.charAt(charPosition);

    System.out.println("Current char: " + currentCharacter + " state: " + state + " char pos: " + charPosition);

    if (!Alphabet.isInAlphabet(currentCharacter)) {
      ErreurLex("Invalid character: " + currentCharacter);
      return false;
    }

    switch (state) {
      // État initial.
      case 0:
        // On commence à lire une variable
        if (Alphabet.isIdentifierStart(currentCharacter)) {
          state = 5;
          AddToLexeme(currentCharacter);
          if (ReachedTheEnd()) {
            CreateLexicalUnit(TypeUniteLexicale.identifier);
            return false;
          }
          return true;
        }

        // On commence à lire un nombre
        if (Alphabet.isLiteral(currentCharacter)) {
          state = 3;
          AddToLexeme(currentCharacter);
          if (ReachedTheEnd()) {
            CreateLexicalUnit(TypeUniteLexicale.literal);
            return false;
          }
          return true;
        }

        // Unité lexical créé tout de suite au lieu de perdre du temps dans l'état 2 d'écriture.
        if (Alphabet.isOperator(currentCharacter)) {
          state = 0;
          AddToLexeme(currentCharacter);
          CreateLexicalUnit(TypeUniteLexicale.operator);
          if (ReachedTheEnd()) {
            return false;
          }
          return true;
        }

        // Créé tout de suite au lieu d'avoir un état 1 d'écriture d'unité lexical.
        if (Alphabet.isDelimiter(currentCharacter)) {
          state = 0;
          AddToLexeme(currentCharacter);
          CreateLexicalUnit(TypeUniteLexicale.delimiter);
          if (ReachedTheEnd()) {
            return false;
          }
          return true;
        }

        if (Alphabet.isIdentifier(currentCharacter)) {
          if (Character.isLowerCase(currentCharacter)) {
            ErreurLex("Identifiers cannot start with lowercase character. You used: '" + currentCharacter + "'");
            return false;
          }

          if (currentCharacter == '_') {
            ErreurLex("Identifiers cannot start with an underscore.");
            return false;
          }

          ErreurLex("Invalid starting character to an identifier: '" + currentCharacter + "'");
          return false;
        }

        ErreurLex("Engineering skill issue, reached the end of case 0");
        return false;

      // Lecture d'un nombre
      case 3:
        // Le nombre est pas encore terminé
        if (Alphabet.isLiteral(currentCharacter)) {
          state = 3;
          AddToLexeme(currentCharacter);

          if (ReachedTheEnd()) {
            CreateLexicalUnit(TypeUniteLexicale.literal);
            return false;
          }
          return true;
        }

        // Pas un nombre! Donc c'est la fin du lexeme et on peut recommencer.
        CreateLexicalUnit(TypeUniteLexicale.literal);
        state = 0;
        return true;

      // Lecture d'un identificateur
      case 5:
        if (currentCharacter == '_') {
          state = 7;
          AddToLexeme(currentCharacter);
          if (ReachedTheEnd()) {
            ErreurLex("Identificator cannot end with '_'");
            return false;
          }
          return true;
        }

        if (Alphabet.isIdentifier(currentCharacter)) {
          state = 5;
          AddToLexeme(currentCharacter);
          if (ReachedTheEnd()) {
            CreateLexicalUnit(TypeUniteLexicale.identifier);
            return false;
          }
          return true;
        }

        // Fin du lexeme! On retourne à l'état 0 pour qu'il gère le charactère actuel
        CreateLexicalUnit(TypeUniteLexicale.identifier);
        state = 0;
        return true;

      // Vérification d'un identificateur avec un underscore dedans.
      case 7:
        if (currentCharacter == '_') {
          ErreurLex("Identificator cannot have 2 consecutive underscores.");
          return false;
        }

        // Le underscore à été utilisé correctement.
        if (Alphabet.isIdentifier(currentCharacter)) {
          state = 5;
          AddToLexeme(currentCharacter);
          if (ReachedTheEnd()) {
            CreateLexicalUnit(TypeUniteLexicale.identifier);
            return false;
          }
          return true;
        }

        // L'identificateur termine par un underscore! C'est pas bon!
        CreateLexicalUnit(TypeUniteLexicale.identifier);
        state = 0;
        ErreurLex("Identificator cannot end with an underscore");
        return false;
    }

    ErreurLex("Skill issue: Reached below the case statement");
    return false;
  }

  private void AddToLexeme(char toAdd) {
    currentLexeme = currentLexeme + toAdd;
    charPosition++;
  }

  private void CreateLexicalUnit(TypeUniteLexicale type) {
    Terminal createdUnit = new Terminal(currentLexeme, type);
    terminalArray.add(createdUnit);
    currentLexeme = "";
  }

  private boolean ReachedTheEnd() {
    boolean reached = charPosition == expression.length();
    if (reached) {
      System.out.println("Reached the end of expression");
    }
    return reached;
  }


  /** resteTerminal() retourne :
      false  si tous les terminaux de l'expression arithmetique ont ete retournes
      true s'il reste encore au moins un terminal qui n'a pas ete retourne 
  */
  public boolean resteTerminal( ) {
    return terminalArray.size() > 0;
  }
  
  
  /** prochainTerminal() retourne le prochain terminal
      Cette methode est une implementation d'un AEF
  */
  public Terminal prochainTerminal( ) {
    Terminal toReturn = terminalArray.get(0);
    terminalArray.remove(0);
    return toReturn;
  }

 
  /** ErreurLex() envoie un message d'erreur lexicale
  */
  public void ErreurLex(String s) {	
    System.out.println("LEX ERROR: " + s);
    isWrong = true;
  }

  public static void end(String outputFileName, String toWrite) {
    Writer w = new Writer(outputFileName,toWrite); // Ecriture de toWrite dans fichier args[1]
    System.out.println("Writing to output file " + outputFileName);
  }
  
  //Methode principale a lancer pour tester l'analyseur lexical
  public static void main(String[] args) {
    String toWrite = "";
    System.out.println("Debut d'analyse lexicale");
    System.out.println("Working Directory = " + System.getProperty("user.dir"));

    if (args.length == 0) { // Aucun arguments données
      args = new String [2];
      args[0] = System.getProperty("user.dir") + "/ExpArith.txt";
      args[1] = System.getProperty("user.dir") + "/ResultatLexical.txt";
    }

    Reader r = new Reader(args[0]);
    System.out.println("Contenue de l'expression = " + r.toString());

    System.out.println("Roulement de l'analyseur...");
    AnalLex lexical = new AnalLex(r.toString()); // Creation de l'analyseur lexical

    if(lexical.isWrong) {
      System.out.println("\tAn error was detected. The contents cannot be used.");
    } else {
      System.out.println("\tNo lexical errors detected.");
    }

    System.out.println("Printing the contents of the lexical units:\n\n");

    // Execution de l'analyseur lexical
    Terminal t = null;
    while(lexical.resteTerminal()) {
      t = lexical.prochainTerminal();
      toWrite += t.toString();
      System.out.println(t.toString());
    }
    System.out.println("\nFin d'analyse lexicale");
    end(args[1], toWrite);
  }
}
