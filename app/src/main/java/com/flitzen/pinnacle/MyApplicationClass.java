package com.flitzen.pinnacle;

import android.app.Application;
import android.content.Context;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplicationClass extends Application {

    public static final String BASE_URL = "https://pinnacletech.work/apis/";
    //public static final String BASE_URL = "https://pinnacle.zone//";
    //public static final String BASE_URL = "https://erp.pinnacletechnocrats.com/apis/";
    //https://erp.pinnacletechnocrats.com/apis/order/pending
    private static Retrofit retrofit = null;
    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        MyApplicationClass.context = getApplicationContext();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }


    //TODO For connection timeout
    /*if (t instanceof SocketTimeoutException)
    {
        // "Connection Timeout";
        System.out.println("=========Connection Timeout");
    }
                    else if (t instanceof IOException)
    {
        // "Timeout";
        System.out.println("=========Timeout");
    }
                    else
    {
        //Call was cancelled by user
        if(call.isCanceled())
        {
            System.out.println("=======Call was cancelled forcefully");
        }
        else
        {
            //Generic error handling
            System.out.println("===========Network Error :: " + t.getLocalizedMessage());
        }
    }*/

}
