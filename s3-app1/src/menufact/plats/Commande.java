package menufact.plats;

public class Commande implements PlatChoisiEtat {

    private PlatChoisi plat;

    public Commande(PlatChoisi plat){
        this.plat = plat;
        System.out.println("Plat " + this.plat.getDescription() + " commandé. Cuisine notifié!");
    }
    @Override
    public void getEtat(){
        System.out.println("Plat  " + this.plat.getDescription() + " en commande");
    }
    @Override
    public PlatChoisiEtat Preparer(PlatChoisi plat){
        EnPreparation a = new EnPreparation(plat);
        return a;
    }
    @Override
    public PlatChoisiEtat Terminer(PlatChoisi plat){
        System.out.println("Plat " + this.plat.getDescription() + " en commande, préparer le plat avant la prochaine étape");
        return this;
    }
    @Override
    public PlatChoisiEtat Servir(PlatChoisi plat){
        System.out.println("Plat  " + this.plat.getDescription() + " en commande, préparer le plat avant la prochaine étape");
        return this;
    }
}