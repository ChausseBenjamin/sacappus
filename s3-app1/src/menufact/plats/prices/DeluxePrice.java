package menufact.plats.prices;

public class DeluxePrice extends PriceFlyweight {
    private static DeluxePrice instance = new DeluxePrice();
    private DeluxePrice(){
        super(30);
    }
    public static DeluxePrice getInstance(){
        return instance;
    }
    public void setPrice(double prix){
        this.price = prix;
    }
}
