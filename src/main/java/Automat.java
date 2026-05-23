public class Automat {
    private int czestotliwoscDostaw;
    private Region poziomZamoznosci;
    private int licznikTransakcji;
    private int sprzedaneProdukty;
    private int nieudaneTransakcje;

    public Automat(int czestotliwoscDostaw, Region poziomZamoznosci){
        this.czestotliwoscDostaw = czestotliwoscDostaw;
        this.poziomZamoznosci = poziomZamoznosci;
        this.licznikTransakcji = 0;
        this.sprzedaneProdukty = 0;
        this.nieudaneTransakcje = 0;
    }
    public static void uruchomSymulacje(int iloscProb, Region region, int dostawyCo, int StartoweMonety){
        Automat automat_metadruk = new Automat(dostawyCo,region);

    }
    private void sprawdzDostawy(){

    }
    public Transakcja sprobujKupic(Produkt produkt, double wrzuconaKwota){
    }
    public void generujPodsumowanie(){

    }
}
