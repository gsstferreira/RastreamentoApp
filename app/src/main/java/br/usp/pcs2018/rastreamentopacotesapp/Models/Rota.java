package br.usp.pcs2018.rastreamentopacotesapp.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Rota {

    private Date DataInicio;
    private Date DataFim;
    private Endereco Origem;
    private Endereco Destino;

    private List<Localizacao> AmostrasLocalizacao;

    public Rota(){

        this.AmostrasLocalizacao = new ArrayList<>();
    }

    public Date getDataInicio() {
        return DataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        DataInicio = dataInicio;
    }

    public Date getDataFim() {
        return DataFim;
    }

    public void setDataFim(Date dataFim) {
        DataFim = dataFim;
    }

    public Endereco getOrigem() {
        return Origem;
    }

    public void setOrigem(Endereco origem) {
        Origem = origem;
    }

    public Endereco getDestino() {
        return Destino;
    }

    public void setDestino(Endereco destino) {
        Destino = destino;
    }

    public List<Localizacao> getAmostrasLocalizacao() {
        return AmostrasLocalizacao;
    }

    public void setAmostrasLocalizacao(List<Localizacao> amostrasLocalizacao) {
        AmostrasLocalizacao = amostrasLocalizacao;
    }
}
