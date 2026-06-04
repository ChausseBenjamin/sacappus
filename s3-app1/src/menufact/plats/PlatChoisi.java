package menufact.plats;
import java.util.ArrayList;

import menufact.plats.Plat;

public class PlatChoisi {
    private PlatAuMenu plat;
    private int quantite;
    PlatChoisiEtat etat;

    public PlatChoisi(PlatAuMenu plat, int quantite) {
        this.plat = plat;
        this.quantite = quantite;
        etat =  new Commande(this);
    }

    @Override
    public String toString() {
        return "menufact.plats.PlatChoisi{" +
                "quantite=" + "quantite" +
                ", plat=" + "plat" +
                '}';
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public PlatAuMenu getPlat() {
        return plat;
    }

    public PlatChoisiEtat getEtat() {
        return this.etat;
    }
    public void Preparer() {
        etat = etat.Preparer(this);
    }
    public String getDescription(){
        return plat.getDescription();
    }
    public void Terminer() {
        etat = etat.Terminer(this);
    }
    public void Servir() {
        etat = etat.Servir(this);
    }
}
