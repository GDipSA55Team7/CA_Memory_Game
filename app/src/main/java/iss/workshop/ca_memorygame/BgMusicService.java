package iss.workshop.ca_memorygame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;


public class BgMusicService extends Service {

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
    public void onDestroy() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}