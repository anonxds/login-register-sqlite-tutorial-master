package com.androidtutorialshub.loginregister;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Pulseactivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;


    Timer timeoutTimer;
    final Random myRandom = new Random();
    GenerateTask genTask = new GenerateTask();
    final ArrayList<Object> arry1 = new ArrayList<Object>();
    private Handler mHandler = new Handler();
    LineGraphSeries<DataPoint> series;
    private String m_Text = "200";
    String datak;
    class GenerateTask extends TimerTask {
        boolean started = false;
        @Override
        public void run() {
            if (started) {
                System.out.println("generating");
                final TextView textGenerateNumber = (TextView)findViewById(R.id.txt_contador);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        datak = textGenerateNumber.getText().toString();
                        textGenerateNumber.setText(String.valueOf(myRandom.nextInt(150-70+1)+70));
                        datak = textGenerateNumber.getText().toString();
                    }
                });
            }
        }
    }

    final int min = 20;
    final int max = 80;
    double lastindex = 0;
    final int random = new Random().nextInt((max - min) + 1) + min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulseactivity);
        Button open = findViewById(R.id.button);
        TextView textGenerateNumber = (TextView)findViewById(R.id.txt_contador);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p();
            }
        });
        textGenerateNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            List<String> list;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Integer.parseInt(datak)>=Integer.parseInt(m_Text)){

                    Log.d("id", "Mensaje enviado");
                    sendSMSMessage();
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder= new AlertDialog.Builder(Pulseactivity.this);
                    builder.setMessage("¡Se activo la alerta!")
                            .setTitle("ALERTA")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }})
                            .setNegativeButton(" NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();



                }

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sendSMSMessage();


        GraphView graph = (GraphView) findViewById(R.id.graph);
        series  = new LineGraphSeries<DataPoint>();
        if (!genTask.started) {
            genTask.started=true;
            timeoutTimer = new Timer();
            timeoutTimer.scheduleAtFixedRate(genTask, 0, 10000);
        } else {
            genTask.started=false;
            timeoutTimer.cancel();
        }

        graph.addSeries(series);
        graph.getViewport().setMinX(70);
        graph.getViewport().setMaxX(150);
        graph.getViewport().setXAxisBoundsManual(true);
        addDatapoint();
    }


    private void addDatapoint(){

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                series.appendData(new DataPoint(lastindex++,Integer.parseInt(datak)),true,10);
                addDatapoint();
                Log.d("dick",datak);
                Log.d("dicks",m_Text);


            }
        },10000);
    }
    protected void sendSMSMessage() {
        String  phoneNo = "";
        String    message = "";

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                Toast.makeText(getApplicationContext(), "SMS sent.",  Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
                Toast.makeText(getApplicationContext(), "SMS sent.",  Toast.LENGTH_LONG).show();

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    //  smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    void p(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertStyle);
        builder.setTitle("Establecer Alerta cardiaca HPB");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                // datak = m_Text;
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
