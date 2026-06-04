package menufact.plats;

public class Servi implements PlatChoisiEtat {
    private PlatChoisi plat;

    public Servi(PlatChoisi plat){
        this.plat = plat;
        System.out.println("Plat " + this.plat.getDescription() + " Servi!");
    }
    @Override
    public void getEtat(){
        System.out.println("Plat en servi");
    }
    @Override
    public PlatChoisiEtat Preparer(PlatChoisi plat){
        System.out.println("Plat " + this.plat.getDescription() + " déjà servi");
        return this;
    }
    @Override
    public PlatChoisiEtat Terminer(PlatChoisi plat){
        System.out.println("Plat " + this.plat.getDescription() + " déjà servi");
        return this;
    }
    @Override
    public PlatChoisiEtat Servir(PlatChoisi plat){
        System.out.println("Plat " + this.plat.getDescription() + " déjà servi");
        return this;
    }
}
