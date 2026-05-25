public class Transakcja {
    private StatusTransakcji status;
    private double wrzuconaKwota;

    public Transakcja(StatusTransakcji status,double wrzuconaKwota){
        this.status = status;
        this.wrzuconaKwota = wrzuconaKwota;
    }
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
