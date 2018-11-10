package toblindr.student.chalmers.se.thealchemist;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class BackgroundMusicService extends Service {
    private MediaPlayer player;
    public static final String PAUSE = "pause";

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this,R.raw.background);
        player.setLooping(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent!= null){
            String action = intent.getAction();
            if(action!=null){
                if(action.equals(PAUSE)){
                    player.pause();
                }
        }
            else{
                player.start();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
    }

}
