package averin.sirs_rskg.com.Model;

public class FotoRad {

    private String no_urut,url_imgrad, namatindakan;

    public FotoRad(String no_urut, String url_imgrad, String namatindakan) {
        this.no_urut        = no_urut;
        this.url_imgrad     = url_imgrad;
        this.namatindakan   = namatindakan;

    }
    public String getNo_urut() {return no_urut; }
    public String getUrl_imgrad() {return url_imgrad; }
    public String getNamatindakan() {return namatindakan; }

}
