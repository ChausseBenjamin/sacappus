package menufact.facture;

import menufact.facture.exceptions.FactureException;

public class Ouverte implements FactureEtat {
    protected Facture facture;
    public Ouverte(Facture fact){
        fact = facture;
    }
    @Override
    public FactureEtat Payer(Facture fact){
        System.out.println("Vous devez fermer cette facture avant de la payer");
        return this;
    }

    @Override
    public FactureEtat Fermer(Facture fact){
        System.out.println("Fermeture de la facture en cours... Réussi");
        FactureEtat a = new Fermee(fact);
        return a;
    }

    @Override
    public FactureEtat Ouvrir(Facture fact){
        System.out.println("Cette facture est déjà ouverte");
        return this;
    }

}
