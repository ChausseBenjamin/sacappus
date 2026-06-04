package ingredients;

/** Ingredient qui enrobe le sushi (ex: Panure ou feuille d'algues) */
public class Enrobage extends Ingredient {
    public Enrobage(String nom, String description, int quantite, int kcalPercent, int fatPercent, int cholPercent, boolean sante) {
        super(nom, description, quantite, kcalPercent, fatPercent, cholPercent, sante);
    }
}
