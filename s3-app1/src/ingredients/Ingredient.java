package ingredients;

import ingredients.exceptions.IngredientException;
import menufact.Observer;


public abstract class Ingredient {
    /** Nom de l'ingredient */
    private String nom;
    /** Description de l'ingredient */
    private String description;

    /**
     * Quantity in grams of that ingredient:
     *  - In a PlatAuMenu, this is how much of this ingredient is used to make the meal
     *  - In the Inventaire, this is the current stock of that ingredient
     */
    private int quantity; // Quantity in grams of that ingredient:


    /** Calories per 100 gram of this ingredient */
    private int kcalPercent;

    /** Fat per 100 gram of this ingredient */
    private int fatPercent;

    /** Cholesterol per 100 gram of this ingredient */
    private int cholPercent;
    boolean sante;

    public Ingredient(String nom, String description, int quantity, int kcalPercent, int fatPercent, int cholPercent,boolean sante) {
        this.nom = nom;
        this.description = description;
        this.quantity = quantity;
        this.kcalPercent = kcalPercent;
        this.fatPercent = fatPercent;
        this.cholPercent = cholPercent;
        this.sante = sante;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /** Adds the given quantity to the current quantity
     * This is mostly used in the Inventaire to update the stock of an ingredient
     * PlatAuMenu should not use this method. If one would like to change the quantity of an ingredient in a PlatAuMenu,
     * they should use the setQuantity method instead.
     * @param qty the quantity to add
     */
    public void updateQuantity(int qty) throws IngredientException{
        quantity += qty;
        // This is used to remove any Plat from the menu that has an ingredient with insufficient stock
        if (qty < 0) Observer.trimMenu();
        // If ingredients were added to the stock, we need to check if we can add any Plat back into the menu
        else Observer.populateMenu();
        if (quantity < 0) throw new IngredientException("La quantité ne peut pas être négative");
    }

    /** Returns the name of an ingredient */
    public String getNom() {
        return nom;
    }

    /** Change the name of an ingredient */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /** Returns the description of an ingredient */
    public String getDescription() {
        return description;
    }

    /** Change the description of an ingredient */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Calculates the total amount of cholesterol in this ingredient
     * @return the amount of cholesterol in this ingredient
     */
    public int getChol() {
        return cholPercent * quantity / 100;
    }

    /**
     * Calculates the total amount of fat in this ingredient
     * @return the amount of fat in this ingredient
     */
    public int getFat() {
        return fatPercent * quantity / 100;
    }

    /**
     * Calculates the total amount of kcal in this ingredient
     * @return the amount of kcal in this ingredient
     */
    public int getKcal(){
        return kcalPercent * quantity / 100;
    }

    /**
     * Gives the amount of kcal per 100g in this ingredient
     * @return the amount of cholesterol per 100g in this ingredient
     */
    public int getCholPercent() {
        return cholPercent;
    }

    /**
     * Gives the amount of fat per 100g in this ingredient
     * @return the amount of fat per 100g in this ingredient
     */
    public int getFatPercent() {
        return fatPercent;
    }

    /**
     * Gives the amount of kcal per 100g in this ingredient
     * @return the amount of kcal per 100g in this ingredient
     */
    public int getKcalPercent() {
        return kcalPercent;
    }
    public boolean isSante() {
        return sante;
    }
}
