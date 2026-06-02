public class Symulacja {
    public static void main(String[] args){
        System.out.println("POCZĄTEK SYMULACJI");

        int iloscProb = 10000;
        Region region = Region.SREDNI;
        int dostawyCo = 60;
        int startoweMonety = 1;

        Automat.uruchomSymulacje(
         iloscProb,
         region,
         dostawyCo,
         startoweMonety
        );
        System.out.println("KONIEC SYMULACJI");
    }
}
