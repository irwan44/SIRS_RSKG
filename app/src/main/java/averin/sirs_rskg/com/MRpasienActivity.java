package averin.sirs_rskg.com;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import averin.sirs_rskg.com.Adapter.MRpasienAdapter;
import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.MRpasien;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;

public class MRpasienActivity extends AppCompatActivity {

    String val_token, no_ktp, iniktp, nama_px, umur_px, gender_px, goldarah_px, tgl_mr, nomr_px, url_rs, tgl_cari,
            nama_klinik, idReg, tgl_daftar, jam_awal, nama_bagian, nama_dokter, wkt_periksa;
    String kode_klinik = "C00002";
    TextView txt_null;
    //    List MR
    RecyclerView MRpasien_Recyleview;
    private ArrayList<MRpasien> listMRpasien = new ArrayList<>();
    private MRpasienAdapter MRpasienadapter;
    //    URL API
    public String APIurl = RequestHandler.APIdev;
    public String urlListMR = APIurl+"get-list-mr.php";

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    EditText edt_tglmr;
    ImageButton cariMR;
    private Calendar mCalendar;

    //Null data
    CardView cd_null;
    ImageView img_null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrpasien2);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar LabToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(LabToolbar);
        LabToolbar.setLogoDescription(getResources().getString(R.string.app_name));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LabToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //GET DATA FROM CONTROLLER
        Login login = AppController.getInstance(this).getPasien();
        Token token = AppController.getInstance(this).isiToken();
        val_token = String.valueOf(token.gettoken());
        no_ktp    = String.valueOf(login.getKTP_pasien());

        Bundle kiriman = getIntent().getExtras();
        if(kiriman != null){

            kode_klinik = kiriman.get("kd_klinik").toString();
            nama_klinik = kiriman.get("nama_klinik").toString();
        }

//        Declarate Object
        cd_null = findViewById(R.id.cv_null_history);
        txt_null = findViewById(R.id.txt_null_history);
        img_null = findViewById(R.id.img_null_history);
        edt_tglmr = findViewById(R.id.edt_caritgl);
        cariMR = findViewById(R.id.btn_cari);
        cd_null.setVisibility(View.GONE);
        txt_null.setVisibility(View.GONE);
        img_null.setVisibility(View.GONE);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LabToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//      FUNGSI CARI TANGGAL
        edt_tglmr.setText("Pilih tanggal");
        edt_tglmr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                cekToken();
            }
        });
        cariMR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekToken();
            }
        });
        edt_tglmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar = Calendar.getInstance();
                new DatePickerDialog(MRpasienActivity.this, R.style.DialogTheme, datestart, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        viewMR();
        MRpasien_Recyleview = (RecyclerView) findViewById(R.id.listMR_viewCycle);
        MRpasienadapter = new MRpasienAdapter(listMRpasien);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MRpasienActivity.this);
        MRpasien_Recyleview.setLayoutManager(layoutManager);
        MRpasien_Recyleview.setAdapter(MRpasienadapter);
    }

    private final DatePickerDialog.OnDateSetListener datestart = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            edt_tglmr.setText(df.format(mCalendar.getTime()));
        }
    };

    public void onBackPressed() {
        Intent startMain = new Intent(MRpasienActivity.this,MainActivity.class);
        startActivity(startMain);
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
                        viewMR();
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
                        viewMR();

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

    public void viewMR() {
        //first getting the values
        listMRpasien.clear();
        final String iniToken   = val_token;
        final String ktpPX      = no_ktp;
        tgl_cari         = edt_tglmr.getText().toString();
        if (TextUtils.isEmpty(tgl_cari)) {
            edt_tglmr.setError("Please input date");
            edt_tglmr.requestFocus();
            return;
        }
        if(tgl_cari.equals("Pilih tanggal")){
            tgl_mr = "";
        }else{
            tgl_mr = tgl_cari;
        }

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("kode_klinik", kode_klinik);
                params.put("ktp", ktpPX);
                params.put("url_rs", "https://rskg-ainun.sirs.co.id/");
                params.put("tgl_mr", tgl_mr);

                //returing the response
                return requestHandler.requestData(urlListMR, "POST", "application/json; charset=utf-8", "X-Api-Token",
                        iniToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if(obj.getString("code").equals("500")){
                        cd_null.setVisibility(View.VISIBLE);
                        txt_null.setVisibility(View.VISIBLE);
                        img_null.setVisibility(View.VISIBLE);
                        MRpasien_Recyleview.setVisibility(View.GONE);

                    }else if(obj.getString("code").equals("200")){
                        cd_null.setVisibility(View.GONE);
                        txt_null.setVisibility(View.GONE);
                        img_null.setVisibility(View.GONE);
                        MRpasien_Recyleview.setVisibility(View.VISIBLE);
                        JSONArray px = obj.getJSONArray("px");
                        JSONArray mr = obj.getJSONArray("res");

//                        Data Pasien
                        for(int p = 0; p < px.length(); p++) {
                            JSONObject jne = px.getJSONObject(p);
                            iniktp      = jne.getString("ktp_px");
                            nomr_px     = jne.getString("nomr_px");
                            nama_px     = jne.getString("nama_px");
                            gender_px   = jne.getString("gender");
                            goldarah_px = jne.getString("gol_darah");
                            umur_px     = jne.getString("umur");

                        }
                        for (int a = 0; a < mr.length(); a++) {
//                            txt_infonull.setVisibility(View.GONE);
                            JSONObject jso = mr.getJSONObject(a);
//                            kode_klinik = jso.getString("kode_klinik");
//                            nama_klinik = jso.getString("nama_klinik");
                            idReg       = jso.getString("idReg");
                            tgl_daftar  = jso.getString("tgl_periksa");
                            jam_awal    = jso.getString("wkt_periksa");
                            nama_bagian = jso.getString("nama_bagian");
                            wkt_periksa = jso.getString("wkt_periksa");
                            nama_dokter = jso.getString("nama_dokter");
                            url_rs      = jso.getString("url_rs");
                            listMRpasien.add(new MRpasien(idReg,kode_klinik,nama_klinik,nama_dokter,nama_bagian,tgl_daftar,
                                    jam_awal, url_rs));
                        }
                        MRpasienadapter.notifyDataSetChanged();
//                        tv_namapasien.setText(nama_px);
////                        tv_noktp.setText(nama_px);
//                        tv_umur.setText(umur_px);
//                        tv_goldarah.setText(goldarah_px);
//                        if(gender_px.equals("l")){
//                            tv_gender.setText("Laki-laki");
//                        }else if (gender_px.equals("P")){
//                            tv_gender.setText("Perempuan");
//                        }
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
