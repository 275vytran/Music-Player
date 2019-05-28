package com.vytran.regularservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button playButton, stopButton, pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button)findViewById(R.id.playButton);
        stopButton = (Button)findViewById(R.id.stopButton);
        pauseButton = (Button)findViewById(R.id.pauseButton);

        playButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(getBaseContext(), MainService.class);
        intent.setAction(MainService.ACTION_UI_ONSCREEN);
        startService(intent);
        super.onResume();
    }

    protected void onPause() {
        Intent intent = new Intent(getBaseContext(), MainService.class);
        intent.setAction(MainService.ACTION_UI_OFFSCREEN);
        startService(intent);
        super.onPause();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playButton:
                Intent intent = new Intent(getBaseContext(), MainService.class);
                intent.setAction(MainService.ACTION_PLAY);
                startService(intent);
                break;
            case R.id.stopButton:
                Intent intent1 = new Intent(getBaseContext(), MainService.class);
                intent1.setAction(MainService.ACTION_STOP);
                startService(intent1);
                break;
            case R.id.pauseButton:
                String status = pauseButton.getText().toString();
                if (status.equals("PAUSE"))
                    pauseButton.setText("UNPAUSE");
                else if (status.equals("UNPAUSE"))
                    pauseButton.setText("PAUSE");

                Intent intent3 = new Intent(getBaseContext(), MainService.class);
                intent3.setAction(MainService.ACTION_PAUSE);
                startService(intent3);
                break;
        }
    }
}
