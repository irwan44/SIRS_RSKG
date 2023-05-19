package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditPasienLama extends AppCompatActivity {

    String val_token, no_ktp, url_fotoPasien, nama, usia, gol_darah, alergi, alamat, gender, tgl_lahir, no_hp,
            tmp_lahir, tgl_konvert, jenKelamin, kd_menu;
    EditText et_nama, et_usia, et_goldarah, et_alergi, et_alamat, et_tglLahir, et_tmptLahir, et_ktpPasien, et_nohp;
    TextView txt_info_success, txt_info_failed;
    Button btn_ok_failed, btn_ok_success, fabEdit, fabEditPrivy;
    TextInputLayout edt_tgllahir;
    LinearLayout ln_foto;
    LinearLayoutCompat ln_form;

    //    ImageView img_fotoPasien;
    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //BOTTOM SHEET EDIT FOTO
    CircleImageView img_fotoPasien, cb_editFoto, cb_iconedit;
    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog sheetDialog;
    View  sheetView;
    private byte[] byt_foto;

    SimpleDateFormat df = new SimpleDateFormat("YYY-MM-dd");
    ProgressDialog pDialog;
    ConnectivityManager conMgr;
    private Calendar mCalendar;

    //Spinner Element
    String[] gndr = new String[] {"Laki-laki", "Perempuan"};
    String[] golDarah = new String[] {"A+","A-","B+","B-","AB+","AB-","O-","O+","Belum Diperiksa"};
    AutoCompleteTextView actGender,actgolDarah;

    //Dialog Confirm
    AlertDialog.Builder builder_success, builder_failed, dial_builder;
    AlertDialog dial_success, dial_failed, dial_Foto;
    LayoutInflater inflater;
    View v_success_regist, v_failed_regist, v_delete_foto;

    //URL JSON
    String APIurl = RequestHandler.APIdev;
    public String url_getPasien = APIurl+"get-data-pasien.php";
    public String url_editPasien = APIurl+"edit_pasien_lama.php";
    public String linkJanda  = APIurl+"get-token.php";

    //  =========================================== Start Content =================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pasien_lama);


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

        sheetView = findViewById(R.id.bottom_ubah_foto);
        sheetBehavior   = BottomSheetBehavior.from(sheetView);
        cb_editFoto     = findViewById(R.id.cb_edit_foto);
        cb_iconedit     = findViewById(R.id.btn_openEditFoto);
        img_fotoPasien  = findViewById(R.id.fotoPasien);
        et_nama         = findViewById(R.id.et_namapasien);
        et_usia         = findViewById(R.id.et_usia);
        actGender       = findViewById(R.id.act_gender);
        actgolDarah     = findViewById(R.id.act_goldarah);
        et_alergi       = findViewById(R.id.et_alergi);
        et_alamat       = findViewById(R.id.et_alamat);
        et_tglLahir     = findViewById(R.id.et_tglLahir);
        edt_tgllahir    = findViewById(R.id.edt_tgllahir);
        et_tmptLahir    = findViewById(R.id.et_tmptLahir);
        et_ktpPasien    = findViewById(R.id.et_ktpPasien);
        et_nohp         = findViewById(R.id.et_nohp);
        fabEdit         = findViewById(R.id.fabEdit);
        fabEditPrivy    = findViewById(R.id.fabEditPrivy);
        ln_foto         = findViewById(R.id.frame);
        ln_form         = findViewById(R.id.linearLayoutCompat2);
        getPasien();

//        Bundle kiriman = getIntent().getExtras();
//        if(kiriman != null){
//            kd_menu = kiriman.get("kd_menu").toString();
//        }

//        if(kd_menu.equals("prv")){
//            fabEditPrivy.setVisibility(View.VISIBLE);
//            fabEdit.setVisibility(View.GONE);
//        }else{
//            fabEdit.setVisibility(View.VISIBLE);
//            fabEditPrivy.setVisibility(View.GONE);
//        }

        fabEditPrivy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEditProfile();
            }
        });

        DatePickerDialog.OnDateSetListener datelahir =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH,month);
                mCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        //Dialog Delete Foto
        dial_builder = new AlertDialog.Builder(EditPasienLama.this,R.style.CustomAlertDialog);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ViewGroup vg = findViewById(com.google.android.material.R.id.content);
        inflater = getLayoutInflater();
        v_delete_foto = inflater.inflate(R.layout.dialog_daftar_poli, vg, false);
        Button btn_act_yes = v_delete_foto.findViewById(R.id.btn_ya);
        Button btn_act_no = v_delete_foto.findViewById(R.id.btn_tidak);
        TextView txtAtas = v_delete_foto.findViewById(R.id.txt1);
        TextView txtBawah = v_delete_foto.findViewById(R.id.txt2);
        txtAtas.setText("Apakah anda yakin ingin menghapus foto profile anda ?");
        txtBawah.setVisibility(View.GONE);
        dial_builder.setView(v_delete_foto);
        dial_Foto = dial_builder.create();
        dial_Foto.setCancelable(false);

        btn_act_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                hapusFotoProfile();

            }
        });
        btn_act_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_Foto.dismiss();
            }
        });

        cb_editFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowEditFoto();
            }
        });
        cb_iconedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowEditFoto();
            }
        });

        img_fotoPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowEditFoto();
            }
        });

        edt_tgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar = Calendar.getInstance();
                new DatePickerDialog(EditPasienLama.this, R.style.DialogTheme,datelahir,mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        et_tglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar = Calendar.getInstance();
                new DatePickerDialog(EditPasienLama.this, R.style.DialogTheme,datelahir,mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEditProfile();
            }
        });

        //Dialog Confirm
        builder_success = new AlertDialog.Builder(EditPasienLama.this,R.style.CustomAlertDialog);
        builder_failed = new AlertDialog.Builder(EditPasienLama.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        inflater = getLayoutInflater();
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
        btn_ok_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPasien();
                dial_success.dismiss();

            }
        });
        btn_ok_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_failed.dismiss();
            }
        });

        ArrayAdapter<String> AdaptGender = new ArrayAdapter<>(this, R.layout.dialog_spinner, gndr);
        ArrayAdapter<String> AdaptGoldarah = new ArrayAdapter<>(this, R.layout.dialog_spinner, golDarah);
        actGender.setAdapter(AdaptGender);
        actgolDarah.setAdapter(AdaptGoldarah);
    }

    public void onBackPressed() {
        Intent bck = new Intent(EditPasienLama.this,MainActivity.class);
        startActivity(bck);
    }

    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.getDefault());
        et_tglLahir.setText(dateFormat.format(mCalendar.getTime()));
    }

    private void getPasien() {
        //first getting the values
        final String iniToken   = val_token;
        final String iniKTP  = no_ktp;

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("no_ktp", iniKTP);

                //returing the response
                return requestHandler.requestData(url_getPasien, "POST", "application/json; charset=utf-8", "X-Api-Token",
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
                ln_form.setVisibility(View.VISIBLE);
                ln_foto.setVisibility(View.VISIBLE);

                try {//converting response to json object
                    JSONObject jb = new JSONObject(s);
                    //if no error in response
                    if (jb.getString("code").equals("200")) {
                        JSONArray jr = jb.getJSONArray("res");
                        for (int a = 0; a < jr.length(); a++) {
                            JSONObject obj = jr.getJSONObject(a);
                            nama = obj.getString("nama_pasien");
                            no_hp = obj.getString("no_hp");
                            usia = obj.getString("umur");
                            alergi = obj.getString("alergi");
                            gol_darah = obj.getString("gol_darah");
                            gender = obj.getString("gender");
                            alamat = obj.getString("alamat");
                            tgl_lahir = obj.getString("tgl_lhr");
                            tmp_lahir = obj.getString("tmp_lhr");
                            url_fotoPasien = obj.getString("foto_pasien");
                            et_ktpPasien.setText(no_ktp);
                            et_nama.setText(nama);
                            byt_foto = url_fotoPasien.getBytes(StandardCharsets.UTF_8);

                            if(no_ktp.equals("3174586231698546")){
                                img_fotoPasien.setImageResource(R.drawable.foto_bos);
                            }else{
                                if (url_fotoPasien.equals("null")) {
                                    img_fotoPasien.setImageResource(R.drawable.profile22);
                                } else {
                                    Glide.with(EditPasienLama.this).load(url_fotoPasien).into(img_fotoPasien);
                                }
                            }

                            if(no_hp.equals("null") || no_hp.equals("")){
                                et_nohp.setText(" - ");
                            }else{
                                et_nohp.setText(no_hp);
                            }
                            if(usia.equals("null")){
                                et_usia.setText(" - ");
                            }else {
                                et_usia.setText(usia+" Tahun");
                            }
                            if(gender.equals("null") || gender.equals("")){
                                actGender.setHint("Jenis Kelamin");
                            }else if(gender.equals("L")){
                                actGender.setHint("Laki-laki");
                            }else if(gender.equals("P")){
                                actGender.setHint("Perempuan");
                            }
                            if(gol_darah.equals("null") || gol_darah.equals("")){
                                actgolDarah.setHint("Gol. Darah");
                            }else{
                                actgolDarah.setHint(gol_darah);
                            }
                            if(alergi.equals("null")){
                                et_alergi.setText(" - ");
                            }else {
                                et_alergi.setText(alergi);
                            }
                            if(alamat.equals("null")){
                                et_alamat.setText(" - ");
                            }else {
                                et_alamat.setText(alamat);
                            }
//                            if(gol_darah.equals("null")){
//                                et_goldarah.setText("-");
//                            }else {
//                                et_goldarah.setText(gol_darah);
//                            }
                            if(tgl_lahir.equals("null") || tgl_lahir.equals("")){
                                et_tglLahir.setText(" - ");
                            }else {
                                Date tgl = null;
                                try {
                                    tgl = inputFormat.parse(tgl_lahir);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                tgl_konvert = outputFormat.format(tgl);
                                et_tglLahir.setText(tgl_konvert);
                            }
                            if(tmp_lahir.equals("null")){
                                et_tmptLahir.setText(" - ");
                            }else {
                                et_tmptLahir.setText(tmp_lahir);
                            }
                        }
                    }else if(jb.getString("code").equals("500")){
//                        dial_failed.show();
                        img_fotoPasien.setImageResource(R.drawable.profile22);
                        actGender.setHint("Jenis Kelamin");
                        actgolDarah.setHint("GOl. Darah");
                        et_nohp.setText("Data not found");
                        et_nama.setText("Data not found");
                        et_usia.setText("Data not found");
                        et_alergi.setText("Data not found");
                        et_alamat.setText("Data not found");
//                        et_goldarah.setText("Data not found");
                        et_tglLahir.setText("Data not found");
                        et_tmptLahir.setText("Data not found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        masukPakEko pl = new masukPakEko();
        pl.execute();
    }

    private void ShowEditFoto(){
        View v = getLayoutInflater().inflate(R.layout.swipe_ganti_foto, null);
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        (v.findViewById(R.id.cv_hapusFoto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dial_Foto.show();
            }
        });

        (v.findViewById(R.id.cv_open_cam)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ci = new Intent(EditPasienLama.this, CropImage.class);
                ci.putExtra("pick_kode", 1);
                ci.putExtra("urlFoto", "");
                startActivity(ci);
            }
        });

        (v.findViewById(R.id.cv_open_galery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ci = new Intent(EditPasienLama.this, CropImage.class);
                ci.putExtra("pick_kode", 2);
                ci.putExtra("urlFoto", "");
                startActivity(ci);
            }
        });

        sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(v);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            sheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        sheetDialog.show();
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sheetDialog = null;
            }
        });
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }

    private void sendEditProfile() {

        String gol_Darah;
        final String isiToken       = val_token;
        final String iniKTP         = no_ktp;
        final String nohp           = et_nohp.getText().toString();
        final String namaPasien     = et_nama.getText().toString();
        final String usiaPasien     = et_usia.getText().toString();
        final String alergiPasien   = et_alergi.getText().toString();
        final String alamatPasien   = et_alamat.getText().toString();
        final String tglLahir       = et_tglLahir.getText().toString();
        final String tmpLahir       = et_tmptLahir.getText().toString();
        String jekel                = actGender.getText().toString();
        if(jekel.equals("Laki-laki")){
            jenKelamin = "L";
        }else if(jekel.equals("Perempuan")){
            jenKelamin = "P";
        }

        String goldarah           = actgolDarah.getText().toString();
        if(goldarah.equals("Belum Diperiksa")) {
            gol_Darah = "---";
        }else{
            gol_Darah = goldarah;
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
                params.put("no_ktp", iniKTP);
                params.put("namaPasien", namaPasien);
                params.put("no_HP", nohp);
//                params.put("umurPasien", usiaPasien);
                params.put("goldarah", gol_Darah);
                params.put("tanggal_lhr", tglLahir);
                params.put("tempat_lhr", tmpLahir);
                params.put("gender", jenKelamin);
                params.put("alergi", alergiPasien);
                params.put("alamat", alamatPasien);

                //returing the response
                return requestHandler.requestData(url_editPasien, "POST", "application/json; charset=utf-8", "X-Api-Token",
                        isiToken, "X-Px-Key", "", params);
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
                    if (obj.getString("code").equals("200")) {
                        dial_success.show();
                        txt_info_success.setText(obj.getString("msg"));
                        Login updatelogin = new Login(namaPasien,iniKTP,url_fotoPasien);
                        AppController.getInstance(getApplicationContext()).userLogin(updatelogin);
//                        if(kd_menu.equals("prv")){
//                            Intent rp = new Intent(EditPasienLama.this, FormPrivy.class);
//                            startActivity(rp);
//                        }else {
                            getPasien();
//                        }
                    } else {
                        dial_failed.show();
                        txt_info_failed.setText(obj.getString("msg"));
                        getPasien();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}