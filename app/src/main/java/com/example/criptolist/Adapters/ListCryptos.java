package com.example.criptolist.Adapters;

import android.content.Context;
import android.util.Log;
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

public class ListCryptos extends BaseAdapter {
    private ArrayList<Cryptos> cryptolist;
    Context mContext;
    Functions functions=new Functions();
    int layaout;
    DecimalFormat formato;
    public ListCryptos(ArrayList<Cryptos> cryptolist, Context mContext, int layaout) {
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

        v= layoutInflater.inflate(R.layout.model_crypto, null);
        // Valor actual según la posición

        String Name  = cryptolist.get(position).getName();
        String Symbol  = cryptolist.get(position).getSymbol();
        String Image  = cryptolist.get(position).getImage();
        String getdrawable=Symbol;
        int iconDrawable=mContext.getResources().getIdentifier(getdrawable, "drawable", mContext.getPackageName());

        double price=   functions.ConvertDoubleString(cryptolist.get(position).getPrice());
        // Referenciamos el elemento a modificar y lo rellenamos
        ImageView icon=  v.findViewById(R.id.imagecryptolist);
        TextView textName =  v.findViewById(R.id.namecryptolist);
        TextView textSymbol=  v.findViewById(R.id.symbolcryptolist);
        TextView textPrice=v.findViewById(R.id.amountlist);
        textName.setText(Name);
        textSymbol.setText(Symbol);

        String pricefactor="$"+functions.FormatPrice(price);
        textPrice.setText(pricefactor);
        if(iconDrawable==0){
            Log.i("criptosnoimage",Symbol);
            Glide.with(mContext)
                    .load(Image)
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
