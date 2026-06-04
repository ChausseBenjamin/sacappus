package menufact.plats.prices;

public abstract class PriceFlyweight{
    protected double price;

    public PriceFlyweight(double price){
        this.price = price;
    }


    public double getPrice(){
        return price;
    }
    public abstract void setPrice(double prix);
}

