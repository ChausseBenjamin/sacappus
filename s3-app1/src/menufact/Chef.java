package menufact;

import menufact.facture.Facture;
import menufact.facture.Ouverte;
import menufact.plats.Commande;
import menufact.plats.PlatAuMenu;
import menufact.plats.PlatChoisi;

import java.util.ArrayList;

public class Chef {
    private static Chef instance = null;
    private String nom;
    private Controller controller;
    private PlatChoisi platEnPreparation;

    private Chef(String nom){
        this.nom = nom;
        this.controller = Controller.getInstance(null);
    }
    private PlatChoisi getMeal() {
        ArrayList<Facture> factures = controller.getFactures();
        // Go through every facture
        for (Facture f: factures) {
            if (f.getEtat() instanceof Ouverte) {
                // Go through every platChoisi in the facture
                for (PlatChoisi pc: f.getPlats()) {
                    if (pc.getEtat() instanceof Commande){
                        return pc;
                    }
                }
            }
        }
        // If there is no platChoisi to prepare, return null
        return null;
    }

    /**
     * This methods sets which platChoisi is being prepared by the chef.
     * It uses the getMeal() method to find the next platChoisi to prepare.
     * It updates the state of that plat to EnPreparation
     * @return the platChoisi that is being prepared
     */
    public PlatChoisi startMeal(){
        if (platEnPreparation != null){
            platEnPreparation.Terminer();
        }
        PlatChoisi platChoisi = getMeal();
        if (platChoisi != null) {
            platEnPreparation = platChoisi;
            platEnPreparation.Preparer();
        }else{
            System.out.println("Aucune commande à préparer.");
        }
        return platChoisi;
    }
    public PlatChoisi getPlatEnPreparation(){
        return platEnPreparation;
    }

    /**
     * This method is called when the chef is done preparing a platChoisi.
     * The state of that platChoisi is changed to Termine.
     * @return the plat that just got done.
     */
    public PlatChoisi finishMeal() {
        PlatChoisi platChoisi = platEnPreparation;
        platEnPreparation.Terminer();
        platEnPreparation = null;
        return platChoisi;
    }
}
