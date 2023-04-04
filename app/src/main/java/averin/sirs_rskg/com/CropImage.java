package averin.sirs_rskg.com;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.fenchtose.nocropper.BitmapResult;
import com.fenchtose.nocropper.CropInfo;
import com.fenchtose.nocropper.CropMatrix;
import com.fenchtose.nocropper.CropResult;
import com.fenchtose.nocropper.CropState;
import com.fenchtose.nocropper.CropperCallback;
import com.fenchtose.nocropper.CropperView;
import com.fenchtose.nocropper.ScaledCropper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;
import averin.sirs_rskg.com.Ui.BitmapUtil;

public class CropImage extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_PERMISSION = 22;
    private static final String TAG = "CropImage";
    private static final int REQUEST_GALLERY = 21;
    private static final int REQUEST_CAM = 123;

    //Dialog Confirm
    AlertDialog.Builder builder_success, builder_failed, dial_builder;
    AlertDialog dial_success, dial_failed, dial_login;
    LayoutInflater inflater;
    View v_success_regist, v_failed_regist, dialogView;
    TextView txt_info_success,txt_info_failed;
    Button btn_ok_success, btn_ok_failed;

    String val_token, nomr, no_ktp, namaPasien, code_kirim, strImgPath, urlfoto, strFilePath, strEncodedImage;
    CropperView mImageView;
    CheckBox originalImageCheckbox, cropAsyncCheckbox;
    byte[] imageBytes;

    private Bitmap mBitmap ,originalBitmap;
    private boolean isSnappedToCenter = false;
    private int rotationCount = 0;
    private HashMap<String, CropMatrix> matrixMap = new HashMap<>();
    private String currentFilePath = null;
    public String url_editFoto = RequestHandler.APIdev+"edit_foto_pasien.php";
    public String linkJanda  = RequestHandler.APIdev+"get-token.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

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

        mImageView = findViewById(R.id.imageview);
//        originalImageCheckbox = findViewById(R.id.original_checkbox);
        cropAsyncCheckbox = findViewById(R.id.cropasync_checkbox);

        Bundle data = getIntent().getExtras();
        if(data!=null){
            code_kirim = data.get("pick_kode").toString();
            urlfoto = data.get("urlFoto").toString();
        }
        if(code_kirim.equals("1")){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PackageManager.PERMISSION_GRANTED);
            checkCameraHardware(this);
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, REQUEST_CAM);
        }else if(code_kirim.equals("2")) {
            if (!hasGalleryPermission()) {
                askForGalleryPermission();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_GALLERY);
        }

        Token token = AppController.getInstance(this).isiToken();
        Login lgn = AppController.getInstance(this).getPasien();
        val_token  = String.valueOf(token.gettoken());
//        nomr       = String.valueOf(lgn.getMR_pasien());
        no_ktp     = String.valueOf(lgn.getKTP_pasien());
        namaPasien = String.valueOf(lgn.getNama_pasien());

        //Dialog Confirm
        ViewGroup viewGroup = findViewById(android.R.id.content);
        builder_success = new AlertDialog.Builder(CropImage.this,R.style.CustomAlertDialog);
        builder_failed = new AlertDialog.Builder(CropImage.this,R.style.CustomAlertDialog);
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
        Intent balikedit = new Intent(CropImage.this, EditPasienLama.class);

        btn_ok_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(balikedit);
                dial_success.dismiss();
                Intent i = new Intent(CropImage.this, EditPasienLama.class);
                startActivity(i);

            }
        });
        btn_ok_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(balikedit);
                dial_failed.dismiss();
            }
        });

        findViewById(R.id.crop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageCropClicked();
            }
        });

        findViewById(R.id.rotate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateImage();
            }
        });

        findViewById(R.id.snap_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snapImage();
            }
        });

        findViewById(R.id.gesture_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleGestures();
            }
        });

        mImageView.setDebug(true);

        mImageView.setGridCallback(new CropperView.GridCallback() {
            @Override
            public boolean onGestureStarted() {
                return true;
            }

            @Override
            public boolean onGestureCompleted() {
                return false;
            }
        });
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            Toast.makeText(getBaseContext(), "No camera found on this device",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void onImageCropClicked() {
        if (cropAsyncCheckbox.isChecked()) {
            cropImageAsync();
        } else {
            cropImage();
        }
    }

    public void toggleGestures() {
        boolean enabled = mImageView.isGestureEnabled();
        enabled = !enabled;
        mImageView.setGestureEnabled(enabled);
        Toast.makeText(this, "Gesture " + (enabled ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
    }

    private void startGalleryIntent() {

        if (currentFilePath != null) {
            matrixMap.put(currentFilePath, mImageView.getCropMatrix());
        }

        if (!hasGalleryPermission()) {
            askForGalleryPermission();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private boolean hasGalleryPermission() {
        return ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void askForGalleryPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_READ_PERMISSION);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if(requestCode == REQUEST_CAM && responseCode == RESULT_OK){
//            convertImage(strImgPath);
//            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            String strBitmap = BitmapUtil.getFilePathFromUri(this, data.getData());
            loadNewImage(strBitmap);
        }
        if (requestCode == REQUEST_GALLERY && responseCode == RESULT_OK) {
            String absPath = BitmapUtil.getFilePathFromUri(this, data.getData());
            loadNewImage(absPath);
        }
    }

    private void loadNewImage(String filePath) {
        this.currentFilePath = filePath;
        rotationCount = 0;
        Log.i(TAG, "load image: " + filePath);
        mBitmap = BitmapFactory.decodeFile(filePath);
        originalBitmap = mBitmap;
        Log.i(TAG, "bitmap: " + mBitmap.getWidth() + " " + mBitmap.getHeight());

        int maxP = Math.max(mBitmap.getWidth(), mBitmap.getHeight());
        float scale1280 = (float)maxP / 1280;
        Log.i(TAG, "scaled: " + scale1280 + " - " + (1/scale1280));

        if (mImageView.getWidth() != 0) {
            mImageView.setMaxZoom(mImageView.getWidth() * 2 / 1280f);
        } else {

            ViewTreeObserver vto = mImageView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    mImageView.setMaxZoom(mImageView.getWidth() * 2 / 1280f);
                    return true;
                }
            });

        }

        mBitmap = Bitmap.createScaledBitmap(mBitmap, (int)(mBitmap.getWidth()/scale1280),
                (int)(mBitmap.getHeight()/scale1280), true);

        mImageView.setImageBitmap(mBitmap);
        final CropMatrix matrix = matrixMap.get(filePath);
        if (matrix != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImageView.setCropMatrix(matrix, true);
                }
            }, 30);
        }
    }

    private void convertImage(String imageFilePath) {
        File imageFile = new File(imageFilePath);
        if (imageFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            final Bitmap bitmap = BitmapFactory.decodeFile(strFilePath, options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageBytes = baos.toByteArray();
            strEncodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryIntent();
                return;
            }
        }

        Toast.makeText(this, "Gallery permission not granted", Toast.LENGTH_SHORT).show();
    }

    private void cropImageAsync() {
        CropState state = mImageView.getCroppedBitmapAsync(new CropperCallback() {
            @Override
            public void onCropped(Bitmap bitmap) {
                if (bitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    strEncodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    sendEditFoto(strEncodedImage);
                }
            }

            @Override
            public void onOutOfMemoryError() {

            }
        });

        if (state == CropState.FAILURE_GESTURE_IN_PROCESS) {
            Toast.makeText(this, "unable to crop. Gesture in progress", Toast.LENGTH_SHORT).show();
        }

//        if (originalImageCheckbox.isChecked()) {
//            cropOriginalImageAsync();
//        }
    }

    private void cropImage() {

        BitmapResult result = mImageView.getCroppedBitmap();

        if (result.getState() == CropState.FAILURE_GESTURE_IN_PROCESS) {
            Toast.makeText(this, "unable to crop. Gesture in progress", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap = result.getBitmap();

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            strEncodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            sendEditFoto(strEncodedImage);
        }
    }

    private ScaledCropper prepareCropForOriginalImage() {
        CropResult result = mImageView.getCropInfo();
        if (result.getCropInfo() == null) {
            return null;
        }

        float scale;
        if (rotationCount % 2 == 0) {
            // same width and height
            scale = (float) originalBitmap.getWidth()/mBitmap.getWidth();
        } else {
            // width and height are interchanged
            scale = (float) originalBitmap.getWidth()/mBitmap.getHeight();
        }

        CropInfo cropInfo = result.getCropInfo().rotate90XTimes(mBitmap.getWidth(), mBitmap.getHeight(), rotationCount);
        return new ScaledCropper(cropInfo, originalBitmap, scale);
    }

    private void cropOriginalImage() {
        if (originalBitmap != null) {
            ScaledCropper cropper = prepareCropForOriginalImage();
            if (cropper == null) {
                return;
            }

            Bitmap bitmap = cropper.cropBitmap();
            if (bitmap != null) {
                try {
                    BitmapUtil.writeBitmapToFile(bitmap, new File(Environment
                            .getExternalStorageDirectory() + "/crop_test_info_orig.jpg"), 90);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void cropOriginalImageAsync() {
        if (originalBitmap != null) {
            ScaledCropper cropper = prepareCropForOriginalImage();
            if (cropper == null) {
                return;
            }

            cropper.crop(new CropperCallback() {
                @Override
                public void onCropped(Bitmap bitmap) {
                    if (bitmap != null) {
                        try {
                            BitmapUtil.writeBitmapToFile(bitmap, new File(Environment.getExternalStorageDirectory() + "/crop_test_info_orig.jpg"), 90);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void rotateImage() {
        if (mBitmap == null) {
            Log.e(TAG, "bitmap is not loaded yet");
            return;
        }

        mBitmap = BitmapUtil.rotateBitmap(mBitmap, 90);
        mImageView.setImageBitmap(mBitmap);
        rotationCount++;
    }

    private void snapImage() {
        if (isSnappedToCenter) {
            mImageView.cropToCenter();
        } else {
            mImageView.fitToCenter();
        }

        isSnappedToCenter = !isSnappedToCenter;
    }

    private void sendEditFoto(String strImage) {

        final String isiToken       = val_token;
        final String iniKTP         = no_ktp;
        final String strFoto        = "data:image/jpeg;base64,"+strImage;

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
                params.put("fotoProfile", strFoto);

                //returing the response
                return requestHandler.requestData(url_editFoto, "POST", "application/json; charset=utf-8", "X-Api-Token",
                        isiToken, "X-Px-Key", "", params);
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
                        dial_success.show();
                        txt_info_success.setText(obj.getString("msg"));
                        Login updlogin = new Login(namaPasien,no_ktp,strFoto);
                        AppController.getInstance(getApplicationContext()).userLogin(updlogin);
                    } else {
                        dial_failed.show();
                        txt_info_failed.setText(obj.getString("msg"));
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
}

