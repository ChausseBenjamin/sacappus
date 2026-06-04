package menufact;

import menufact.facture.Facture;
import menufact.plats.PlatChoisi;

/**
 * The View interface is part of the MVC design pattern.
 * The logic is handled by the Controller, and the View is responsible for
 * displaying information to the user and getting input from the user. For the
 * controller to be able to do its job, it needs any View to implement this interface.
 */
public interface View {
    /** Controller which handles the application logic */
    Controller controller = null;

    /**
     *  This method should do the following:
     *  1. Ask the user for a ClientID, a Name, and a Credit Card Number
     *  2. Trigger the controller's login() method once the user has entered valid information
     */
    void showLogin();

    /**
     * This method is called by the controller after the user has logged in
     * It should do the following:
     * - If the user is logging into a new facture, say it
     * - If the user is reconnecting to an existing facture, show information about
     *   that pre-existing facture
     */
    void showPostLogin(boolean reconnect, Facture f);

    /**
     * This method should do the following:
     * 1. Display a message to the user that he has been logged out
     */
    void showLogout();

    /**
     * This method should do the following:
    *  1. Display the menu
    *  2. Let the user choose a Menu option (call the controller's chooseMenuOption() method)
    *  3. Let the user ask to see his current facture/pay his bill (call the controller's showFacture() method)
    *  4. Let the user logout (call the controller's logout() method)
     * To achieve this, it needs the following information:
     * @param m the menu to display
    */
    void showMenu(Menu m);

    /**
     * This method should do the following:
     * 1. Tell the user that his choice has been added to the facture
     */
    void showPostChoixMenu(PlatChoisi p);

    /**
     * This method should do the following:
     * 1. Display the current facture
     * 2. Let the user pay his bill (call the controller's payFacture() method)
     * 3. Let the user logout (call the controller's logout() method)
     * 4. Let the user ask to see the menu (ask the controller for the menu & call it's own showMenu() method)
     */
    void showFacture(Facture f);
}
