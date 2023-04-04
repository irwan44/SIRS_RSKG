package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import averin.sirs_rskg.com.Adapter.KlinikAdapter;
import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.Klinik;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistKlinik extends AppCompatActivity {

    ConnectivityManager conMgr;
    RecyclerView listKlinik;
    String no_ktp, val_token, np, alamat, idk, kota,telpon1, email, ket, urllogo, code_menu="2";
    byte[] logo;
    TextView txt_login, txt_namaPasien, txt_subjudul;
    CircleImageView fotoPasien;
    AlertDialog.Builder dialog;
    View dialogView;
    LayoutInflater inflater;
    String APIurl = RequestHandler.APIdev;

    private ArrayList<Klinik> ArrayKlinik = new ArrayList<>();
    private KlinikAdapter Klinikadapter;
    public String postUrl = APIurl+"/api/v1/get-data-klinik.php";
//    public String postUrl= "https://tel.d-medis.id/api/v1/get-data-klinik.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_klinik);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar LabToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(LabToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LabToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        //Dialog Alert
        dialog = new AlertDialog.Builder(RegistKlinik.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_ask_login, null);
        dialog.setView(dialogView);

        Login login = AppController.getInstance(this).getPasien();
        Token token = AppController.getInstance(this).isiToken();
        no_ktp    = (String.valueOf(login.getKTP_pasien()));
//        val_token = (String.valueOf(token.gettoken()));

        txt_login       = findViewById(R.id.txt_login);
//        txt_namaPasien  = findViewById(R.id.namaPasien);
        //txt_subjudul    = findViewById(R.id.subjudul);
        //fotoPasien      = findViewById(R.id.tb_poto);

        if (!AppController.getInstance(this).isLoggedIn()) {
            txt_login.setVisibility(View.VISIBLE);
//            txt_namaPasien.setVisibility(View.GONE);
//            fotoPasien.setVisibility(View.GONE);
//            txt_subjudul.setVisibility(View.GONE);
        }else{
            txt_login.setVisibility(View.GONE);
//            txt_namaPasien.setVisibility(View.VISIBLE);
//            fotoPasien.setVisibility(View.VISIBLE);
//            txt_subjudul.setVisibility(View.VISIBLE);
//            SET DATA T0 TOOLBAR
//            txt_namaPasien.setText((String.valueOf(login.getNama_pasien())));
        }

        cekToken();
        listKlinik = findViewById(R.id.listKlinik);
        listKlinik.setLayoutManager(new LinearLayoutManager(this));
        Klinikadapter = new KlinikAdapter(this, ArrayKlinik, this);
        listKlinik.setAdapter(Klinikadapter);

    }

    public void cekToken() {
        //first getting the values
        final String isiToken    = val_token;
        final String ktp         = no_ktp;
        final String APIurl      = RequestHandler.APIdev;

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
                return requestHandler.requestData(APIurl+"/api/v1/get-data-px.php", "POST", "application/json; charset=utf-8", "X-Api-Token",
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
                        viewKlinik();
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
                return requestHandler.reqToken(APIurl+"/api/v1/get-token.php", params);
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
                        viewKlinik();

                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ambilToken pl = new ambilToken();
        pl.execute();
    }

    private void viewKlinik() {
        //first getting the values
        final String iniToken   = val_token;

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("idKota", "");
//                params.put("User", user);
//                params.put("Pass", pass);

                //returing the response
                return requestHandler.requestData(postUrl, "POST", "application/json; charset=utf-8", "X-Api-Token",
                        iniToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressBar = findViewById(R.id.progressBar);
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
                        Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
//
//                        tes.setText("ini buat tes kata");
                        JSONArray jr = obj.getJSONArray("res");
                        ArrayKlinik.clear();
                        for (int a = 0; a < jr.length(); a++) {

//                            ArrayKlinik = new ArrayList<>();
                            JSONObject jso = jr.getJSONObject(a);
//                            String ("", jso.getString("idhp"));
//                            String idp = jso.getString("id_perusahaan");
                            np = jso.getString("nama_perusahaan");
                            alamat = jso.getString("alamat");
                            idk = jso.getString("id");
                            urllogo = jso.getString("logo_yankes");
//                            logo = urllogo.getBytes(StandardCharsets.UTF_8);
                            ArrayKlinik.add(new Klinik(idk,np,alamat,urllogo));
                        }
                        Klinikadapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        masukPakEko pl = new masukPakEko();
        pl.execute();
    }

    public void openvcs(View v) {
        dialog.setCancelable(true);
        dialog.show();
    }

    public void actLogin(View v) {
        Intent i = new Intent(RegistKlinik.this, LoginActivity.class);
        i.putExtra("last_menu", code_menu);
        startActivity(i);
    }
}