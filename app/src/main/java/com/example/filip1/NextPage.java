package com.example.filip1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.android.service.MqttAndroidClient;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.*;

public class NextPage extends AppCompatActivity {
    private static final String TAG="Validare";
    Button btn_ac;
    Button btn_led_exterior;
    Button btn_led_interior;
    Button pompa;
    ImageView venti;
    ImageView led_interior;
    ImageView led_exterior;
    ImageView pomp;
    String[] mesaj={"Oprit_ventilator","Oprit_pompa","Oprit_led_interior","Oprit_led_exterior"};
    Button  MainActivityback;
   // public static String btn_fan;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextpage);
        btn_ac= findViewById(R.id.btn_ac);
        btn_led_interior=findViewById(R.id.btn_led_interior);
        btn_led_exterior=findViewById(R.id.btn_led_exterior);
        pompa=findViewById(R.id.pompa);
        led_interior=findViewById(R.id.imageView10);
        led_exterior=findViewById(R.id.imageView8);
        venti =findViewById(R.id.imageView6);
        pomp =findViewById(R.id.imageView);
        if (MainActivity.getVentilator()==true){
            btn_ac.setBackgroundColor(Color.GREEN);
            venti.setImageResource(R.drawable.ventilator2);
        }
        else {btn_ac.setBackgroundColor(Color.BLACK);
            venti.setImageResource(R.drawable.ventilator);
        }
        if (MainActivity.getPompa() == true){
            pompa.setBackgroundColor(Color.GREEN);
        }
        else {pompa.setBackgroundColor(Color.BLACK);}

        if(MainActivity.getLed()==0) {
            btn_led_interior.setBackgroundColor(Color.GREEN);
            led_interior.setImageResource(R.drawable.led2);
        }
        else{
            btn_led_interior.setBackgroundColor(Color.BLACK);
            led_interior.setImageResource(R.drawable.led);
        }

        if(MainActivity.getled2()==0) {
            btn_led_exterior.setBackgroundColor(Color.GREEN);
            led_exterior.setImageResource(R.drawable.led2);
        }
        else{
            btn_led_exterior.setBackgroundColor(Color.BLACK);
            led_exterior.setImageResource(R.drawable.led);
        }


        MainActivityback = findViewById(R.id.goToMainActivity);
    }
    public void goToMainActivity(View view){
        Intent myIntent = new Intent(NextPage.this, MainActivity.class);
        startActivity(myIntent);
    }
    public void Led(){
        if(MainActivity.getLed()==0) {
            btn_led_interior.setBackgroundColor(Color.GREEN);
            led_interior.setImageResource(R.drawable.led2);
            mesaj[2] = "Pornit_led_interior";
            mqtt_connect(mesaj[2]);
            MainActivity.setLed(1);
        }
        else{
            btn_led_interior.setBackgroundColor(Color.BLACK);
            led_interior.setImageResource(R.drawable.led);
            MainActivity.setLed(0);
            mesaj[2] = "Oprit_led_interior";
            mqtt_connect(mesaj[2]);
        }
    }
    public void Ven(){
        if (MainActivity.getVentilator()==true) {
            venti.setImageResource(R.drawable.ventilator2);
            MainActivity.setVentilator2(false);
            btn_ac.setBackgroundColor(Color.GREEN);
            mesaj[0]="Oprit_ventilator";
            mqtt_connect(mesaj[0]);
        }
        else {
            MainActivity.setVentilator2(true);
            venti.setImageResource(R.drawable.ventilator);
            btn_ac.setBackgroundColor(Color.BLACK);
            mesaj[0]="Pornit_ventilator";
            mqtt_connect(mesaj[0]);
        }
    }

    public void Pompa(){
        if (MainActivity.getPompa() == true) {
            MainActivity.setPompa2(false);
            pomp.setImageResource(R.drawable.poma2);
            pompa.setBackgroundColor(Color.BLACK);
            mesaj[1]="Oprit_pompa";
            mqtt_connect(mesaj[1]);
        }
        else {
            MainActivity.setPompa2(true);
            pomp.setImageResource(R.drawable.pompa);
            pompa.setBackgroundColor(Color.GREEN);
            mesaj[1]="Pornit_pompa";
            mqtt_connect(mesaj[1]);
        }
    }

    public void Led2(){
        if(MainActivity.getled2()==0) {
            btn_led_exterior.setBackgroundColor(Color.GREEN);
            led_exterior.setImageResource(R.drawable.led2);
            mesaj[3] = "Pornit_led_exterior";
            mqtt_connect(mesaj[3]);
            MainActivity.setLed2(1);
            //Probleme !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
        else{
            btn_led_exterior.setBackgroundColor(Color.BLACK);
            led_exterior.setImageResource(R.drawable.led);
            mesaj[3] = "Oprit_led_exterior";
            mqtt_connect(mesaj[3]);
            MainActivity.setLed2(0);
        }
    }

    public void Fan(View view){
        Ven();
    }
    public void Ledd(View view){
        Led();
    }
    public void Leddd(View view){
        Led2();
    }
    public void Pompa_apa(View view){
        Pompa();
    }
    public void mqtt_connect(String mesaj_trimis){
            String client_Id = MqttClient.generateClientId();
            MqttAndroidClient client =
                    new MqttAndroidClient(this.getApplicationContext(), "tcp://mqtt.beia-telemetrie.ro:1883", client_Id);
            try {
                IMqttToken token = client.connect();
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // We are connected
                        Log.d(TAG, "Conectat la Broker");
                        MqttMessage message =new MqttMessage(mesaj_trimis.getBytes());
                        message.setQos(0);
                        message.setRetained(false);
                        String topic="training/device/F";
                        try {
                            client.publish(topic, message);
                            Log.i(TAG, "Message published");

                            // client.disconnect();
                            //Log.i("mqtt", "client disconnected");

                        } catch (MqttPersistenceException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                        } catch (MqttException e) {
                            // TODO Auto-generated catch block
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
    }

}