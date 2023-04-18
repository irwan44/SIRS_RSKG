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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
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

    TextView txt_info_success, txt_info_failed, txt_tipePX, txt_infoPx1, txt_infoPx2, txt_asuransi, txt_kunjbpjs;
    EditText edt_tglPeriksa, edt_namaPasien, edt_jnsPx1, edt_jnsPx2;
    String val_token, ktpPasien, flagPesan, kd_dokter, nm_dokter, jns_kunj_bpjs,
             kd_klinik, kd_asuransi, nama_asuransi, tglKonvert, jenispx, px_lama;
    ConnectivityManager conMgr;
    Button btn_kirim, btn_ok_failed, btn_ok_success;
    Intent Antrian;
    ImageButton imgbtn_home;
    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat inputFormat = new SimpleDateFormat("dd-LLL-yyyy");

    String[] waktukunj = new String[]{"Pagi", "Siang"};
    AutoCompleteTextView spn_waktu_kunj;
    ProgressDialog pDialog;
    Spinner spn_dokter, spn_asuransi, spn_jnsPx;
    SpinnerAdapter adapter, adapt;
    List<isiSpinner> listisian = new ArrayList<isiSpinner>();
    List<isiSpinner> listAsuransi = new ArrayList<isiSpinner>();

    //JenisPasien
    LinearLayout lay_jenisPx;
    List<String> listJenis = new ArrayList<>();
    CardView cr_jnsPx1, cr_jnsPx2, cr_rgbpjs, cr_asuransi;
    RadioGroup rg_bpjs;
    RadioButton rb_rujuk, rb_kontrol, rbpx_baru, rbpx_lama;

    private Calendar mCalendar = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    String APIurl = RequestHandler.APIdev;
    public String urlDaftar = APIurl+"post-daftar-hemo.php";
    public String urlcekPasien = APIurl+"get-data-px.php";
    public String urlDokterPenanggung = APIurl+"get-dokter-hemo.php";
    public String urlAsuransi = APIurl+"get-asuransi-px.php";
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

        ImageSlider imgSlider = findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel("https://www.rshabibie.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2FHD.293cc9fd.jpg&w=3840&q=75", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://www.rshabibie.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2FPHD.d45cb866.jpg&w=3840&q=75", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://www.rshabibie.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2FPHDAnak.b55a7369.jpg&w=3840&q=75", ScaleTypes.FIT));

        listJenis.add("Umum");
        listJenis.add("BPJS");
        listJenis.add("Asuransi/Perusahaan");
        listJenis.add("Karyawan");

        imgSlider.setImageList(slideModels);
        imgSlider.setSlideAnimation(AnimationTypes.ZOOM_OUT);

        edt_namaPasien = findViewById(R.id.txt_namaPasien);
        edt_tglPeriksa = findViewById(R.id.tglPeriksa);
        spn_jnsPx      = findViewById(R.id.spn_jnsPasien);
        spn_waktu_kunj = findViewById(R.id.spn_waktu);
        imgbtn_home    = findViewById(R.id.imgbtn_home);
        btn_kirim      = findViewById(R.id.btn_kirim);
        spn_dokter     = findViewById(R.id.spn_dokter);
        spn_asuransi   = findViewById(R.id.spn_asuransi);

        //Jenis Pasien
        lay_jenisPx     = findViewById(R.id.lay_jnsPx);
        cr_jnsPx1       = findViewById(R.id.cr_jnsPx1);
        cr_jnsPx2       = findViewById(R.id.cr_jnsPx2);
        cr_asuransi     = findViewById(R.id.cr_asuransi);
        cr_rgbpjs       = findViewById(R.id.cr_rgbpjs);
        edt_jnsPx1      = findViewById(R.id.edt_jnspx1);
        edt_jnsPx2      = findViewById(R.id.edt_jnspx2);
        txt_infoPx1     = findViewById(R.id.txt_infoPx1);
        txt_infoPx2     = findViewById(R.id.txt_infoPx2);
        txt_asuransi    = findViewById(R.id.txt_asuransi);
        txt_tipePX      = findViewById(R.id.txt_tipePx);
        txt_kunjbpjs    = findViewById(R.id.txt_kunbpjs);
        rg_bpjs         = findViewById(R.id.rg_bpjs);
        rb_rujuk        = findViewById(R.id.rbrujuk);
        rb_kontrol      = findViewById(R.id.rbkontrol);

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
            getListAsuransi();
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
        ArrayAdapter<String> AdaptSpnWaktu = new ArrayAdapter<>(this, R.layout.dialog_spinner, waktukunj);
        spn_waktu_kunj.setAdapter(AdaptSpnWaktu);

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
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegistHemo.this, R.layout.dialog_spinner, listJenis);
        adapter = new SpinnerAdapter(RegistHemo.this, listisian);
        adapt = new SpinnerAdapter(RegistHemo.this, listAsuransi);
        spn_dokter.setAdapter(adapter);
        spn_jnsPx.setAdapter(dataAdapter);
        spn_asuransi.setAdapter(adapt);

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

        spn_jnsPx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String nama_jnsPx = parent.getItemAtPosition(position).toString();
                String nama_jnsPx = parent.getItemAtPosition(position).toString();
                getJenisPX(nama_jnsPx);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_asuransi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kd_asuransi = listAsuransi.get(position).getId();
                nama_asuransi = listAsuransi.get(position).getKet();
                listAsuransi.isEmpty();
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

    private void getListAsuransi() {
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
                return requestHandler.requestData(urlAsuransi, "POST", "application/json; charset=utf-8", "X-Api-Token",
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

                            item.setId(jr.getString("kode_perusahaan"));
                            item.setKet(jr.getString("nama_perusahaan"));
                            listAsuransi.add(item);
                        }
                        adapt.notifyDataSetChanged();
//                        hideDialog();
                    } else if(obj.getString("code").equals("500")) {
                        isiSpinner item = new isiSpinner();
                        item.setId("");
                        item.setKet(obj.getString("msg"));
                        listAsuransi.add(item);
                        adapt.notifyDataSetChanged();
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
        final String jenis_px       = jenispx;
        final String kode_asuransi  = kd_asuransi;
        final String edt_jnspx1     = edt_jnsPx1.getText().toString();
        final String edt_jnspx2     = edt_jnsPx2.getText().toString();
        final String jen_kunj_BPJS  = jns_kunj_bpjs;
        final String pxLama         = px_lama;

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
                params.put("jenis_px", jenis_px);
                params.put("kd_asuransi", kode_asuransi);
                params.put("field1", edt_jnspx1);
                params.put("field2", edt_jnspx2);
                params.put("jns_kunj_bpjs", jen_kunj_BPJS);
                params.put("px_lama", pxLama);
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

    public void getJenisPX(String nama_jnsPx){
        if(nama_jnsPx.equals("Umum")){
            lay_jenisPx.setVisibility(View.GONE);
            jenispx = "1";
        }else if(nama_jnsPx.equals("BPJS")){
            txt_tipePX.setText("BPJS");
            txt_infoPx1.setText("No Kartu BPJS :");

            lay_jenisPx.setVisibility(View.VISIBLE);
            rg_bpjs.setVisibility(View.VISIBLE);
            cr_rgbpjs.setVisibility(View.VISIBLE);
            txt_kunjbpjs.setVisibility(View.VISIBLE);
            txt_infoPx1.setVisibility(View.VISIBLE);
            cr_jnsPx1.setVisibility(View.VISIBLE);
            cr_asuransi.setVisibility(View.GONE);
            txt_asuransi.setVisibility(View.GONE);
            jenispx = "3";
        }else if(nama_jnsPx.equals("Asuransi/Perusahaan")){
            txt_tipePX.setText("Asuransi/Perusahaan");
            txt_infoPx2.setText("No Polis :");
            lay_jenisPx.setVisibility(View.VISIBLE);
            rg_bpjs.setVisibility(View.GONE);
            cr_rgbpjs.setVisibility(View.GONE);
            cr_jnsPx1.setVisibility(View.GONE);
            txt_infoPx1.setVisibility(View.GONE);
            txt_kunjbpjs.setVisibility(View.GONE);
            cr_asuransi.setVisibility(View.VISIBLE);
            txt_asuransi.setVisibility(View.VISIBLE);
            jenispx = "2";
        }else if(nama_jnsPx.equals("Karyawan")){
            lay_jenisPx.setVisibility(View.GONE);
            jenispx = "4";
        }
    }

    public void clickFungsi(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rbrujuk:
                if (checked)
                    jns_kunj_bpjs = "1";
                    txt_infoPx2.setText("No Rujukan :");
                    break;
            case R.id.rbkontrol:
                if (checked)
                    jns_kunj_bpjs = "2";
                    txt_infoPx2.setText("No Surat Kontrol :");
                    break;
        }
    }

    public void CekPxLama(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rbpxbaru:
                if (checked)
                    px_lama = "1";
                break;
            case R.id.rbpxlama:
                if (checked)
                    px_lama = "2";
                break;
        }
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