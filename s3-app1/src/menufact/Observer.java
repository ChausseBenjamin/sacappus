package menufact;

import inventaire.Inventaire;
import menufact.plats.PlatAuMenu;
import ingredients.Ingredient;
import menufact.plats.PlatEnfant;
import menufact.plats.PlatMemento;

import java.util.ArrayList;

/**
 * This class holds a method which checks if every item in the menu has sufficient ingredients to be prepared.
 */
public class Observer {
    /** This method looks for menu items which cannot be prepared anymore and asks the controller to archive them.
     *  It goes through every ingredient in every plat and compares its quantity requirements to the quantities in the inventory.
     *  This observer method is called every time an inventory ingredient's value decreases.
     * @see Controller#archivePlatAuMenu(PlatAuMenu)
     */
    public static void trimMenu() {

        Controller controller = Controller.getInstance(null);
        Menu menu = controller.getMenu();
        if (menu.getPlats().isEmpty() == true) return;
        Inventaire inventaire = controller.getInventaire();
        // for each plat in the menu
        ArrayList<PlatAuMenu> platsToArchive = new ArrayList<PlatAuMenu>();
        for (PlatAuMenu plat: menu.getPlats()){
            // for each ingredient in the plat
            for (Ingredient ingredient: plat.getIngredients()){
                float proportion = 1;
                if (plat instanceof PlatEnfant){
                    proportion = ((PlatEnfant) plat).getProportion();
                }
                // if the ingredient quantity in inventory is less than the quantity required by the plat
                if (inventaire.getIngredient(ingredient).getQuantity() < ingredient.getQuantity()*proportion){
                    // archive the plat
                    platsToArchive.add(plat);
                    // don't check the other ingredients of this plat
                    break;
                }
            }
        }
        for (int i= 0; i < platsToArchive.size(); i++){
            controller.archivePlatAuMenu(platsToArchive.get(i));
        }
    }
    /** This method looks for menu items which can be prepared again and asks the controller to unarchive them.
     *  It goes through every ingredient in every plat and compares its quantity requirements to the quantities in the inventory.
     *  This observer method is called every time an inventory ingredient's value increases.
     * @see Controller#(PlatAuMenu)
     */
    public static void populateMenu(){
        Controller controller = Controller.getInstance(null);
        Inventaire inventaire = controller.getInventaire();
        ArrayList<PlatMemento> plats = controller.getArchivedPlats();
        // for each plat in the archived plats
        if (plats == null) return;
        ArrayList<PlatMemento> platsToRecover = new ArrayList<PlatMemento>();
        for (PlatMemento plat: plats){
            float proportion = plat.getProportion();
            boolean canRecover = true;
            // for each ingredient in the plat
            for (Ingredient ingredient: plat.getIngredients()){
                // if the ingredient quantity in inventory is less than the quantity required by the plat
                if (inventaire.getIngredient(ingredient).getQuantity() < ingredient.getQuantity()*proportion){
                    // don't recover the plat
                    canRecover = false;
                    break;
                }
            }
            if (canRecover){
                platsToRecover.add(plat);
            }
        }
        for (int i= 0; i < platsToRecover.size(); i++){
            controller.recoverPlat(platsToRecover.get(i));
        }
    }
}






























