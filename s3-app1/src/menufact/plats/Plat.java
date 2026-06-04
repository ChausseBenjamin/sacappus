package menufact.plats;

import ingredients.Ingredient;
import menufact.plats.prices.PriceFlyweight;

import java.util.ArrayList;

public class Plat implements PlatAuMenu {
    private int code;
    private String description;

    protected PriceFlyweight prix;

    private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

    public Plat(int code, String description, PriceFlyweight prix, ArrayList<Ingredient> ingredients) {
        this.code = code;
        this.description = description;
        this.prix = prix;
        this.ingredients = ingredients;
    }

    public Plat() {
    }
    @Override
    public String toString() {
        return "menufact.plats.PlatAuMenu{" +
                "code=" + code +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                "}\n";
    }
    @Override
    public int getCode() {
        return code;
    }
    @Override
    public void setCode(int code) {
        this.code = code;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public double getPrix() {
        return prix.getPrice();
    }
    @Override
    public PriceFlyweight getPrixFlyweight() {
        return prix;
    }
    public void setPrixFlyweight(PriceFlyweight prix) {
        this.prix = prix;
    }
    @Override
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

}
