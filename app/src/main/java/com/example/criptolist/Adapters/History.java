package com.example.criptolist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.criptolist.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class History extends BaseAdapter {
    private ArrayList<com.example.criptolist.Model.History> history;
    Context mContext;
    int layaout;
    DecimalFormat formato;
    public History(ArrayList<com.example.criptolist.Model.History> history, Context mContext, int layaout) {
        this.history = history;
        this.mContext = mContext;
        this.layaout = layaout;
    }

    @Override
    public int getCount() {
        return this.history.size();
    }

    @Override
    public Object getItem(int position) {
        return history.get(position);
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

        v= layoutInflater.inflate(R.layout.model_history, null);
        // Valor actual según la posición

        String name  = history.get(position).getName();
        String description  = history.get(position).getDescription();
        double value  = history.get(position).getValor();
        // Referenciamos el elemento a modificar y lo rellenamos
        TextView textName =  v.findViewById(R.id.hname);
        TextView textDescription=  v.findViewById(R.id.hdescription);
        TextView textValue=v.findViewById(R.id.hvalue);


        textName.setText(name);
        textDescription.setText(description);
        DecimalFormat formato = new DecimalFormat("0.00");
        String valuefactor="$"+formato.format(value);
       textValue.setText(valuefactor);
        //Devolvemos la vista inflada
        return v;
    }
}
