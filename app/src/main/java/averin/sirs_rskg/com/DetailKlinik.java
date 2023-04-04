package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailKlinik extends AppCompatActivity {

    ConnectivityManager conMgr;
    String val_token, no_ktp, idk, kd_klinik, nm_klinik, alamat_klinik, site_klinik;
    ImageView img_fotoKlinik;
    TextView txt_login, txt_namaPasien, txt_noktp, txt_lbl, txt_null, nmKlinik, alamatKlinik, emailKlinik;
    CircleImageView fotoPasien;
    Button btn_registPoli;

    //Dialog Confirm
    AlertDialog.Builder dial_builder;
    AlertDialog dial_login;
    LayoutInflater inflater;
    View dialogView;

    Intent i;

    String APIurl = RequestHandler.APIdev;
    public String urlKlinikDetail = APIurl+"get-klinik-detail.php";
    public String linkJanda  = APIurl+"get-token.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_klinik);

        //TOOLBAR
        Toolbar LabToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(LabToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LabToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //CEK CONNECTION
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        //GET DATA FROM CONTROLLER
        Login login = AppController.getInstance(this).getPasien();
        Token token = AppController.getInstance(this).isiToken();
        val_token = (String.valueOf(token.gettoken()));
        no_ktp = (String.valueOf(login.getKTP_pasien()));

        nmKlinik = findViewById(R.id.nama_klinik);
        emailKlinik = findViewById(R.id.email_klinik);
        alamatKlinik = findViewById(R.id.alamat_klinik);
        img_fotoKlinik = findViewById(R.id.img_fotoKlinik);
        btn_registPoli = findViewById(R.id.btn_registPoli);
        i = new Intent(DetailKlinik.this, RegistPoli.class);

        Bundle kiriman = getIntent().getExtras();
        if (kiriman != null) {
            idk   = kiriman.get("idk").toString();
        }
        viewDetail();

        //Dialog ask login
        dial_builder = new AlertDialog.Builder(DetailKlinik.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_ask_login, viewGroup, false);
        Button btn_act_login = dialogView.findViewById(R.id.btn_login);
        Button btn_act_cancel = dialogView.findViewById(R.id.btn_cancel);
        dial_builder.setView(dialogView);
        dial_login = dial_builder.create();
        dial_login.setCancelable(false);
        btn_act_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailKlinik.this, LoginActivity.class);
                i.putExtra("email_px", "");
                i.putExtra("pass_px", "");
                i.putExtra("last_menu", "4");
                startActivity(i);
                dial_login.dismiss();

            }
        });
        btn_act_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_login.dismiss();
            }
        });

        if(!AppController.getInstance(this).isLoggedIn()) {
            btn_registPoli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dial_login.show();
                }
            });
        }else{
            btn_registPoli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(i);
                }
            });
        }
    }

    private void viewDetail() {
        //first getting the values
        final String iniToken   = val_token;
        final String id_klinik  = idk;

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id", id_klinik);

                //returing the response
                return requestHandler.requestData(urlKlinikDetail, "POST", "application/json; charset=utf-8", "X-Api-Token",
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
                    JSONObject jr = new JSONObject(s);
                    //if no error in response
                    if (jr.getString("code").equals("500")) {
                        nmKlinik.setText(" - ");
                        emailKlinik.setText(" - ");

                    } else if(jr.getString("code").equals("200")){
                        JSONArray res = jr.getJSONArray("res");
                        for(int a = 0; a < jr.length(); a++) {
                            JSONObject obj = res.getJSONObject(a);
                            kd_klinik = obj.getString("kode_klinik");
                            site_klinik = obj.getString("url_site");
                            nm_klinik = obj.getString("nama_perusahaan");
                            alamat_klinik = obj.getString("alamat");
                            String mailKlinik = obj.getString("email");
                            nmKlinik.setText(nm_klinik);
                            if (mailKlinik.equals("null")) {
                                emailKlinik.setText(" - ");
                            } else {
                                emailKlinik.setText(mailKlinik);
                            }
                            alamatKlinik.setText(alamat_klinik);
                            i.putExtra("kde_Klinik", kd_klinik);
                            i.putExtra("kde_dokter", "");
                            i.putExtra("nma_Klinik", nm_klinik);
                            i.putExtra("nma_dokter", "");
                        }
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
//        if(dialogView.getParent() != null){
//        ((ViewGroup)dialogView.getParent()).removeView(dialogView);
//        }
        dial_login.show();
    }
}