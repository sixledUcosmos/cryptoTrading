package com.example.criptolist.Ui.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.criptolist.Model.History;
import com.example.criptolist.R;
import com.example.criptolist.Bd.SqliteBD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class Home extends Fragment {
  View view;
  Context mcontext;
  TextView viewtotal;
  double monney;
  private RequestQueue requestQueue;
  SQLiteDatabase db;
  ArrayList<History> histories =new ArrayList<>();
  private ListView listview;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
// Inflate the layout for this fragment
    mcontext=container.getContext();
    requestQueue= Volley.newRequestQueue(mcontext);
    view = inflater.inflate(R.layout.fragment_home, container, false);
    SqliteBD mDbHelper = new SqliteBD(mcontext);

    viewtotal =view.findViewById(R.id.viewtotal);
// get the reference of Button
    db = mDbHelper.getWritableDatabase();
    Cursor us = db.rawQuery("SELECT  money,generate,pay,install FROM usuario", null);
    if (us.moveToFirst()) {
      do {
         monney = us.getDouble(0);
        double generate = us.getDouble(1);
        String pay = us.getString(2);
        boolean install= Boolean.parseBoolean(us.getString(3));
        if(install){
          getRequestCryptos();
          db.execSQL("UPDATE usuario SET  install='"+false+"' ");
        }
        SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = today.format(new Date());
        if(!pay.equals(date)){
          monney+=generate;

          db.execSQL("UPDATE usuario SET  money='"+monney+"' ,pay='" +date +"' ");
          Log.i("moneyview", "entro");
          db.execSQL("INSERT INTO history (name,description,value) " + "VALUES ('Stalking usd','recompensa','"+generate+"')");
        }
        Log.i("moneyviewxxx", String.valueOf(monney));
        DecimalFormat formato = new DecimalFormat("0.00");
        String changefactor24="U$"+formato.format(monney);
        viewtotal.setText(changefactor24);

      } while (us.moveToNext());
    }
    us.close();

    return view;
  }
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    // Setup any handles to view objects here
    // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    histories.clear();
    Cursor xc = db.rawQuery("SELECT  name,description,value FROM history ORDER BY id DESC ", null);
    if (xc.moveToFirst()) {
      do {
        String name = xc.getString(0);
        String description = xc.getString(1);
        double value=xc.getDouble(2);

        histories.add(new History(name,description,value));

      } while (xc.moveToNext());
    }
    xc.close();
    com.example.criptolist.Adapters.History myAdapter = new com.example.criptolist.Adapters.History(histories,mcontext, R.layout.model_history);
    listview =  view.findViewById(R.id.hlist);
    listview.setAdapter(myAdapter);
  }
  private void getRequestCryptos(){
    String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false";
    JsonArrayRequest request = new JsonArrayRequest(url,
            jsonArray -> {
              for(int i = 0; i < jsonArray.length(); i++) {
                try {
                  JSONObject jsonObject = jsonArray.getJSONObject(i);
                  String ref= jsonObject.getString("id");
                  String name= jsonObject.getString("name");
                  String symbol= jsonObject.getString("symbol");
                  String image= jsonObject.getString("image");
                  db.execSQL("INSERT INTO activos (ref,name,symbol,image,amount) " + "VALUES ('"+ref+"','"+name+"', '"+symbol+"','"+image+"','0.0000')");

                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

            }, error -> {
              Toasty.error(mcontext, "Check connection", Toast.LENGTH_SHORT, true).show();
              Log.i("loadapp",error.toString());
            });

    requestQueue.add(request);
  }
}
