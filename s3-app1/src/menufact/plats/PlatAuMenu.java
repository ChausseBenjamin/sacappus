package menufact.plats;

import ingredients.Ingredient;
import menufact.plats.prices.PriceFlyweight;

import java.util.ArrayList;

public interface PlatAuMenu {
    void setCode(int code);
    void setDescription(String description);

    int getCode();
    String getDescription();
    double getPrix();
    ArrayList<Ingredient> getIngredients();
    PriceFlyweight getPrixFlyweight();
    void setPrixFlyweight(PriceFlyweight prix);
    String toString();

}
