package com.vytran.regularservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class MainService extends IntentService {

    boolean service_running;
    boolean music_playing;
    boolean music_pausing;
    boolean ui_onscreen;

    MediaPlayer mp;

    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_STOP = "STOP";
    public static final String ACTION_PAUSE = "PAUSE";
    public static final String ACTION_UI_ONSCREEN = "ONSCREEN";
    public static final String ACTION_UI_OFFSCREEN = "OFFSCREEN";

    public MainService() {
        super("MainService");
        service_running = false;
        music_playing = false;
        music_pausing = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ui_onscreen = true;

        mp = null;
        mp = MediaPlayer.create(this, R.raw.music);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                service_running = false;
                music_playing = false;
                if (!ui_onscreen) {
                    Notify();
                }
            }
        });
    }


    private void Notify() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Music Player")
                .setContentText("Open to play music again");

        int mNotificationId = 999;
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(mNotificationId, mBuilder.build());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_PLAY)) {
            ui_onscreen = true;
            if (!service_running) {
                mp.start();
                service_running = true;
                music_playing = true;
            }
        }
        else if (action.equals(ACTION_STOP)) {
            ui_onscreen = true;
            music_pausing = false;
            music_playing = false;
            if (mp != null) {
                mp.stop();
                mp.release();
                mp = null;
            }
            stopSelf();
        }
        else if (action.equals(ACTION_PAUSE)) {
            ui_onscreen = true;
            if (music_playing) {
                mp.pause();
                music_pausing = true;
                music_playing = false;
            }
            else if (music_pausing && !music_playing) {
                mp.start();
                music_playing = true;
                music_pausing = false;
            }
        }
        else if (action.equals(ACTION_UI_ONSCREEN)) {
            ui_onscreen = true;
        }
        else if (action.equals(ACTION_UI_OFFSCREEN)) {
            ui_onscreen = false;
        }
    }

    @Override
    public void onDestroy() {
        service_running = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
