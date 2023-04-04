package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Adapter.SpinnerAdapter;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.isiSpinner;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;

import java.util.Date;

public class DaftarPoli extends AppCompatActivity {

    TextView txt_bagian, txt_namaDokter, txt_namaklinik, txt_info_success, txt_info_failed, txt_askPX_atas, txt_askPX_bawah;
    EditText edt_tglPeriksa, edt_namaPasien, edt_noAntrian;
    String val_token, ktpPasien, nomr, urlFoto, kd_dokter, id_dokter, nm_dokter, kd_bag, bagian, nm_klinik, kd_klinik, durasi, jdwl_periksa, tgl_now,tglKonvert;
    ConnectivityManager conMgr;
    Button btn_kirim, btn_ok_failed, btn_ok_success, btn_cancel_px, btn_yes_px;
    Intent Antrian;
    ImageView fotoDokter;
    ImageButton imgbtn_home;
    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat inputFormat = new SimpleDateFormat("dd-LLL-yyyy");

    private byte[] byt_Foto;
    private Calendar mCalendar = Calendar.getInstance();

    String APIurl = RequestHandler.APIdev;
    String px_baru = "baru";
    String px_lama = "lama";
    public String urlDaftar = APIurl+"post-daftar-px.php";
    public String urlAntrian = APIurl+"get-antrian-dokter.php";
    public String urlcekPasien = APIurl+"get-data-px.php";
    public String linkJanda  = APIurl+"get-token.php";

    //Dialog Confirm
    AlertDialog.Builder builder_success, builder_failed, dial_builder, builder_ask_px;
    AlertDialog dial_success, dial_failed, dial_login, dial_newPX;
    LayoutInflater inflater;
    View v_success_regist, v_failed_regist, v_ask_newPX, dialogView;

    //ELEMENT FOR SPINNER
    Spinner s_jamPeriksa;
    ProgressDialog pDialog;
    SpinnerAdapter adapter;
    List<isiSpinner> listisian = new ArrayList<isiSpinner>();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_poli);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar LabToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(LabToolbar);
//        LabToolbar.setLogoDescription(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LabToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_bagian     = findViewById(R.id.txt_bagian);
        txt_namaDokter = findViewById(R.id.txt_namadokter);
        edt_namaPasien = findViewById(R.id.txt_namaPasien);
        txt_namaklinik = findViewById(R.id.txt_namaklinik);
        edt_noAntrian  = findViewById(R.id.txt_noAntrian);
        edt_tglPeriksa = findViewById(R.id.tglPeriksa);
        s_jamPeriksa   = findViewById(R.id.jamPeriksa);
        btn_kirim      = findViewById(R.id.btn_kirim);
        imgbtn_home    = findViewById(R.id.imgbtn_home);
        fotoDokter     = findViewById(R.id.fotoDokter);

        //Dialog Confirm
        ViewGroup viewGroup = findViewById(android.R.id.content);
        builder_success = new AlertDialog.Builder(DaftarPoli.this,R.style.CustomAlertDialog);
        builder_failed = new AlertDialog.Builder(DaftarPoli.this,R.style.CustomAlertDialog);
        builder_ask_px = new AlertDialog.Builder(DaftarPoli.this,R.style.CustomAlertDialog);
        inflater = getLayoutInflater();
        v_success_regist = inflater.inflate(R.layout.dialog_success_regist, viewGroup, false);
        v_failed_regist = inflater.inflate(R.layout.dialog_failed_regist, viewGroup, false);
        v_ask_newPX = inflater.inflate(R.layout.dialog_ask_regist_new_px, viewGroup, false);
        btn_ok_success = v_success_regist.findViewById(R.id.btn_oke);
        btn_ok_failed = v_failed_regist.findViewById(R.id.btn_oke_failed);
        btn_yes_px = v_ask_newPX.findViewById(R.id.btn_daftar);
        btn_cancel_px = v_ask_newPX.findViewById(R.id.btn_cancel);
        txt_info_success = v_success_regist.findViewById(R.id.txt_info_success);
        txt_info_failed = v_failed_regist.findViewById(R.id.txt_info_failed);
        txt_askPX_atas = v_ask_newPX.findViewById(R.id.txt_info_atas);
        txt_askPX_bawah = v_ask_newPX.findViewById(R.id.txt_info_bawah);
        builder_success.setView(v_success_regist);
        builder_failed.setView(v_failed_regist);
        builder_ask_px.setView(v_ask_newPX);
        dial_success = builder_success.create();
        dial_success.setCancelable(false);
        dial_failed = builder_failed.create();
        dial_failed.setCancelable(false);
        dial_newPX = builder_ask_px.create();
        dial_newPX.setCancelable(false);

        //Dialog ask login
        dial_builder = new AlertDialog.Builder(DaftarPoli.this,R.style.CustomAlertDialog);
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
                Intent i = new Intent(DaftarPoli.this, LoginActivity.class);
                i.putExtra("email_px", "");
                i.putExtra("pass_px", "");
                i.putExtra("last_menu", "dp");
                startActivity(i);
                dial_login.dismiss();

            }
        });
        btn_act_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DaftarPoli.this, MainActivity.class);
                startActivity(i);
                dial_login.dismiss();
            }
        });
        imgbtn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DaftarPoli.this, MainActivity.class);
                startActivity(i);
            }
        });
        btn_cancel_px.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_newPX.dismiss();
            }
        });
        btn_yes_px.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nontonSiskae("baru");
            }
        });

        if (!AppController.getInstance(this).isLoggedIn()) {
            dial_login.show();
        }

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

        edt_namaPasien.setEnabled(false);
        edt_noAntrian.setEnabled(false);
//        edt_tglPeriksa.setEnabled(false);

        Antrian =new Intent(DaftarPoli.this,AntrianActivity.class);
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
//        nomr = String.valueOf(login.getMR_pasien());

        Bundle kiriman = getIntent().getExtras();
        if (kiriman != null) {
            urlFoto         = kiriman.get("urlFoto").toString();
            kd_klinik       = kiriman.get("kd_klinik").toString();
            nm_klinik       = kiriman.get("nm_klinik").toString();
            kd_dokter       = kiriman.get("kd_dokter").toString();
            id_dokter       = kiriman.get("idnya_dokter").toString();
            nm_dokter       = kiriman.get("nm_dokter").toString();
            kd_bag          = kiriman.get("kd_bag").toString();
            bagian          = kiriman.get("nm_bag").toString();
            durasi          = kiriman.get("durasi").toString();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());

        txt_bagian.setText(bagian);
        txt_namaDokter.setText(nm_dokter);
        txt_namaklinik.setText(nm_klinik);
        tgl_now = df.format(new Date());
        edt_tglPeriksa.setText("- Pilih tanggal -");

        byt_Foto = Base64.decode(urlFoto, Base64.DEFAULT);
        Bitmap bitmapDokter = BitmapFactory.decodeByteArray(byt_Foto, 0, byt_Foto.length);
        if(urlFoto.equals("")){
            fotoDokter.setImageResource(R.drawable.doctor1);
        }else {
            fotoDokter.setImageBitmap(bitmapDokter);
        }
        edt_tglPeriksa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new DatePickerDialog(DaftarPoli.this, R.style.DialogTheme, datestart, mCalendar.get(Calendar.YEAR),
//                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
                DatePickerDialog dialog = new DatePickerDialog(DaftarPoli.this,R.style.DialogTheme, datestart,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                dialog.show();
            }
        });

        adapter = new SpinnerAdapter(DaftarPoli.this, listisian);
        s_jamPeriksa.setAdapter(adapter);
        s_jamPeriksa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                jdwl_periksa =listisian.get(position).getKet();
                edt_noAntrian.setText(listisian.get(position).getId());
                listisian.isEmpty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekPasienlama();
//                nontonSiskae();
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
            listisian.clear();
            GoletiPerawan(edt_tglPeriksa.getText().toString());
            edt_noAntrian.setText("");

        }
    };

    private void cekPasienlama() {
        final String iniToken   = val_token;
        final String noKTP      = ktpPasien;
        final String kdeKlinik  = kd_klinik;

        class  kirimData extends AsyncTask<Void, Void, String> {

            private HashMap params;
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                params = new HashMap<String, HashMap<String, String>>();
                params.put("kode_klinik", kdeKlinik);
                params.put("no_ktp", noKTP);

                //returing the response
                return requestHandler.requestData(urlcekPasien, "POST", "application/json; charset=utf-8", "X-Api-Token",
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
                    if (obj.getString("code").equals("200")){
                        nontonSiskae("lama");
                    } else if(obj.getString("code").equals("500")){
                        dial_newPX.show();
                        txt_askPX_atas.setText("Anda Belum Terdaftar \n Sebagai Pasien Lama Pada \n "+nm_klinik);
                        txt_askPX_bawah.setText("Apakah anda ingin mendaftar sebagai pasien baru pada "+nm_klinik);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        kirimData pl = new kirimData();
        pl.execute();
    }

    private void GoletiPerawan(final String tgl_pilih) {
        //first getting the values
        final String iniToken     = val_token;
        final String kode_klinik  = kd_klinik;
        final String kode_dokter  = kd_dokter;
        final String id           = id_dokter;
        listisian.clear();

        pDialog = new ProgressDialog(DaftarPoli.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        showDialog();

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
                    val.put("id", id);
                    val.put("kode_dokter", kode_dokter);
                    val.put("tgl_daftar", tgl_pilih);
                    params.put("kode_klinik", kode_klinik);
                    params.put("src", val);

                //returing the response
                return requestHandler.requestData(urlAntrian, "POST", "application/json; charset=utf-8", "X-Api-Token",
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
                    if (obj.getString("code").equals("500")) {
                        isiSpinner item = new isiSpinner();
                        item.setId("");
                        item.setKet(obj.getString("msg"));
                        listisian.add(item);
                        adapter.notifyDataSetChanged();
                        hideDialog();
                    } else if(obj.getString("code").equals("200")) {
                        JSONArray jso = obj.getJSONArray("list");
                        for (int a = 0; a < jso.length(); a++) {

                            isiSpinner item = new isiSpinner();
                            JSONObject jr = jso.getJSONObject(a);

                            item.setId(jr.getString("antrian"));
                            item.setKet(jr.getString("jam"));
                            edt_noAntrian.setText(jr.getString("antrian"));

                            listisian.add(item);
                        }
                        adapter.notifyDataSetChanged();
                        hideDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        masukPakEko pl = new masukPakEko();
        pl.execute();
    }

    public void nontonSiskae(final String stat_px) {
        final String isiToken       = val_token;
        final String kdKlinik       = kd_klinik;
        final String nmKlinik       = nm_klinik;
        final String nmPasien       = edt_namaPasien.getText().toString();
        final String no_mr_px       = nomr;
        final String noKTP          = ktpPasien;
        final String kdDokter       = kd_dokter;
        final String nmDokter       = nm_dokter;
        final String kdBagian       = kd_bag;
        final String nmBagian       = bagian;
        final String wkt_periksa    = durasi;
        final String no_Antrian     = edt_noAntrian.getText().toString();
        final String jadwal         = jdwl_periksa;
        final String tanggal        = edt_tglPeriksa.getText().toString();

        if (TextUtils.isEmpty(no_Antrian)) {
            edt_noAntrian.setError("No Antrian tidak tersedia");
            edt_noAntrian.requestFocus();
            return;
        }

        pDialog = new ProgressDialog(DaftarPoli.this);
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
                params.put("jadwal", jadwal);
                params.put("no_antrian", no_Antrian);
                params.put("no_ktp", noKTP);
                params.put("kode_dokter", kdDokter);
                params.put("kode_bagian", kdBagian);
                params.put("nama_bagian", nmBagian);
                params.put("nama_klinik", nmKlinik);
                params.put("nama_dokter", nmDokter);
                params.put("durasi", wkt_periksa);
                params.put("ket_klinik", stat_px);
                params.put("kode_klinik", kdKlinik);
                params.put("tgl_daftar", tanggal);
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