package averin.sirs_rskg.com.Model;

public class ResepObat {
    private String nourut, namaobat, noteobat, namadosis, jumlahobat, ketobat, jns_obat;

    public ResepObat(String nourut, String namaobat, String noteobat, String namadosis, String jumlahobat, String ketobat, String jns_obat) {
        this.nourut=nourut;
        this.namaobat=namaobat;
        this.noteobat=noteobat;
        this.namadosis=namadosis;
        this.jumlahobat=jumlahobat;
        this.ketobat=ketobat;
        this.jns_obat=jns_obat;
    }

    public String getNourut() { return nourut;}
    public String getNamaobat() { return  namaobat;}
    public String getNoteobat() { return noteobat;}
    public String getNamadosis() { return namadosis;}
    public String getJumlahobat() {
        return jumlahobat;
    }
    public String getKetobat() {
        return ketobat;
    }
    public String getJnsobat() {
        return jns_obat;
    }


}