package menufact.plats;

public interface PlatChoisiEtat {

    PlatChoisiEtat Preparer(PlatChoisi plat);
    PlatChoisiEtat Terminer(PlatChoisi plat);
    PlatChoisiEtat Servir(PlatChoisi plat);

    void getEtat();

}
