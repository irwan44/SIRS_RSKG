package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistPasienLama extends AppCompatActivity {

    String val_token, idk, kd_klinik, nm_klinik, url_fotoPasien;
    TextView tv_namapasien, tv_nomr;
    String APIurl = RequestHandler.APIdev;
    CircleImageView img_fotoPasien;
    CardView btn_privy;
    ImageView btn_back;
    Button btn_editprofile, btn_changepass, btnLogout;
    private byte[] byt_Foto;

    //Dialog Confirm
    AlertDialog.Builder dial_builder;
    AlertDialog dial_logout;
    LayoutInflater inflater;
    View dialogView;

    public String postUrl = APIurl+"get-klinik-detail.php";
    public String linkJanda  = APIurl+"get-token.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_pasien_lama);

       /* //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar LabToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(LabToolbar);
//        LabToolbar.setLogoDescription(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LabToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        img_fotoPasien  = findViewById(R.id.fotoPasien);
        tv_namapasien   = findViewById(R.id.txt_namaPasien);
        tv_nomr         = findViewById(R.id.txt_no_mr);
//        btn_privy       = findViewById(R.id.btn_privy);
        btn_editprofile = findViewById(R.id.btn_editprofile);
//        btn_changepass  = findViewById(R.id.btn_changepass);
        btnLogout      = findViewById(R.id.btnLogout);
        btn_back        =findViewById(R.id.btn_back);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idk = extras.get("idk").toString();
        }

        Token token = AppController.getInstance(this).isiToken();
        Login lg = AppController.getInstance(this).getPasien();
        val_token = String.valueOf(token.gettoken());
        url_fotoPasien = String.valueOf(lg.getFoto_pasien());
        tv_namapasien.setText(String.valueOf(lg.getNama_pasien()));
        tv_nomr.setText(String.valueOf(lg.getKTP_pasien()));
        String noKTP = tv_nomr.getText().toString();

        if(noKTP.equals("3174586231698546")) {
            img_fotoPasien.setImageResource(R.drawable.foto_bos);
        }else {
            if (url_fotoPasien.equals("null")) {
                img_fotoPasien.setImageResource(R.drawable.profile22);
            } else {
                Glide.with(RegistPasienLama.this).load(url_fotoPasien).into(img_fotoPasien);
            }
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistPasienLama.this, MainActivity.class);
                startActivity(i);
            }

        });

        btn_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistPasienLama.this, EditPasienLama.class);
                startActivity(i);
            }

        });
//        btn_changepass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(RegistPasienLama.this, ChangePassword.class);
//                startActivity(i);
//            }
//
//        });
//        btn_privy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(RegistPasienLama.this, PrivyInfo.class);
//                startActivity(i);
//            }
//
//        });

        //Dialog ask login
        dial_builder = new AlertDialog.Builder(RegistPasienLama.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(com.google.android.material.R.id.content);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_ask_logout, viewGroup, false);
        Button btn_act_logout = dialogView.findViewById(R.id.btn_logout);
        Button btn_act_cancel = dialogView.findViewById(R.id.btn_cancel);
        dial_builder.setView(dialogView);
        dial_logout = dial_builder.create();
        dial_logout.setCancelable(false);
        //setOnclick listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_logout.show();
            }
        });
        btn_act_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance(getApplicationContext()).logout();
                dial_logout.dismiss();

            }
        });
        btn_act_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_logout.dismiss();
            }
        });
    }

    public void openDial(View v) {
        dial_logout.show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegistPasienLama.this, MainActivity.class);
        startActivity(i);
    }
}