package com.rafaosousa.example.smslistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Switch switchMain;

    BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("y")) {
                String code = intent.getStringExtra("y");
                textView.setText(code);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{RECEIVE_SMS, READ_SMS}, 0);
        }

        registerReceiver(broadcastReceiver, new IntentFilter("S"));

        changeSharedPreference(true);

        this.switchMain = (Switch) findViewById(R.id.switch_main);
        this.textView = (TextView) findViewById(R.id.textview_main);

        switchMain.setChecked(true);
        switchMain.setText("On");

        switchMain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchMain.setChecked(b);
                switchMain.setText(b ? "On" : "Off");

                textView.setText("");

                changeSharedPreference(b);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void changeSharedPreference(Boolean b) {
        this.getSharedPreferences("x", 0).edit().putBoolean("0", b).commit();
    }
}
