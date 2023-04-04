package averin.sirs_rskg.com.Model;

public class DokterPoli {
    private String  kd_klinik, nm_klinik, nm_dokter, kd_dokter, idnya_dokter, kd_bag, bag,
            jam_mulai, jam_akhir, wkt_periksa, range_hari, sFoto;

    public DokterPoli(String kd_klinik, String nm_klinik, String nm_dokter, String kd_dokter, String idnya_dokter,
                      String kd_bag, String bag, String jam_mulai, String jam_akhir, String wkt_periksa, String range_hari, String sFoto) {
        this.kd_klinik      = kd_klinik;
        this.nm_klinik      = nm_klinik;
        this.nm_dokter      = nm_dokter;
        this.kd_dokter      = kd_dokter;
        this.idnya_dokter   = idnya_dokter;
        this.kd_bag         = kd_bag;
        this.bag            = bag;
        this.jam_mulai      = jam_mulai;
        this.jam_akhir      = jam_akhir;
        this.wkt_periksa    = wkt_periksa;
        this.range_hari     = range_hari;
        this.sFoto          = sFoto;
    }

    public String getKd_klinik() {return kd_klinik;}
    public String getNm_klinik() {return nm_klinik;}
    public String getNama_dokter() {return nm_dokter;}
    public String getKode_dokter() {return kd_dokter;}
    public String getIdnya_dokter() {return idnya_dokter;}
    public String getKode_bag() {return kd_bag;}
    public String getBagian() {return bag;}
    public String getJam_mulai() {return jam_mulai;}
    public String getJam_akhir() {return jam_akhir;}
    public String getWaktu_periksa() {return wkt_periksa;}
    public String getRange_hari() {return range_hari;}
    public String getFoto_dokter() {return sFoto;}
    public void setNama_dokter(String nm_dokter) {
        this.nm_dokter = nm_dokter; }
}
