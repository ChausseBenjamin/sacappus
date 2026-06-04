package menufact.plats;

import ingredients.Ingredient;
import menufact.plats.prices.PriceFlyweight;
import menufact.plats.PlatAuMenu;
import menufact.plats.Factory;

import java.util.ArrayList;

public class PlatMemento {

    // Data required to recreate a Plat
    int code;
    String nom;
    String description;
    PriceFlyweight prix;
    ArrayList<Ingredient> ingredients;
    /**
     * This is required when building a PlatEnfant
     * It is set to 1 if a Plat is not a PlatEnfant
     */
    float proportion;
    // Constructor
    public PlatMemento(PlatAuMenu plat){
        this.code = plat.getCode();
        this.nom = plat.getDescription();
        this.description = plat.getDescription();
        this.prix = plat.getPrixFlyweight();
        this.ingredients = plat.getIngredients();
        // Store wether the Plat is a PlatEnfant or not
        if (plat instanceof PlatEnfant){
            this.proportion = ((PlatEnfant) plat).getProportion();
        } else{
            this.proportion = 1;
        }
    };
    /** The following methods allows to match the PlatMemento with the Plat it was created from
     * This is required to recover a Plat from a PlatMemento
     */
    public int getCode() {
        return code;
    }
    /** The following method allows to check the required ingredients needed to make a Plat
     * This is required to check if a plat can be recovered (when the inventory is updated).
     */
    public ArrayList<Ingredient> getIngredients () {
        return ingredients;
    }
    // Recovering deleted Plat by rebuilding it
    public PlatAuMenu recover(){
        Factory factory = new Factory();
        PlatAuMenu plat = factory.CreatePlat(code, description, prix, ingredients);
        if (proportion != 1){
            plat = factory.CreatePlat(plat, proportion);
        }
        return plat;
    }

    public float getProportion() {
        return proportion;
    }
}
