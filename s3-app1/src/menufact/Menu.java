package menufact;

import menufact.exceptions.MenuException;
import menufact.plats.PlatAuMenu;

import java.util.ArrayList;

public class Menu {
    private String description;
    private int courant;
    private ArrayList<PlatAuMenu> plat = new ArrayList<PlatAuMenu>();
    private static Menu menu = null;

    private Menu(String description) {
        this.description = description;
    }
    public static Menu getInstance(String description) {
        if (menu == null) {
            menu = new Menu(description);
        }
        return menu;
    }

    void ajoute (PlatAuMenu p) {
        plat.add(p);
    }

    void retire (PlatAuMenu p) {
        plat.remove(p);
    }

    public void position(int i) {
        courant = i;
    }

    public PlatAuMenu platCourant() {
        return plat.get(courant);
    }

    public void positionSuivante() throws MenuException {
        if (courant+1 >= plat.size())
            throw new MenuException("On depasse le nombre maximale de plats.");
        else
            courant++;
    }

    public void positionPrecedente() throws MenuException {
        if (courant-1 < 0)
            throw new MenuException("On depasse le nombre minimale de plats");
        else
            courant--;
    }

    public ArrayList<PlatAuMenu> getPlats(){
        return plat;
    }

    // TODO: Perhaps delete the courrant tracker. Ideally, this kind of logic should be implemented by the view.
    // TODO: If courrant gets removed, update toString to print every Menu Element.
    @Override
    public String toString() {
        return "menufact.Menu{" +
                "description='" + description + '\'' +
                ", courant=" + courant +
                ", plat=" + "\n" + plat +
                '}';
    }
}
