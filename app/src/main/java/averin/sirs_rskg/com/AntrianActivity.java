package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import averin.sirs_rskg.com.Adapter.AntrianAdapter;
import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.Antrian;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;
import de.hdodenhof.circleimageview.CircleImageView;

public class AntrianActivity extends AppCompatActivity {

    String val_token, no_ktp, dateNow;
    String APIurl = RequestHandler.APIdev;
    ProgressBar prgbar;
    EditText edt_pilihtanggal;
    TextView txt_login, txt_namaPasien, txt_noktp, txt_lbl, txt_nullantrian;
    ImageView img_nullantrian;
    CardView cv_nullantrian;
    ImageButton cariAntrian;
    CircleImageView fotoPasien;
    RecyclerView listAntrian;
    RelativeLayout rl_null;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<Antrian> ArrayAntrian = new ArrayList<>();
    private AntrianAdapter Aa;
    private Calendar mCalendar;
    public String urlAntrian = APIurl+"get-jadwal-px.php";
    public String linkJanda  = APIurl+"get-token.php";

    LinearLayout personalinfo, experience, review;
    TextView personalinfobtn, experiencebtn, reviewbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antrian);

        personalinfo = findViewById(R.id.personalinfo);
        experience = findViewById(R.id.experience);
        personalinfobtn = findViewById(R.id.personalinfobtn);
        experiencebtn = findViewById(R.id.experiencebtn);
        /*making personal info visible*/
        personalinfo.setVisibility(View.VISIBLE);
        experience.setVisibility(View.GONE);


        personalinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.VISIBLE);
                experience.setVisibility(View.GONE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.birufigma));
                experiencebtn.setTextColor(getResources().getColor(R.color.gery));

            }
        });

        experiencebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.GONE);
                experience.setVisibility(View.VISIBLE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.gery));
                experiencebtn.setTextColor(getResources().getColor(R.color.birufigma));

            }
        });

//        reviewbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                personalinfo.setVisibility(View.GONE);
//                experience.setVisibility(View.GONE);
//                review.setVisibility(View.VISIBLE);
//                personalinfobtn.setTextColor(getResources().getColor(R.color.gery));
//                experiencebtn.setTextColor(getResources().getColor(R.color.gery));
//                reviewbtn.setTextColor(getResources().getColor(R.color.birufigma));
//
//            }
//        });

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

        listAntrian = (RecyclerView) findViewById(R.id.listAntrian);
        edt_pilihtanggal = findViewById(R.id.tglAntrian);
        cariAntrian = findViewById(R.id.btn_cari);
        txt_nullantrian = findViewById(R.id.txt_null_antrian);
        img_nullantrian = findViewById(R.id.img_null_antrian);
        cv_nullantrian = findViewById(R.id.cv_null_antrian);
        rl_null = findViewById(R.id.rl_null_antrian);
        prgbar = findViewById(R.id.progressBar);

        edt_pilihtanggal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                cekToken();
            }
        });

        cariAntrian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekToken();
            }
        });

        //GET DATA FROM CONTROLLER
        Login login = AppController.getInstance(this).getPasien();
        Token token = AppController.getInstance(this).isiToken();
        no_ktp    = (String.valueOf(login.getKTP_pasien()));
        val_token    = (String.valueOf(token.gettoken()));

        cekToken();
        listAntrian.setLayoutManager(new LinearLayoutManager(this));
        Aa = new AntrianAdapter(this, ArrayAntrian);
        listAntrian.setAdapter(Aa);

        dateNow = df.format(new Date());
        edt_pilihtanggal.setText(dateNow);

        edt_pilihtanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar = Calendar.getInstance();
                new DatePickerDialog(AntrianActivity.this, R.style.DialogTheme, datestart, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void onBackPressed() {
        Intent startMain = new Intent(AntrianActivity.this,MainActivity.class);
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
                        viewAntrian();
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
                return requestHandler.reqToken(linkJanda, params);
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
                        viewAntrian();

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

    private void viewAntrian() {
        //first getting the values
        ArrayAntrian.clear();
        final String iniToken   = val_token;
        final String ktp     = no_ktp;
        final String tgl_cari   = edt_pilihtanggal.getText().toString();

        if (TextUtils.isEmpty(tgl_cari)) {
            edt_pilihtanggal.setError("Please input date");
            edt_pilihtanggal.requestFocus();
            return;
        }

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("kode_klinik", "C00002");
                params.put("no_ktp", ktp);
                params.put("tgl", tgl_cari);
//                params.put("Pass", pass);

                //returing the response
                return requestHandler.requestData(urlAntrian, "POST", "application/json; charset=utf-8", "X-Api-Token",
                        iniToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                prgbar = findViewById(R.id.progressBar);
                prgbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                prgbar.setVisibility(View.GONE);

                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response

                    if(obj.getString("code").equals("200")){

                        cv_nullantrian.setVisibility(View.GONE);
                        txt_nullantrian.setVisibility(View.GONE);
                        img_nullantrian.setVisibility(View.GONE);
                        JSONArray jr = obj.getJSONArray("list");
                        for (int a = 0; a < jr.length(); a++) {
                            txt_nullantrian.setVisibility(View.GONE);
                            img_nullantrian.setVisibility(View.GONE);
                            cv_nullantrian.setVisibility(View.GONE);
                            listAntrian.setVisibility(View.VISIBLE);
                            JSONObject jso = jr.getJSONObject(a);
                            String idReg = jso.getString("idReg");
                            String no_antrian = jso.getString("no_antrian");
                            String nmDokter = jso.getString("nama_dokter");
                            String tgl = jso.getString("tgl");
                            String jamAwal = jso.getString("jam_awal");
                            String jamAkhir = jso.getString("jam_akhir");
                            String status = jso.getString("status");
                            String nmKlinik = jso.getString("nama_klinik");
                            String nmBagian = jso.getString("nama_bagian");
//                            ArrayKlinik.add(new Klinik(idk,np,alamat,logo));
                            ArrayAntrian.add(new Antrian(idReg,no_antrian,nmDokter,tgl,jamAwal,jamAkhir,status,nmKlinik,nmBagian));
                        }
                        Aa.notifyDataSetChanged();

                    }else if(obj.getString("code").equals("500")){
                        cv_nullantrian.setVisibility(View.VISIBLE);
                        img_nullantrian.setVisibility(View.VISIBLE);
                        txt_nullantrian.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), obj.getString("msg"),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        masukPakEko pl = new masukPakEko();
        pl.execute();
    }

    private final DatePickerDialog.OnDateSetListener datestart = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            edt_pilihtanggal.setText(df.format(mCalendar.getTime()));
        }
    };
}