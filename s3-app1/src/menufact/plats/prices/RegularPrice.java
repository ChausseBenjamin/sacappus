package menufact.plats.prices;

public class RegularPrice extends PriceFlyweight {
    private static RegularPrice instance = new RegularPrice();
    private RegularPrice(){
        super(20);
    }
    public static RegularPrice getInstance(){
        return instance;
    }
    public void setPrice(double prix){
        this.price = prix;
    }
}
