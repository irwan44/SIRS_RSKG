package averin.sirs_rskg.com.Model;

public class Berita {
    private String  judul, tgl_berita;

    public Berita(String judul, String tgl_berita) {
        this.judul = judul;
//        this.sub_judul = sub_judul;
        this.tgl_berita = tgl_berita;
//        this.img_berita = img_berita;
    }

    public String getJudul_berita() {return judul;}
//    public String getSubjudul_berita() {return sub_judul; }
    public String getTgl_berita() {return tgl_berita; }
//    public String getImg_berita() {return img_berita; }

    public void setJudul_berita(String judul) {
        this.judul = judul;
    }
    public void setTgl_berita(String tgl_berita) {
        this.tgl_berita = tgl_berita;
    }
//    public void setImg_berita(String img_berita) {
//        this.img_berita = img_berita;
//    }
}
