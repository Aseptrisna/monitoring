package omkabel.Project.myapplication.Features;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import omkabel.Project.myapplication.Koneksi.Koneksi_RMQ;
import omkabel.Project.myapplication.R;

public class Monitoring extends AppCompatActivity implements Myservices{
//    Koneksi_RMQ rmq ;
    TextView SUHU,JAM,TANGGAL,KELEMABAN,VALUEKIPAS,VALUELAMPU;
    Button  KIPASON,KIPASOFF,LAMPUON,LAMPUOFF;
    ProgressDialog progressDialog;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_monitoring);
//        linearLayout=(LinearLayout)findViewById(R.id.linearLayoutButton);
//        linearLayout.setVisibility(View.GONE);

        SUHU=(TextView)findViewById(R.id.valuesuhu);
        KELEMABAN=(TextView)findViewById(R.id.valuekelemababan);
        JAM=(TextView)findViewById(R.id.valuejam);
        TANGGAL=(TextView)findViewById(R.id.valuedate);
        VALUELAMPU=(TextView)findViewById(R.id.valueBtnLampu);
        VALUEKIPAS=(TextView)findViewById(R.id.valueButtonKipas);
        LAMPUON=(Button)findViewById(R.id.btnLampuon);
        LAMPUOFF=(Button)findViewById(R.id.btnLampuoff);
        KIPASON=(Button)findViewById(R.id.btnKipason);
        KIPASOFF=(Button)findViewById(R.id.btnKipasoff);
        Tampil_data();
        LAMPUON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Kirim("0111#0");
                String kondisikipas = null;
                int kondisilampu=0;
                if (VALUEKIPAS.getText().toString().equals("ON")){
                    kondisikipas="0";
                }else if(VALUEKIPAS.getText().toString().equals("OFF")){
                    kondisikipas="1";
                }else if(VALUELAMPU.getText().toString().equals("ON")){
                    kondisilampu='0';
                }else if (VALUELAMPU.getText().toString().equals("OFF")){
                    kondisilampu='0';
                }
                progressDialog = ProgressDialog.show(Monitoring.this,"Loading.....",null,true,true);
                String pesan=kondisikipas+"0"+"1"+"1"+"#"+"60000";
                Kirim(pesan);

            }
        });
        LAMPUOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kondisikipas = null;
                int kondisilampu=0;
                if (VALUEKIPAS.getText().toString().equals("ON")){
                    kondisikipas="0";
                }else if(VALUEKIPAS.getText().toString().equals("OFF")){
                    kondisikipas="1";
                }else if(VALUELAMPU.getText().toString().equals("ON")){
                    kondisilampu='0';
                }else if (VALUELAMPU.getText().toString().equals("OFF")){
                    kondisilampu='0';
                }
                progressDialog = ProgressDialog.show(Monitoring.this,"Loading.....",null,true,true);
                String pesan=kondisikipas+"1"+"1"+"1"+"#"+"60000";
                Kirim(pesan);
            }
        });
        KIPASON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kondisikipas = 0;
                String kondisilampu = null;
                if (VALUEKIPAS.getText().toString().equals("ON")){
                    kondisikipas='0';
                }else if(VALUEKIPAS.getText().toString().equals("OFF")){
                    kondisikipas='1';
                }else if(VALUELAMPU.getText().toString().equals("ON")){
                    kondisilampu="0";
                }else if (VALUELAMPU.getText().toString().equals("OFF")){
                    kondisilampu="1";
                }
                progressDialog = ProgressDialog.show(Monitoring.this,"Loading.....",null,true,true);
                String pesan="0"+kondisilampu+"1"+"1"+"#"+"60000";
                Kirim(pesan);
            }
        });
        KIPASOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kondisikipas = 0;
                int kondisilampu=0;
                if (VALUEKIPAS.getText().toString().equals("ON")){
                    kondisikipas='0';
                }else if(VALUEKIPAS.getText().toString().equals("OFF")){
                    kondisikipas='1';
                }else if(VALUELAMPU.getText().toString().equals("ON")){
                    kondisilampu='0';
                }else if (VALUELAMPU.getText().toString().equals("OFF")){
                    kondisilampu='0';
                }
                progressDialog = ProgressDialog.show(Monitoring.this,"Loading.....",null,true,true);
                String pesan="1"+kondisilampu+"1"+"1"+"#"+"60000";
                Kirim(pesan);
            }

        });
}



    private void Tampil_data() {
        Koneksi_RMQ rmq=new Koneksi_RMQ(Monitoring.this);
        rmq.setupConnectionFactory();
        final Handler incomingMessageHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                Log.d("RMQMessage", message);
                String s = message.toString();
                try {
                    JSONObject jsonRESULTS = new JSONObject(s);
                    String Kelembaban = jsonRESULTS.getString("humidity");
                    String temperature = jsonRESULTS.getString("temperature");
                    String statuslampu = jsonRESULTS.getString("statuslampu");
                    String statuskipas = jsonRESULTS.getString("statuskipas");

                    Log.d("H",Kelembaban);
                    Log.d("T",temperature);
                    Log.d("Lampu",statuslampu);
                    Log.d("Kipas",statuskipas);
                    SUHU.setText(temperature+"°C");
                    KELEMABAN.setText(Kelembaban+"%");
                    VALUELAMPU.setText(statuslampu);
                    VALUEKIPAS.setText(statuskipas);
                    tampiljam();
                    tampiltanggal();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String[] tokens = s.split("");

//                LoginPassword.setText(message);
//                Toast.makeText(Data_Sampah.this, "ini data dari RMQ"+message, Toast.LENGTH_SHORT).show();
            }
        };

        Thread subscribeThread = new Thread(); //ini gua coba iseng kak
        rmq.subscribe(incomingMessageHandler,subscribeThread);
    }

    private void tampiljam(){
        DateFormat jam = new SimpleDateFormat("dd-MM-YYYY");
        Date Formatjam = new Date();
        TANGGAL.setText(jam.format(Formatjam));

    }
    private void tampiltanggal(){
        DateFormat dateFormattanggal = new SimpleDateFormat("HH:mm:ss");
        Date tanggal = new Date();
        JAM.setText(dateFormattanggal.format(tanggal));

    }

    private void Kirim(String pesan) {
        Koneksi_RMQ rmq=new Koneksi_RMQ(Monitoring.this);
        rmq.setupConnectionFactory();
        rmq.publish(pesan);
        Log.v("pesan", pesan);
    }

    @Override
    public void Berhasil(){
        progressDialog.dismiss();
        Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void Gagag(){
        progressDialog.dismiss();
        Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();

    }

}