package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen2 extends AppCompatActivity {

    private int waktu_loading=3000;
    private static final String TAG = SplashScreen2.class.getSimpleName();
    public static final String TAG_CODE                 = "code";
    public static final String TAG_KodeApi              = "KodeApi";
    public static final String TAG_KeyApi               = "KeyApi";
    public static final String TAG_KeyCode              = "KeyCode";
    public static final String TAG_MESSAGE              = "message";
    public static final String TAG_TOKEN                = "token";
    public static final String my_shared_preferences    = "my_shared_preferences";
    public static final String session_status           = "session_status";
    private String url = "https://tel.d-medis.id/api/v1/get-token.php";
    int code;
    SharedPreferences sharedpreferences;
    //    AwesomeText ShowHidePassword;
    Boolean session = false, pwd_status = true;
    String token, KodeApi, KeyApi, KeyCode, tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                gettoken();
//                Intent home = new Intent(SplashScreen.this, MainActivity.class);
//                startActivity(home);
                finish();
            }
        },waktu_loading);
    }

    private void gettoken() {
        final String KodeApi = "MUSA";
        final String KeyApi = "@@TTWYYW";
        final String KeyCode = "602f07f23fc390cfd4461b268f95eddfbd4fc87d9b313b64a943801b5e4c5b12";

//        class getToken extends AsyncTask<Void, Void, String> {
//            ProgressBar progressBar;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
                //displaying the progress bar while user registers on the server
//                progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
//            }

//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
                //hiding the progressbar after completion
//                progressBar.setVisibility(View.GONE);

//                try {
                    //converting response to json object
//                    JSONObject obj = new JSONObject(s);

                    //if no error in response
//                    if (obj.getString("code").equals("200")) {

                        //getting the user from the response
//                        JSONObject tokenJson = obj.getJSONObject("");

                        //creating a new user object
//                        Token token = new Token(
//                                tokenJson.getString("token")
//                        );

                        //storing the user in shared preferences
//                        AppController.getInstance(getApplicationContext()).getToken(token);

                        //starting the profile activity
//                        finish();
//                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    } else {
//                        Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }

//            @Override
//            protected String doInBackground(Void... voids) {
//                //creating request handler object
//                RequestHandler requestHandler = new RequestHandler();
//
//                //creating request parameters
//                HashMap<String, String> params = new HashMap<>();
//                params.put("KodeApi", KodeApi);
//                params.put("KeyApi", KeyApi);
//                params.put("KeyCode", KeyCode);
//
//                //returing the response
//                return requestHandler.sendPostRequest(url, params, params);
//            }
//        }
//        getToken gt = new getToken();
//        gt.execute();
    }
}