package omkabel.Project.myapplication.Features;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import omkabel.Project.myapplication.R;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        int waktu_loading=1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home=new Intent(Splash_Screen.this, Monitoring.class);
                startActivity(home);
                finish();
            }
        },waktu_loading);
    }
}