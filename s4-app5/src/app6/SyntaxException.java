package app6;

import java.util.ArrayList;

public class SyntaxException extends Exception {
    private final String expectedToken;
    private final String actualToken;
    private final int position;
    private final String context;
    
    public SyntaxException(String message, String expectedToken, String actualToken, int position, String context) {
        super(buildMessage(message, expectedToken, actualToken, position, context));
        this.expectedToken = expectedToken;
        this.actualToken = actualToken;
        this.position = position;
        this.context = context;
    }
    
    public SyntaxException(String message, ArrayList<Terminal> remainingTokens, int originalLength) {
        super(buildMessage(message, remainingTokens, originalLength));
        this.expectedToken = null;
        this.actualToken = remainingTokens != null && !remainingTokens.isEmpty() ? remainingTokens.get(0).lexeme : "EOF";
        this.position = originalLength - (remainingTokens != null ? remainingTokens.size() : 0);
        this.context = remainingTokens != null ? remainderToString(remainingTokens) : "";
    }
    
    private static String buildMessage(String message, String expectedToken, String actualToken, int position, String context) {
        StringBuilder sb = new StringBuilder();
        sb.append("Syntax Error at position ").append(position).append(": ").append(message);
        if (expectedToken != null) {
            sb.append("\n  Expected: ").append(expectedToken);
        }
        if (actualToken != null) {
            sb.append("\n  Found: ").append(actualToken);
        }
        if (context != null && !context.isEmpty()) {
            sb.append("\n  Context: ").append(context);
        }
        return sb.toString();
    }
    
    private static String buildMessage(String message, ArrayList<Terminal> remainingTokens, int originalLength) {
        StringBuilder sb = new StringBuilder();
        int position = originalLength - (remainingTokens != null ? remainingTokens.size() : 0);
        sb.append("Syntax Error at position ").append(position).append(": ").append(message);
        
        if (remainingTokens != null && !remainingTokens.isEmpty()) {
            sb.append("\n  Found: ").append(remainingTokens.get(0).lexeme);
            sb.append("\n  Context: ").append(remainderToString(remainingTokens));
        } else {
            sb.append("\n  Found: End of input");
        }
        
        return sb.toString();
    }
    
    private static String remainderToString(ArrayList<Terminal> remainder) {
        if (remainder == null || remainder.isEmpty()) {
            return "EOF";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(remainder.size(), 5); i++) {
            if (i > 0) sb.append(" ");
            sb.append(remainder.get(i).lexeme);
        }
        if (remainder.size() > 5) {
            sb.append("...");
        }
        return sb.toString();
    }
    
    public String getExpectedToken() { return expectedToken; }
    public String getActualToken() { return actualToken; }
    public int getPosition() { return position; }
    public String getContext() { return context; }
}