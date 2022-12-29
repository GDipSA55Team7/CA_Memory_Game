package iss.workshop.ca_memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView test = findViewById(R.id.testimage);
        Glide.with(this)
                .load("https://cdn.stocksnap.io/img-thumbs/280h/tree-desert_JMTQNOJWU8.jpg")
                .override(300,300).into(test);
//        Intent intent = new Intent(this, GamePage.class);
//        startActivity(intent);
    }
}