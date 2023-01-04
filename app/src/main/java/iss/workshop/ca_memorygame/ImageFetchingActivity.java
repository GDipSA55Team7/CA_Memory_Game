package iss.workshop.ca_memorygame;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImageFetchingActivity extends AppCompatActivity {

    ImageFetchingService imageFetchingService;
    EditText urlSearchBar;
    private BaseAdapter adapter;
    private Thread threadDownloadImage;
    private int maxImages = 20;
    private ProgressBar progressBar;
    private TextView progressDes;
    private boolean isThreadRunning;
    private ImageSelectListener listener;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_fetching);

        Intent bgMusicIntent = new Intent(this, BgMusicService.class);
        bgMusicIntent.setAction("play");
        startService(bgMusicIntent);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

        urlSearchBar = findViewById(R.id.urlSearchBar);
        progressBar = findViewById(R.id.progressBar);
        progressDes = findViewById(R.id.progressDes);
        progressBar.setVisibility(View.INVISIBLE);
        progressDes.setText("");

        this.imageFetchingService = new ImageFetchingService();
        initializeGridView();
    }

    private void loadDefaultImage() {
        this.imageFetchingService.imageContents = new ArrayList<>();
        for (int i = 0; i < maxImages; i++) {
            this.imageFetchingService.imageContents.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.placeholder));
        }
    }

    private void initializeGridView() {
        loadDefaultImage();
        GridView gridView = findViewById(R.id.gvImages);
        adapter = new MainActivity.GridImageAdapter(this, this.imageFetchingService.imageContents);
        listener = new ImageSelectListener(this, mode);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(listener);
    }

    private void prepareDataForListener() {
        listener.setFiles(imageFetchingService.imageFiles);
        List<Boolean> list = new ArrayList<>(Arrays.asList(new Boolean[imageFetchingService.imageContents.size()]));
        Collections.fill(list, Boolean.FALSE);
        listener.setSelectedImages(list);
        listener.setDownloadFinished(true);
    }

    public void fetchImageClickHandler(View view) {
        if (isThreadRunning && threadDownloadImage != null) {
            threadDownloadImage.interrupt();
            isThreadRunning = false;
        }

        threadDownloadImage = new Thread(() -> {
            isThreadRunning = true;
            String result = imageFetchingService.prepareImageUrls(urlSearchBar.getText().toString());

            if (result == "success") {
                imageFetchingService.imageContents = new ArrayList<>();
                int count = 1;
                for (String imageUrl : imageFetchingService.imgUrlList) {
                    File tempFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "img" + count + ".jpg");
                    imageFetchingService.downloadImage(imageUrl, tempFile);
                    runOnUiThread(new UpdateProgressRunnable(count));
                    count++;
                }
                prepareDataForListener();
                runOnUiThread(new UpdateGridViewRunnable());
            } else {
                runOnUiThread(() -> Toast.makeText(this, result, Toast.LENGTH_LONG).show());
            }
        });
        threadDownloadImage.start();
    }

    public class UpdateProgressRunnable implements Runnable {

        protected int imgIdDone;

        UpdateProgressRunnable(int idDone) {
            super();
            this.imgIdDone = idDone;
        }

        @Override
        public void run() {
            updateProgressBar(imgIdDone);
        }
    }

    public void updateProgressBar(int count) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(5 * count);
        int progress = progressBar.getProgress();

        String loadingText = "Downloading " + progress / 5 + " of 20 images";
        if (progress == 100) {
            loadingText = "Downloaded 20 of 20 images. \nSelect 6 images to start game!";
        }
        progressDes.setText(loadingText);
    }

    public class UpdateGridViewRunnable implements Runnable {

        protected int imgIdDone;

        UpdateGridViewRunnable() {
            super();
        }

        @Override
        public void run() {
            updateGridView();
        }
    }

    public void updateGridView() {
        MainActivity.GridImageAdapter fetchedImageAdapter = new MainActivity.GridImageAdapter(this, this.imageFetchingService.imageContents);
        GridView imageGridView = findViewById(R.id.gvImages);
        if (imageGridView != null) {
            imageGridView.setAdapter(fetchedImageAdapter);
        }
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

    public static class BgMusicService extends Service {

        MediaPlayer mMediaPlayer;
        public static String onBGMusic = "off";

        public BgMusicService() {

        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.bg_pixabay);
                mMediaPlayer.setLooping(true);
            }
            String action = intent.getAction();
            if (action != null && onBGMusic.equals("on")) {
                if (action.equals("play")) {
                    mMediaPlayer.setVolume(1f, 1f);
                    mMediaPlayer.start();
                } else if (action.equals("gaming")) {
                        mMediaPlayer.setVolume(0.5f, 0.5f);
                        mMediaPlayer.start();
                } else if (action.equals("pause")) {
                    mMediaPlayer.pause();
                }
            } else if (action != null && onBGMusic.equals("off")) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy(){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public static class ImageFetchingService {

        public ArrayList<String> imgUrlList = new ArrayList<>();
        public ArrayList<Bitmap> imageContents = new ArrayList<>();
        public ArrayList<File> imageFiles = new ArrayList<>();

        public String prepareImageUrls(String url){
            if(url == null || url == ""){
                return "Invalid Url";
            }
            try {
                imgUrlList = new ArrayList<>();
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("img[src]");
                for (Element link : links) {
                    if (link.attr("src").contains(".jpg") && link.attr("src").contains("https://")
                            && !link.attr("src").contains("?")) {
                        imgUrlList.add(link.attr("src"));
                        if (imgUrlList.size() == 20)
                            break;
                    }

                    if (link.attr("data-src").contains(".jpg") && link.attr("data-src").contains("https://")
                            && !link.attr("data-src").contains("?")) {
                        imgUrlList.add(link.attr("data-src"));
                        if (imgUrlList.size() == 20)
                            break;
                    }
                }

                if(imgUrlList.size() < 20)
                {
                    return "Insufficient Images";
                }


                return "success";
            }
            catch (IOException e)
            {
                return "fail";
            }
            catch (Exception e)
            {
                String message = e.getMessage();
                return e.getMessage();
            }
        }

        public boolean downloadImage(String url,File targetFile)
        {
            try {
                URL myURL = new URL(url);
                URLConnection conn = myURL.openConnection();

                InputStream in = conn.getInputStream();
                FileOutputStream out = new FileOutputStream(targetFile);
                byte[] buffer = new byte[1024];
                int bytesRead = -1;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                out.close();
                in.close();
                imageFiles.add(targetFile);
                imageContents.add(BitmapFactory.decodeFile(targetFile.getAbsolutePath()));
                return true;

            } catch (Exception e) {
                return false;
            }
        }
    }
}