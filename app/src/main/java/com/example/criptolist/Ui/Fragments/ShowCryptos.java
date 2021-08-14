package com.example.criptolist.Ui.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.criptolist.Adapters.ListCryptos;
import com.example.criptolist.Model.Cryptos;
import com.example.criptolist.R;
import com.example.criptolist.Ui.Activitis.Market;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ShowCryptos extends Fragment {
  View view;
  ArrayList<Cryptos> cryptos =new ArrayList<>();
  private ListView listview;
  private RequestQueue requestQueue;
  Context mcontext;
  ProgressBar progresload;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
// Inflate the layout for this fragment
    mcontext=container.getContext();
    view = inflater.inflate(R.layout.fragment_cryptos, container, false);
// get the reference of Button
    requestQueue= Volley.newRequestQueue(mcontext);
    progresload =view.findViewById(R.id.loadcripto);
    return view;
  }
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    // Setup any handles to view objects here
    // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    progresload.setVisibility(View.VISIBLE);
    cryptos.clear();
    getRequestCryptos();
  }
  private void getRequestCryptos(){
    Log.i("loadgin","true");
    String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false";
    JsonArrayRequest request = new JsonArrayRequest(url,
            jsonArray -> {
              for(int i = 0; i < jsonArray.length(); i++) {
                try {
                  JSONObject jsonObject = jsonArray.getJSONObject(i);
                  String name= jsonObject.getString("name");
                  String ref= jsonObject.getString("id");
                  String symbol= jsonObject.getString("symbol");
                  String price= jsonObject.getString("current_price");
                  String image= jsonObject.getString("image");
                  cryptos.add(new Cryptos(ref,name,symbol,price,image,0.00));

                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
              ListCryptos myAdapter = new ListCryptos(cryptos,mcontext, R.layout.model_crypto);
              listview =  view.findViewById(R.id.listallcryptos);
              listview.setAdapter(myAdapter);
              listview.setOnItemClickListener((parent, view, position, id) -> {
                Intent market= new Intent(mcontext, Market.class);
                market.putExtra("refcripto", cryptos.get(position).getRef());
                market.putExtra("namecripto", cryptos.get(position).getName());
                market.putExtra("imagecripto", cryptos.get(position).getImage());
                market.putExtra("symbolcripto", cryptos.get(position).getSymbol());
                startActivity(market);
              });
              progresload.setVisibility(View.GONE);
            }, error -> {
              Toasty.error(mcontext, "Check connection", Toast.LENGTH_SHORT, true).show();
              Log.i("loadapp",error.toString());
            });

    requestQueue.add(request);
  }
}
