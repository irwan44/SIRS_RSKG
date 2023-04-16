package averin.sirs_rskg.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class test_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dmedis);

        ImageSlider imgSlider = findViewById(R.id.image_slider1);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel("https://www.rshabibie.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2FKami.1bb6e1c9.jpg&w=3840&q=75", ScaleTypes.FIT));

        imgSlider.setImageList(slideModels);
        imgSlider.setSlideAnimation(AnimationTypes.ZOOM_OUT);

    }
}