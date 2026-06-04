package menufact;

import ingredients.Enrobage;
import ingredients.Feculant;
import ingredients.Garniture;
import ingredients.Ingredient;
import inventaire.Inventaire;
import menufact.plats.Factory;
import menufact.plats.PlatAuMenu;
import menufact.plats.prices.EconoPrice;
import menufact.plats.prices.RegularPrice;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ObserverTest {

    @Test
    public void trimMenu() {
        // This class needs the following to work:
        // - A Controller instance (which is what the Observer class interacts with)
        // - A DataBroker instance (which is where the Controller gets its data from)
        // - A couple of Ingredients
        // - A Populated Menu (which contains PlatAuMenu) instance inside the DataBroker
        // - A Populated Inventaire (which contais Ingredients) which the DataBroker has access to.

        // Let's create the ingredients for two menu items qantities (100g each and 150g of panko)
        Ingredient Concombre    = new Garniture("Concombre",      "Légume vert rafraichissant",                   100, 10, 10, 10, true);
        Ingredient Saumon       = new Garniture("Saumon",         "Saumon d'élevage",                             100, 10, 10, 10, true);
        Ingredient Riz          = new Feculant( "Riz",            "Riz Calrose",                                  100, 10, 10, 10, true);
        Ingredient FeuilleAlgue = new Enrobage( "Feuille Algue","Enrobage commun santé pour contenir un sushi", 100, 10, 10, 10, true);
        Ingredient Panko        = new Enrobage( "Panure Panko",   "Panure à base de pain généralement fritte",    150, 10, 10, 10, false);
        // Let's create ingredient lists for two PlatAuMenu
        ArrayList<Ingredient> sushiSaumonIngredients = new ArrayList<>();
        sushiSaumonIngredients.add(Saumon);
        sushiSaumonIngredients.add(Riz);
        sushiSaumonIngredients.add(Panko);
        ArrayList<Ingredient> sushiConcombreIngredients = new ArrayList<>();
        sushiConcombreIngredients.add(Concombre);
        sushiConcombreIngredients.add(Riz);
        sushiConcombreIngredients.add(FeuilleAlgue);
        // Let's create two PlatAuMenu with those ingredients (and a kid version of the first one with .5 proportions)
        Factory factory = new Factory();
        PlatAuMenu pankoSalmon = factory.CreatePlat(0, "Sushi au saumon pané", RegularPrice.getInstance(), sushiSaumonIngredients   );
        PlatAuMenu cucumber    = factory.CreatePlat(1, "Sushi au concombre",   EconoPrice.getInstance(),   sushiConcombreIngredients);
        PlatAuMenu pankoKid    = factory.CreatePlat(pankoSalmon, .5f);
        // Let's create those same ingredients but with quantities for the inventory (1000g each)
        Ingredient iConcombre    = new Garniture("Concombre",      "Légume vert rafraichissant",                1000, 10, 10, 10, true);
        Ingredient iSaumon       = new Garniture("Saumon",         "Saumon d'élevage",                          1000, 10, 10, 10, true);
        Ingredient iRiz          = new Feculant( "Riz",            "Riz Calrose",                               1000, 10, 10, 10, true);
        Ingredient iFeuilleAlgue = new Enrobage( "Feuille Algue","Enrobage commun santé pour contenir un sushi",1000, 10, 10, 10, true);
        Ingredient iPanko        = new Enrobage( "Panure Panko",   "Panure à base de pain généralement fritte", 1000, 10, 10, 10, false);
        // Let's instantiate Controller (which should instantiate DataBroker which should instantiate Menu & Inventaire)
        // And let's add those ingredients to the inventory
        Controller controller = Controller.getInstance(null);
        controller.addIngredient(iConcombre);
        controller.addIngredient(iSaumon);
        controller.addIngredient(iRiz);
        controller.addIngredient(iFeuilleAlgue);
        controller.addIngredient(iPanko);
        // We should now be able to add our meals to the Menu
        Menu menu = controller.getMenu();
        menu.ajoute(pankoSalmon);
        menu.ajoute(cucumber);
        menu.ajoute(pankoKid);
        // The menu should still have 3 items after trimming
        Observer.trimMenu();
        assertTrue(menu.getPlats().size()==3);
        // Let's remove 900g of Panko from the inventory
        // This should remove the pankoSalmon from the menu
        // But not the pankoKid (as it only requires 150g/2 = 75g)
        try {
            controller.updateIngredient(iPanko, -900);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Observer.trimMenu();
        assertTrue(menu.getPlats().size()==2);
    }

    @Test
    public void populateMenu() {
        // Let's create the ingredients for two menu items qantities (100g each and 150g of panko)
        Ingredient Concombre    = new Garniture("Concombre",      "Légume vert rafraichissant",                   100, 10, 10, 10, true);
        Ingredient Saumon       = new Garniture("Saumon",         "Saumon d'élevage",                             100, 10, 10, 10, true);
        Ingredient Riz          = new Feculant( "Riz",            "Riz Calrose",                                  100, 10, 10, 10, true);
        Ingredient FeuilleAlgue = new Enrobage( "Feuille Algue","Enrobage commun santé pour contenir un sushi", 100, 10, 10, 10, true);
        Ingredient Panko        = new Enrobage( "Panure Panko",   "Panure à base de pain généralement fritte",    150, 10, 10, 10, false);
        // Let's create ingredient lists for two PlatAuMenu
        ArrayList<Ingredient> sushiSaumonIngredients = new ArrayList<>();
        sushiSaumonIngredients.add(Saumon);
        sushiSaumonIngredients.add(Riz);
        sushiSaumonIngredients.add(Panko);
        ArrayList<Ingredient> sushiConcombreIngredients = new ArrayList<>();
        sushiConcombreIngredients.add(Concombre);
        sushiConcombreIngredients.add(Riz);
        sushiConcombreIngredients.add(FeuilleAlgue);
        // Let's create two PlatAuMenu with those ingredients (and a kid version of the first one with .5 proportions)
        Factory factory = new Factory();
        PlatAuMenu pankoSalmon = factory.CreatePlat(0, "Sushi au saumon pané", RegularPrice.getInstance(), sushiSaumonIngredients   );
        PlatAuMenu cucumber    = factory.CreatePlat(1, "Sushi au concombre",   EconoPrice.getInstance(),   sushiConcombreIngredients);
        PlatAuMenu pankoKid    = factory.CreatePlat(pankoSalmon, .5f);
        // Let's create those same ingredients but with insuficient quantities in the inventory (100g each)
        Ingredient iConcombre    = new Garniture("Concombre",      "Légume vert rafraichissant",                100, 10, 10, 10, true);
        Ingredient iSaumon       = new Garniture("Saumon",         "Saumon d'élevage",                          100, 10, 10, 10, true);
        Ingredient iRiz          = new Feculant( "Riz",            "Riz Calrose",                               100, 10, 10, 10, true);
        Ingredient iFeuilleAlgue = new Enrobage( "Feuille Algue","Enrobage commun santé pour contenir un sushi",100, 10, 10, 10, true);
        Ingredient iPanko        = new Enrobage( "Panure Panko",   "Panure à base de pain généralement fritte", 100, 10, 10, 10, false);
        // Let's instantiate Controller (which should instantiate DataBroker which should instantiate Menu & Inventaire)
        // And let's add those ingredients to the inventory
        Controller controller = Controller.getInstance(null);
        controller.addIngredient(iConcombre);
        controller.addIngredient(iSaumon);
        controller.addIngredient(iRiz);
        controller.addIngredient(iFeuilleAlgue);
        controller.addIngredient(iPanko);
        // We should now be able to add our meals to the Menu
        Menu menu = controller.getMenu();
        menu.ajoute(pankoSalmon);
        menu.ajoute(cucumber);
        menu.ajoute(pankoKid);
        // Since 150g of Panko is required for the pankoSalmon, trim should remove it from the menu
        Observer.trimMenu();
        // Now if we populate the menu, it should not add the pankoSalmon back into the menu as it is still missing 50g of Panko
        Observer.populateMenu();
        assertTrue(menu.getPlats().size()==2);
        // Let's add 900g of Panko to the inventory
        // This should add the pankoSalmon back into the menu
        try {
            controller.updateIngredient(iPanko, 900);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Observer.trimMenu();
        assertTrue(menu.getPlats().size()==3);
        // Let's now remove 950g of Panko from the inventory so that even the pankoKid is missing 25g of Panko
        try {
            controller.updateIngredient(iPanko, -950);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Observer.trimMenu();
        // Now if we populate the menu, it should not add the pankoKid back into the menu as it is still missing 25g of Panko
        Observer.populateMenu();
        assertTrue(menu.getPlats().size()==1);
        // Now let's add 1000g of Panko to the inventory and see if both pankoSalmon and pankoKid are back in the menu
        try {
            controller.updateIngredient(iPanko, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Observer.populateMenu();
        assertTrue(menu.getPlats().size()==3);

    }
}