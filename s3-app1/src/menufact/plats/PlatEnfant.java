package menufact.plats;

import ingredients.Ingredient;
import menufact.plats.prices.PriceFlyweight;

import java.util.ArrayList;

public class PlatEnfant extends Plat{
    private float proportion;

    public PlatEnfant() {
    }
    public PlatEnfant(int code, String description, PriceFlyweight prix, float proportion, ArrayList<Ingredient> ingredients) {
        super(code, description, prix, ingredients);
        this.proportion = proportion;
    }
    public void setPrixFlyweight(PriceFlyweight prix) {
        this.prix = prix;
    }
    public float getProportion() {
        return proportion;
    }

    @Override
    public String toString() {
        return "PlatEnfant{" +
                "proportion=" + proportion +
                "} " + super.toString();
    }
}
