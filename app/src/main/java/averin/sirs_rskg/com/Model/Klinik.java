package averin.sirs_rskg.com.Model;

public class Klinik {
    private String  idk, nama_perusahaan, alamat, logo;
//    private byte[] logo;
    public Klinik(String idk, String nama_perusahaan, String alamat, String logo) {
        this.idk                = idk;
        this.nama_perusahaan    = nama_perusahaan;
        this.alamat             = alamat;
        this.logo               = logo;
//        this.kode_klinik = kode_klinik;
//        this.alamat = alamat;
//        this.kota = kota;
//        this.telpon = telpon;
//        this.email = email;
//        this.ket = ket;

    }

    public String getId_klinik() {return  idk; }
    public String getNama_klinik() {return nama_perusahaan; }
    public String getAlamat_klinik() {return alamat; }
    public String getLogo_klinik() {return logo; }
//    public String getKode_klinik() {return  kode_klinik; }
//    public String getKota_klinik() {return kota; }
//    public String getTelpon_klinik() {return telpon; }
//    public String getEmail_klinik() {return email; }
//    public String getKet_klinik() {return ket;
}
