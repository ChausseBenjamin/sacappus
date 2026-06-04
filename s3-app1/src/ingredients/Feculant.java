package ingredients;

/** Ingrédient le plus prominent du suhi (ex: Riz, Quinoa, Couscous) */
public class Feculant extends Ingredient {
    public Feculant(String nom, String description, int quantite, int kcalPercent, int fatPercent, int cholPercent, boolean sante) {
        super(nom, description, quantite, kcalPercent, fatPercent, cholPercent, sante);
    }
}
