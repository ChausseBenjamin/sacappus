package inventaire;

import ingredients.Ingredient;
import ingredients.exceptions.IngredientException;
import menufact.Menu;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Objects;

public class Inventaire {

    // Initialisation
    private static Inventaire instance = null;

    private Inventaire(){}

    public static Inventaire getInstance(){
        if (instance == null) {
            instance = new Inventaire();
        }
        return instance;
    }

    // Data
    private ArrayList<Ingredient> lesIngredients = new ArrayList<Ingredient>();

    // Methods

    public void ajouter (Ingredient ingredient) {
        lesIngredients.add(ingredient);
    }

    public void updateStockIngredient (Ingredient ingredient,int qty) throws IngredientException {
        for (Ingredient i: lesIngredients){
            try  {
                if (Objects.equals(i.getNom(), ingredient.getNom())) {
                    i.updateQuantity(qty);}
            } catch (Exception e) {
                // TODO: is this the right way to handle this exception?
                System.err.println(e.getMessage());
                System.err.println("Failed to update quantity of " + ingredient.getNom() + ".");
            }
        }
    }

    public Ingredient getIngredient(Ingredient ingredient) {
        // TODO: Réimplémenter le code pour qu'il fonctionne avec les nouvelles classes d'ingrédients.
        // Tries to find if this ingredient exists in the inventory
        for(Ingredient i: lesIngredients) {
            if (Objects.equals(i.getNom(), ingredient.getNom())) {
                return i;
            }
        }
        // If it isn't found, add an instance of it with a quantity of 0
        Ingredient newIngredient = null;
        try {
            Constructor<? extends Ingredient> constructor = ingredient.getClass().getDeclaredConstructor();
            newIngredient = constructor.newInstance(
                    ingredient.getNom(),
                    ingredient.getDescription(),
                    0,
                    ingredient.getKcalPercent(),
                    ingredient.getFatPercent(),
                    ingredient.getCholPercent(),
                    ingredient.isSante()
            );
        } catch (Exception e) {
            System.err.println("Failed to create new instance of " + ingredient.getClass().getName() + ". Using default constructor instead.");
        }
        ajouter(newIngredient);

        return newIngredient;
    }
}
