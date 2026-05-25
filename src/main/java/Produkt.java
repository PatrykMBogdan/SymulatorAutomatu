public class Produkt {
    private String nazwa;
    private double cena;
    private int dostepnaIlosc;
    private int maksymalnaPojemnosc;

    public Produkt(String nazwa,double cena,int maksymalnaPojemnosc){
        this.nazwa = nazwa;
        this.cena = cena;
        this.maksymalnaPojemnosc = maksymalnaPojemnosc;
        this.dostepnaIlosc = maksymalnaPojemnosc;
    }
    public void zmniejszIlosc(){
        if(this.dostepnaIlosc>0){
            this.dostepnaIlosc = this.dostepnaIlosc-1;
        }
    }
    public void uzupelnijDoPelna(){
    this.dostepnaIlosc = this.maksymalnaPojemnosc;
    }
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
