/**
 * Klasa reprezentująca pojedynczy produkt w automacie.
 * Przechowuje informacje o nazwie, cenie oraz fizycznie dostępnych sztukach na półce.
 * * @author Szymon Łakomy, Patryk Bogdan
 */
public class Produkt {
    private String nazwa;
    private double cena;
    private int dostepnaIlosc;
    private int maksymalnaPojemnosc;
    /**
     * * Konstruktor klasy Produkt.
     *
     * @param nazwa Nazwa produktu wyświetlana w statystykach
     * @param cena Cena produktu w PLN
     * @param maksymalnaPojemnosc Maksymalna liczba sztuk mieszcząca się na półce
     */
    public Produkt(String nazwa,double cena,int maksymalnaPojemnosc){
        this.nazwa = nazwa;
        this.cena = cena;
        this.maksymalnaPojemnosc = maksymalnaPojemnosc;
        this.dostepnaIlosc = maksymalnaPojemnosc;
    }
    /**
     *  Zmniejsza dostępną ilość produktu o 1 sztukę po udanej transakcji.
     */
    public void zmniejszIlosc(){
        if(this.dostepnaIlosc>0){
            this.dostepnaIlosc = this.dostepnaIlosc-1;
        }
    }
    /**
     * Odnawia zapasy produktu, ustawiając dostępną ilość na maksymalną pojemność.
     */
    public void uzupelnijDoPelna(){
    this.dostepnaIlosc = this.maksymalnaPojemnosc;
    }
    /**
     * Sprawdza, czy produkt fizycznie znajduje się jeszcze na półce.
     *
     * @return true jeśli produkt jest dostępny, false w przypadku braku
     */
    public boolean czyDostepny(){
        if(this.dostepnaIlosc>0){
           return true;
        }else{
            return false;
        }
    }
    public String getNazwa(){
        return this.nazwa;
    }
    public double getCena(){
        return this.cena;
    }
}
