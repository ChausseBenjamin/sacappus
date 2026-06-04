package menufact.facture;

import menufact.facture.exceptions.FactureException;

public class Fermee implements FactureEtat{
    protected Facture facture;
    public Fermee(Facture fact){
        fact = facture;
    }
    @Override
    public FactureEtat Payer(Facture fact){
        System.out.println("Paiement en cours....ACCEPTÉE");
        FactureEtat a = new Payee(fact);
        return a;
    }

    @Override
    public FactureEtat Fermer(Facture fact){
        System.out.println("Cette facture est déjà fermée");
        return this;
    }

    @Override
    public FactureEtat Ouvrir(Facture fact){
        System.out.println("Réouverture de la facture");
        FactureEtat a = new Ouverte(fact);
        return a;
    }
}
