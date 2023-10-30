package com.example.peidosaguacelica106.retrofit;



import com.example.peidosaguacelica106.Datos.FCMBody;
import com.example.peidosaguacelica106.Datos.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA6ZsI5Cs:APA91bGy7LDO_tPzh2PJna1uAR7GLzEk-E-x6sOUvO4cfSsgF3sDPHD8lAm0uR3St5F6RaNzrGtngtAV6WWAxtvOX104SeIB5SQhoI1oIbbf7YZxRp300vY2NcW0vU8bY5hgP5W2Rc6B"

            })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);

}
