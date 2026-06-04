package menufact.plats.prices;

public class EconoPrice extends PriceFlyweight {
    private static EconoPrice instance = new EconoPrice();
    private EconoPrice(){
        super(10);
    }
    public static PriceFlyweight getInstance(){
        return instance;
    }
    public void setPrice(double prix){
        this.price = prix;
    }

}