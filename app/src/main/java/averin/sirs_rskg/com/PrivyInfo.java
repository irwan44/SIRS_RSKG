package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PrivyInfo extends AppCompatActivity {
    CardView btn_batal;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privy_info);

        btn_batal       = findViewById(R.id.btn_batal);

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PrivyInfo.this, RegistPasienLama.class);
                startActivity(i);
            }

        });
    }

    @Override
    public void onBackPressed() {
    }

}