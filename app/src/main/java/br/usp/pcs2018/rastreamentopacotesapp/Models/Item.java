package br.usp.pcs2018.rastreamentopacotesapp.Models;

public class Item {

    private String Descricao;
    private int Quantidade;

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public int getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(int quantidade) {
        Quantidade = quantidade;
    }
}
