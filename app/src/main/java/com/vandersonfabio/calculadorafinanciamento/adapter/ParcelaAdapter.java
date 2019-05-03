package com.vandersonfabio.calculadorafinanciamento.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandersonfabio.calculadorafinanciamento.R;
import com.vandersonfabio.calculadorafinanciamento.model.Parcela;

import java.text.DecimalFormat;
import java.util.List;

public class ParcelaAdapter extends ArrayAdapter<Parcela> {

    private List<Parcela> items;

    public ParcelaAdapter(Context context, int textViewResourceId, List<Parcela> items){
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null){
            Context ctx = getContext();
            LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.linha_parcela, null);
        }

        Parcela parcela = items.get(position);

        DecimalFormat df = new DecimalFormat("###,##0.00");

        if(parcela != null){
            ((TextView) view.findViewById(R.id.tvNumero)).setText(Integer.toString(parcela.getNumero()));

            ((TextView) view.findViewById(R.id.tvPrestacao)).setText("R$ "+ df.format(parcela.getPrestacao()));
            ((TextView) view.findViewById(R.id.tvSaldoDevedor)).setText("R$ "+ df.format(parcela.getSaldoDevedor()));
        }

        return view;
    }
}
