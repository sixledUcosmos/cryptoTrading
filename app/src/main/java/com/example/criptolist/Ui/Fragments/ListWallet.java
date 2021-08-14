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
import com.example.criptolist.Helper.Functions;
import com.example.criptolist.Model.Cryptos;
import com.example.criptolist.R;
import com.example.criptolist.Bd.SqliteBD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ListWallet extends Fragment {
  View view;
  ArrayList<Cryptos> cryptos =new ArrayList<>();
  private RequestQueue requestQueue;
  Context mcontext;
  double montototalwallet=0.000000;
  double valortotalwallet=0.000000;
  SQLiteDatabase db;
  Functions functions=new Functions();
  double priceBTC;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
// Inflate the layout for this fragment
    mcontext=container.getContext();
    requestQueue= Volley.newRequestQueue(mcontext);

    view = inflater.inflate(R.layout.fragment_wallet, container, false);
// get the reference of Button
    SqliteBD mDbHelper = new SqliteBD(mcontext);

    db = mDbHelper.getWritableDatabase();
    TextView valorw=view.findViewById(R.id.valorwallet);
    TextView mountw=view.findViewById(R.id.montowallet);
    cryptos.clear();
    getUpdateCryptos();
    //obtener las criptos activas
    Cursor us = db.rawQuery("SELECT  ref,name,symbol,image,amount,price FROM activos WHERE amount != '0.0' ", null);
    if (us.moveToFirst()) {
      do {
        String ref= us.getString(0);
        String name = us.getString(1);
        String symbol = us.getString(2);
        String image = us.getString(3);
        double amount=us.getDouble(4);
        double valortotal= us.getDouble(5)*amount;
        valortotalwallet+=valortotal;
        DecimalFormat formato = new DecimalFormat("0.00");
        String pricefactor=formato.format(valortotal);
        cryptos.add(new Cryptos(ref,name,symbol,pricefactor,image,amount));

      } while (us.moveToNext());
    }
    us.close();


//obtener las criptos no activas


    Cursor xc = db.rawQuery("SELECT  ref,name,symbol,image,amount FROM activos WHERE amount == '0.0' ", null);
    if (xc.moveToFirst()) {
      do {

        String ref= xc.getString(0);
        String name = xc.getString(1);
        String symbol = xc.getString(2);
        String image = xc.getString(3);
        double amount=xc.getDouble(4);
        cryptos.add(new Cryptos(ref,name,symbol,"0",image,amount));

      } while (xc.moveToNext());
    }
    xc.close();


    com.example.criptolist.Adapters.Wallet myAdapter = new com.example.criptolist.Adapters.Wallet(cryptos,mcontext, R.layout.model_crypto_wallet);
    ListView listview = view.findViewById(R.id.listallcryptos2);
    listview.setAdapter(myAdapter);
    if(priceBTC!=0.0 && valortotalwallet!=0.0){
      valorw.setText("$"+functions.FormatPriceAndConvertDouble(valortotalwallet));
      Log.i("entro","btc"+priceBTC);
      montototalwallet= functions.FormatPriceAndConvertDouble( valortotalwallet/priceBTC);
      mountw.setText(String.valueOf(montototalwallet));
    }

    return view;
  }
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    // Setup any handles to view objects here
    // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    montototalwallet=0.000000;
    valortotalwallet=0.0;
  }
  private void getUpdateCryptos(){
    String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false";
    JsonArrayRequest request = new JsonArrayRequest(url,
            jsonArray -> {
              for(int i = 0; i < jsonArray.length(); i++) {
                try {
                  JSONObject jsonObject = jsonArray.getJSONObject(i);
                  String symbol= jsonObject.getString("symbol");
                  String price= jsonObject.getString("current_price");
                  db.execSQL("UPDATE activos SET price='"+price+"' WHERE symbol='"+symbol+"'");
                  if(symbol.equals("btc")){
                    priceBTC=functions.ConvertDoubleString(price);

                  }
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
