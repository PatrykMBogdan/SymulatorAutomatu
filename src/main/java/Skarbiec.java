import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Skarbiec {
    private Map<Nominal,Integer> monety;

    void zaladujStartowe(int ilosc){
        this.monety = new HashMap<>();
        for(Nominal nominal : Nominal.values()){
            this.monety.put(nominal, ilosc);
        }
    }
    private static List<Nominal> obliczWrzut(double wrzucone){
        List<Nominal> obliczonyWrzut = new ArrayList<>();
        int wrzuconeGrosze = (int) (wrzucone *100);
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
            if(wrzucone <= 0) return obliczonyWrzut;
        }
        return obliczonyWrzut;
    }

    void dodajMonetyWrzut(List<Nominal> wrzucone){
        for(Nominal wrzuconaMoneta : wrzucone){
            int obecnaIlosc = monety.getOrDefault(wrzuconaMoneta, 0);
            this.monety.put(wrzuconaMoneta, obecnaIlosc + 1);
        }
    }
    //TODO : Wydawanie reszty niech uwzglednia jakie ma monety
    public static List<Nominal> obliczOptymalnaReszte(double resztaDoWydania){
        List<Nominal> monetyReszta = obliczWrzut(resztaDoWydania);
        return monetyReszta;
    }

    public void wydajMonety(List<Nominal> monetyDoWydania) {
        for(Nominal wrzuconaMoneta : monetyDoWydania) {
            int obecnaIlosc = monety.getOrDefault(wrzuconaMoneta, 0);
            this.monety.put(wrzuconaMoneta, obecnaIlosc - 1);
        }

    }
}
