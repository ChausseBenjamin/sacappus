package menufact;

import ingredients.Ingredient;
import ingredients.exceptions.IngredientException;
import inventaire.Inventaire;
import menufact.facture.Facture;
import menufact.facture.Ouverte;
import menufact.facture.exceptions.FactureException;
import menufact.plats.PlatAuMenu;
import menufact.plats.PlatChoisi;
import menufact.plats.PlatMemento;

import java.util.ArrayList;

/**
 * The controller of the MenuFact system (which is part of the MVC design pattern)
 * The controller is the only class that can access the database. It handles all the logic of the system
 * and tells the view what to display. It is the only class that can access the database (handled by the DataBroker class
 * which acts as the Model of the MVC design pattern).
 */
public class Controller {
    /**
     * The instance of the controller. It is a singleton.
     */
    private static Controller instance = null;
    /**
     * The current facture id. It is set to -1 if there is no current facture.
     */
    int currentFacture;
    /**
     * The current client id. It is set to -1 if there is no current client.
     */
    int currentClient;
    /**
     * The database of the system.
     */
    DataBroker db;
    /**
     * The view of the system.
     */
    View view;

    /**
     * The controller is a singleton. This method returns the instance of the controller.
     * @param v set the view of the controller
     *          The view is set to null if the controller is constructed by the Chef
     *          Calling this method again with a non-null view will set the view of the controller
     *          This way, the controller can be constructed by the Chef and the view can be set later
     * @return the instance of the controller
     */
    public static Controller getInstance(View v){
        if (instance == null) instance = new Controller(v);
        // If the controller was constructed by the Chef, it has no view
        // This ensures that if a view is created after the Chef, the view is assigned to the controller
        else {
            if (instance.view == null) instance.view = v;
        }
        return instance;
    }
    /**
     * The constructor of the controller. It is private because the controller is a singleton.
     * @param v the view of the controller
     */
    private Controller(View v){
        view = v;
        db = DataBroker.getInstance();
        currentFacture = -1;
        currentClient = -1;
    }

    /**
     * This method initializes the ui at the start of the program.
     * It serves as the entry point of the program.
     */
    public void start(){
        view.showLogin();
    }
    /**
     * This method is called when the user wishes to logout.
     * It resets the currentFacture and currentClient to -1 (no current facture and no current client).
     * It signals to the user that he has logged out, and that he can login again.
     * It then tells the view to display the login screen.
     */
    public void logout(){
        currentFacture = -1;
        currentClient = -1;
        view.showLogout();
        view.showLogin();
    }

    /**
     * This method is called to add a new Client to the database.
     * It is used by the signupClient method.
     * @param idClient
     * @param nom
     * @param numeroCarteCredit
     */
    private void createClient(int idClient, String nom, String numeroCarteCredit){
        db.ajouteClient(new Client(idClient, nom, numeroCarteCredit));
    }

    /**
     * This method is called by login when the client does not exist in the database.
     * In which case, the client is assigned a new idClient and his information is added to the database.
     * @param nom
     * @param numeroCarteCredit
     * @return the idClient of the new client so that the login method can then tell the view to display the idClient
     */
    private int signupClient(String nom, String numeroCarteCredit){
        int idClient = db.getClients().size();
        db.ajouteClient(new Client(idClient, nom, numeroCarteCredit));
        return idClient;
    }

    /**
     * This method is called by the view when the user wishes to login.
     * It checks if the client exists in the database.
     * If the client exists, it sets the currentClient to the idClient of the client.
     * If the client does not exist, it creates a new client and sets the currentClient to the idClient of the new client.
     * It then checks if the client has an open facture.
     * If the client has an open facture, it sets the currentFacture to the idFacture of the open facture.
     * It also tells the view to notify the user about the fact that this isn't a new facture (reconnect = true).
     * @param idClient
     * @param nom
     * @param numeroCarteCredit
     * @throws FactureException
     */
    public void login(int idClient, String nom, String numeroCarteCredit) throws FactureException {
        boolean reconnect = false;
        // Set currentClient to the idClient if the client exists in the database
        // ignore the nom and numeroCarteCredit
        boolean clientInexistant = true;
        for (Client c: db.getClients()){
            if (c.getIdClient() == idClient){
                currentClient = idClient;
                clientInexistant = false;
                break;
            }
        }
        // If the client does not exist in the database, create a new client
        if (clientInexistant) {
            currentClient = signupClient(nom, numeroCarteCredit);
        }
        // Check if the client has an Open Facture
        //   yes: set currentFacture to the idFacture
        //   no: create a new facture and set currentFacture to the idFacture
        boolean factureOuverte = false;
        for (Facture f: db.getFactures()){
            if (f.getClient().getIdClient() == currentClient && f.getEtat() instanceof Ouverte){
                currentFacture = f.getId();
                reconnect = true;
                factureOuverte = true;
                break;
            }
        }
        // Create a new facture if the client does not have an open facture
        if (!factureOuverte) {
            currentFacture = db.getFactures().size();
            // TODO: What should the the facture's description be?
            Facture f = new Facture("", currentFacture);
            f.associerClient(db.getClient(currentClient));
            db.ajouterFacture(f);
        }
        // TODO: What should the terminal display?
        //  - Maybe the client's id and the facture's id?
        //  - It should probably display the menu after this method is called
        view.showPostLogin(reconnect, db.getFacture(currentFacture));
        view.showMenu(db.getMenu());
    }

    public void addIngredient(Ingredient ingredient){
        db.addIngredient(ingredient);
    }

    public void updateIngredient(PlatAuMenu plat) throws IngredientException {
        db.updateInventaireIngredient(plat);
    }
    public void updateIngredient(Ingredient ingredient, int quantity)throws IngredientException{
        db.updateInventaireIngredient(ingredient, quantity);
    }

    /**
     * This method is called by the view when the user makes a menu choice.
     * A new PlatChoisi is created and added to the current facture (with a Comandee state).
     * The inventory is updated to reflect the new quantities of ingredients
     * This acts as some sort of ingredient reservation as the ingredients are returned to the inventory if the plat is removed.
     * This works because it's impossible to remove a plat once the Chef has started preparing it.
     * After the inventory is updated, an Observer is triggered to check if it's still possible to make every plat on the menu.
     * The observer will then remove every plat that can no longer be made.
     * The view is then told to display the menu again.
     */
    void chooseMenuOption(PlatAuMenu plat) throws FactureException {
        PlatChoisi platChoisi = new PlatChoisi(plat,1);
        Facture f = db.getFacture(currentFacture);
        f.ajoutePlat(platChoisi);
        view.showPostChoixMenu(platChoisi);
        view.showMenu(db.getMenu());
    }

    /**
     * This method is called by the Observer when it notices a plat can no longer be made
     * The DataBroker is then told to remove the plat from the menu and store it's recipe in a Memento.
     * @param plat the plat that can no longer be made
     */
    public void archivePlatAuMenu(PlatAuMenu plat){
        db.retirerDuMenu(plat);
    }
    public ArrayList<PlatMemento> getArchivedPlats(){
        return db.getMementos();
    }

    void recoverPlat(PlatMemento plat){
        db.ajouterAuMenu(plat.getCode());
    }

    /**
     * This method is called by the view when the user wishes to add a plat to his current facture.
     * @param plat the plat to be added
     * @throws FactureException if it's impossible to add the plat to the facture
     */
    void addPlat(PlatAuMenu plat) throws FactureException {
        Facture f = db.getFacture(currentFacture);
    }


    public ArrayList<Facture> getFactures() {
        return db.getFactures();
    }

    public Menu getMenu() {
        return db.getMenu();
    }
    public Inventaire getInventaire() {
        return db.getInventaire();
    }
}
