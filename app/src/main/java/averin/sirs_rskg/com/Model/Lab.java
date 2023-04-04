package averin.sirs_rskg.com.Model;

public class Lab {
    private String no_urut, url_rs, kode_penunjang, txt_kategori, nomr_px, nama_tindakan_lab, tglhasil;

    public Lab(String no_urut, String url_rs, String kode_penunjang, String txt_kategori, String nomr_px,
               String nama_tindakan_lab, String tglhasil) {
        this.no_urut = no_urut;
        this.url_rs = url_rs;
        this.kode_penunjang = kode_penunjang;
        this.txt_kategori = txt_kategori;
        this.nomr_px = nomr_px;
        this.nama_tindakan_lab = nama_tindakan_lab;
        this.tglhasil = tglhasil;
    }

    public String getNo_urut() { return no_urut; }
    public String getUrl_rs() { return url_rs; }
    public String getKode_penunjang() {
        return kode_penunjang;
    }
    public String getTxt_kategori() {
        return txt_kategori;
    }
    public String getNomr_px() {
        return nomr_px;
    }
    public String getNama_tindakan() {
        return nama_tindakan_lab;
    }
    public String getTglhasil() { return tglhasil; }

}
