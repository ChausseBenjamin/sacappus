package app6;

import java.util.ArrayList;

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  // Attributs
  public AnalLex lexicalAnalyzer;
  ArrayList<Terminal> lexicalUnits;
  private int originalTokenCount;
  /** Constructeur de DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
  */
  public DescenteRecursive(String inputFilePath, String outputSyntaxicFilePath, String outputLexicalFilePath) {
    System.out.println("Reading lexical output...");
    Reader lexReader = new Reader(inputFilePath);
    System.out.println("Parsing into lexical units...");
    AnalLex lexicalAnalyzer = new AnalLex(lexReader.toString()); // <- Constructor already build the array
    if (lexicalAnalyzer.isWrong) {
      System.out.println("OUATTE DE PHOQUE!");
      // ErreurSynt("Cannot continue further because the parsing failed");
    } else {
      lexicalUnits = lexicalAnalyzer.terminalArray;
      originalTokenCount = lexicalUnits.size();
    }  }
/** ErreurSynt() envoie un message d'erreur syntaxique avec contexte
 */
  public void ErreurSynt(String message, ArrayList<Terminal> remainingTokens) throws SyntaxException {
    throw new SyntaxException(message, remainingTokens, originalTokenCount);
  }
  
  public void ErreurSynt(String message, String expected, String actual, ArrayList<Terminal> remainingTokens) throws SyntaxException {
    int position = originalTokenCount - (remainingTokens != null ? remainingTokens.size() : 0);
    String context = remainingTokens != null ? remainderRawStr(remainingTokens) : "";
    throw new SyntaxException(message, expected, actual, position, context);
  }
//Methode principale a lancer pour tester l'analyseur syntaxique
public static void main(String[] args) {
  String toWriteLect = "";
  String toWriteEval = "";

  System.out.println("Debut d'analyse syntaxique");

  if (args.length < 3) {
    String baseDir = System.getProperty("user.dir");
    args = new String[] {
      baseDir + "/ResultatSyntaxique.txt",
      baseDir + "/ResultatLexical.txt",
      baseDir + "/ExpArith.txt"
    };
  }

  DescenteRecursive dr = new DescenteRecursive(args[2], args[0], args[1]);

  try {
    ElemAST RacineAST = dr.AnalSynt();
    toWriteLect += "Lecture de l'AST trouve : " + RacineAST.LectAST() + "\n";
    System.out.println(toWriteLect);
    toWriteEval += "Evaluation de l'AST trouve : " + RacineAST.EvalAST() + "\n";
    System.out.println(toWriteEval);
    Writer w = new Writer(args[0], toWriteLect + toWriteEval);
  } catch (Exception e) {
    System.out.println(e);
    e.printStackTrace();
    System.exit(50);
  }

  System.out.println("Analyse syntaxique terminee");
}


  /** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
    *    Elle retourne une reference sur la racine de l'AST construit
  */
  public ElemAST AnalSynt() throws SyntaxException {
    System.out.println("Analysing... Calling E...");
    RecDescentResp result = E(lexicalUnits);
    if (!util.isNullOrEmpty(result.remainder)) {
      ErreurSynt("Failed to parse complete expression - unexpected tokens remain", result.remainder);
    }
    System.out.println("Analysis over... Generating Postfix Notation");
    System.out.println(result.elem.AsPostFix());
    System.out.println("Postfix Notation Complete");
    return result.elem;
  }

  // Methode pour chaque symbole non-terminal de la grammaire retenue
  public RecDescentResp E(ArrayList<Terminal> parseData) throws SyntaxException {
    System.out.printf("Analysing... Calling T... Remaining: %s\n", remainderRawStr(parseData));
    RecDescentResp resp = T(parseData);
    ElemAST elemT = resp.elem;
    ArrayList<Terminal> remainder = resp.remainder;

    // Handle left-associative addition/subtraction
    while (!util.isNullOrEmpty(remainder)) {
      Terminal pivot = remainder.get(0);
      String pivotStr = pivot.lexeme;
      
      if (pivotStr.equals("+") || pivotStr.equals("-")) {
        ArrayList<Terminal> rightTokens = new ArrayList<>(remainder.subList(1, remainder.size()));
        if (rightTokens.isEmpty()) {
          ErreurSynt("Expected operand after operator", "number or expression", "end of input", remainder);
        }
        RecDescentResp right = T(rightTokens);
        elemT = new NoeudAST(pivot, elemT, right.elem);
        remainder = right.remainder;
      } else {
        break;
      }
    }
    return new RecDescentResp(remainder, elemT);
  }

  public RecDescentResp T(ArrayList<Terminal> parseData) throws SyntaxException {
    System.out.printf("Analysing... Calling P... Remaining: %s\n", remainderRawStr(parseData));
    RecDescentResp resp = P(parseData);
    ElemAST elemP = resp.elem;
    ArrayList<Terminal> remainder = resp.remainder;

    // Handle left-associative multiplication/division
    while (!util.isNullOrEmpty(remainder)) {
      Terminal pivot = remainder.get(0);
      String pivotStr = pivot.lexeme;
      
      if (pivotStr.equals("*") || pivotStr.equals("/")) {
        ArrayList<Terminal> rightTokens = new ArrayList<>(remainder.subList(1, remainder.size()));
        if (rightTokens.isEmpty()) {
          ErreurSynt("Expected operand after operator", "number or expression", "end of input", remainder);
        }
        RecDescentResp right = P(rightTokens);        System.out.printf("Analysing... Calling P... Remaining: %s\n", remainderRawStr(right.remainder));
        elemP = new NoeudAST(pivot, elemP, right.elem);
        remainder = right.remainder;
      } else {
        break;
      }
    }

    return new RecDescentResp(remainder, elemP);
  }
  public RecDescentResp P(ArrayList<Terminal> parseData) throws SyntaxException {
    // When reaching P(), you MUST be able to release at least 1 literal
    if (util.isNullOrEmpty(parseData)) {
      ErreurSynt("Expected expression", "literal, identifier, or '('", "end of input", parseData);
    };

    Terminal pivot = parseData.get(0); // In this case the "pivot" is either our leaf or an opening "("
    String pivotStr = pivot.lexeme;
    ArrayList<Terminal> remainder = new ArrayList<>(parseData.subList(1, parseData.size()));

    switch (pivot.type) {
      case delimiter:
        // Ensure we start a delim block with an opening paren
        if ( !pivotStr.equals("(")) {
          ErreurSynt("Expected opening parenthesis", "'('", pivotStr, parseData);
        }
        // Parse the inside/inner (we expect there to be a remaining/trailing ')' after that pass)
        System.out.printf("Analysing... Calling E... Remaining: %s\n", remainderRawStr(remainder));
        RecDescentResp inner = E(remainder);
        if (util.isNullOrEmpty(inner.remainder)) {
          ErreurSynt("Expected closing parenthesis", "')'", "end of input", inner.remainder);
        }
        if ( !inner.remainder.get(0).lexeme.equals(")")) {
          ErreurSynt("Expected closing parenthesis", "')'", inner.remainder.get(0).lexeme, inner.remainder);
        }
        return new RecDescentResp(
          new ArrayList<>(inner.remainder.subList(1,inner.remainder.size())),
          inner.elem
        );

      case identifier,literal:
        System.out.printf("Found literal/identifier: %s, Remaining: %s\n", pivotStr, remainderRawStr(remainder));
        return new RecDescentResp(
          remainder,
          new FeuilleAST(pivot)
        );

      default:
        ErreurSynt("Unexpected token", "literal, identifier, or '('", pivotStr, parseData);
        return null; // This will never be reached due to exception
    }

  }
  public static String remainderRawStr(ArrayList<Terminal> arr) {
    StringBuilder s = new StringBuilder();
    for (Terminal term : arr) {
      s.append(term.lexeme);
    }
    return s.toString();
  }

}
