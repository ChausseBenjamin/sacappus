package menufact.plats;

public class Termine implements PlatChoisiEtat {
    private PlatChoisi plat;

    public Termine(PlatChoisi plat){
        this.plat = plat;
        System.out.println("Plat " + this.plat.getDescription() + " terminé Serveur notifié!");
    }
    @Override
    public void getEtat(){
        System.out.println("Plat " + this.plat.getDescription() + " en Terminé");
    }
    @Override
    public PlatChoisiEtat Preparer(PlatChoisi plat){
        System.out.println("Plat " + this.plat.getDescription() + " terminé");
        return this;
    }
    @Override
    public PlatChoisiEtat Terminer(PlatChoisi plat){
        System.out.println("Plat terminé");
        return this;
    }
    @Override
    public PlatChoisiEtat Servir(PlatChoisi plat){
        Servi a = new Servi(plat);
        return a;
    }
}