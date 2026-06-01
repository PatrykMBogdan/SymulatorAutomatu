import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Skarbiec {
    private Map<Nominal,Integer> monety;

    public Map<Nominal, Integer> getMonety() {
        return monety;
    }

    void zaladujStartowe(int ilosc){
        this.monety = new HashMap<>();
        for(Nominal nominal : Nominal.values()){
            this.monety.put(nominal, ilosc);
        }
    }
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
    //TODO : Wydawanie reszty niech uwzglednia jakie ma monety
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

    public void wydajMonety(List<Nominal> monetyDoWydania) {
        for(Nominal wrzuconaMoneta : monetyDoWydania) {
            int obecnaIlosc = monety.getOrDefault(wrzuconaMoneta, 0);
            this.monety.put(wrzuconaMoneta, obecnaIlosc - 1);
        }
    }

    public double przeliczNaKwote(Map<Nominal, Integer> monety){
        double suma = 0;
        for(Map.Entry<Nominal, Integer> nominal : this.monety.entrySet()){
            Nominal moneta = nominal.getKey();
            int iloscSztuk = nominal.getValue();
            suma += moneta.getWartosc() * iloscSztuk;
        }
        return Math.round(suma * 100)/100.0;
    }
}
