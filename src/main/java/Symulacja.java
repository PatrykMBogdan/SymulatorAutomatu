public class Symulacja {
    public static void main(String[] args){
        System.out.println("POCZĄTEK SYMULACJI");

        int iloscProb = 100;
        Region region = Region.SREDNI;
        int dostawyCo = 20;
        int startoweMonety = 20;

        Automat.uruchomSymulacje(
         iloscProb,
         region,
         dostawyCo,
         startoweMonety
        );
        System.out.println("KONIEC SYMULACJI");
    }
}
