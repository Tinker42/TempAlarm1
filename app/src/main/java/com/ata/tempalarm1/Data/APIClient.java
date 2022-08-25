package com.ata.tempalarm1.Data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit;
    private static final String baseURL= "https://weatherapi-com.p.rapidapi.com";

    public static Retrofit getRetrofit() {

        if(retrofit == null){
            retrofit= new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
            //Retrofit.Builder() is the constructor for this Retrofit object
            //  behind the scenes, this object handles the requests made to the API with OKhttp2
            //  which is the object that's used for any internet request made while handling the boilerplate code
            //.baseUrl( specifies the baseUrl of the API
            //.addConverterFactory( tells Retrofit what Gson library to use so that the term from the API is in JSON converted to the Java object to compare to other Java objects
            //.build executes once all the parameters are set

        }
        return retrofit;
    }
}
