package ingredients;

/** Ingredient qui se trouve au centre du sushi (ex: Concombre, Thon, Saumon, Avocat)*/
public class Garniture extends Ingredient {
    public Garniture(String nom, String description, int quantite, int kcalPercent, int fatPercent, int cholPercent, boolean sante) {
        super(nom, description, quantite, kcalPercent, fatPercent, cholPercent, sante);
    }
}
