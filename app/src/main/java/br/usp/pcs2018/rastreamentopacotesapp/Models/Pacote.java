package br.usp.pcs2018.rastreamentopacotesapp.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pacote {

    private String PacoteId;
    private String Remetente;
    private String Destinatario;
    private Date DataPostagem;
    private List<Rota> Rotas;
    private boolean Entregue;
    private Endereco Destino;

    public Pacote(){
        this.Rotas = new ArrayList<>();
    }

    public String getPacoteId() {
        return PacoteId;
    }

    public void setPacoteId(String pacoteId) {
        this.PacoteId = pacoteId;
    }

    public String getRemetente() {
        return Remetente;
    }

    public void setRemetente(String remetente) {
        this.Remetente = remetente;
    }

    public String getDestinatario() {
        return Destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.Destinatario = destinatario;
    }

    public Date getDataPostagem() {
        return DataPostagem;
    }

    public void setDataPostagem(Date dataPostagem) {
        this.DataPostagem = dataPostagem;
    }

    public List<Rota> getRotas() {
        return Rotas;
    }

    public void setRotas(List<Rota> rotas) {
        this.Rotas = rotas;
    }

    public boolean isEntregue() {
        return Entregue;
    }

    public void setEntregue(boolean entregue) {
        Entregue = entregue;
    }

    public Endereco getDestino() {
        return Destino;
    }

    public void setDestino(Endereco destino) {
        Destino = destino;
    }
}
