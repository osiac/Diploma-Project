package com.example.filip1;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Notificare extends Application {
    public static final String Canal1 = "Pericol";
    public static final String Canal2 = "Pericol2";
    @Override
    public void onCreate() {
        super.onCreate();
        Notificare();
        Notificare2();
    }
    private void Notificare(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel Pericol = new NotificationChannel(
                    Canal1,
                    "Incendiu Detectat!",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            Pericol.setDescription("Incendiu in interiorul casei!!!!");
            Pericol.canShowBadge();
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(Pericol);
        }
    }
    private void Notificare2(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel Pericol2 = new NotificationChannel(
                    Canal2,
                    "Furtuna Detectata!",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            Pericol2.setDescription("Inchide ferestrele casei!!!!");
            Pericol2.canShowBadge();
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(Pericol2);
        }
    }
}
