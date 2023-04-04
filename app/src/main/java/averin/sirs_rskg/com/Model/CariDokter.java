package averin.sirs_rskg.com.Model;

public class CariDokter {

    private String  idDokter, kdDokter, namaDokter;

    public CariDokter(String idDokter, String kdDokter, String namaDokter) {
        this.idDokter = idDokter;
        this.kdDokter = kdDokter;
        this.namaDokter = namaDokter;
    }

    public String getIdDokter() {return  idDokter;}
    public String getkodeDokter() {return kdDokter; }
    public String getnamaDokter() {return namaDokter; }
}
