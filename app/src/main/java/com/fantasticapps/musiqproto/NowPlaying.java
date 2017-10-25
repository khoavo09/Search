package com.fantasticapps.musiqproto;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

public class NowPlaying extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private static final String API_KEY = "AIzaSyDmBo_Ue4UxXi-8I0-425d9EEJshlw-SwE";
    private static final String VIDEO_ID = "Bt626CEUCMQ";
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    YouTubePlayerFragment myYouTubePlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_now_playing);

        myYouTubePlayerFragment= (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player);
        myYouTubePlayerFragment.initialize(API_KEY, NowPlaying.this);

    }

    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {

        if(errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        }
        else {
            String errorMessage = String.format("There was an error initializing the YouTubePlayer (%1$s", errorReason.toString());
            Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

        if(!wasRestored) {
            player.cueVideo(VIDEO_ID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode== RECOVERY_DIALOG_REQUEST) {
            getYouTubePlayerProvider().initialize(API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_player);
    }

}

