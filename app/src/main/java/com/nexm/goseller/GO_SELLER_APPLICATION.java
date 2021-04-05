package com.nexm.goseller;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class GO_SELLER_APPLICATION extends Application {

    public static String sellerID,sellerName;
    public static int noOfProducts = 0 , productsLimit = 3;
    public static DatabaseReference reference;

    @Override
    public void onCreate(){
        super.onCreate();
        reference = FirebaseDatabase.getInstance().getReference();
    }
    public static float round(float d){

        int pow = 10;
        for(int i = 1; i<1;i++){
            pow *= 10;}
        float temp = d * pow;
        return (float)(int)((temp - (int)temp) > 0.5f ? temp+1 : temp) / pow;

    }
}
