package iss.workshop.ca_memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Intent bgMusicIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bgMusicIntent = new Intent(MainActivity.this, BgMusicService.class);
        bgMusicIntent.setAction("play");
        startService(bgMusicIntent);
    }

    public void playClickHandlerSP(View view) {
        Intent imageFetchIntent = new Intent(this, ImageFetchingActivity.class);
        imageFetchIntent.putExtra("mode", "sp");
        startActivity(imageFetchIntent);
    }

    public void playClickHandlerMP(View view) {
        Intent imageFetchIntent = new Intent(this, ImageFetchingActivity.class);
        imageFetchIntent.putExtra("mode", "mp");
        startActivity(imageFetchIntent);
    }

    public void scoreBoardClickHandler(View view) {
        Intent scoreBoardIntent = new Intent(this, Scoreboard.class);
        startActivity(scoreBoardIntent);
    }

    public void bgSoundHandler(View view) {
        ImageView soundIcon = findViewById(R.id.bgSoundIcon);
        Intent bgMusicIntent = new Intent(this, BgMusicService.class);
        if (BgMusicService.onBGMusic.equals("on")) {
            BgMusicService.onBGMusic = "off";
            soundIcon.setImageResource(R.drawable.music_off);
        } else {
            BgMusicService.onBGMusic = "on";
            soundIcon.setImageResource(R.drawable.music_on);
        }
        bgMusicIntent.setAction("play");
        startService(bgMusicIntent);
    }

    @Override
    protected void onPause() {
        Intent bgMusicIntent = new Intent(this, BgMusicService.class);
        bgMusicIntent.setAction("pause");
        startService(bgMusicIntent);
        super.onPause();
    }

    @Override
    protected void onResume(){
        Intent bgMusicIntent = new Intent(this, BgMusicService.class);
        bgMusicIntent.setAction("play");
        startService(bgMusicIntent);
        super.onResume();
    }
}