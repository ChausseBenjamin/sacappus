package menufact;

import java.util.ArrayList;

import ingredients.exceptions.IngredientException;
import menufact.Client;
import menufact.Menu;
import menufact.facture.Facture;
import inventaire.Inventaire;
import ingredients.Ingredient;
import inventaire.Inventaire;
import menufact.facture.Facture;
import menufact.facture.exceptions.FactureException;
import menufact.plats.PlatAuMenu;
import menufact.plats.PlatEnfant;
import menufact.plats.PlatMemento;

import java.util.ArrayList;

public class DataBroker {
    // Initialisation:
    private static DataBroker instance = new DataBroker();
    private DataBroker(){
        menu = Menu.getInstance("Menu courant");
        clients = new ArrayList<Client>();
        factures = new ArrayList<Facture>();
        platMementos = new ArrayList<PlatMemento>();
    }
    public static DataBroker getInstance(){
        return instance;
    }


    // Database Contents:
//    private Inventaire inventaire;
    private Menu menu;
    private ArrayList<PlatMemento> platMementos;
    private ArrayList<Client> clients;
    private ArrayList<Facture> factures;

    // Menu Management:
    public Menu getMenu() {
        return menu;
    }
    /**
     * Adds a PlatAuMenu to the menu (Used when recovering a PlatAuMenu from the database)
     * @param codePlatAuMenu the code of the PlatAuMenu to add to the menu
     */
    public void ajouterAuMenu(int codePlatAuMenu){
       for (PlatMemento m: platMementos){
           if (m.getCode() == codePlatAuMenu){
               // add the plat to the menu and remove it from the platMementos
               menu.ajoute(m.recover());
                platMementos.remove(m);
               return;
           }
       }
    }
    /**
     * Adds a PlatAuMenu to the menu (Used when creating a new PlatAuMenu)
     * @param plat the PlatAuMenu to add to the menu
     */
    public void ajouterAuMenu(PlatAuMenu plat){
        menu.ajoute(plat);
    }
    public void retirerDuMenu(PlatAuMenu plat){
        // add the plat to the platMementos
        platMementos.add( new PlatMemento(plat) );
        // remove the plat from the menu
        menu.retire(plat);
    }
    public ArrayList<PlatMemento> getPlatMementos() {
        return platMementos;
    }
    // Inventory management (ingredients in the inventaire)
    public Ingredient getInventaireIngredient(Ingredient ingredient){
        Inventaire inventaire = Inventaire.getInstance();
        return inventaire.getIngredient(ingredient);
    }
    public void updateInventaireIngredient(PlatAuMenu plat) throws IngredientException {
        Inventaire inventaire = Inventaire.getInstance();
        float proportion = 1;
        if (plat instanceof PlatEnfant){
            proportion = ((PlatEnfant) plat).getProportion();
        }
        for (Ingredient i: plat.getIngredients()) {
            inventaire.updateStockIngredient(i, (int) (i.getQuantity()*proportion));
        }
    }
    public void updateInventaireIngredient(Ingredient ingredient, int quantity) throws IngredientException {
        Inventaire inventaire = Inventaire.getInstance();
        inventaire.updateStockIngredient(ingredient, quantity);
    }

    // Client Management:
    public ArrayList<Client> getClients(){
        return clients;
    }
    public Client getClient(int idClient){
        for (Client c: clients){
            if (c.getIdClient() == idClient) return c;
        }
        return null; // Null should indicate to the Client that the client does not exist in the database
        // TODO: Maybe replace the return null with an exception...
    }
    public void ajouteClient(Client c){
        clients.add(c);
    }

    // Facture Management:
    public ArrayList<Facture> getFactures(){
        return factures;
    }
    public Facture getFacture(int idFacture) throws FactureException {
        for (Facture f : factures) {
            if (f.getId() == idFacture) return f;
        }
        // TODO: maybe throw an exception if the facture does not exist in the database
        throw new FactureException("La facture n'existe pas dans la base de données");
    }
    public void ajouterFacture(Facture f){
        factures.add(f);
    }

    public Inventaire getInventaire() {
    	return Inventaire.getInstance();
    }

    public ArrayList<PlatMemento> getMementos() {
        return platMementos;
    }

    public void addIngredient(Ingredient ingredient) {
        Inventaire.getInstance().ajouter(ingredient);
    }

    //TODO: Opérations sur le menu (enlever/ajouter des plats)



}
