package menufact.plats;

import menufact.plats.PlatAuMenu;
import menufact.plats.prices.PriceFlyweight;
import ingredients.Ingredient;

import java.util.ArrayList;


public class PlatSante extends Plat {

        public PlatSante(int code, String description, PriceFlyweight prix, ArrayList<Ingredient> ingredients) {
        super(code, description, prix, ingredients);
    }

    public PlatSante() {
    }
    @Override
    public String toString() {
        return "menufact.plats.PlatSante{" +
                "kcal=" + getKcal() +
                ", chol=" + getChol() +
                ", gras=" + getGras() +
                "} " + super.toString();
    }
    public void setPrixFlyweight(PriceFlyweight prix) {
        this.prix = prix;
    }
    public double getKcal() {
        int total = 0;
        for (Ingredient i : getIngredients()){
            total += i.getKcal();
        }
        return total;
    }

    public double getChol() {
        int total = 0;
        for (Ingredient i : getIngredients()){
            total += i.getChol();
        }
        return total;
    }

    public double getGras() {
        int total = 0;
        for (Ingredient i : getIngredients()){
            total += i.getFat();
        }
        return total;
    }
}
