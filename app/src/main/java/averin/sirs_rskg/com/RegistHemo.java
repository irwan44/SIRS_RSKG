package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Adapter.SpinnerAdapter;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Model.isiSpinner;
import averin.sirs_rskg.com.Ui.AppController;

public class RegistHemo extends AppCompatActivity {

    TextView txt_info_success, txt_info_failed;
    TextInputLayout wrapjnsPx1, wrapjnsPx2, wrapSpinner;
    EditText edt_tglPeriksa, edt_namaPasien, edt_jnsPx1, edt_jnsPx2;
    RadioGroup rg_bpjs;
    RadioButton rb_rujuk, rb_kontrol;
    String val_token, ktpPasien, flagPesan, urlFoto, kd_dokter, id_dokter, nm_dokter,
            kd_bag, bagian, nm_klinik, kd_klinik, durasi, jdwl_periksa, tgl_now,tglKonvert, jenispx;
    ConnectivityManager conMgr;
    Button btn_kirim, btn_ok_failed, btn_ok_success;
    Intent Antrian;
    ImageButton imgbtn_home;
    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat inputFormat = new SimpleDateFormat("dd-LLL-yyyy");
    CardView cr_JnsPx;
    String[] jnsPasien = new String[]{"Umum", "BPJS", "Asuransi/Perusahaan", "Karyawan"};
    String[] waktukunj = new String[]{"Pagi", "Siang"};
    AutoCompleteTextView spn_jnsPasien,spn_waktu_kunj;
    ProgressDialog pDialog;
    Spinner spn_dokter, spn_asuransi;
    SpinnerAdapter adapter;
    List<isiSpinner> listisian = new ArrayList<isiSpinner>();

    private Calendar mCalendar = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    String APIurl = RequestHandler.APIdev;
    String px_baru = "baru";
    String px_lama = "lama";
    public String urlDaftar = APIurl+"post-daftar-hemo.php";
    public String urlcekPasien = APIurl+"get-data-px.php";
    public String urlDokterPenanggung = APIurl+"get-dokter-hemo.php";
    public String linkJanda  = APIurl+"get-token.php";

    //Dialog Confirm
    AlertDialog.Builder builder_success, builder_failed, dial_builder, builder_ask_px;
    AlertDialog dial_success, dial_failed, dial_login, dial_newPX;
    LayoutInflater inflater;
    View v_success_regist, v_failed_regist, v_ask_newPX, dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_hemo);

        edt_namaPasien = findViewById(R.id.txt_namaPasien);
        edt_tglPeriksa = findViewById(R.id.tglPeriksa);
        edt_jnsPx1     = findViewById(R.id.txt_jnspx1);
        edt_jnsPx2     = findViewById(R.id.txt_jnspx2);
        wrapjnsPx1     = findViewById(R.id.wrapjnspx1);
        wrapjnsPx2     = findViewById(R.id.wrapjnspx2);
        wrapSpinner    = findViewById(R.id.wrapSpinner);
        rg_bpjs        = findViewById(R.id.rg_bpjs);
        rb_rujuk       = findViewById(R.id.rbrujuk);
        rb_kontrol     = findViewById(R.id.rbkontrol);
        spn_jnsPasien  = findViewById(R.id.spn_jnsPasien);
        spn_waktu_kunj = findViewById(R.id.spn_waktu);
        cr_JnsPx       = findViewById(R.id.cr_jnsPx);
        imgbtn_home    = findViewById(R.id.imgbtn_home);
        btn_kirim      = findViewById(R.id.btn_kirim);
        spn_dokter     = findViewById(R.id.spn_dokter);
        spn_asuransi   = findViewById(R.id.spn_asuransi);
        edt_namaPasien.setEnabled(false);
        getDokterPenunjang();

        //toolbar NoActionBar
        Toolbar LabToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(LabToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LabToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
        //getting the current user
        Token token = AppController.getInstance(this).isiToken();
        val_token = (String.valueOf(token.gettoken()));
        Login login = AppController.getInstance(this).getPasien();
        edt_namaPasien.setText(String.valueOf(login.getNama_pasien()));
        ktpPasien = String.valueOf(login.getKTP_pasien());
        Bundle kiriman = getIntent().getExtras();
        if (kiriman != null) {
            kd_klinik   = kiriman.get("kode_rs").toString();
            getDokterPenunjang();
        }

        //Dialog Confirm
        ViewGroup viewGroup = findViewById(android.R.id.content);
        inflater = getLayoutInflater();
        builder_success = new AlertDialog.Builder(RegistHemo.this,R.style.CustomAlertDialog);
        builder_failed = new AlertDialog.Builder(RegistHemo.this,R.style.CustomAlertDialog);
        v_success_regist = inflater.inflate(R.layout.dialog_success_regist, viewGroup, false);
        v_failed_regist = inflater.inflate(R.layout.dialog_failed_regist, viewGroup, false);
        btn_ok_success = v_success_regist.findViewById(R.id.btn_oke);
        btn_ok_failed = v_failed_regist.findViewById(R.id.btn_oke_failed);
        txt_info_success = v_success_regist.findViewById(R.id.txt_info_success);
        txt_info_failed = v_failed_regist.findViewById(R.id.txt_info_failed);
        builder_success.setView(v_success_regist);
        builder_failed.setView(v_failed_regist);
        dial_success = builder_success.create();
        dial_success.setCancelable(false);
        dial_failed = builder_failed.create();
        dial_failed.setCancelable(false);

        imgbtn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistHemo.this, MainActivity.class);
                startActivity(i);
            }
        });

        btn_ok_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Antrian);
                dial_success.dismiss();

            }
        });
        btn_ok_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_failed.dismiss();
            }
        });

        spn_waktu_kunj.setHint("-- Pilih Waktu Kunjungan --");
        spn_jnsPasien.setHint("-- Pilih Pasien --");
        ArrayAdapter<String> AdaptJnsPasien = new ArrayAdapter<>(this, R.layout.dialog_spinner, jnsPasien);
        ArrayAdapter<String> AdaptSpnWaktu = new ArrayAdapter<>(this, R.layout.dialog_spinner, waktukunj);
        spn_jnsPasien.setAdapter(AdaptJnsPasien);
        spn_waktu_kunj.setAdapter(AdaptSpnWaktu);
        jenispx = spn_jnsPasien.getText().toString();

        if(jenispx.equals("BPJS")){
            wrapSpinner.setVisibility(View.GONE);
            spn_asuransi.setVisibility(View.GONE);
            wrapjnsPx1.setVisibility(View.VISIBLE);
            wrapjnsPx2.setVisibility(View.VISIBLE);
            edt_jnsPx1.setVisibility(View.VISIBLE);
            edt_jnsPx2.setVisibility(View.VISIBLE);
            rg_bpjs.setVisibility(View.VISIBLE);
            edt_jnsPx1.setHint("No Kartu BPJS");
            rg_bpjs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(checkedId){
                        case R.id.rbrujuk:
                            edt_jnsPx2.setHint("No Rujukan");
                            break;
                        case R.id.rbkontrol:
                            edt_jnsPx2.setHint("No Surat Kontrol");
                            // do operations specific to this selection
                            break;
                    }
                }
            });
        }else if(jenispx.equals("Asuransi/Perusahaan")){
            wrapSpinner.setVisibility(View.VISIBLE);
            spn_asuransi.setVisibility(View.VISIBLE);
            wrapjnsPx1.setVisibility(View.GONE);
            wrapjnsPx2.setVisibility(View.GONE);
            edt_jnsPx1.setVisibility(View.GONE);
            edt_jnsPx2.setVisibility(View.GONE);
            rg_bpjs.setVisibility(View.GONE);
        }else if(jenispx.equals("Umum")) {
            cr_JnsPx.setVisibility(View.GONE);
        }else if(jenispx.equals("Karyawan")){
            cr_JnsPx.setVisibility(View.GONE);
        }

        edt_tglPeriksa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(RegistHemo.this,R.style.DialogTheme, datestart,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                dialog.show();
            }
        });

        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nontonSiskae();
            }
        });

        adapter = new SpinnerAdapter(RegistHemo.this, listisian);
        spn_dokter.setAdapter(adapter);
        spn_dokter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kd_dokter = listisian.get(position).getId();
                nm_dokter = listisian.get(position).getKet();
                listisian.isEmpty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private final DatePickerDialog.OnDateSetListener datestart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

//            edt_tglPeriksa.setText(df.format(mCalendar.getTime()));
            String tgl_get = df.format(mCalendar.getTime());
            Date tgl = null;
            try {
                tgl = inputFormat.parse(tgl_get);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            tglKonvert = outputFormat.format(tgl);
            edt_tglPeriksa.setText(tglKonvert);

        }
    };

    private void getDokterPenunjang() {
        //first getting the values
        final String iniToken     = val_token;
        listisian.clear();

//        pDialog = new ProgressDialog(RegistHemo.this);
//        pDialog.setCancelable(false);
//        pDialog.setMessage("Loading...");
//        showDialog();

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            private HashMap params;
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                params = new HashMap<String, HashMap<String, String>>();
                HashMap<String, String> val = new HashMap<String, String>();

                //returing the response
                return requestHandler.requestData(urlDokterPenanggung, "POST", "application/json; charset=utf-8", "X-Api-Token",
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
                    if (obj.getString("code").equals("200")) {
                        JSONArray jso = obj.getJSONArray("list");
                        for (int a = 0; a < jso.length(); a++) {

                            isiSpinner item = new isiSpinner();
                            JSONObject jr = jso.getJSONObject(a);

                            item.setId(jr.getString("kode_dokter"));
                            item.setKet(jr.getString("nama_dokter"));
                            listisian.add(item);
                        }
                        adapter.notifyDataSetChanged();
//                        hideDialog();
                    } else if(obj.getString("code").equals("500")) {
                        isiSpinner item = new isiSpinner();
                        item.setId("");
                        item.setKet(obj.getString("msg"));
                        listisian.add(item);
                        adapter.notifyDataSetChanged();
//                        hideDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        masukPakEko pl = new masukPakEko();
        pl.execute();
    }

    public void nontonSiskae() {
        final String isiToken       = val_token;
        final String nmPasien       = edt_namaPasien.getText().toString();
        final String noKTP          = ktpPasien;
        final String tanggal        = edt_tglPeriksa.getText().toString();
        final String kodeDokter     = kd_dokter;
        final String nmaDokter      = nm_dokter;
        String waktu_pesan    = spn_waktu_kunj.getText().toString();
        if(waktu_pesan.equals("Pagi")){
            flagPesan = "1";
        }else if(waktu_pesan.equals("Siang")){
            flagPesan = "2";
        }


        pDialog = new ProgressDialog(RegistHemo.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        showDialog();

        class misi_Paket extends AsyncTask<Void, Void, String> {

            private HashMap params;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("nama_pasien", nmPasien);
                params.put("flag_pesan", flagPesan);
                params.put("no_ktp", noKTP);
                params.put("kode_rs", "C00003");
                params.put("tgl_daftar", tanggal);
                params.put("kode_dokter", kodeDokter);
                params.put("nama_dokter", nmaDokter);
                //returing the response
                return requestHandler.requestData(urlDaftar, "POST", "application/json; charset=utf-8", "X-Api-Token",
                        isiToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressBar = findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
            }

            @SuppressLint("ResourceAsColor")
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                progressBar.setVisibility(View.GONE);

                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (obj.getString("code").equals("200")) {
                        dial_success.show();
                        txt_info_success.setText(obj.getString("msg"));
                        hideDialog();
                    } else {
                        dial_failed.show();
                        dial_newPX.dismiss();
                        txt_info_failed.setText(obj.getString("msg"));
                        hideDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        misi_Paket cod = new misi_Paket();
        cod.execute();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}