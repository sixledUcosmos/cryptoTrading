package com.example.criptolist.Ui.Activitis;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.criptolist.Helper.Functions;
import com.example.criptolist.R;
import com.example.criptolist.Bd.SqliteBD;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class Market extends AppCompatActivity {
   RequestQueue requestQueue;
  String crypto;
  TextView priceview;
  TextView pricechangeview;
  SqliteBD mDbHelper = new SqliteBD(this);
  SQLiteDatabase db;
  String price;
  int position=0;
  double monney;
  EditText priceedit;
  EditText cantidadedit;
  Button marketbuy;
  Button marketsell;
  TextView totalview;
  TextView marketmoney;
  DecimalFormat formato;
  String namecripto;
  String refcripto;
  String imagecripto;
  String  symbolcripto;
  double newamount;
  boolean showpricecrypto=true;
  double total;
  Functions functions=new Functions();
  Timer t;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_market);
    requestQueue= Volley.newRequestQueue(getApplicationContext());

    formato = new DecimalFormat("0.000000");
    refcripto = getIntent().getStringExtra("refcripto");
    namecripto = getIntent().getStringExtra("namecripto");
    imagecripto = getIntent().getStringExtra("imagecripto");
    symbolcripto = getIntent().getStringExtra("symbolcripto");

    TextView marketname=findViewById(R.id.marketname);
    marketname.setText(namecripto);
     marketbuy=findViewById(R.id.marketbuy);
     marketsell=findViewById(R.id.marketsell);
    crypto=refcripto;
    Log.i("criptoname",crypto);
     priceview= findViewById(R.id.marketnameprice);
    pricechangeview=findViewById(R.id.marketnamechange);
    TabLayout change=findViewById(R.id.marketchange);
    marketmoney =findViewById(R.id.markettotalusd);
    totalview=findViewById(R.id.textView9);
     priceedit=findViewById(R.id.marketprice);

    // obtener cantidad de plata
    db = mDbHelper.getWritableDatabase();
    Cursor us = db.rawQuery("SELECT  money FROM usuario", null);
    if (us.moveToFirst()) {
      do {
        monney = us.getDouble(0);
        String tener="Tienes $ "+monney;
        marketmoney.setText(tener);
      } while (us.moveToNext());
    }
    us.close();
    cantidadedit=findViewById(R.id.markettotal);
    cantidadedit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if(!cantidadedit.getText().toString().isEmpty()){

         double  cantidad =functions.ConvertDoubleString(cantidadedit.getText().toString());
         double precio=Double.parseDouble(price);
          String changefactor24= formato.format(cantidad*precio);
        total=functions.FormatPriceAndConvertDouble(cantidad*precio) ;
          totalview.setText("Total "+changefactor24);
        }

      }
    });

    // al apretar boton comprar
    marketbuy.setOnClickListener(v -> {

        //compra
      if( !priceedit.getText().toString().isEmpty()) {

        double getprice=functions.ConvertDoubleString( priceedit.getText().toString());
        formato = new DecimalFormat("0.00");
        double pricefactor=functions.FormatPriceAndConvertDouble(Double.parseDouble(price));
        if (getprice < pricefactor) {
          priceedit.setText(price);
          Toasty.warning(getApplicationContext(), "El precio tiene que ser mayor o igual", Toast.LENGTH_SHORT, true).show();
        } else {
          if (!cantidadedit.getText().toString().isEmpty()) {
            monney = Double.parseDouble(formato.format(monney).replaceAll(",", "."));
            if (monney >= total) {

              monney = Double.parseDouble(formato.format(monney - total).replaceAll(",", "."));
              marketmoney.setText("Tienes $ " + monney);

              Cursor ax = db.rawQuery("SELECT  amount FROM activos WHERE symbol='" + symbolcripto + "' ", null);
              if (ax.moveToFirst()) {
                do {
                  double amount = ax.getDouble(0);
                  newamount = Double.parseDouble(cantidadedit.getText().toString().replaceAll(",", "."));
                  newamount+=amount;
                  db.execSQL("UPDATE activos SET amount='" + newamount + "' WHERE symbol='" + symbolcripto + "'");
                  db.execSQL("INSERT INTO history (name,description,value) " + "VALUES ('Compra ','"+namecripto+"','"+total+"')");
                  db.execSQL("UPDATE usuario SET money='" + monney + "'");
                  priceedit.setText("");
                  cantidadedit.setText("");
                  totalview.setText("Total 0.000000");
                  Toasty.success(getApplicationContext(), "Comprado", Toast.LENGTH_SHORT, true).show();
                } while (ax.moveToNext());
              }

              ax.close();

            } else {
              Toasty.warning(getApplicationContext(), "Dinero insuficiente", Toast.LENGTH_SHORT, true).show();
            }
          }else {
            Toasty.warning(getApplicationContext(), "Se requier cantidad", Toast.LENGTH_SHORT, true).show();
          }
        }
      }else {
        Toasty.warning(getApplicationContext(), "Se requier precio", Toast.LENGTH_SHORT, true).show();
      }

    });
    // al apretar boton vender
    marketsell.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //compra
        if( !priceedit.getText().toString().isEmpty()) {

          double getprice = Double.parseDouble(priceedit.getText().toString().replaceAll(",", "."));
          if (getprice > Double.parseDouble(price)) {
            priceedit.setText(price);
            Toasty.warning(getApplicationContext(), "El precio tiene que ser menor o igual", Toast.LENGTH_SHORT, true).show();
          } else {
            if (!cantidadedit.getText().toString().isEmpty()) {
              double caintidad =functions.ConvertDoubleString( cantidadedit.getText().toString());
              if (caintidad > 0.000000) {
                Cursor jk = db.rawQuery("SELECT  amount FROM activos WHERE symbol='" + symbolcripto + "' ", null);
                if (jk.moveToFirst()) {
                  do {
                    String amount = jk.getString(0);
                    double amounttotal = Double.parseDouble(amount);
                    double caintidadnew =functions.ConvertDoubleString(cantidadedit.getText().toString());
                    if (amounttotal <= caintidadnew && amounttotal!=0.00) {
                      double totalamount = caintidadnew - amounttotal;
                      db.execSQL("UPDATE activos SET amount='" + totalamount + "' WHERE symbol='" + symbolcripto + "'");
                      db.execSQL("INSERT INTO history (name,description,value) " + "VALUES ('Venta ','"+namecripto+"','"+total+"')");
                      monney += caintidadnew * Double.parseDouble(price);
                    }

                  } while (jk.moveToNext());
                }
                db.execSQL("UPDATE usuario SET money='" + monney + "'");
                jk.close();
                priceedit.setText("");
                cantidadedit.setText("");
                totalview.setText("Total 0.000000");
                String tener="Tienes $ "+monney;
                Toasty.success(getApplicationContext(), "Vendido", Toast.LENGTH_SHORT, true).show();
              }
            }else {
              Toasty.warning(getApplicationContext(), "Se requier cantidad", Toast.LENGTH_SHORT, true).show();
            }
          }
        }else {
          Toasty.warning(getApplicationContext(), "Se requier precio", Toast.LENGTH_SHORT, true).show();
        }
      }
    });
    // selecionar venta o compra
    change.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        Log.i("tab", String.valueOf(tab.getPosition()));
        position=tab.getPosition();
        if(position==0){
          marketbuy.setVisibility(View.VISIBLE);
          marketsell.setVisibility(View.GONE);
        }else {
          marketbuy.setVisibility(View.GONE);
          marketsell.setVisibility(View.VISIBLE);
          Cursor hg = db.rawQuery("SELECT  amount FROM activos WHERE symbol='"+symbolcripto+"' ", null);
          if (hg.moveToFirst()) {
            do {
              double amount = hg.getDouble(0);
              formato = new DecimalFormat("0.000000");
              String pricefactor=formato.format(Double.valueOf(amount));
              cantidadedit.setText(pricefactor);
            } while (hg.moveToNext());
          }
          db.execSQL("UPDATE usuario SET money='"+monney+"'");
          hg.close();

        }

      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    });

     t = new Timer();
//Set the schedule function and rate
    t.scheduleAtFixedRate(new TimerTask() {

                            @Override
                            public void run() {
                              getRequestPriceCryptos();
                              //Called each time when 1000 milliseconds (1 second) (the period parameter)
                            }

                          },
            0,

            3000);
  }
  @Override
  protected   void onDestroy() {
    super.onDestroy();
    if (t!= null) {
      t.cancel();
      Log.i("Main", "cancel timer");
      t = null;
    }

  }

public void getRequestPriceCryptos(){
    String url = "https://api.coingecko.com/api/v3/simple/price?ids="+crypto+"&vs_currencies=usd&include_24hr_change=true";
    Log.i("url",url);
  JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,null,
          new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

              try {
                JSONObject jsonObject = response.getJSONObject(crypto);
                  price= jsonObject.getString("usd");

                String pricefactor=functions.FormatPrice(functions.ConvertDoubleString(price));
                priceview.setText(pricefactor);

                double change24= jsonObject.getDouble("usd_24h_change");
                DecimalFormat formato1 = new DecimalFormat("0.00");
                if(change24>=0){
                  pricechangeview.setTextColor(getResources().getColor(R.color.greenchange));
                }else {
                  pricechangeview.setTextColor(getResources().getColor(R.color.redchange));
                }
              String changefactor24="% "+formato1.format(change24);
                pricechangeview.setText(changefactor24);
                if(showpricecrypto){
                  priceedit.setText(pricefactor);
                  showpricecrypto=false;
                }

                } catch (JSONException e) {
                e.printStackTrace();
              }

            }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Toasty.error(getApplicationContext(), "Check connection", Toast.LENGTH_SHORT, true).show();
        Log.i("loadapp",error.toString());
      }

    });

    requestQueue.add(jsonObjectRequest);
  }
}