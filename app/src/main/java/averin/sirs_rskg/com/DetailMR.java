package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import averin.sirs_rskg.com.Adapter.DetailMRAdapter;
import averin.sirs_rskg.com.Adapter.LabAdapter;
import averin.sirs_rskg.com.Adapter.RadAdapter;
import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Adapter.ResepDokterAdapter;
import averin.sirs_rskg.com.Model.Lab;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Rad;
import averin.sirs_rskg.com.Model.ResepObat;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Model.isiSpinner;
import averin.sirs_rskg.com.Ui.AppController;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailMR extends AppCompatActivity {

    String val_token, no_ktp, foto_px, kodeklinik, idRegist, url_rs,
            nama_px, nomr_px, umur_px, goldarah_px, gender_px, alergi_px,
            namaKlinik, namaDokter, namaBagian, tglPeriksa, wktPeriksa, no_resep, kode_resep,
            keadaan_umum, tekanan_darah, suhu, tinggi_badan, kesadaran, nadi, pernafasan, bb,
            nama_obat,jml_obat,note_obat,ket_obat,aturan_pakai, jns_obat, satuan_obat;
    TextView txt_namaklinik, txt_namadokter, txt_namapoli, txt_tglperiksa, txt_null_resep,
            txt_namapasien, txt_nomr, txt_umur, txt_goldarah, txt_jekel, txt_alergi,
            txt_KeadaanUmum, txt_TekananDarah, txt_suhu, txt_tinggi, txt_kesadaran, txt_nadi, txt_pernafasan, txt_bb;

    //Dialog Confirm
    AlertDialog.Builder dial_builder;
    AlertDialog dial_info_null;
    LayoutInflater inflater;
    View view_null_data;
    CircleImageView imgPasien;

    //Tindakan & ICD10
    DetailMRAdapter adapt_tindakan, adapt_icd10;
    ListView lsTindakan, lsICD10;
    ArrayList<isiSpinner> listtindakan = new ArrayList<>();
    ArrayList<isiSpinner> listicd10 = new ArrayList<>();

    //Resep Dokter
    RecyclerView rc_tindakan, rc_icd10, rc_resepdokter;
    ResepDokterAdapter adaptResepDokter;
    List<ResepObat> listresep = new ArrayList<ResepObat>();
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;
    LinearLayout ly_print;

    //Lab & Rad
    RecyclerView rc_laboratorium, rc_radiologi;
    LinearLayout ly_null_lab, ly_null_rad, ly_hasillab, ly_hasilrad;
    LabAdapter LabAdapt;
    RadAdapter RadAdapt;

    private ArrayList<Lab> listlab = new ArrayList<>();
    private ArrayList<Rad> listrad = new ArrayList<>();

    String APIurl = RequestHandler.APIdev;
    public String urlDetailMR = APIurl+"get-detail-riwayat.php";
    public String linkJanda  = APIurl+"get-token.php";

    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mr);

//        LinearLayout linearLayout = findViewById(R.id.resepdolter);
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetBehavior);
//
//        Button button =findViewById(R.id.irwan);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            }
//        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

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

        //GET DATA FROM CONTROLLER
        Login login = AppController.getInstance(this).getPasien();
        Token token = AppController.getInstance(this).isiToken();
        val_token = String.valueOf(token.gettoken());
        no_ktp    = String.valueOf(login.getKTP_pasien());
        nama_px   = String.valueOf(login.getNama_pasien());
        foto_px   = String.valueOf(login.getFoto_pasien());

        Bundle kiriman = getIntent().getExtras();
        if(kiriman != null){

            kodeklinik = kiriman.get("kd_klinik").toString();
            idRegist = kiriman.get("idRegKlinik").toString();
            url_rs = kiriman.get("url_rs").toString();

            namaKlinik = kiriman.get("nama_klinik").toString();
            namaBagian = kiriman.get("nama_bagian").toString();
            namaDokter = kiriman.get("nama_dokter").toString();
            tglPeriksa = kiriman.get("tgl_daftar").toString();

            getDetailMR();
        }

//        Card Pasien MR Info & Dokter Periksa
        imgPasien = findViewById(R.id.imgPasien);
        txt_namaklinik = findViewById(R.id.txt_namaklinik);
        txt_namadokter = findViewById(R.id.txt_namadokter);
        txt_namapoli = findViewById(R.id.txt_namaPoli);
        txt_tglperiksa = findViewById(R.id.txt_tgl_periksa);

        txt_namapasien = findViewById(R.id.txt_namaPasien);
        txt_nomr = findViewById(R.id.txt_mrPasien);
        txt_umur = findViewById(R.id.txt_umurPasien);
        txt_goldarah = findViewById(R.id.txt_golDarah);
        txt_jekel = findViewById(R.id.txt_jekelPasien);
        txt_alergi = findViewById(R.id.txt_alergiPasien);

        if(no_ktp.equals("3174586231698546")) {
            imgPasien.setImageResource(R.drawable.foto_bos);
        }else {
            if (foto_px.equals("null")) {
                imgPasien.setImageResource(R.drawable.profile22);
            } else {
                Glide.with(DetailMR.this).load(foto_px).into(imgPasien);
            }
        }

        txt_namaklinik.setText(namaKlinik);
        txt_namadokter.setText(namaDokter);
        txt_namapoli.setText(namaBagian);
        txt_tglperiksa.setText(tglPeriksa);

//        Vital Sign
        txt_KeadaanUmum = findViewById(R.id.txt_keadaanumum);
        txt_TekananDarah = findViewById(R.id.txt_TekananDarah);
        txt_suhu = findViewById(R.id.txt_Suhu);
        txt_tinggi = findViewById(R.id.txt_TinggiBadan);
        txt_nadi = findViewById(R.id.txt_Nadi);
        txt_kesadaran = findViewById(R.id.txt_Kesadaran);
        txt_bb = findViewById(R.id.txt_Berat);
        txt_pernafasan = findViewById(R.id.txt_Pernafasan);

//        Tindakan
        rc_tindakan = findViewById(R.id.rc_tindakan);
        rc_tindakan.setLayoutManager(new LinearLayoutManager(this));
        adapt_tindakan = new DetailMRAdapter(this, listtindakan,this);
        rc_tindakan.setAdapter(adapt_tindakan);

//        ICD10
        rc_icd10 = findViewById(R.id.rc_icd10);
        rc_icd10.setLayoutManager(new LinearLayoutManager(this));
        adapt_icd10 = new DetailMRAdapter(this, listicd10,this);
        rc_icd10.setAdapter(adapt_icd10);

//        Resep Obat
        rc_resepdokter = findViewById(R.id.rc_resepobat);
        txt_null_resep = findViewById(R.id.txt_null_resep);
        ly_print = findViewById(R.id.ly_print);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        rc_resepdokter.setLayoutManager(RecyclerViewLayoutManager);
        HorizontalLayout = new LinearLayoutManager(DetailMR.this, LinearLayoutManager.HORIZONTAL,false);
        rc_resepdokter.setLayoutManager(HorizontalLayout);
        adaptResepDokter = new ResepDokterAdapter(listresep);
        rc_resepdokter.setAdapter(adaptResepDokter);

//        Lab & Rad
        rc_laboratorium = findViewById(R.id.rc_laboratorium);
        rc_radiologi    = findViewById(R.id.rc_radiologi);
        ly_null_lab     = findViewById(R.id.ly_null_lab);
        ly_null_rad     = findViewById(R.id.ly_null_rad);
        ly_hasillab     = findViewById(R.id.ly_hasil_lab);
        ly_hasilrad     = findViewById(R.id.ly_hasil_rad);
        rc_laboratorium.setLayoutManager(new LinearLayoutManager(this));
        rc_radiologi.setLayoutManager(new LinearLayoutManager(this));
        LabAdapt = new LabAdapter(this, listlab,this);
//        RadAdapt = new RadAdapter(this, listrad, this);

        RadAdapt = new RadAdapter(this, listrad, this, new RadAdapter.ClickListener() {
            @Override
            public void onPositionClicked(int position) {

            }

            @Override
            public void onLongClicked(int position) {

            }
        });

        rc_laboratorium.setAdapter(LabAdapt);
        rc_radiologi.setAdapter(RadAdapt);

        //Dialog Empty Data
        ViewGroup vg = findViewById(android.R.id.content);
        dial_builder = new AlertDialog.Builder(DetailMR.this,R.style.CustomAlertDialog);
        inflater = getLayoutInflater();
//        txt_info_null = view_null_data.findViewById(R.id.txt_info_failed);
//        view_null_data = inflater.inflate(R.layout.dialog_failed_regist, vg, false);
//        Button btn_ok = view_null_data.findViewById(R.id.btn_oke_failed);
//        dial_builder.setView(view_null_data);
//        dial_info_null = dial_builder.create();
//        dial_info_null.setCancelable(false);
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dial_info_null.dismiss()
//                Intent i = new Intent(DetailMR.this, MRpasienActivity.class);
//                startActivity(i);
//            }
//        });
    }

    public void onBackPressed(){
        Intent bck = new Intent(DetailMR.this,MRpasienActivity.class);
        bck.putExtra("kd_klinik", kodeklinik);
        bck.putExtra("nama_klinik", namaKlinik);
        startActivity(bck);

    }
    private void getDetailMR() {
        final String iniToken = val_token;
        final String kdklinik = kodeklinik;
        final String Regid = idRegist;

        listtindakan.clear();listicd10.clear();

        class ambilDataMR extends AsyncTask<Void, Void, String> {

            private HashMap params;
            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                params = new HashMap<String, HashMap<String, String>>();
                HashMap<String, String> val = new HashMap<String, String>();
                params.put("kode_klinik", kdklinik);
                val.put("id", Regid);
                val.put("url_rs", url_rs);
                params.put("param", val);
                params.put("kode_klinik", kdklinik);
                params.put("id", Regid);
                params.put("url_rs", url_rs);

                //returing the response
                return requestHandler.requestData(urlDetailMR, "POST", "application/json; charset=utf-8", "X-Api-Token",
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

                        Toast.makeText(DetailMR.this, jr.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else if(jr.getString("code").equals("200")){

//                      Data PX
                        JSONArray res_px = jr.getJSONArray("px");
                        for(int vs = 0; vs < res_px.length(); vs++) {
                            JSONObject obj_vs = res_px.getJSONObject(vs);
                            nama_px = obj_vs.getString("nama_px");
                            nomr_px = obj_vs.getString("nomr_px");
                            gender_px = obj_vs.getString("gender");
                            goldarah_px = obj_vs.getString("gol_darah");
                            umur_px = obj_vs.getString("umur");
                            alergi_px = obj_vs.getString("alergi");

                            txt_namapasien.setText(nama_px);
                            txt_nomr.setText(nomr_px);
                            txt_umur.setText(umur_px);
                            txt_goldarah.setText(goldarah_px);
                            txt_jekel.setText(gender_px);
                            txt_alergi.setText(alergi_px);
                        }

//                      Vital Sign
                        JSONArray res_vital_sign = jr.getJSONArray("vital_sign");
                        for(int vs = 0; vs < res_vital_sign.length(); vs++) {
                            JSONObject obj_vs = res_vital_sign.getJSONObject(vs);
                            keadaan_umum = obj_vs.getString("keadaan_umum");
                            tekanan_darah = obj_vs.getString("tekanan_darah");
                            suhu = obj_vs.getString("suhu");
                            tinggi_badan = obj_vs.getString("tinggi_badan");
                            kesadaran = obj_vs.getString("kesadaran_pasien");
                            nadi = obj_vs.getString("nadi");
                            pernafasan = obj_vs.getString("pernafasan");
                            bb = obj_vs.getString("berat_badan");

                            txt_KeadaanUmum.setText(keadaan_umum);
                            txt_TekananDarah.setText(tekanan_darah);
                            txt_suhu.setText(suhu);
                            txt_pernafasan.setText(pernafasan);
                            txt_tinggi.setText(tinggi_badan);
                            txt_kesadaran.setText(kesadaran);
                            txt_nadi.setText(nadi);
                            txt_bb.setText(bb);
                        }

//                      Tindakan
                        if(jr.getString("tindakan").equals("")){

                            isiSpinner item_tdk = new isiSpinner();
                            item_tdk.setId("");
                            item_tdk.setKet("Tidak ada tindakan");
                            listtindakan.add(item_tdk);
                            adapt_tindakan.notifyDataSetChanged();

                        } else {

                            JSONArray res_tindakan = jr.getJSONArray("tindakan");
                            for (int td = 0; td < res_tindakan.length(); td++) {
                                isiSpinner item_tdk = new isiSpinner();
                                JSONObject obj_tdk = res_tindakan.getJSONObject(td);
                                item_tdk.setId(obj_tdk.getString("no_tdk"));
                                item_tdk.setKet(obj_tdk.getString("nama_tindakan"));
                                listtindakan.add(item_tdk);
                            }
                            adapt_tindakan.notifyDataSetChanged();

                        }
//                        ICD-10
                        if(jr.getString("icd10").equals("")){

                            isiSpinner item_icd = new isiSpinner();
                            item_icd.setId("");
                            item_icd.setKet("Tidak ada data");
                            listicd10.add(item_icd);
                            adapt_icd10.notifyDataSetChanged();

                        } else {

                            JSONArray res_icd10 = jr.getJSONArray("icd10");
                            for (int icd = 0; icd < res_icd10.length(); icd++) {
                                isiSpinner item_icd = new isiSpinner();
                                JSONObject obj_icd = res_icd10.getJSONObject(icd);
                                item_icd.setId(obj_icd.getString("no_icd"));
                                item_icd.setKet(obj_icd.getString("icd10"));
                                listicd10.add(item_icd);
                            }
                            adapt_icd10.notifyDataSetChanged();

                        }
//                        Resep Obat
                        if(jr.getString("resep").equals("") || jr.getString("resep").equals("null")){
                            ly_print.setVisibility(View.GONE);
                            txt_null_resep.setVisibility(View.VISIBLE);
                            rc_resepdokter.setVisibility(View.GONE);
                        }else {
                            JSONArray res_resep = jr.getJSONArray("resep");
                            for (int ro = 0; ro < res_resep.length(); ro++) {
                                JSONObject obj_ro = res_resep.getJSONObject(ro);
                                String nom = obj_ro.getString("no");
                                kode_resep = obj_ro.getString("kode_pesan");
                                no_resep = obj_ro.getString("no_resep");
                                nama_obat = obj_ro.getString("nama_brg");
                                jns_obat = obj_ro.getString("satuan");
                                note_obat = obj_ro.getString("note");
                                jml_obat = obj_ro.getString("jumlah_pesan");
                                aturan_pakai = obj_ro.getString("nama_dosis");
                                ket_obat = obj_ro.getString("ket");
                                if (jns_obat.equals("false")) {
                                    satuan_obat = " -- ";
                                } else {
                                    satuan_obat = jns_obat;
                                }

                                listresep.add(new ResepObat(nom, nama_obat, note_obat, aturan_pakai, jml_obat, ket_obat, satuan_obat));
                            }
                        }
                        adaptResepDokter.notifyDataSetChanged();

//                        Laboratorium
                        if(jr.getString("hasil_lab").equals("") || jr.getString("hasil_lab").equals("null")){
                            ly_null_lab.setVisibility(View.VISIBLE);
                            ly_hasillab.setVisibility(View.GONE);

                        } else {

                            JSONArray res_lab = jr.getJSONArray("hasil_lab");
                            for (int a = 0; a < res_lab.length(); a++) {
                                JSONObject jso = res_lab.getJSONObject(a);
                                String url_rs               = jso.getString("url_rs");
                                String no_urut              = jso.getString("no_urut");
                                String url_pdf_lab          = jso.getString("url_pdf_lab");
                                String kode_penunjang       = jso.getString("kode_penunjang");
                                String txt_kategori         = jso.getString("txt_kategori");
                                String tglhasil       = jso.getString("tgl_isihasil");
                                String nomr_px              = jso.getString("no_mr_px");
                                String nama_tindakan_lab    = jso.getString("diagnosa");
                                String flag_cetak           = jso.getString("flag_cetak");
                                
//                                String tglhasil             = jso.getString("tgl_hasil");
                                listlab.add(new Lab(no_urut, url_pdf_lab, kode_penunjang, txt_kategori, nomr_px, nama_tindakan_lab, tglhasil));
                            }
                            LabAdapt.notifyDataSetChanged();
                        }

//                        Radiologi
                        if(jr.getString("hasil_rad").equals("") || jr.getString("hasil_rad").equals("null")){
                            ly_null_rad.setVisibility(View.VISIBLE);
                            ly_hasilrad.setVisibility(View.GONE);

                        } else {

                            JSONArray res_rad = jr.getJSONArray("hasil_rad");
                            for (int a = 0; a < res_rad.length(); a++) {
                                JSONObject jso = res_rad.getJSONObject(a);

                                String no_urut                  = jso.getString("no_urut");
                                String url_rs                   = jso.getString("url_rs");
                                String url_pdf_rad              = jso.getString("url_pdf_rad");
                                String kode_penunjang           = jso.getString("kode_penunjang_rad");
                                String kd_tc_hasilpenunjang     = jso.getString("kode_tc_hasilpenunjang");
                                String kode_tarif               = jso.getString("kode_tarif_rad");
                                String jmlThorax                = jso.getString("jml_thorax");
                                String nama_tindakan_rad        = jso.getString("nama_tindakan_rad");
                                String txt_kategori             = jso.getString("txt_kategori");
                                listrad.add(new Rad(no_urut, url_rs, url_pdf_rad, kodeklinik, kode_penunjang, kd_tc_hasilpenunjang, txt_kategori, kode_tarif, jmlThorax, nama_tindakan_rad));
                            }
                            RadAdapt.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        ambilDataMR pl = new ambilDataMR();
        pl.execute();
    }

    public void CetakResep(View v){
        Intent cr = new Intent(DetailMR.this, WebViewPDF.class);
        String url_pdfresep = url_rs+"/_api/cetak_resep_px.php?"+
                "no_registrasi="+idRegist+"&kode_resep="+kode_resep;
        cr.putExtra("kategori","1");
        cr.putExtra("url_pdf", url_pdfresep);
        cr.putExtra("title_bar","Resep Dokter");
        cr.putExtra("tindakan","No Resep "+no_resep);
        cr.putExtra("namafile","resep_dokter");
        startActivity(cr);
    }
}