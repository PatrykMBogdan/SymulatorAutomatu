import java.util.*;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
/**
 * Główna klasa symulująca działanie automatu vendingowego.
 * Zarządza listą dostępnych produktów, historią transakcji oraz przeprowadza cały proces kupowania przez klienta.
 * * @author Szymon Łakomy, Patryk Bogdan
 */

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
        listaProduktow.add(new Produkt("Coca-Cola 0.5l", 4.5, 15));
        listaProduktow.add(new Produkt("Pepsi 0.5l", 4.5, 15));
        listaProduktow.add(new Produkt("Woda Niegazowana 0.5l", 3.0, 20));
        listaProduktow.add(new Produkt("Woda Gazowana 0.5l", 3.0, 20));
        listaProduktow.add(new Produkt("Baton Snickers", 3.5, 25));
        listaProduktow.add(new Produkt("Baton Mars", 3.5, 25));
        listaProduktow.add(new Produkt("Baton Twix", 3.5, 25));
        listaProduktow.add(new Produkt("Rogalik 7Days", 4.0, 10));
        listaProduktow.add(new Produkt("Chipsy Lays Paprykowe", 5.5, 8));
        listaProduktow.add(new Produkt("Chipsy Lays Solone", 5.5, 8));
        listaProduktow.add(new Produkt("Paluszki Beskidzkie", 3.0, 12));
        listaProduktow.add(new Produkt("Zelki Haribo", 4.5, 15));
    }

    private void sprawdzDostawy(int numerKlienta){
        if(numerKlienta > 0 && numerKlienta % this.czestotliwoscDostaw == 0){
            for(int j = 0 ; j<listaProduktow.size(); j++){
                listaProduktow.get(j).uzupelnijDoPelna();
            }
        }
    }
    /**
     * Konstruktor klasy Automat.
     *
     * @param poziomZamoznosci Region określający portfele klientów
     * @param dostawyCo Liczba transakcji, po których następuje uzupełnienie towaru
     */
    public Automat(Region poziomZamoznosci, int dostawyCo){
        this.poziomZamoznosci = poziomZamoznosci;
        this.czestotliwoscDostaw =  dostawyCo;
    }
    /**
     * Uruchamia główną pętlę symulacji dla ustalonej w klasie Symulacja, liczby klientów.
     *
     * @param iloscProb Liczba transakcji do przeprowadzenia w danej symulacji
     * @param region Status majątkowy klientów determinujący wrzucane kwoty
     * @param dostawyCo Co ile transakcji automat ma być uzupełniany spowrotem do pełna
     * @param StartoweMonety Ilość sztuk każdej monety w kasetce przy rozpoczeciu symulacji
     */
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
        /**
         * Próbuje przeprowadzić transakcję zakupu dla podanego produktu.
         *
         * @param produkt Wybrany produkt z maszyny
         * @param wrzuconaKwota Kwota wrzucona przez klienta
         * @param skarbiec Instancja skarbca do weryfikacji możliwości wydania reszty
         * @return Obiekt transakcji zawierający status i wrzuconą kwotę
         */
    public Transakcja sprobujKupic(Produkt produkt, double wrzuconaKwota, Skarbiec skarbiec) {
        if (!produkt.czyDostepny()) return new Transakcja(StatusTransakcji.BRAK_PRODUKTU, wrzuconaKwota);
        if (wrzuconaKwota < produkt.getCena()) return new Transakcja(StatusTransakcji.ZA_MALO_GOTOWKI, wrzuconaKwota);

        double oczekiwanaReszta = Math.round((wrzuconaKwota - produkt.getCena()) * 100) / 100.0;
        double resztaZeSkarbca = skarbiec.przeliczNaKwote(skarbiec.obliczOptymalnaReszte(oczekiwanaReszta));

        if (resztaZeSkarbca != oczekiwanaReszta) {
            return new Transakcja(StatusTransakcji.NIE_MA_JAK_WYDAC, wrzuconaKwota);
        }

        produkt.zmniejszIlosc();
        return new Transakcja(StatusTransakcji.UDANA, wrzuconaKwota);
    }
     /**
      * Generuje i wyświetla w konsoli końcowy raport z symulacji.
      * Wypisuje stan kasetki, zysk netto, statystyki błędów oraz listę bestsellerów.
      *
      * @param skarbiec Skarbiec zawierający utarg
      * @param automat Automat posiadający statystyki sprzedaży
      */
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
