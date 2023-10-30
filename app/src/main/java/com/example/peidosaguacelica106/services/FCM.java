package com.example.peidosaguacelica106.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCM extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("token","mi tocken es: "+s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from=remoteMessage.getFrom();
        Log.e("TAG","Mensaje recibido de : "+from);

        if(remoteMessage.getNotification()!=null){
            Log.e("TAG","Titulo es : "+remoteMessage.getNotification().getTitle());
            Log.e("TAG","Detalle es : "+remoteMessage.getNotification().getBody());
        }
    }
}
