package averin.sirs_rskg.com.Model;

//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
public class Login {

    private String nama_pasien, no_mr, no_ktp, foto_pasien;

    public Login(String nama_pasien, String no_ktp, String foto_pasien) {
        this.nama_pasien = nama_pasien;
//        this.no_mr = no_mr;
        this.no_ktp = no_ktp;
        this.foto_pasien = foto_pasien;
    }

    public String getNama_pasien() {
        return nama_pasien;
    }
//    public String getMR_pasien() { return no_mr; }
    public String getKTP_pasien() { return no_ktp; }
    public String getFoto_pasien() { return foto_pasien; }
}
