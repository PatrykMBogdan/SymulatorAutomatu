/**
 * Klasa reprezentująca pojedynczą próbę zakupu w automacie.
 * Przechowuje status zakończonej operacji oraz kwotę wrzuconą przez klienta.
 *
 * @author Szymon Łakomy, Patryk Bogdan
 */
public class Transakcja {
    private StatusTransakcji status;
    private double wrzuconaKwota;
    /**
     * Konstruktor klasy Transakcja.
     *
     * @param status Końcowy wynik operacji zakupu
     * @param wrzuconaKwota Ilość pieniędzy wrzucona przez klienta do maszyny
     */
    public Transakcja(StatusTransakcji status,double wrzuconaKwota){
        this.status = status;
        this.wrzuconaKwota = wrzuconaKwota;
    }
    /**
     * Sprawdza, czy transakcja zakończyła się pełnym sukcesem i wydaniem towaru.
     *
     * @return true jeśli transakcja była udana, w przeciwnym razie false
     */
    public boolean czyUdaloSie(){
        if(this.status == StatusTransakcji.UDANA){
            return true;
        }else{
            return false;
        }
    }
    public StatusTransakcji getStatus(){
        return this.status;
    }
    public double getWrzuconaKwota(){
        return this.wrzuconaKwota;
    }
}
