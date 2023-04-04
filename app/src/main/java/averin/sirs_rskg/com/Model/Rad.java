package averin.sirs_rskg.com.Model;

public class Rad {
    private String no_urut, url_rs, url_pdf_rad, kode_klinik, kode_penunjang, kode_tc_hasilpenunjang, txt_kategori, kode_tarif, jmlThorax, nama_tindakan_rad;

    public Rad() {
    }
    public Rad(String no_urut, String url_rs, String url_pdf_rad, String kode_klinik, String kode_penunjang, String kode_tc_hasilpenunjang, String txt_kategori, String kode_tarif,
               String jmlThorax, String nama_tindakan_rad) {
        this.no_urut = no_urut;
        this.url_rs = url_rs;
        this.url_pdf_rad = url_pdf_rad;
        this.kode_klinik = kode_klinik;
        this.kode_penunjang = kode_penunjang;
        this.kode_tc_hasilpenunjang = kode_tc_hasilpenunjang;
        this.txt_kategori = txt_kategori;
        this.kode_tarif = kode_tarif;
        this.jmlThorax = jmlThorax;
        this.nama_tindakan_rad = nama_tindakan_rad;
    }

    public String getNo_urut() { return  no_urut; }
    public String getUrl_rs() { return url_rs; }
    public String getUrl_pdf_rad() { return url_pdf_rad; }
    public String getKode_klinik() { return kode_klinik; }
    public String getKode_penunjang() {
        return kode_penunjang;
    }
    public String getTc_hasilpenunjang() {
        return kode_tc_hasilpenunjang;
    }
    public String getTxt_kategori() {
        return txt_kategori;
    }
    public String getKode_tarif() {
        return kode_tarif;
    }
    public String getJmlThorax() {
        return jmlThorax;
    }
    public String getTindakanrad() { return nama_tindakan_rad; }

}
