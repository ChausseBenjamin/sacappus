package menufact.facture;

import menufact.facture.exceptions.FactureException;

public interface FactureEtat {


    FactureEtat Payer(Facture fact);
    FactureEtat Fermer(Facture fact);
    FactureEtat Ouvrir(Facture fact);

}