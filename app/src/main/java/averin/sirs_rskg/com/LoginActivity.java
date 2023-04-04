package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;
import averin.sirs_rskg.com.Ui.base64;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String TAG_USERNAME             = "username";
    public static final String TAG_KODE                 = "kode";
    public static final String TAG_PASSWORD             = "password";
    public  static final String TAG_EMAIL               = "email";
    public  static final String TAG_SUCCESS             = "success";
    public static final String TAG_MESSAGE              = "message";
    public static final String my_shared_preferences    = "my_shared_preferences";
    public static final String session_status           = "session_status";

    String APIurl = RequestHandler.APIdev;
    public String postUrl= APIurl+"px-akses.php";
    public String urlKlinikDetail = APIurl+"get-klinik-detail.php";
    public String linkJanda  = APIurl+"get-token.php";
    // Sending side
    Button btn_login, btn_ok_failed;
    /*ImageButton btn_clearuser, btn_clearpass, btn_showhide_pass;*/
    String val_token, email_px, pass_px;
    EditText txt_token, txt_username, txt_password, txt_email;
    TextView txt_info_failed;
    ConnectivityManager conMgr;
    Boolean pwd_stat = true;

    //SHow Hide Keyboarad
    ConstraintLayout layAwal;
    ImageView img_login;

    //Dialog Confirm
    AlertDialog.Builder builder_success, builder_failed, dial_builder;
    AlertDialog dial_success, dial_failed, dial_login;
    LayoutInflater inflater;
    View v_success_regist, v_failed_regist, dialogView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        btn_login           = findViewById(R.id.btn_login);
        /*btn_clearuser       = findViewById(R.id.tmbl_clear);
        btn_clearpass       = findViewById(R.id.tmbl_clear_pass);
        btn_showhide_pass   = findViewById(R.id.showhidden_pass);*/
//        txt_email = findViewById(R.id.txt_email);
        txt_username        = findViewById(R.id.txt_username);
        txt_password        = findViewById(R.id.txt_password);
        layAwal             = findViewById(R.id.layawal);

        //CEK KONEKSI INTERNET
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

        Bundle kiriman = getIntent().getExtras();
        if(kiriman != null){
            email_px = kiriman.get("email_px").toString();
            pass_px     = kiriman.get("pass_px").toString();
        }
        if(email_px != null){
            txt_username.setText(email_px);
        }

        if(pass_px != null){
            txt_password.setText(pass_px);
        }

        img_login = findViewById(R.id.imageView);
        txt_username.requestFocus();
        layAwal.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                layAwal.getWindowVisibleDisplayFrame(r);
                int screenHeight = layAwal.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
//                            Toast.makeText(LoginActivity.this,"Keyboard is closed",Toast.LENGTH_SHORT).show();
                } else {
//                            Toast.makeText(LoginActivity.this,"keyboard showing",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Dialog Confirm
        ViewGroup viewGroup = findViewById(android.R.id.content);
        builder_failed = new AlertDialog.Builder(LoginActivity.this,R.style.CustomAlertDialog);
        inflater = getLayoutInflater();
        v_failed_regist = inflater.inflate(R.layout.dialog_failed_regist, viewGroup, false);
        btn_ok_failed = v_failed_regist.findViewById(R.id.btn_oke_failed);
        txt_info_failed = v_failed_regist.findViewById(R.id.txt_info_failed);
        builder_failed.setView(v_failed_regist);
        dial_failed = builder_failed.create();
        dial_failed.setCancelable(false);

        //getting the current user
        Token token = AppController.getInstance(this).isiToken();
        val_token = String.valueOf(token.gettoken());
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                masukPakEko();
            }
        });
       /* btn_clearuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_username.getText().clear();
            }
        });*/
        /*btn_clearpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_password.getText().clear();
            }
        });
        btn_showhide_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pwd_stat) {
                    txt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwd_stat = false;
                    btn_showhide_pass.setImageResource(R.drawable.ic_eye_view_pass);
                    txt_password.setSelection(txt_password.length());
                }else{
                    txt_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    pwd_stat = true;
                    btn_showhide_pass.setImageResource(R.drawable.ic_eye_hidden_pass);
                    txt_password.setSelection(txt_password.length());
                }
            }
        });*/


        btn_ok_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_failed.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void cekToken() {
        //first getting the values
        final String isiToken    = val_token;
        final String idk         = "NDcx";

        //if everything is fine
        class cekToken extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id", idk);

                //returing the response
                return requestHandler.requestData(urlKlinikDetail, "POST", "application/json; charset=utf-8", "X-Api-Token",
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
                                obj.getString("token")
                        );

                        //storing the user in shared preferences
                        AppController.getInstance(getApplicationContext()).getToken(token);
                        val_token = obj.getString("token");
                        masukPakEko();

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

    private void masukPakEko() {
        //first getting the values
//        final String email      = txt_email.getText().toString();
        final String user       = txt_username.getText().toString();
        final String pass       = txt_password.getText().toString();
        final String iniToken   = val_token;

        //For Encrypt Password
        byte[] base64data;
        String base64str;
        String MD5_Hash_String;

        if (TextUtils.isEmpty(user)) {
            txt_username.setError("Silahkan masukkan email / No. Telephone anda");
            txt_username.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            txt_password.setError("Silahkan masukkan password anda");
            txt_password.requestFocus();
            return;
        }

        //Encrpt to MD%
        MD5_Hash_String = base64.md5(pass);
        //Encrypt to base64
        base64data = MD5_Hash_String.getBytes(StandardCharsets.UTF_8);
        base64str = Base64.encodeToString(base64data, Base64.DEFAULT);

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
//                params.put("Mail", email);
                params.put("User", user);
                params.put("Pass", MD5_Hash_String);
                return requestHandler.postRequest(postUrl, "POST", "X-Api-Token", iniToken, params);
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
                    if (obj.getString("code").equals("200")) {
                        //getting the user from the response
                        JSONObject lj = obj.getJSONObject("res");

                        //creating a new user object
                        Login login = new Login(
                                lj.getString("nama_pasien"),
//                                lj.getString("no_mr"),
                                lj.getString("no_ktp"),
                                lj.getString("foto_pasien")
                        );

                        //storing the user in shared preferences
                        AppController.getInstance(getApplicationContext()).userLogin(login);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else if(obj.getString("code").equals("500")){
//                        Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                        ambilToken();
                    }else if(obj.getString("code").equals("300")){
//                        Intent ra = new Intent(LoginActivity.this,RegistrasiActivity.class);
//                        ra.putExtra("userName", user);
//                        ra.putExtra("pass", pass);
//                        startActivity(ra);
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
    public void hidenUnderBarInEditText(EditText editText) {
        editText.setBackgroundResource(android.R.color.transparent);
    }

    public void RegistrasiAkun(View v){
        Intent i = new Intent(LoginActivity.this, RegistrasiActivity.class);
        startActivity(i);
    }
    public void SkipLogin(View v){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }
    public void LupaPassword(View v){
        Intent i = new Intent(LoginActivity.this, LupaPassword.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {

    }
}