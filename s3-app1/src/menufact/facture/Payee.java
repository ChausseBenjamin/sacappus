package menufact.facture;

import menufact.facture.exceptions.FactureException;

public class Payee implements FactureEtat {
    protected Facture facture;
    public Payee(Facture fact){
        fact = facture;
    }
    @Override
    public FactureEtat Payer(Facture fact){
        System.out.println("Cette facture est déjà payée");
        return this;
    }

    @Override
    public FactureEtat Fermer(Facture fact){
        System.out.println("Imposible de fermer une facture payée");
        return this;
    }

    @Override
    public FactureEtat Ouvrir(Facture fact){
        System.out.println("Imposible d'ouvrir une facture payée");
        return this;
    }
}