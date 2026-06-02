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

    private double poczatkowyStanKasetki;
    private Map<String, Integer> sprzedaneBestsellery = new HashMap<>();

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
        this.czestotliwoscDostaw =  dostawyCo;
    }
    public static void uruchomSymulacje(int iloscProb, Region region, int dostawyCo, int StartoweMonety){
        Automat automat = new Automat(region, dostawyCo);
        Skarbiec skarbiec = new Skarbiec();
        skarbiec.zaladujStartowe(StartoweMonety);
        dodajTestoweProdukty();

        automat.poczatkowyStanKasetki = skarbiec.przeliczNaKwote(skarbiec.getMonety());
        Random random = new Random();

        for(int i = 0; i < iloscProb; i++){
            automat.sprawdzDostawy(i);
            Produkt wylosowanyProdukt = wylosujProdukt();
            double wrzuconaKwota = 0;
            switch (region) {
                case UBOGI -> {
                    wrzuconaKwota = random.nextInt(20, 50) / 10.0;
                }
                case SREDNI -> {
                    wrzuconaKwota = random.nextInt(50, 100) / 10.0;
                }
                case BOGATY -> {
                    wrzuconaKwota = random.nextInt(10, 31);
                }
                default -> wrzuconaKwota = 10.0;
            }
            automat.listaTransakcji.add(automat.sprobujKupic(wylosowanyProdukt, wrzuconaKwota, skarbiec));
            if(automat.listaTransakcji.getLast().czyUdaloSie()){
                skarbiec.dodajMonetyWrzut(skarbiec.obliczWrzut(wrzuconaKwota));
                skarbiec.wydajMonety(skarbiec.obliczOptymalnaReszte(wrzuconaKwota - wylosowanyProdukt.getCena()));
                automat.sprzedaneProdukty++;
                automat.sprzedaneBestsellery.put(wylosowanyProdukt.getNazwa(), automat.sprzedaneBestsellery.getOrDefault(wylosowanyProdukt.getNazwa(), 0) + 1);
            }
            else automat.nieudaneTransakcje++;

        }
        automat.generujPodsumowanie(skarbiec, automat);
    }
    public Transakcja sprobujKupic(Produkt produkt, double wrzuconaKwota, Skarbiec skarbiec) {
        if (!produkt.czyDostepny()) return new Transakcja(StatusTransakcji.BRAK_PRODUKTU, wrzuconaKwota);
        if (wrzuconaKwota < produkt.getCena()) return new Transakcja(StatusTransakcji.ZA_MALO_GOTOWKI, wrzuconaKwota);
        if (skarbiec.przeliczNaKwote(skarbiec.obliczOptymalnaReszte(wrzuconaKwota - produkt.getCena())) != (wrzuconaKwota - produkt.getCena())) return new Transakcja(StatusTransakcji.NIE_MA_JAK_WYDAC, wrzuconaKwota);
        produkt.zmniejszIlosc();
        return new Transakcja(StatusTransakcji.UDANA, wrzuconaKwota);
    }

    public void generujPodsumowanie(Skarbiec skarbiec, Automat automat){
        System.out.println("\n==================================================");
        System.out.println("      RAPORT DZIENNY Z SYMULACJI AUTOMATU     ");
        System.out.println("==================================================");

        double obecnyStanKasetki = skarbiec.przeliczNaKwote(skarbiec.getMonety());
        double zyskNetto = Math.round((obecnyStanKasetki - automat.poczatkowyStanKasetki) * 100) / 100.0;

        System.out.println("\n FINANSE:");
        System.out.println("--------------------------------------------------");
        System.out.println(" Laczna kwota w kasetce: " + obecnyStanKasetki + " PLN");
        System.out.println(" Zysk na czysto (utarg): " + zyskNetto + " PLN");
        System.out.println("\n Ilosc monet w kasetce: ");

        skarbiec.getMonety().forEach((nominal, ilosc) ->
                System.out.println("   - " + nominal + ": " + ilosc + " szt.")
        );

        System.out.println("\n STATYSTYKI SPRZEDAZY:");
        System.out.println("--------------------------------------------------");
        Map<StatusTransakcji, Long> iloscPoStatusie = automat.listaTransakcji.stream()
                .collect(Collectors.groupingBy(Transakcja::getStatus, Collectors.counting()));


        long udane = iloscPoStatusie.getOrDefault(StatusTransakcji.UDANA, 0L);
        long zaMaloHajsu = iloscPoStatusie.getOrDefault(StatusTransakcji.ZA_MALO_GOTOWKI, 0L);
        long brakTowaru = iloscPoStatusie.getOrDefault(StatusTransakcji.BRAK_PRODUKTU, 0L);
        long nieMaJakWydac = iloscPoStatusie.getOrDefault(StatusTransakcji.NIE_MA_JAK_WYDAC, 0L);

        System.out.println("  Udane transakcje (wydano towar):    " + udane);
        System.out.println("  Odrzucone (za malo gotowki):        " + zaMaloHajsu);
        System.out.println("  Odrzucone (brak towaru na polce):   " + brakTowaru);
        System.out.println("  Odrzucone (automat nie mial jak wydac reszty):   " + nieMaJakWydac);

        System.out.println("\n TOP PRODUKTY (BESTSELLERY):");
        System.out.println("--------------------------------------------------");
        if (automat.sprzedaneBestsellery.isEmpty()) {
            System.out.println("  Brak sprzedanych produktow.");
        } else {
            automat.sprzedaneBestsellery.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).forEach(entry -> System.out.println("  - " + entry.getKey() + ": " + entry.getValue() + " szt."));
        }
        System.out.println("\n==================================================");
        System.out.println("              ZAMKNIECIE SYSTEMU                  ");
        System.out.println("==================================================\n");
    }
}
