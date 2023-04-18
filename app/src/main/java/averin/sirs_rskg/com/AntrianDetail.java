package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;
//Scanner Funct

public class AntrianDetail extends AppCompatActivity{

    String val_token, regId, no_antri, nm_dokter, tgl_antri, jam_awal, jam_akhir, status_antri, tgl_now,
            nm_klinik, nm_bag, tglKonvert, jam_konvert, stat_px;
    TextView txt_nmKlinik, txt_jnsPoli, txt_nmDokter, txt_noAntri, txt_tglPeriksa, txt_jamPeriksa, txt_jnsPasien,
            txt_wktPeriksa, lb_antrian, txt_info_antrian, txt_stat_antri;
    ImageView imgKlinik;
    Button btn_scan;
    CardView cd_scan;
    DateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat outputwaktu = new SimpleDateFormat("HH:mm", Locale.getDefault());
    DateFormat inputwaktu = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    String APIurl = RequestHandler.APIdev;
    public String urlDetailAntrian = APIurl+"get-jadwal-px-detail.php";
    public String linkJanda  = APIurl+"get-token.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_antrian_detail);

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

        Token tkn = AppController.getInstance(this).isiToken();
        val_token = String.valueOf(tkn.gettoken());

        txt_nmKlinik = findViewById(R.id.nmKlinik);
        txt_jnsPoli = findViewById(R.id.jenisPoli);
        txt_nmDokter = findViewById(R.id.nma_Dokter);
        txt_noAntri = findViewById(R.id.no_Antrian);
        txt_tglPeriksa = findViewById(R.id.tglPeriksa);
        txt_jamPeriksa = findViewById(R.id.jamPeriksa);
        txt_jnsPasien = findViewById(R.id.jenisPasien);
        txt_wktPeriksa = findViewById(R.id.wktPeriksa);
        txt_info_antrian = findViewById(R.id.txt_info_antrian);
        lb_antrian =  findViewById(R.id.lb_antrian);
        txt_stat_antri = findViewById(R.id.txt_stat_hadir);
        btn_scan = findViewById(R.id.btn_scan);
        cd_scan = findViewById(R.id.cd_btnscan);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            regId = extras.get("regId").toString();
            viewDetailAntrian();
        }

    }

    public void onBackPressed() {
        Intent startMain = new Intent(AntrianDetail.this,AntrianActivity.class);
        startActivity(startMain);
    }

    private void viewDetailAntrian() {
        //first getting the values
        final String iniToken   = val_token;
        final String no_regist = regId;

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("kode_klinik","C00002");
                params.put("idReg", no_regist);

                //returing the response
                return requestHandler.requestData(urlDetailAntrian, "POST", "application/json; charset=utf-8", "X-Api-Token",
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

                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response

                    JSONArray jr = obj.getJSONArray("list");
                    if (jr.equals("null")) {
                        Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        for (int a = 0; a < jr.length(); a++) {
                            JSONObject jso = jr.getJSONObject(a);
                            regId = jso.getString("idReg");
                            no_antri = jso.getString("no_antrian");
                            nm_dokter = jso.getString("nama_dokter");
                            nm_klinik = jso.getString("nama_klinik");
                            nm_bag = jso.getString("nama_bagian");
                            tgl_antri = jso.getString("tgl");
                            jam_awal = jso.getString("jam_awal");
                            jam_akhir = jso.getString("jam_akhir");
                            status_antri = jso.getString("status");
                            stat_px = jso.getString("ket_klinik");
                            String stat_jam = jso.getString("stat_jam");
                            tgl_now = df.format(new Date());
                            String tgl_antri_konvert;
                            Date tgl = null;
                            Date wkt = null;
                            Date wkt_akhir = null;
                            try {
                                tgl = inputFormat.parse(tgl_antri);
                                wkt = inputwaktu.parse(jam_awal);
                                wkt_akhir = inputwaktu.parse(jam_akhir);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            tglKonvert = outputFormat.format(tgl);
                            jam_konvert = outputwaktu.format(wkt);
                            tgl_antri_konvert = inputFormat.format(tgl);
                            String jam_akhir_konvert = outputwaktu.format(wkt_akhir);

                                if (status_antri.equals("1")) {
                                    btn_scan.setVisibility(View.GONE);
                                    txt_stat_antri.setVisibility(View.VISIBLE);
                                    txt_info_antrian.setText("Silahkan menuju FO untuk menanyakan status antrian Poli anda");
                                } else {
                                    if(tgl_now.equals(tgl_antri_konvert)) {
                                        if (stat_px.equals("lama") || stat_px.equals("null")) {
                                            if(stat_jam.equals("1")){
                                                btn_scan.setVisibility(View.GONE);
                                                cd_scan.setVisibility(View.GONE);
                                                txt_info_antrian.setText("Jadwal periksa anda telah lewat jadwal \n silahkan datang ke FO untuk informasi lebih lanjut");
                                            }else {
                                                btn_scan.setVisibility(View.VISIBLE);
                                                cd_scan.setVisibility(View.VISIBLE);
                                                txt_info_antrian.setText("Datang 30 menit sebelum pemeriksaan dan \n konfirmasi kehadiran baik melalui FO atau \n Scan QR pada RS");
                                            }
                                        } else{
                                            btn_scan.setVisibility(View.GONE);
                                            cd_scan.setVisibility(View.GONE);
                                            txt_info_antrian.setText("Silahkan datang ke FO dan membawa KTP anda \n untuk melakukan registrasi ulang");
                                        }
                                    }else{
                                        btn_scan.setVisibility(View.GONE);
                                        txt_stat_antri.setVisibility(View.GONE);
                                        cd_scan.setVisibility(View.GONE);
                                        txt_info_antrian.setText("Datang 30 menit sebelum jadwal pemeriksaan dan \n konfirmasi kehadiran baik melalui FO atau \n Scan QR pada RS");
                                    }
                                }

                            txt_noAntri.setText(no_antri);
                            txt_nmDokter.setText(nm_dokter);
                            txt_nmKlinik.setText(nm_klinik);
                            txt_jnsPoli.setText(nm_bag);
                            txt_wktPeriksa.setText(jam_konvert+" - "+jam_akhir_konvert);
                            txt_tglPeriksa.setText(tglKonvert);
                            txt_jamPeriksa.setText(jam_konvert);
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

//    SCANNER FUNCTION
    public void ScanKlinik(View v){
        Intent i = new Intent(AntrianDetail.this, ScanQRCode.class);
        i.putExtra("regId", regId);
        i.putExtra("noAntrian", no_antri);
        i.putExtra("nma_dokter", nm_dokter);
        i.putExtra("tgl_antri", tgl_antri);
        i.putExtra("jam_awal", jam_awal);
        i.putExtra("jam_akhir", jam_akhir);
        i.putExtra("status_antri", status_antri);
        i.putExtra("nm_klinik", nm_klinik);
        i.putExtra("nm_bag", nm_bag);
        i.putExtra("stat_px", stat_px);
        startActivity(i);
    }
}