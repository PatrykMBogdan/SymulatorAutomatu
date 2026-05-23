import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class Automat {
    private int czestotliwoscDostaw;
    private Region poziomZamoznosci;
    private int licznikTransakcji;
    private int sprzedaneProdukty;
    private int nieudaneTransakcje;
    private static List<Produkt> listaProduktow = new ArrayList<>();
    //TODO: Zastapic klasa klient te zmienne i funkcje do sprawdzDostawy
    public static double sredniaKwota;

    private static Produkt wylosujProdukt(){
        Random random = new Random();
        int wylosowanyIndeks = random.nextInt(listaProduktow.size());
        return listaProduktow.get(wylosowanyIndeks);
    }
    private void sprawdzDostawy(){

    }
    public Automat(Region poziomZamoznosci, int dostawyCo){
        this.poziomZamoznosci = poziomZamoznosci;
        this.czestotliwoscDostaw = dostawyCo;
    }
    public static void uruchomSymulacje(int iloscProb, Region region, int dostawyCo, int StartoweMonety){
        Automat automat = new Automat(region, dostawyCo);
        switch (region){
            case UBOGI -> sredniaKwota = 3;
            case SREDNI -> sredniaKwota = 5;
            case BOGATY -> sredniaKwota = 10;
            default -> sredniaKwota = 10;
        }
        for(int i=0; i<iloscProb; i++){
            Produkt wylosowanyProdukt = wylosujProdukt();

        }
    }
    /*public Transakcja sprobujKupic(Produkt produkt, double wrzuconaKwota){
        //if(wylosowanyProdukt.czyDostepny())
        // if(wylosowanyProdukt.cena?);     nw czy dziala bo nie ma produkt xddddd
    }

     */
    public void generujPodsumowanie(){

    }
}
