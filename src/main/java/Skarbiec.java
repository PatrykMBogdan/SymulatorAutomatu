import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Klasa reprezentująca kasetkę na pieniądze w automacie.
 * Odpowiada za przeliczanie bilonu, przyjmowanie wpłat oraz wydawanie reszty algorytmem zachłannym.
 *
 * @author Szymon Łakomy, Patryk Bogdan
 */
public class Skarbiec {
    private Map<Nominal,Integer> monety;
    /**
     * Pobiera aktualny stan kasetki z monetami.
     *
     * @return Mapa przechowująca nominały i ich fizyczną ilość
     */
    public Map<Nominal, Integer> getMonety() {
        return monety;
    }

    void zaladujStartowe(int ilosc){
        this.monety = new HashMap<>();
        for(Nominal nominal : Nominal.values()){
            this.monety.put(nominal, ilosc);
        }
    }
    /**
     * Rozbija wrzuconą przez klienta kwotę na konkretne nominały monet algorytmem zachłannym.
     *
     * @param wrzucone Kwota wrzucona w PLN przez klienta
     * @return Lista monet reprezentująca wrzuconą kwotę
     */
    public List<Nominal> obliczWrzut(double wrzucone){
        List<Nominal> obliczonyWrzut = new ArrayList<>();
        int wrzuconeGrosze = (int) Math.round(wrzucone *100);
        int monetaObliczanaWGroszach = 0;
        for(Nominal nominal : Nominal.values()){
            switch (nominal){
                case PLN_5_00 -> monetaObliczanaWGroszach = 500;
                case PLN_2_00 -> monetaObliczanaWGroszach = 200;
                case PLN_1_00 -> monetaObliczanaWGroszach = 100;
                case GR_50 -> monetaObliczanaWGroszach = 50;
                case GR_20 -> monetaObliczanaWGroszach = 20;
                case GR_10 -> monetaObliczanaWGroszach = 10;
            }
            while (wrzuconeGrosze - monetaObliczanaWGroszach >= 0){
                wrzuconeGrosze -= monetaObliczanaWGroszach;
                obliczonyWrzut.add(nominal);
            }
            if(wrzuconeGrosze <= 0) return obliczonyWrzut;
        }
        return obliczonyWrzut;
    }

    void dodajMonetyWrzut(List<Nominal> wrzucone){
        for(Nominal wrzuconaMoneta : wrzucone){
            int obecnaIlosc = monety.getOrDefault(wrzuconaMoneta, 0);
            this.monety.put(wrzuconaMoneta, obecnaIlosc + 1);
        }
    }
    /**
     * Oblicza optymalną resztę do wydania, bazując na fizycznie dostępnych monetach w kasetce.
     *
     * @param resztaDoWydania Kwota reszty wyrażona w złotówkach (np. 2.50)
     * @return Lista nominałów do fizycznego wydania klientowi
     */
    public List<Nominal> obliczOptymalnaReszte(double resztaDoWydania){
        List<Nominal> wydawanaReszta = new ArrayList<>();

        int resztaWGroszach = (int) Math.round(resztaDoWydania*100);
        int wartoscMonetyWGroszach = 0;
        for(Nominal nominal : Nominal.values()){
            switch (nominal){
                case PLN_5_00 -> wartoscMonetyWGroszach = 500;
                case PLN_2_00 -> wartoscMonetyWGroszach = 200;
                case PLN_1_00 -> wartoscMonetyWGroszach = 100;
                case GR_50 -> wartoscMonetyWGroszach = 50;
                case GR_20 -> wartoscMonetyWGroszach = 20;
                case GR_10 -> wartoscMonetyWGroszach = 10;
            }
            int dostepneMonety = this.monety.getOrDefault(nominal, 0);
            int potrzebneMonety = resztaWGroszach / wartoscMonetyWGroszach;
            int ileWydac = Math.min(dostepneMonety,potrzebneMonety);

            for(int i=0;i<ileWydac;i++){
                wydawanaReszta.add(nominal);
                resztaWGroszach = resztaWGroszach - wartoscMonetyWGroszach;
            }
            if(resztaWGroszach==0){
                break;
            }
        }
        return wydawanaReszta;
    }
    /**
     * Fizycznie usuwa monety ze skarbca podczas wydawania reszty klientowi.
     *
     * @param monetyDoWydania Lista monet, które opuszczają kasetkę
     */
    public void wydajMonety(List<Nominal> monetyDoWydania) {
        for(Nominal wrzuconaMoneta : monetyDoWydania) {
            int obecnaIlosc = monety.getOrDefault(wrzuconaMoneta, 0);
            this.monety.put(wrzuconaMoneta, obecnaIlosc - 1);
        }
    }
    /**
     * Przelicza stan skarbca (mapę monet) na łączną kwotę w złotówkach.
     *
     * @param monety Mapa nominałów i ich ilości
     * @return Łączna wartość w PLN zaokrąglona do dwóch miejsc po przecinku
     */
    public double przeliczNaKwote(Map<Nominal, Integer> monety){
        double suma = 0;
        for(Map.Entry<Nominal, Integer> nominal : monety.entrySet()){
            Nominal moneta = nominal.getKey();
            int iloscSztuk = nominal.getValue();
            suma += moneta.getWartosc() * iloscSztuk;
        }
        return Math.round(suma * 100)/100.0;
    }
    /**
     * Przelicza listę monet (np. resztę do wydania) na łączną kwotę w złotówkach.
     *
     * @param monety Lista nominałów
     * @return Łączna wartość w PLN zaokrąglona do dwóch miejsc po przecinku
     */
    public double przeliczNaKwote(List<Nominal> monety){
        double suma = 0;
        for(Nominal moneta : monety){
            suma += moneta.getWartosc();
        }
        return Math.round(suma*100.0)/100.0;
    }
}
