package averin.sirs_rskg.com;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Adapter.SpinnerAdapter;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Model.isiSpinner;
import averin.sirs_rskg.com.Ui.AppController;
import averin.sirs_rskg.com.Ui.base64;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;

import android.net.Uri;
import android.provider.Settings;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrasiActivity extends AppCompatActivity{

//    InputViewModel inputViewModel;
    Toolbar toolbar;
    Spinner gender, go_darah;
    TextView txt_info_success, txt_info_failed;
    EditText inputNIK, inputNama, inputEmail, inputPassword, inputConfirmPass, inputHP, inputTgllahir,
            inputAlamat, inputAlergi, inputTempatlahir, inputGoldarah;
    ImageView imageKTP, btnGallery, btnCamera;
    Button fabSave;
    String val_token, jenKelamin;
    String userName = null;
    String pass = null;
    String APIurl = RequestHandler.APIdev;
    Button btn_ok_failed, btn_ok_success;
    HashMap<String, String> params = new HashMap<>();

    //Spinner Element
    String[] gndr = new String[]{"Laki-laki", "Perempuan"};
    String[] gol_darah = new String[] {"A+","A-","B+","B-","AB+","AB-","O-","O+","Belum Diperiksa"};
    AutoCompleteTextView actGender, actgoldarah;
    ProgressDialog pDialog;
    List<isiSpinner> listisian = new ArrayList<isiSpinner>();
    SpinnerAdapter adapter;

    //Dialog Confirm
    AlertDialog.Builder builder_success, builder_failed, dial_builder;
    AlertDialog dial_success, dial_failed, dial_login;
    LayoutInflater inflater;
    View v_success_regist, v_failed_regist, dialogView;

    private Calendar mCalendar;
    private static final int STORAGE_PERMISSION_CODE = 123;
    public String postUrl = APIurl + "post-daftar-px-baru.php";
    public String linkJanda  = APIurl+"get-token.php";
    public String urlGolDarah = APIurl+"get-golDarah.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_form);

        toolbar       = findViewById(R.id.toolbar);
        inputNIK      = findViewById(R.id.inputNIK);
        inputNama     = findViewById(R.id.inputNama);
        inputEmail    = findViewById(R.id.inputEmail);
        inputHP       = findViewById(R.id.inputHP);
        inputAlamat   = findViewById(R.id.inputAlamat);
        inputAlergi   = findViewById(R.id.inputAlergi);
        inputTgllahir = findViewById(R.id.inputTgllahir);
        inputTempatlahir = findViewById(R.id.inputTempatlahir);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPass = findViewById(R.id.inputConfirmPassword);
        fabSave = findViewById(R.id.fabSave);
        actGender = findViewById(R.id.act_gender);
        actgoldarah = findViewById(R.id.act_goldarah);

        Bundle kiriman = getIntent().getExtras();
        if (kiriman != null) {
            userName = kiriman.get("userName").toString();
            pass = kiriman.get("pass").toString();
        }
        if (userName != null) {
            if (userName.matches("\\d+(?:\\.\\d+)?")) {
                inputHP.setText(userName);
            } else {
                inputEmail.setText(userName);
            }
        }

        if (pass != null) {
            inputPassword.setText(pass);
        }

        actGender.setHint("-- Pilih Jenis Kelamin --");
        actgoldarah.setHint("-- Pilih Golongan Darah --");
        ArrayAdapter<String> AdaptGender = new ArrayAdapter<>(this, R.layout.dialog_spinner, gndr);
        ArrayAdapter<String> AdaptGoldarah = new ArrayAdapter<>(this, R.layout.dialog_spinner, gol_darah);
        actGender.setAdapter(AdaptGender);
        actgoldarah.setAdapter(AdaptGoldarah);

        //Dialog Confirm
        ViewGroup viewGroup = findViewById(android.R.id.content);
        builder_success = new AlertDialog.Builder(RegistrasiActivity.this, R.style.CustomAlertDialog);
        builder_failed = new AlertDialog.Builder(RegistrasiActivity.this, R.style.CustomAlertDialog);
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

        DatePickerDialog.OnDateSetListener datelahir =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH,month);
                mCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        inputTgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar = Calendar.getInstance();
                new DatePickerDialog(RegistrasiActivity.this, R.style.DialogTheme,datelahir,mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Token tkn = AppController.getInstance(this).isiToken();
        val_token = String.valueOf(tkn.gettoken());
//        setInitLayput();
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambilToken();
            }
        });

        btn_ok_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lg = new Intent(RegistrasiActivity.this, LoginActivity.class);
                lg.putExtra("email_px", inputEmail.getText().toString());
                lg.putExtra("pass_px", inputPassword.getText().toString());
                startActivity(lg);
                dial_success.dismiss();

            }
        });
        btn_ok_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_failed.dismiss();
            }
        });
    }

    public void onBackPressed() {
        Intent startMain = new Intent(RegistrasiActivity.this, LoginActivity.class);
        startActivity(startMain);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }

        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.getDefault());
        inputTgllahir.setText(dateFormat.format(mCalendar.getTime()));
    }

    public void ambilToken() {
        //first getting the values
        final String KodeApi    = "MUSA";
        final String KeyApi     = "@@TTWYYW";
        final String KeyCode    = "602f07f23fc390cfd4461b268f95eddfbd4fc87d9b313b64a943801b5e4c5b12";

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
                return requestHandler.reqToken(APIurl+"get-token.php", params);
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
                                obj.getString("token")
                        );

                        //storing the user in shared preferences
                        AppController.getInstance(getApplicationContext()).getToken(token);
                        val_token = obj.getString("token");
                        masukBaru();

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

    private void masukBaru() {
        //first getting the values

        String gol_Darah;
        final String iniToken           = val_token;
        final String nama_pasien        = inputNama.getText().toString();
        final String email              = inputEmail.getText().toString();
        final String no_ktp             = inputNIK.getText().toString();
        final String no_hp              = inputHP.getText().toString();
        final String password           = inputPassword.getText().toString();
        final String cPass              = inputConfirmPass.getText().toString();
        final String alamat             = inputAlamat.getText().toString();
        final String alergi             = inputAlergi.getText().toString();
        final String tanggal_lahir      = inputTgllahir.getText().toString();
        final String tempat_lahir       = inputTempatlahir.getText().toString();
        String jekel                    = actGender.getText().toString();
        if(jekel.equals("Laki-laki")){
            jenKelamin = "L";
        }else if(jekel.equals("Perempuan")){
            jenKelamin = "P";
        }

        String goldarah           = actgoldarah.getText().toString();
        if(goldarah.equals("Belum Diperiksa")) {
            gol_Darah = "---";
        }else{
            gol_Darah = goldarah;
        }

        //Encrpt to MD5
        String MD5_Hash_String;
        MD5_Hash_String = base64.md5(password);

        if(!password.equals(cPass)){
            inputConfirmPass.setError("Password tidak sesuai");
            inputConfirmPass.requestFocus();
            return;
        }

        if(no_ktp.length() < 16){
            inputNIK.setError("No KTP harus 16 digit");
            inputNIK.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nama_pasien)) {
            inputNama.setError("Silahkan input nama anda");
            inputNama.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Silahkan input email dengan benar");
            inputEmail.requestFocus();
            return;
        }

        if (jekel.equals("Jenis Kelamin")) {
            actGender.setError("Silahkan pilih jenis kelamin anda");
            actGender.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(alamat)) {
            inputAlamat.setError("Silahkan input alamat anda");
            inputAlamat.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tanggal_lahir)) {
            inputTgllahir.setError("Silahkan input tgl lahir anda");
            inputTgllahir.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tempat_lahir)) {
            inputTempatlahir.setError("Silahkan input tempat lahir anda");
            inputTempatlahir.requestFocus();
            return;
        }

        if (no_hp.length() < 10) {
            inputHP.setError("Masukkan No. HP dengan benar");
            inputHP.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Silahkan input password anda");
            inputPassword.requestFocus();
            return;
        }

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
//                HashMap<String, String> params = new HashMap<>();
                params.put("nama_pasien", nama_pasien);
                params.put("email", email);
                params.put("no_ktp", no_ktp);
                params.put("jenis_kelamin", jenKelamin);
                params.put("tanggal_lahir", tanggal_lahir);
                params.put("tempat_lahir", tempat_lahir);
                params.put("no_hp", no_hp);
                params.put("alamat", alamat);
                params.put("alergi", alergi);
                params.put("golongan_darah", gol_Darah);
                params.put("password", password);

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
                    if (obj.getString("code").equals("200")) {
                        dial_success.show();
                        txt_info_success.setText(obj.getString("msg"));
                    } else {
                        dial_failed.show();
                        txt_info_failed.setText(obj.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        masukPakEko pl = new masukPakEko();
        pl.execute();
    }

    public void balikLogin(View v){
        Intent lg = new Intent(RegistrasiActivity.this, LoginActivity.class);
        lg.putExtra("email_px","");
        lg.putExtra("pass_px","");
        startActivity(lg);
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