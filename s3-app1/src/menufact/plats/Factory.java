package menufact.plats;
import ingredients.Ingredient;
import menufact.plats.PlatAuMenu;
import menufact.plats.prices.PriceFlyweight;
import menufact.plats.Plat;

import java.util.ArrayList;

public class Factory {
    public PlatAuMenu CreatePlat(int code, String description, PriceFlyweight prix, ArrayList<Ingredient> ingredients){
        boolean sante = true;
        for (Ingredient i : ingredients){
            if (!i.isSante()){
                sante = false;
                break;
            }
        }
        if (sante){
            return new PlatSante(code, description, prix, ingredients);
        }
        else{
            return new Plat(code, description, prix, ingredients);
        }
    }
    public PlatAuMenu CreatePlat(PlatAuMenu plat, float fraction){
        return new PlatEnfant(
                plat.getCode(),
                plat.getDescription(),
                plat.getPrixFlyweight(),
                fraction,
                plat.getIngredients()
        );
    }
}