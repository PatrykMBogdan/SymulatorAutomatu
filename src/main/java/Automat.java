import java.util.*;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;

public class Automat {
    private int czestotliwoscDostaw;
    private Region poziomZamoznosci;
    private int licznikTransakcji;
    public int sprzedaneProdukty;
    private int nieudaneTransakcje;
    private static List<Produkt> listaProduktow = new ArrayList<>();
    private List<Transakcja> listaTransakcji = new ArrayList<>();

    private static Produkt wylosujProdukt(){
        Random random = new Random();
        int wylosowanyIndeks = random.nextInt(listaProduktow.size());
        return listaProduktow.get(wylosowanyIndeks);
    }

    private static void dodajTestoweProdukty(){
        listaProduktow.add(new Produkt("CocaCola", 3.9, 12));
        listaProduktow.add(new Produkt("Pepsi", 4.9, 10));
        listaProduktow.add(new Produkt("Fajny Wafel", 2, 22));
        listaProduktow.add(new Produkt("Kupon na talon", 12, 4));
    }

    private void sprawdzDostawy(int numerKlienta){
        if(numerKlienta > 0 && numerKlienta % this.czestotliwoscDostaw == 0){
            for(int j = 0 ; j<listaProduktow.size(); j++){
                listaProduktow.get(j).uzupelnijDoPelna();
            }
        }
    }
    public Automat(Region poziomZamoznosci, int dostawyCo){
        this.poziomZamoznosci = poziomZamoznosci;
        this.czestotliwoscDostaw = dostawyCo;
    }
    public static void uruchomSymulacje(int iloscProb, Region region, int dostawyCo, int StartoweMonety){
        Automat automat = new Automat(region, dostawyCo);
        Skarbiec skarbiec = new Skarbiec();
        skarbiec.zaladujStartowe(StartoweMonety);
        dodajTestoweProdukty();
        Random random = new Random();

        for(int i = 0; i < iloscProb; i++){
            automat.sprawdzDostawy(i);
            Produkt wylosowanyProdukt = wylosujProdukt();
            double wrzuconaKwota = 0;
            switch (region) {
                case UBOGI -> {
                    double[] mozliweKwoty = {2.0, 3.0, 4.0};
                    wrzuconaKwota = mozliweKwoty[random.nextInt(mozliweKwoty.length)];
                }
                case SREDNI -> {
                    double[] mozliweKwoty = {5.0, 6.0, 7.0, 8.0, 9.0};
                    wrzuconaKwota = mozliweKwoty[random.nextInt(mozliweKwoty.length)];
                }
                case BOGATY -> {
                    double[] mozliweKwoty = {10.0, 15.0, 20.0, 25.0, 30.0};
                    wrzuconaKwota = mozliweKwoty[random.nextInt(mozliweKwoty.length)];
                }
                default -> wrzuconaKwota = 10.0;
            }
            automat.listaTransakcji.add(automat.sprobujKupic(wylosowanyProdukt, wrzuconaKwota));
            if(automat.listaTransakcji.getLast().czyUdaloSie()){
                skarbiec.dodajMonetyWrzut(skarbiec.obliczWrzut(wrzuconaKwota));
                skarbiec.wydajMonety(skarbiec.obliczOptymalnaReszte(wrzuconaKwota - wylosowanyProdukt.getCena()));
                automat.sprzedaneProdukty++;
            }
            else automat.nieudaneTransakcje++;

        }
        automat.generujPodsumowanie(skarbiec, automat);
    }
    public Transakcja sprobujKupic(Produkt produkt, double wrzuconaKwota){
        if(!produkt.czyDostepny()) return new Transakcja(StatusTransakcji.BRAK_PRODUKTU, wrzuconaKwota);
        if(wrzuconaKwota >= produkt.getCena()){
            produkt.zmniejszIlosc();
            return new Transakcja(StatusTransakcji.UDANA, wrzuconaKwota);
        }
        else return new Transakcja(StatusTransakcji.ZA_MALO_GOTOWKI, wrzuconaKwota);
    }

    public void generujPodsumowanie(Skarbiec skarbiec, Automat automat){
        System.out.println(skarbiec.przeliczNaKwote(skarbiec.getMonety()));
        Map<StatusTransakcji, Long> iloscPoStatusie = automat.listaTransakcji.stream().collect(Collectors.groupingBy(Transakcja::getStatus, Collectors.counting()));
        System.out.println(iloscPoStatusie);
        System.out.println("Stan szufladek po dniu pracy: " + skarbiec.getMonety());
    }
}
