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

    List<Parcela> items;

    public ParcelaAdapter(Context context, int textViewResourceId, List<Parcela> items){
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if(v == null){
            Context ctx = getContext();
            LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.linha_parcela, null);
        }

        Parcela parcela = items.get(position);


        if(parcela != null){
            ((TextView) v.findViewById(R.id.tvNumero)).setText(Integer.toString(parcela.getNumero()));

            //String valorDevido = convertFloatToStringComVirgula(pagamento.getValorPagamento());
            DecimalFormat df = new DecimalFormat("###,##0.00");

            ((TextView) v.findViewById(R.id.tvPrestacao)).setText("R$ "+ df.format(parcela.getPrestacao()));
            ((TextView) v.findViewById(R.id.tvSaldoDevedor)).setText("R$ "+ df.format(parcela.getSaldoDevedor()));
        }

        return v;
    }

    public String convertFloatToStringComVirgula(float valor){
        float valorTemp = valor * 100;
        String stringTemp, stringResultado;
        stringTemp = String.valueOf(valorTemp);
        stringResultado = stringTemp.replace(".0", "");
        StringBuilder stringBuilder = new StringBuilder(stringResultado);
        stringBuilder.insert(stringResultado.length()-2, ",");

        return stringBuilder.toString();
    }
}
