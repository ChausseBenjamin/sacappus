package menufact.plats;

public class EnPreparation implements PlatChoisiEtat {
    private PlatChoisi plat;

    public EnPreparation(PlatChoisi plat){
        this.plat = plat;
        System.out.println("Plat en préparation");
    }
    @Override
    public void getEtat(){
        System.out.println("Plat " + this.plat.getDescription() + " en préparation");
    }
    @Override
    public PlatChoisiEtat Preparer(PlatChoisi plat){
        System.out.println("Plat " + this.plat.getDescription() + " déjà en cours de préparation");
        return this;
    }
    @Override
    public PlatChoisiEtat Terminer(PlatChoisi plat){
        Termine a = new Termine(plat);
        return a;
    }
    @Override
    public PlatChoisiEtat Servir(PlatChoisi plat){
        System.out.println("Plat " + this.plat.getDescription() + " en commande, préparer le plat avant la prochaine étape");
        return this;
    }
}
