package menufact;

import ingredients.Enrobage;
import ingredients.Feculant;
import ingredients.Garniture;
import ingredients.Ingredient;
import ingredients.exceptions.IngredientException;
import inventaire.Inventaire;
import menufact.exceptions.MenuException;
import menufact.facture.Facture;
import menufact.facture.exceptions.FactureException;
import menufact.plats.*;
import menufact.plats.prices.DeluxePrice;
import menufact.plats.prices.EconoPrice;
import menufact.plats.prices.PriceFlyweight;
import menufact.plats.prices.RegularPrice;
import inventaire.Inventaire;
//import sun.jvm.hotspot.utilities.Assert;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;

public class TestMenuFact03 {

    public static void main(String[] args) throws FactureException {
        TestMenuFact03 t = new TestMenuFact03();

        /**
         * Test unitaire "Menu"
         */
        PlatAuMenu platdetest;
        Menu m1 = null;
        Menu m2 = null;
        m1 = Menu.getInstance("menu1");
        m2 = Menu.getInstance("menu2");
        assertEquals(m1,m2);

        /**
         * Test unitaire "Ingredient"
         */

        //Inventaire de départ
        Feculant feculant = new Feculant("feculant", "Total de Feculant", 1000, 0, 0, 0, true);
        Garniture garniture = new Garniture("garniture", "Total de garniture", 500, 0, 0, 0, true);
        Enrobage enrobage = new Enrobage("enrobage", "Total d'enrobage", 25, 0, 0, 0, true);

        //Pour Sushi Clasic
        ArrayList<Ingredient> clasic = new ArrayList<Ingredient>();
        Feculant rizcollant = new Feculant("riz1", "Riz collant", 100, 320, 0, 0, true);
        Garniture concombre = new Garniture("con1", "Concombre du Qc", 20, 40, 1, 2, true);
        Enrobage feuille_dalgue = new Enrobage("alg1", "Algue du St-laurent", 2, 2, 0, 0, true);
        clasic.add(rizcollant);
        clasic.add(concombre);
        clasic.add(feuille_dalgue);

        //Pour Sushi Dragon
        ArrayList<Ingredient> dragon = new ArrayList<Ingredient>();
        Feculant riz_epice = new Feculant("riz2", "Riz Épicé", 100, 320, 0, 0, true);
        Garniture Piment_fort = new Garniture("pim1", "Piment jalapeno", 20, 35, 1, 2, true);
        Enrobage Panure_cajun = new Enrobage("pan1", "Pannure Cajun", 4, 2, 0, 0, false);
        dragon.add(riz_epice);
        dragon.add(Piment_fort);
        dragon.add(Panure_cajun);

        /**
         * Test unitaire "Inventaire"
         */

        Inventaire restaurantstock = Inventaire.getInstance();


        //Ajout dans les stocks
        restaurantstock.ajouter(feculant);
        restaurantstock.ajouter(garniture);
        restaurantstock.ajouter(enrobage);

        //Update des quantités
        try {
            restaurantstock.updateStockIngredient(feculant, -500);
        } catch (IngredientException e) {
            System.out.println(e.getMessage());
        }

        try {
            restaurantstock.updateStockIngredient(garniture, 0);
        } catch (IngredientException e) {
            System.out.println(e.getMessage());
        }
        try {
            restaurantstock.updateStockIngredient(enrobage, 5);
        } catch (IngredientException e) {
            System.out.println(e.getMessage());
        }
        //Affichage des stocks
        restaurantstock.getIngredient(feculant);
        restaurantstock.getIngredient(garniture);
        restaurantstock.getIngredient(enrobage);

        /**
         * Test unitaire "PriceFlyweight"
         */
        PriceFlyweight econoPrice = EconoPrice.getInstance();
        econoPrice.setPrice(16.99);

        PriceFlyweight regularPrice = RegularPrice.getInstance();
        regularPrice.setPrice(23.99);

        PriceFlyweight deluxePrice = DeluxePrice.getInstance();
        deluxePrice.setPrice(31.99);

        /**
         * Test unitaire "Factory"
         */
        Factory factory = new Factory();
        PlatAuMenu dragonSushi = factory.CreatePlat(123, "Dragon Sushi",deluxePrice, dragon);
        PlatAuMenu ConcombreReg = factory.CreatePlat(321, "Sushi Régulier",  regularPrice, clasic);
        PlatAuMenu ConcombreRegEnfant = factory.CreatePlat(ConcombreReg, 0.5f);
        ConcombreRegEnfant.setPrixFlyweight(econoPrice);

        System.out.println(dragonSushi.toString());
        System.out.println(ConcombreReg.toString());
        System.out.println(ConcombreRegEnfant.toString());

        /**
         * Test unitaire "Client"
         */
        Client c1 = new Client(1,"Mr Client A","1234567890");
        Client c2 = new Client(2,"Mr Client B","978563244");

        try {
            assertEquals(c1.getIdClient(),1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            assertEquals(c2.getIdClient(),2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /**
         * Test unitaire "Etat de préparation des plats"
         */
        PlatChoisi pch1 = new PlatChoisi(dragonSushi, 2);
        PlatChoisi pch2 = new PlatChoisi(ConcombreReg, 3);
        if(!(pch1.getEtat() instanceof Commande)){System.out.println("Erreur de commande");}
        if(!(pch2.getEtat() instanceof Commande)){System.out.println("Erreur de commande");}

        pch1.Preparer();
        pch2.Terminer();
        if(!(pch1.getEtat() instanceof EnPreparation)){System.out.println("Erreur d'état de commande");}
        if(!(pch2.getEtat() instanceof Commande)){System.out.println("Erreur d'état de commande");}

        pch1.Terminer();
        pch2.Preparer();
        if(!(pch1.getEtat() instanceof Termine)){System.out.println("Erreur d'état de commande");}
        if(!(pch2.getEtat() instanceof EnPreparation)){System.out.println("Erreur d'état de commande");}


        pch1.Terminer();
        pch2.Servir();
        if(!(pch1.getEtat() instanceof Termine)){System.out.println("Erreur d'état de commande");}
        if(!(pch2.getEtat() instanceof EnPreparation)){System.out.println("Erreur d'état de commande");}

        pch1.Servir();
        pch2.Terminer();
        if(!(pch1.getEtat() instanceof Servi)){System.out.println("Erreur d'état de commande");}
        if(!(pch2.getEtat() instanceof Termine)){System.out.println("Erreur d'état de commande");}

        pch1.Servir();
        pch2.Servir();
        if(!(pch1.getEtat() instanceof Servi)){System.out.println("Erreur d'état de commande");}
        if(!(pch2.getEtat() instanceof Servi)){System.out.println("Erreur d'état de commande");}

        /**
         * Test unitaire "Facture"
         */
        Facture f1 = new Facture("Facture table 10",0);
        Facture f2 = new Facture("Facture table 13",1);
        f1.associerClient(c1);
        f2.associerClient(c2);
        try {
            assertEquals(f1.getId(),0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            assertEquals(f2.getId(),1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        f1.ajoutePlat(pch1);
        f2.ajoutePlat(pch2);


        f1.ouvrir();
        f1.fermer();
        f1.fermer();
        f1.ouvrir();
        f1.payer();
        f1.fermer();
        f1.payer();
        f1.ouvrir();

        f2.payer();
        f2.fermer();
        f2.payer();
        f2.fermer();
        f2.ouvrir();
        f2.payer();

        System.out.println("FIN DE TOUS LES TESTS...");
        System.out.println(f1.genererFacture());
        System.out.println(f2.genererFacture());
    }
}
