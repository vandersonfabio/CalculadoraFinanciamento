package com.vandersonfabio.calculadorafinanciamento.model;

import java.util.ArrayList;

public class Parcela {

    private int numero;
    private double prestacao;
    private double juros;
    private double amortizacao;
    private double saldoDevedor;
    private double somatorio;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getPrestacao() {
        return prestacao;
    }

    public void setPrestacao(double prestacao) {
        this.prestacao = prestacao;
    }

    public double getJuros() {
        return juros;
    }

    public void setJuros(double juros) {
        this.juros = juros;
    }

    public double getAmortizacao() {
        return amortizacao;
    }

    public void setAmortizacao(double amortizacao) {
        this.amortizacao = amortizacao;
    }

    public double getSaldoDevedor() {
        return saldoDevedor;
    }

    public void setSaldoDevedor(double saldoDevedor) {
        this.saldoDevedor = saldoDevedor;
    }

    public double getSomatorio() {
        return somatorio;
    }

    public void setSomatorio(double somatorio) {
        this.somatorio = somatorio;
    }

    public ArrayList<Parcela> getParcelasSAC(double capital, double taxa, int tempo){

        //Lista que irá armazezar todas as parcelas
        ArrayList<Parcela> listaParcelas = new ArrayList<>();

        //Atributos adicionais da PARCELA
        double prestacao = 0.0;
        double juros = 0.0;
        double amortizacao = 0.0;
        double saldoDevedor = capital;
        double somatorio = 0.0;

        //Parcela auxiliar para capturar os dados iniciais
        Parcela p0 = new Parcela();
        p0.setNumero(0);
        p0.setPrestacao(prestacao);
        p0.setJuros(juros);
        p0.setAmortizacao(amortizacao);
        p0.setSaldoDevedor(saldoDevedor);
        p0.setSomatorio(somatorio);
        listaParcelas.add(p0);

        //AS AMORTIZAÇÕES SÃO FIXAS
        amortizacao = capital/tempo;
        for(int i = 1; i <= tempo; i++){
            Parcela parcela = new Parcela();

            juros = taxa*saldoDevedor;
            prestacao = amortizacao + juros;
            saldoDevedor -= amortizacao;
            somatorio += prestacao;

            if(saldoDevedor < 0.0) {
                saldoDevedor *= -1;
            }

            parcela.setNumero(i);
            parcela.setPrestacao(prestacao);
            parcela.setJuros(juros);
            parcela.setAmortizacao(amortizacao);
            parcela.setSaldoDevedor(saldoDevedor);
            parcela.setSomatorio(somatorio);

            listaParcelas.add(parcela);
        }
        return listaParcelas;
    }

    public ArrayList<Parcela> getParcelasPrice(double capital, double taxa, int tempo){

        //Lista que irá armazezar todas as parcelas
        ArrayList<Parcela> listaParcelas = new ArrayList<>();

        //Atributos adicionais da PARCELA
        double prestacao = 0.0;
        double juros = 0.0;
        double amortizacao = 0.0;
        double saldoDevedor = capital;
        double somatorio = 0.0;

        //Parcela auxiliar para capturar os dados iniciais
        Parcela p0 = new Parcela();
        p0.setNumero(0);
        p0.setPrestacao(prestacao);
        p0.setJuros(juros);
        p0.setAmortizacao(amortizacao);
        p0.setSaldoDevedor(saldoDevedor);
        p0.setSomatorio(somatorio);
        listaParcelas.add(p0);

        //AS PRESTAÇÕES SÃO FIXAS
        prestacao = capital*taxa/(1-Math.pow(1+taxa,(-tempo)));
        for(int i = 1; i <= tempo; i++){
            Parcela parcela = new Parcela();
            juros = taxa*saldoDevedor;
            amortizacao = prestacao - juros;
            saldoDevedor -= amortizacao;

            if(saldoDevedor < 0.0) {
                saldoDevedor *= -1;
            }

            somatorio += prestacao;

            parcela.setNumero(i);
            parcela.setPrestacao(prestacao);
            parcela.setJuros(juros);
            parcela.setAmortizacao(amortizacao);
            parcela.setSaldoDevedor(saldoDevedor);
            parcela.setSomatorio(somatorio);

            listaParcelas.add(parcela);
        }

        return listaParcelas;
    }
}
