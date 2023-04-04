package averin.sirs_rskg.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Bundle;

import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;

//LIBRARY FOR SCAN QR CODE
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ScanQRCode extends AppCompatActivity {
//    private ZXingScannerView mScannerView;

    ImageView bg_qrcode;
    private CodeScanner mCodeScanner;
    private CodeScannerView scanview;

    private CameraManager cm_flash;
    private String getCameraID, hsl_qrcode;
    private ToggleButton flashOnOff;

    ProgressDialog pDialog;
    String val_token, no_ktp, regId, nm_dokter, no_antrian, tgl_antri, jam_awal, jam_akhir, status_antri, nm_klinik, nm_bag, stat_px, jam_konvert;
    TextView txt_info_success, txt_info_failed;
    Button btn_ok_success, btn_ok_failed;

    //Dialog Confirm
    AlertDialog.Builder builder_success, builder_failed, dial_builder, builder_ask_px;
    AlertDialog dial_success, dial_failed, dial_login, dial_newPX;
    LayoutInflater inflater;
    View v_success_regist, v_failed_regist, v_ask_newPX, dialogView;

    //URL GET DATA FROM DB
    String APIurl =RequestHandler.APIdev;
    public String situsPerawan    = APIurl+"get-data-klinik.php";
    public String prosesScan      = APIurl+"scan_antrian_klinik.php";
    public String linkJanda       = APIurl+"get-token.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
//        bg_qrcode = findViewById(R.id.ivBgContent);
//        bg_qrcode.bringToFront();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            regId        = extras.get("regId").toString();
            no_antrian     = extras.get("noAntrian").toString();
            nm_dokter    = extras.get("nma_dokter").toString();
            tgl_antri    = extras.get("tgl_antri").toString();
            jam_awal     = extras.get("jam_awal").toString();
            jam_akhir    = extras.get("jam_akhir").toString();
            status_antri = extras.get("status_antri").toString();
            nm_klinik    = extras.get("nm_klinik").toString();
            nm_bag       = extras.get("nm_bag").toString();
            stat_px       = extras.get("stat_px").toString();
        }

        CodeScannerView scannerView = findViewById(R.id.scannerView);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hsl_qrcode = result.getText();
                        cekToken(hsl_qrcode);
                    }
                });
            }
        });
        checkCameraPerm();

//Dialog Confirm
        ViewGroup viewGroup = findViewById(android.R.id.content);
        builder_success = new AlertDialog.Builder(ScanQRCode.this,R.style.CustomAlertDialog);
        builder_failed = new AlertDialog.Builder(ScanQRCode.this,R.style.CustomAlertDialog);
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

//        mScannerView = new ZXingScannerView(this);
//        setContentView(mScannerView);
//        flashOnOff = findViewById(R.id.tgl_senter);
//        cm_flash = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
//                PackageManager.PERMISSION_GRANTED);

        btn_ok_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_success.dismiss();
                Intent i = new Intent(ScanQRCode.this, AntrianDetail.class);
                i.putExtra("regId", regId);
                i.putExtra("noAntrian", no_antrian);
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
        });

        btn_ok_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_failed.dismiss();
                Intent i = new Intent(ScanQRCode.this, AntrianDetail.class);
                i.putExtra("regId", regId);
                i.putExtra("tgl_antri", tgl_antri);
                i.putExtra("jam_awal", jam_awal);
                i.putExtra("jam_akhir", jam_akhir);
                i.putExtra("status_antri", status_antri);
                i.putExtra("nm_klinik", nm_klinik);
                i.putExtra("nm_bag", nm_bag);
                startActivity(i);
            }
        });

//        spinner_test = (Spinner) findViewById(R.id.test_spin);
        Token token = AppController.getInstance(this).isiToken();
        Login lg    = AppController.getInstance(this).getPasien();
        val_token = String.valueOf(token.gettoken());
        no_ktp    = String.valueOf(lg.getKTP_pasien());
        // Exception is handled, because to check whether
        // the camera resource is being used by another
        // service or not.
//        try {
//            // O means back camera unit,
//            // 1 means front camera unit
//            getCameraID = cm_flash.getCameraIdList()[0];
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }

    }

    //    FUNGSI SCANNER QR CODE
    @Override
    protected void onResume() {
        super.onResume();
        checkCameraPerm();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void checkCameraPerm(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        mCodeScanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest,
                                                                   PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public void onBackPressed() {
        Intent ad = new Intent(ScanQRCode.this,AntrianDetail.class);
        ad.putExtra("regId", regId);
        startActivity(ad);
    }

    public void cekToken(final String str_qrcode) {
        //first getting the values
        final String isiToken    = val_token;
        final String ktp         = no_ktp;
        final String APIurl      = RequestHandler.APIdev;

        //if everything is fine
        class cekToken extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("no_ktp", ktp);

                //returing the response
                return requestHandler.requestData(APIurl+"cek-data-px.php", "POST", "application/json; charset=utf-8", "X-Api-Token",
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
                        ambilToken(str_qrcode);
                    } else if(obj.getString("code").equals("200")){
                        GetAntriPoli(str_qrcode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        cekToken pl = new cekToken();
        pl.execute();
    }

    public void ambilToken(final String str_qrcode) {
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
                        val_token = String.valueOf(obj.getString("token"));
                        GetAntriPoli(str_qrcode);

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

    private void GetAntriPoli(final String str_qrcode) {
        //first getting the values
        final String iniToken   = val_token;
        final String kdKlinik = str_qrcode;
        final String noKTP = no_ktp;
        final String IDreg = regId;

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("kode_klinik", kdKlinik);
                params.put("no_ktp", noKTP);
                params.put("idRegKlinik", IDreg);

                //returing the response
                return requestHandler.requestData(prosesScan, "POST", "application/json; charset=utf-8", "X-Api-Token",
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
                        dial_success.show();
                        txt_info_success.setText(obj.getString("msg"));

                    }else if(obj.getString("code").equals("500")) {
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

    public void BalikAntrian(View v){
        Intent i = new Intent(ScanQRCode.this, AntrianDetail.class);
        i.putExtra("regID", regId);
        i.putExtra("tgl_antri", tgl_antri);
        i.putExtra("jam_awal", jam_awal);
        i.putExtra("jam_akhir", jam_akhir);
        i.putExtra("status_antri", status_antri);
        i.putExtra("nm_klinik", nm_klinik);
        i.putExtra("nm_bag", nm_bag);
        startActivity(i);

    }
}