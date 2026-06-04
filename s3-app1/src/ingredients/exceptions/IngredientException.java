package ingredients.exceptions;

public class IngredientException extends Exception{
    public IngredientException(String message){
        super("IngredientException: " + message);
    }
}
