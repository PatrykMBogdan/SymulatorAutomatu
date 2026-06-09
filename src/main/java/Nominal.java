/**
 * Typ wyliczeniowy reprezentujący fizyczne monety obsługiwane przez automat.
 * Przechowuje informację o rzeczywistej wartości liczbowej każdego nominału w automacie.
 *
 * @author Szymon Łakomy, Patryk Bogdan
 */
public enum Nominal {
    PLN_5_00(5.0),
    PLN_2_00(2.0),
    PLN_1_00(1.0),
    GR_50(0.5),
    GR_20(0.2),
    GR_10(0.1);

    private final double wartosc;

    Nominal(double wartosc){
        this.wartosc = wartosc;
    }

    public double getWartosc(){
        return this.wartosc;
    }
}

