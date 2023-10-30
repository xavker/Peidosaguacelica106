package com.example.peidosaguacelica106.Provider;



import com.example.peidosaguacelica106.Datos.FCMBody;
import com.example.peidosaguacelica106.Datos.FCMResponse;
import com.example.peidosaguacelica106.retrofit.IFCMApi;
import com.example.peidosaguacelica106.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {
    }

    public Call<FCMResponse> sendNotification(FCMBody body) {
        return RetrofitClient.getClientObject(url).create(IFCMApi.class).send(body);
    }
}
