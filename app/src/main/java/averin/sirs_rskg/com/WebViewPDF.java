package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WebViewPDF extends AppCompatActivity {

    PDFView viewpdf;
    String val_token, no_ktp, url_pdf, title_bar, tindakan, namafile;
    TextView txt_title_bar, txt_namatindakan, txt_askPX_atas, txt_askPX_bawah;
    ConnectivityManager conMgr;
    ProgressBar prgbar;

    //Download PDF
    ImageButton btn_savepdf;
    private File filePath;

    //Dialog Confirm
    AlertDialog.Builder builder_ask_px;
    AlertDialog dial_PX;
    LayoutInflater inft;
    Button btn_cancel_px, btn_yes_px;
    View v_ask_PX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_pdf);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMgr.getActiveNetworkInfo() != null
            && conMgr.getActiveNetworkInfo().isAvailable()
            && conMgr.getActiveNetworkInfo().isConnected()){
            //Network Active and Normal
        }else{
            Toast.makeText(getApplicationContext(), "Please check your internet connection",
                    Toast.LENGTH_LONG).show();
        }

        Bundle kiriman = getIntent().getExtras();
        if(kiriman != null){
            url_pdf = kiriman.get("url_pdf").toString();
            title_bar = kiriman.get("title_bar").toString();
            tindakan = kiriman.get("tindakan").toString();
            namafile = kiriman.get("namafile").toString();
        }

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        filePath = new File(Environment.getExternalStorageDirectory() + "/Documents/" + namafile + ".pdf");
        txt_title_bar = findViewById(R.id.titleBar);
        txt_namatindakan = findViewById(R.id.txt_namatindakan);
        btn_savepdf = findViewById(R.id.imgbtn_savepdf);
        txt_title_bar.setText(title_bar);
        txt_namatindakan.setText(tindakan);
        viewpdf = findViewById(R.id.pdfView);
        new RetrievePDFfromUrl().execute(url_pdf);

        //Dialog Confirm
        ViewGroup viewGroup = findViewById(android.R.id.content);
        builder_ask_px = new AlertDialog.Builder(WebViewPDF.this,R.style.CustomAlertDialog);
        inft = getLayoutInflater();
        v_ask_PX = inft.inflate(R.layout.dialog_confirm, viewGroup, false);
        btn_yes_px = v_ask_PX.findViewById(R.id.btn_daftar);
        btn_cancel_px = v_ask_PX.findViewById(R.id.btn_cancel);
        txt_askPX_atas = v_ask_PX.findViewById(R.id.txt_info_atas);
        builder_ask_px.setView(v_ask_PX);
        dial_PX = builder_ask_px.create();
        dial_PX.setCancelable(false);

        btn_savepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dial_PX.show();
            }
        });

        btn_cancel_px.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_PX.dismiss();
            }
        });
        btn_yes_px.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAndOpenPdf(url_pdf,filePath);
            }
        });
    }

    // create an async task class for loading pdf file from URL.
    class RetrievePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {

            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                prgbar = findViewById(R.id.progressBar);
                prgbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            prgbar.setVisibility(View.GONE);
            viewpdf.fromStream(inputStream)
                    .enableSwipe(true)
                    .load();
        }
    }

    public void downloadAndOpenPdf(String url, final File file) {
        if(!file.isFile()) {
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
            req.setDestinationUri(Uri.fromFile(file));
            req.setTitle("Some title");

            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    unregisterReceiver(this);
                    if (file.exists()) {
                        openPdfDocument(file);
                    }
                }
            };
            registerReceiver(receiver, new IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            dm.enqueue(req);
            Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
        }
        else {
            openPdfDocument(file);
        }
    }

    public boolean openPdfDocument(File file) {

        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        try {
            startActivity(target);
            return true;
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this,"No PDF reader found",Toast.LENGTH_LONG).show();
            return false;
        }

    }
}