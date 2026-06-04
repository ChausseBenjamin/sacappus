package menufact.plats;

import ingredients.Enrobage;
import ingredients.Feculant;
import ingredients.Garniture;
import ingredients.Ingredient;
import menufact.facture.Ouverte;
import menufact.plats.prices.EconoPrice;
import menufact.plats.prices.PriceFlyweight;
import menufact.plats.prices.RegularPrice;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FactoryTest {

    @Test
    public void createPlat() {
        // Let's first create five ingredients (100g of each, 10% kcal, 10% fat, 10% cholesterol, all healthy except Panko)
        Ingredient Concombre    = new Garniture("Concombre",      "Légume vert rafraichissant",                   100, 10, 10, 10, true);
        Ingredient Saumon       = new Garniture("Saumon",         "Saumon d'élevage",                             100, 10, 10, 10, true);
        Ingredient Riz          = new Feculant( "Riz",            "Riz Calrose",                                  100, 10, 10, 10, true);
        Ingredient FeuilleAlgue = new Enrobage( "Feuille d'Algue","Enrobage commun santé pour contenir un sushi", 100, 10, 10, 10, true);
        Ingredient Panko        = new Enrobage( "Panure Panko",   "Panure à base de pain généralement fritte",    100, 10, 10, 10, false);
        // Let's make the ingredient lists for the two sushi we want to make
        ArrayList<Ingredient> pankoSalmonIngredients = new ArrayList<Ingredient>();
        pankoSalmonIngredients.add(Saumon);
        pankoSalmonIngredients.add(Riz);
        pankoSalmonIngredients.add(FeuilleAlgue);
        ArrayList<Ingredient> cucumberIngredients = new ArrayList<Ingredient>();
        cucumberIngredients.add(Concombre);
        cucumberIngredients.add(Riz);
        cucumberIngredients.add(FeuilleAlgue);
        // From these ingredient lists, let's make a panko salmon sushi (unhealty) and a regular cucumber sushi (healthy)
        // The salmon one should have a Regular price, and the cucumber one should have an Econo price
        Factory factory = new Factory();
        PlatAuMenu pankoSalmon = factory.CreatePlat(0, "Sushi au saumon pané", RegularPrice.getInstance(), pankoSalmonIngredients);
        PlatAuMenu cucumber    = factory.CreatePlat(1, "Sushi au concombre",   EconoPrice.getInstance(),   cucumberIngredients);

        // Let's check the characteristics of the two panko salmon sushi
        assertTrue(pankoSalmon instanceof PlatAuMenu); // Would be false if it was a PlatSante
        assertTrue(pankoSalmon.getCode() == 0);
        assertTrue(pankoSalmon.getDescription().equals("Sushi au saumon pané"));
        assertTrue(pankoSalmon.getPrixFlyweight() instanceof RegularPrice);
        for (int i=0; i<pankoSalmon.getIngredients().size(); i++){
            assertTrue(pankoSalmon.getIngredients().get(i).equals(pankoSalmonIngredients.get(i)));
        }

        assertTrue(cucumber instanceof PlatSante);
        assertTrue(cucumber.getCode() == 1);
        assertTrue(cucumber.getDescription().equals("Sushi au concombre"));
        assertTrue(cucumber.getPrixFlyweight() instanceof EconoPrice);
        for (int i=0; i<cucumber.getIngredients().size(); i++){
            assertTrue(cucumber.getIngredients().get(i).equals(cucumberIngredients.get(i)));
        }

    }

    @Test
    public void testCreatePlat() {
        // Let's first create five ingredients (100g of each, 10% kcal, 10% fat, 10% cholesterol, all healthy except Panko)
        Ingredient Concombre    = new Garniture("Concombre",      "Légume vert rafraichissant",                   100, 10, 10, 10, true);
        Ingredient Saumon       = new Garniture("Saumon",         "Saumon d'élevage",                             100, 10, 10, 10, true);
        Ingredient Riz          = new Feculant( "Riz",            "Riz Calrose",                                  100, 10, 10, 10, true);
        Ingredient FeuilleAlgue = new Enrobage( "Feuille d'Algue","Enrobage commun santé pour contenir un sushi", 100, 10, 10, 10, true);
        Ingredient Panko        = new Enrobage( "Panure Panko",   "Panure à base de pain généralement fritte",    100, 10, 10, 10, false);
        // Let's make the ingredient lists for the two sushi we want to make
        ArrayList<Ingredient> pankoSalmonIngredients = new ArrayList<Ingredient>();
        pankoSalmonIngredients.add(Saumon);
        pankoSalmonIngredients.add(Riz);
        pankoSalmonIngredients.add(FeuilleAlgue);
        ArrayList<Ingredient> cucumberIngredients = new ArrayList<Ingredient>();
        cucumberIngredients.add(Concombre);
        cucumberIngredients.add(Riz);
        cucumberIngredients.add(FeuilleAlgue);
        // From these ingredient lists, let's make a panko salmon sushi (unhealty) and a regular cucumber sushi (healthy)
        // The salmon one should have a Regular price, and the cucumber one should have an Econo price
        Factory factory = new Factory();
        PlatAuMenu pankoSalmon = factory.CreatePlat(0, "Sushi au saumon pané", RegularPrice.getInstance(), pankoSalmonIngredients);
        PlatAuMenu cucumber    = factory.CreatePlat(1, "Sushi au concombre",   EconoPrice.getInstance(),   cucumberIngredients);

        // Let's make two new child portions, both 50% of the original sushis
        PlatAuMenu pankoSalmonChild = factory.CreatePlat(pankoSalmon, 0.5f);
        PlatAuMenu cucumberChild    = factory.CreatePlat(cucumber,    0.5f);

        // This test passes if both child portions are PlatEnfant objects (PlatEnfant is more precise than PlatSante or PlatAuMenu)
        assertTrue(pankoSalmonChild instanceof PlatEnfant);
        assertTrue(pankoSalmonChild.getCode() == 0);
        assertTrue(pankoSalmonChild.getDescription().equals("Sushi au saumon pané"));
        assertTrue(pankoSalmonChild.getPrixFlyweight() instanceof RegularPrice);
        for (int i=0; i<pankoSalmonChild.getIngredients().size(); i++){
            assertTrue(pankoSalmonChild.getIngredients().get(i).equals(pankoSalmonIngredients.get(i)));
        }

        assertTrue(cucumberChild instanceof PlatEnfant);
        assertTrue(cucumberChild.getCode() == 1);
        assertTrue(cucumberChild.getDescription().equals("Sushi au concombre"));
        assertTrue(cucumberChild.getPrixFlyweight() instanceof EconoPrice);
        for (int i=0; i<cucumberChild.getIngredients().size(); i++){
            assertTrue(cucumberChild.getIngredients().get(i).equals(cucumberIngredients.get(i)));
        }
        // Check that the child portions are indeed 50% of the original using PlatEnfant's getProportion() method
        assertTrue(( (PlatEnfant)pankoSalmonChild ).getProportion() == 0.5f);

    }
}