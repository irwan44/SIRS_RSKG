package averin.sirs_rskg.com.Model;

public class Antrian {
    private String  idReg, tgl, no_antrian, nm_dokter, jamAwal, jamAkhir, status, nmKlinik, nmBagian;

    public Antrian(String idReg, String no_antrian, String nm_dokter ,String tgl, String jamAwal, String jamAkhir, String status, String nmKlinik, String nmBagian) {
        this.idReg = idReg;
        this.no_antrian = no_antrian;
        this.nm_dokter = nm_dokter;
        this.tgl = tgl;
        this.jamAwal = jamAwal;
        this.jamAkhir = jamAkhir;
        this.status = status;
        this.nmKlinik = nmKlinik;
        this.nmBagian = nmBagian;
    }


    
    public String getIdReg_antrian() {return idReg; }
    public String getNo_antrian() {return  no_antrian; }
    public String getnmDokter_antrian() {return nm_dokter; }
    public String getTgl_antrian() {return tgl; }
    public String getJamAwal_antrian() {return jamAwal; }
    public String getJamAkhir_antrian() {return jamAkhir; }
    public String getStatus_antrian() {return status; }
    public String getnmKlinik_antrian() {return nmKlinik; }
    public String getnmBagian_antrian() {return nmBagian; }
}
