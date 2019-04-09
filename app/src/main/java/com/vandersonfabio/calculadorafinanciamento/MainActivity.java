package com.vandersonfabio.calculadorafinanciamento;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandersonfabio.calculadorafinanciamento.adapter.ParcelaAdapter;
import com.vandersonfabio.calculadorafinanciamento.model.Parcela;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Button botaoCalcular;
    private EditText txtValorFinanciamento;
    private EditText txtTaxaJuros;
    private EditText txtTempo;
    private Spinner spinnerTabela;
    private RadioGroup rgGrupoPeriodo;
    private RadioButton rbPeriodoJuros;

    private double capital;
    private double taxa;
    private int tempo;

    String tipoAmortizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DEFINIR A ORIENTAÇÃO RETRATO
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        configurarBotoes();
    }

    public void configurarBotoes(){

        botaoCalcular = (Button) findViewById(R.id.btCalcularPrestacoes);
        txtValorFinanciamento = (EditText) findViewById(R.id.etValorFinanciamento);
        txtTaxaJuros = (EditText) findViewById(R.id.etTaxaJuros);
        txtTempo = (EditText) findViewById(R.id.etTempo);
        spinnerTabela = (Spinner) findViewById(R.id.spinnerAmortizacao);
        rgGrupoPeriodo = (RadioGroup) findViewById(R.id.rgPeriodo);

        spinnerTabela.setOnItemSelectedListener(this);
        carregarDadosSpinner();

        //MODIFICAR VALORES EM TEMPO DE EXECUÇÃO
        txtValorFinanciamento.addTextChangedListener(new TextWatcher() {

            protected String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //SE A STRING ATUAL FOR DIFERENTE DE ""
                if(!s.equals(current)){

                    txtValorFinanciamento.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[R,$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formattedString = NumberFormat.getCurrencyInstance().format((parsed/100));

                    current = formattedString;

                    txtValorFinanciamento.setText(formattedString);
                    txtValorFinanciamento.setSelection(formattedString.length());

                    txtValorFinanciamento.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //CLIQUE DO BOTÃO CALCULAR
        botaoCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String errorDescription = "";
                int countError = 0;

                if (txtValorFinanciamento.getText().toString().equals("")) {
                    countError++;
                    errorDescription = "Valor do financiamento ausente. Por favor, insira um valor válido.";
                } else if (txtValorFinanciamento.getText().toString().equals("R$0,00")) {
                    countError++;
                    errorDescription = "Valor do financiamento não pode ser R$ 0,00. Por favor, insira um valor válido.";
                } else if (txtTaxaJuros.getText().toString().equals("")) {
                    countError++;
                    errorDescription = "Taxa de juros ausente. Por favor, insira um valor válido.";
                } else if (txtTempo.getText().toString().equals("")) {
                    countError++;
                    errorDescription = "Nº de prestações ausente. Por favor, insira um valor válido.";
                } else if (txtTempo.getText().toString().equals("0")) {
                    countError++;
                    errorDescription = "Nº de prestações não pode ser 0. Por favor, insira um valor válido.";
                }


                if (countError > 0) {
                    final AlertDialog.Builder alertaErrors = new AlertDialog.Builder(MainActivity.this);
                    alertaErrors.setTitle("Atenção!")
                            .setIcon(R.drawable.ic_warning_orange_24dp)
                            .setMessage(errorDescription)
                            .setPositiveButton("Voltar e corrigir", null)
                            .create()
                            .show();
                } else {
                    realizarCalculo();
                }
            }
        });
    }

    public void realizarCalculo(){

        //Intanciando os alertas
        final AlertDialog.Builder alertaParcelas = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog.Builder alertaIndividual = new AlertDialog.Builder(MainActivity.this);

        //Pegando os valores das variáveis
        int rbSelecionado = rgGrupoPeriodo.getCheckedRadioButtonId();
        rbPeriodoJuros = (RadioButton) findViewById(rbSelecionado);
        capital = Double.parseDouble(txtValorFinanciamento.getText().toString().replaceAll("[R,$,.]", "")) / 100;
        taxa = Double.parseDouble(txtTaxaJuros.getText().toString()) / 100;
        tempo = Integer.parseInt(txtTempo.getText().toString());


        //Verificando o período da taxa de juros
        if (rbPeriodoJuros.getText().toString().equals("a.a.")) {
            taxa = Math.pow(1 + taxa, 1.0 / 12.0) - 1;
        }


        Parcela parcela = new Parcela();


        final ArrayList<Parcela> listaParcelas =
                tipoAmortizacao.equals("SAC") ?
                parcela.getParcelasSAC(capital, taxa, tempo) :
                parcela.getParcelasPrice(capital, taxa, tempo);

        //Adaptador para o ListView das parcelas
        ParcelaAdapter adapterParcela = new ParcelaAdapter(getBaseContext(), R.layout.linha_parcela, listaParcelas);

        //Criando o alerta de listagem de parcelas
        alertaParcelas
                .setTitle("Tabela selecionada: " + tipoAmortizacao)
                .setIcon(R.drawable.ic_attach_money_black_24dp);

        //Setando o clique sobre cada parcela do ListView
        alertaParcelas.setSingleChoiceItems(adapterParcela, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final Parcela parcelaSelecionada = (Parcela) listaParcelas.get(which);

                DecimalFormat df = new DecimalFormat("###,##0.00");

                alertaIndividual.setTitle("Detalhamento da parcela # " + parcelaSelecionada.getNumero());
                alertaIndividual.setMessage(
                        "Prestação: R$ " + df.format(parcelaSelecionada.getPrestacao()) +
                        "\nAmortização: R$ " + df.format(parcelaSelecionada.getAmortizacao()) +
                        "\nJuros: R$ " + df.format(parcelaSelecionada.getJuros()) +
                        "\nSaldo Devedor: R$ " + df.format(parcelaSelecionada.getSaldoDevedor())
                );
                alertaIndividual
                        .setPositiveButton("Voltar", null)
                        .setCancelable(false)
                        .create()
                        .show();
            }
        });

        String outraTabela = getOutraTabelaAmortizacao();

        alertaParcelas
                .setNegativeButton("Voltar", null)
                .setPositiveButton("Mudar para " + outraTabela, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mudarTabelaAmortizacao();
                    }
                })
                .create()
                .show();
    }

    public void carregarDadosSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (
                        this,
                        R.array.tabelas,
                        R.layout.support_simple_spinner_dropdown_item
                );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTabela.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(spinnerTabela.getItemAtPosition(position).toString().equals("Tabela SAC - Parcelas descrescentes")){
            tipoAmortizacao = "SAC";
        }
        else{
            tipoAmortizacao = "Price";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void mudarTabelaAmortizacao(){
        if(tipoAmortizacao.equals("SAC"))
            tipoAmortizacao = "Price";
        else
            tipoAmortizacao = "SAC";
        realizarCalculo();
    }

    public String getOutraTabelaAmortizacao(){
        if(tipoAmortizacao.equals("SAC"))
            return "Price";
        else
            return "SAC";
    }
}
