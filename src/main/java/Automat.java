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
    //TODO: Zastapic klasa klient te zmienne i funkcje do sprawdzDostawy
    public static double sredniaKwota;

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

    private void sprawdzDostawy(){

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
        switch (region){
            case UBOGI -> sredniaKwota = 3;
            case SREDNI -> sredniaKwota = 5;
            case BOGATY -> sredniaKwota = 12;
            default -> sredniaKwota = 10;
        }
        for(int i = 0; i < iloscProb; i++){
            if(i > 0 && i % automat.czestotliwoscDostaw == 0)
                for(int j = 0 ; j<listaProduktow.size(); j++) listaProduktow.get(j).uzupelnijDoPelna();
            Produkt wylosowanyProdukt = wylosujProdukt();
            automat.listaTransakcji.add(automat.sprobujKupic(wylosowanyProdukt, sredniaKwota));
            if(automat.listaTransakcji.getLast().czyUdaloSie()){
                skarbiec.dodajMonetyWrzut(skarbiec.obliczWrzut(sredniaKwota));
                skarbiec.wydajMonety(skarbiec.obliczOptymalnaReszte(sredniaKwota - wylosowanyProdukt.getCena()));
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
