package com.wasim.csdl.patient_chatapp.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAva_Wig0:APA91bGXIclyOazCxfo1SB1BMNmQOD4qxboKq9pE1Q5VLn23Caij8YyBQnfm5mLfMIQUDEd3yukEmYw8zDA0C6rznsKBA4S0xo0TSeT0908bjcHn68ew98iobQ8LMLvR3HH1Nd3m4bsC"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body); // Sender is another class  which contains the notification data
}
