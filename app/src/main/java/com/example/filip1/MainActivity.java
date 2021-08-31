package com.example.filip1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.*;
import static com.example.filip1.Notificare.Canal1;
import static com.example.filip1.Notificare.Canal2;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Verificare";
    private NotificationManagerCompat notificationManager;
    static String  temperatura;
    static String umiditate;
    static String foc;
    static String lumina;
    static String umiditate_sol;
    static String ploaie;
    Button NextPage;
    public static int a=0;
    public static int b=0;


    public static int getLed(){return a;}
    public static void setLed(int x){a=x;}
    public static int getled2(){return b;}
    public static void setLed2(int x){b=x;}

    public static boolean ventilator;
    public static boolean getVentilator(){
        return ventilator;
    }
    public static void setVentilator2(Boolean x){
        ventilator=x;
    }

    public static boolean pompa;
    public static boolean getPompa(){
        return pompa;
    }
    public static void setPompa2(Boolean x){
        pompa=x;
    }
    TextView TEMP;
    TextView HUM;
    TextView FIRE;
    TextView LIGHT;
    TextView RAIN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date_senzori();
        NextPage=findViewById(R.id.goToNextPage);
        TEMP = (TextView) findViewById(R.id.Temperatura);
        HUM = (TextView) findViewById(R.id.Umiditate);
        FIRE=(TextView) findViewById(R.id.Foc);
        LIGHT=(TextView) findViewById(R.id.Lumina);
        RAIN=(TextView) findViewById(R.id.Ploaie);
        TEMP.setText(MainActivity.temperatura);
        HUM.setText(MainActivity.umiditate);
        FIRE.setText(MainActivity.foc);
        LIGHT.setText(MainActivity.lumina);
        RAIN.setText(MainActivity.ploaie);
    }
    public void goToNextPage(View view){
        Intent myIntent = new Intent(MainActivity.this, NextPage.class);
        startActivity(myIntent);
    }
    public void Date_senzori() {
        String client_Id = MqttClient.generateClientId();
        MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://mqtt.beia-telemetrie.ro:1883", client_Id);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    try {
                        client.subscribe("training/device/Filip_LICENTA", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(TAG, "subscribed succeed to temperature ");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(TAG, "subscribed failed to temperature");
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }
            //double x = 0;
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                //String temp= message.toString();
                String[] parametru_demunire_valoare = message.toString().split(",");

                //temperatura

                String[] valoare_numerica_temperatura = parametru_demunire_valoare[0].toString().split(":");
                Float temp= Float.parseFloat(valoare_numerica_temperatura[1]);
                MainActivity.temperatura=valoare_numerica_temperatura[1];
                if (temp>26.00) {
                    ventilator=true;
                }
                else {
                    ventilator=false;
                }
                TEMP.setText(valoare_numerica_temperatura[1]);

                Log.d(TAG, valoare_numerica_temperatura[1]);
                //umiditate
                String[] valoare_numerica_umiditate = parametru_demunire_valoare[1].toString().split(":");
                HUM.setText(valoare_numerica_umiditate[1]);
                MainActivity.umiditate=valoare_numerica_umiditate[1];
                //foc

                String[] valoare_numerica_foc = parametru_demunire_valoare[3].toString().split(":");
                Float foc= Float.parseFloat(valoare_numerica_foc[1]);
                String avertizare;
                if (foc== 1){
                    avertizare="Incendiu";
                    addNotification();
                }
                else {avertizare="Zona sigura";}

                MainActivity.foc=avertizare;
                FIRE.setText(avertizare);
                //lumina

                String[] valoare_numerica_lumina = parametru_demunire_valoare[4].toString().split(":");
                Float lumina= Float.parseFloat(valoare_numerica_lumina[1]);
                String Mesaj;
                if (lumina==1){
                    Mesaj="Zi";
                }
                else {
                    Mesaj="Noapte";
                }
                MainActivity.lumina=Mesaj;
                LIGHT.setText(Mesaj);
                //ploaie

                String[] valoare_numerica_ploaie = parametru_demunire_valoare[5].toString().split(":");
                Float ploaie_stare= Float.parseFloat(valoare_numerica_ploaie[1]);
                String Mesaj1;

                if (ploaie_stare==1){
                    Mesaj1="Ploua";
                    addNotification2();
                }
                else {
                    Mesaj1="Insorit";
                }
                MainActivity.ploaie=Mesaj1;
                RAIN.setText(Mesaj1);
                //umiditate sol
                String[] valoare_numerica_sol = parametru_demunire_valoare[2].toString().split(":");
                Float sol= Float.parseFloat(valoare_numerica_sol[1]);
                MainActivity.umiditate_sol=valoare_numerica_sol[1];
                if (sol == 1) {
                    pompa=true;
                }
                else {
                    pompa=false;
                }
                Log.d(TAG, valoare_numerica_ploaie[1]);


                Log.d(TAG, parametru_demunire_valoare[0]);

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
    private void addNotification() {
        notificationManager = NotificationManagerCompat.from(MainActivity.this);
        Notification builder = new NotificationCompat.Builder(MainActivity.this, Canal1)
                .setSmallIcon(R.drawable.ic_baseline_priority_high_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentTitle("Incendiu Detectat!")
                .setContentText("Incendiu in interiorul casei!!!!")
                .build();
        notificationManager.notify(1, builder);
        Log.d(TAG, "Incendiu");
    }
    private void addNotification2() {
        notificationManager = NotificationManagerCompat.from(MainActivity.this);
        Notification builder = new NotificationCompat.Builder(MainActivity.this, Canal2)
                .setSmallIcon(R.drawable.ic_baseline_priority_high_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentTitle("Furtuna Detectata!")
                .setContentText("Inchide ferestrele casei!!!!")
                .build();
        notificationManager.notify(1, builder);
        Log.d(TAG, "Furtuna");
    }
}