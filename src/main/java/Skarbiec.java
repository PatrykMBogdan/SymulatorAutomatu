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
    List<Nominal> obliczWrzut(double wrzucone){
        List<Nominal> obliczonyWrzut = new ArrayList<>();
        int wrzuconeGrosze = (int) wrzucone *100;
        int monetaObliczana = 0;
        for(Nominal nominal : Nominal.values()){
            switch (nominal){
                case PLN_5_00 -> monetaObliczana = 5*100;
                case PLN_2_00 -> monetaObliczana = 2*100;
                case PLN_1_00 -> monetaObliczana = 1*100;
                case GR_50 -> monetaObliczana = (int) 0.5*100;
                case GR_20 -> monetaObliczana = (int) 0.2*100;
                case GR_10 -> monetaObliczana = (int) 0.1*100;
            }
            while (wrzuconeGrosze - monetaObliczana >= 0){
                wrzucone -= monetaObliczana;
                obliczonyWrzut.add(nominal);
            }
            if(wrzucone <= 0) return obliczonyWrzut;
        }
        return obliczonyWrzut;
    }

    void dodajMonetyWrzut(List<Nominal> wrzucone){

    }
    /*List<Nominal> obliczOptymalnaReszte(double kwota){

    }
    */
    public void wydajMonety(List<Nominal> monetyDoWydania) {

    }

    public static void main(String[] args) {
        Skarbiec test = new Skarbiec();
        List<Nominal> re = test.obliczWrzut(04.9);
        System.out.println(re);
    }
}
