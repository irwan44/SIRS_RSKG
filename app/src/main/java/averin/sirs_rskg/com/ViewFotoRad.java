
package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import averin.sirs_rskg.com.Adapter.FotoRadAdapter;
import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.FotoRad;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;

public class ViewFotoRad extends AppCompatActivity {

    String val_token, no_ktp, kode_klinik, kode_penunjang, kode_tarif, url_rs, namatindakan, nama_klinik,
        nama_bagian, nama_dokter, tgl_daftar;
    TextView txt_null_fotorad;
    String APIurl = RequestHandler.APIdev;
    public String linkToken = APIurl+"get-token.php";
    public String linkFotoRad = APIurl+"get-foto-rad.php";

    //RecyleView function
    RecyclerView rc_fotorad;
    private ArrayList<FotoRad> arrayfoto = new ArrayList<>();
    FotoRadAdapter fra;
    HashMap params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_foto_rad);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar tlbr = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlbr);
        tlbr.setLogoDescription(getResources().getString(R.string.app_name));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tlbr.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Token tkn = AppController.getInstance(this).isiToken();
        val_token = String.valueOf(tkn.gettoken());
        Login lgn = AppController.getInstance(this).getPasien();
        no_ktp    = String.valueOf(lgn.getKTP_pasien());
        rc_fotorad = findViewById(R.id.rc_foto_rad);
        txt_null_fotorad = findViewById(R.id.txt_nullfoto);
        txt_null_fotorad.setVisibility(View.GONE);
        Bundle kiriman = getIntent().getExtras();
        if(kiriman != null){
            url_rs          = kiriman.getString("url_rs");
            kode_klinik     = kiriman.getString("kode_klinik");
            kode_penunjang  = kiriman.getString("kode_penunjang");
            kode_tarif      = kiriman.getString("kode_tarif");
            namatindakan    = kiriman.getString("nama_tindakan");

//            nama_klinik    = kiriman.getString("nama_klinik");
//            namatindakan    = kiriman.getString("nama_bagian");
//            namatindakan    = kiriman.getString("nama_dokter");
//            namatindakan    = kiriman.getString("nama_tindakan");
//            namatindakan    = kiriman.getString("nama_tindakan");
            cekToken();
        }

        rc_fotorad.setLayoutManager(new LinearLayoutManager(this));
        fra = new FotoRadAdapter(this, arrayfoto, this);
        rc_fotorad.setAdapter(fra);

    }

//    public void onBackPressed() {
//        Intent i = new Intent(ViewFotoRad.this,DetailMR.class);
//        i.putExtra("kd_klinik", kode_klinik);
//        i.putExtra("idRegKlinik", kode_klinik);
//        i.putExtra("url_rs", url_rs);
//        i.putExtra("nama_klinik", nama_klinik);
//        i.putExtra("nama_bagian", nama_bagian);
//        i.putExtra("nama_dokter", nama_dokter);
//        i.putExtra("tgl_daftar", tgl_daftar);
//        startActivity(i);
//    }

    public void cekToken() {
        //first getting the values
        final String isiToken    = val_token;
        final String ktp         = no_ktp;

        //if everything is fine
        class cekToken extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("no_ktp", ktp);

                //returing the response
                return requestHandler.requestData(APIurl+"cek-data-px.php", "POST", "application/json; charset=utf-8", "X-Api-Token",
                        isiToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                progressBar.setVisibility(View.GONE);

                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (obj.getString("code").equals("500")) {
                        ambilToken();
                    } else {
                        viewFotoRad();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        cekToken pl = new cekToken();
        pl.execute();
    }

    public void ambilToken() {
        //first getting the values
        final String KodeApi    = "MUSA";
        final String KeyApi     = "@@TTWYYW";
        final String KeyCode    = "602f07f23fc390cfd4461b268f95eddfbd4fc87d9b313b64a943801b5e4c5b12";
        final String APIurl     = RequestHandler.APIdev;

//        final String KodeApi = "privy";
//        final String KeyApi  = "@nqgsMKf";
//        final String KeyCode = "befa14f43777a6fbb55bc6dc939784202da3a4c0f43c172ac636978aec117bad";

        //if everything is fine
        class ambilToken extends AsyncTask<Void, Void, String> {
            ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("KodeApi", KodeApi);
                params.put("KeyApi", KeyApi);
                params.put("KeyCode", KeyCode);

                //returing the response
                return requestHandler.reqToken(linkToken, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                progressBar.setVisibility(View.GONE);

                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (obj.getString("code").equals("200")) {
                        //getting the user from the response
//                        JSONObject loginJson = obj.getJSONObject("response");

                        //creating a new user object
                        Token token = new Token(
//                                loginJson.getString("nama_grup")
                                obj.getString("token")
//                                obj.getString("pengguna")
                        );

                        //storing the user in shared preferences
                        AppController.getInstance(getApplicationContext()).getToken(token);
                        viewFotoRad();

                    } else {
//                        Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ambilToken pl = new ambilToken();
        pl.execute();
    }

    private void viewFotoRad() {
        //first getting the values
        arrayfoto.clear();
        final String iniToken     = val_token;
        final String kd_klinik    = kode_klinik;
        final String kd_penunjang = kode_penunjang;
        final String kd_tarif     = kode_tarif;

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                ///creating request parameters
                params = new HashMap<String, HashMap<String, String>>();
                HashMap<String, String> val = new HashMap<String, String>();
                val.put("kd_penunjang", kd_penunjang);
                val.put("kode_tarif", kd_tarif);
                params.put("kode_klinik", kd_klinik);
                params.put("param", val);

                //returing the response
                return requestHandler.requestData(linkFotoRad, "POST", "application/json; charset=utf-8", "X-Api-Token",
                        iniToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                prgbar = findViewById(R.id.progressBar);
//                prgbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                prgbar.setVisibility(View.GONE);

                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response

                    if(obj.getString("code").equals("200")){

                        txt_null_fotorad.setVisibility(View.GONE);
                        rc_fotorad.setVisibility(View.VISIBLE);
                        JSONArray jr = obj.getJSONArray("fotorad");
                        for (int a = 0; a < jr.length(); a++) {
                            JSONObject jso = jr.getJSONObject(a);
                            String nourut = jso.getString("no_urut");
                            String url_foto = jso.getString("url_foto_rad");
                            String lokfoto = "_arsip/foto_rad/";
                            String isi_foto = url_rs+lokfoto+url_foto;
                            arrayfoto.add(new FotoRad(nourut,isi_foto,namatindakan));
                        }
                        fra.notifyDataSetChanged();

                    }else if(obj.getString("code").equals("500")){
                        txt_null_fotorad.setVisibility(View.VISIBLE);
                        rc_fotorad.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        masukPakEko pl = new masukPakEko();
        pl.execute();
    }


}