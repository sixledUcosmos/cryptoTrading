package com.example.criptolist.Ui.Activitis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.criptolist.R;

import es.dmoral.toasty.Toasty;

public class ScreenSplash extends AppCompatActivity {
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
makeRequest();

    }


    private void makeRequest(){
        String url = "https://api.coingecko.com/api/v3/ping";
        JsonObjectRequest
                request
                = new JsonObjectRequest(
                Request.Method.GET,
                url,null,

                response -> new Handler(Looper.getMainLooper()).postDelayed(
                        () -> {
                            Intent main = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(main);
                        },
                        1000), error -> {
                            Toasty.error(getApplicationContext(), "Check connection", Toast.LENGTH_SHORT, true).show();
                            Log.i("loadapp",error.toString());
                        });

       requestQueue.add(request);
    }


}