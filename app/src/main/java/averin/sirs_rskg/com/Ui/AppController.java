package averin.sirs_rskg.com.Ui;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.LoginActivity;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.R;

public class AppController extends Application {

//    public static final String TAG = AppController.class.getSimpleName();
//    private RequestQueue mRequestQueue;
//    private static AppController mInstance;

    private static final String SHARED_PREF_NAME    = "simplifiedcodingsharedpref";
    //    public static final String TAG_PX_KEY           = "x-px-key";
    public static final String TAG_TOKEN            = "token";
    public static final String TAG_NAMAPASIEN       = "nama_pasien";
    public static final String TAG_NOMR             = "no_mr";
    public static final String TAG_NOKTP            = "no_ktp";
    public static final String TAG_FOTOPASIEN       = "foto_pasien";
    public static final String np                   = "nama_perusahaan";
    public static final String em                   = "email";

    private static AppController mInstance;
    private static Context mCtx;

    private AppController(Context context) {
        mCtx = context;
    }

    public static synchronized AppController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppController(context);
        }
        return mInstance;
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
                        //starting the profile activity
//                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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

    //method to let the user login
    //this method will store the token in shared preferences
    public void getToken(Token token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token.gettoken());
        editor.apply();
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(Login login) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_NAMAPASIEN, login.getNama_pasien());
        editor.putString(TAG_NOKTP, login.getKTP_pasien());
        editor.putString(TAG_FOTOPASIEN, login.getFoto_pasien());
//        editor.putString(TAG_PX_KEY, login.get_px_key());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_NOKTP, null) != null;
    }

    //this method will give the logged in user
    public Token isiToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Token(
                sharedPreferences.getString(TAG_TOKEN, null)
        );
    }

    public  Login getPasien() {
        SharedPreferences spr = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Login(
//                spr.getString(TAG_PX_KEY, null)
                spr.getString(TAG_NAMAPASIEN, null),
//                spr.getString(TAG_NOMR, null),
                spr.getString(TAG_NOKTP, null),
                spr.getString(TAG_FOTOPASIEN, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}