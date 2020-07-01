package id.ac.umn.moosic_23490;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

public class NowPlaying extends AppCompatActivity {
    private MediaPlayer mediaPlayer = new MediaPlayer();
    TextView songTxt;
    String songToPlay;
    Button playBtn;
    ImageView img;
    SeekBar positionBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        receiveSong();
        initializeViews();

        playBtn = (Button) findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);
        img = (ImageView) findViewById(R.id.img);

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songToPlay);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        totalTime = mediaPlayer.getDuration();

        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            mediaPlayer.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        //Update timelabel
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null){
                    try{
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e){}
                }
            }
        }).start();
//        playSong();
    }

    private void initializeViews(){
        songTxt = findViewById(R.id.songTxt);
        String title = songToPlay;
        int slashCount = 0;
        for (char c : title.toCharArray()) {
            if (c == '/') {
                slashCount++;
            }
        }
        String afterSlash = title.split("/")[slashCount];
        songTxt.setText(afterSlash);
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            int currentPosition = msg.what;
            //update position
            positionBar.setProgress(currentPosition);

            //update label
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };
    public String createTimeLabel(int time){
        String timelabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timelabel = min + ":";
        if(sec < 10)
            timelabel += "0";
        timelabel += sec;
        return  timelabel;
    }

    private void receiveSong(){

        Intent i = this.getIntent();
        songToPlay = i.getStringExtra("id");

    }

//    private void playSong(){
//        songTxt.setText(songToPlay);
//
//        if(TextUtils.isEmpty(songToPlay)){
//            Toast.makeText(this, "No song to play", Toast.LENGTH_SHORT);
//        } else {
//            try{
//                mediaPlayer.reset();
//                mediaPlayer.setDataSource(songToPlay);
//                mediaPlayer.prepare();
//                mediaPlayer.start();
//            }catch (IOException e){
//                Toast.makeText(getBaseContext(), "Cannot play song!", Toast.LENGTH_SHORT);
//            }
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.reset();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void playBtnClick(View view) {
        if(!mediaPlayer.isPlaying()){

            mediaPlayer.start();
            playBtn.setBackgroundResource(R.drawable.pause);
        } else {
            mediaPlayer.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }
}