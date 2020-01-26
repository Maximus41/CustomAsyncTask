package com.demo.customasyntasktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvCounter;
    private Button btnToggle;
    private CounterTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCounter = findViewById(R.id.counter);
        btnToggle = findViewById(R.id.toggle);
        tvCounter.setText("Start Countdown");
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task == null)
                    task = new CounterTask();
                if(btnToggle.getText().equals("Start"))
                    task.execute();
                else
                    task.cancel();
            }
        });
    }

    private class CounterTask extends CustomAsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnToggle.setText("Stop");
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground callback Called");
            for(int i = 0; i <= 100; i++) {
                publishProgress(i);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Success";
        }

        @Override
        protected void onProgressUpdate(Integer integer) {
            tvCounter.setText(String.valueOf(integer));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("Success"))
                tvCounter.setText("Hurray!");
            btnToggle.setText("Start");
        }
    }
}
