package com.example.criptolist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.criptolist.Helper.Functions;
import com.example.criptolist.Model.Cryptos;
import com.example.criptolist.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Wallet extends BaseAdapter {
    private ArrayList<Cryptos> cryptolist;
    Context mContext;
    Functions functions=new Functions();
    int layaout;
    DecimalFormat formato;
    public Wallet(ArrayList<Cryptos> cryptolist, Context mContext, int layaout) {
        this.cryptolist = cryptolist;
        this.mContext = mContext;
        this.layaout = layaout;
    }

    @Override
    public int getCount() {
        return this.cryptolist.size();
    }

    @Override
    public Object getItem(int position) {
        return cryptolist.get(position);
    }


    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Copiamos la vista
        View v = convertView;

        //Inflamos la vista con nuestro propio layout
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        v= layoutInflater.inflate(R.layout.model_crypto_wallet, null);
        // Valor actual según la posición
        String ref  = cryptolist.get(position).getRef();
        String Name  = cryptolist.get(position).getName();
        String Symbol  = cryptolist.get(position).getSymbol();
        String Image  = cryptolist.get(position).getImage();
        String amount= String.valueOf(cryptolist.get(position).getAmount());

        double price= functions.ConvertDoubleString(cryptolist.get(position).getPrice());

        String getdrawable=Symbol;
        int iconDrawable=mContext.getResources().getIdentifier(getdrawable, "drawable", mContext.getPackageName());


        // Referenciamos el elemento a modificar y lo rellenamos
        ImageView icon=  v.findViewById(R.id.wimagecryptolist);
        TextView textName =  v.findViewById(R.id.hname);
        TextView textSymbol=  v.findViewById(R.id.hdescription);
        TextView amountlist=v.findViewById(R.id.hvalue);
        TextView pricelist=v.findViewById(R.id.wpricelist);

        textName.setText(Name);
        textSymbol.setText(Symbol);
formato = new DecimalFormat("0.000000");
        String pricefactor=formato.format(Double.valueOf(amount));
        amountlist.setText(pricefactor);

        if(price>0.00001){

            String pricetotal="$"+functions.FormatPrice(price);
            pricelist.setText(pricetotal);
        }

        if(iconDrawable==0){
            Glide.with(mContext)
                    .load(Image)
                    .apply(new RequestOptions().override(120, 120))
                    .into(icon);
        }else {
            Glide.with(mContext)
                    .load(iconDrawable)
                    .into(icon);
        }

        //Devolvemos la vista inflada
        return v;
    }
}
