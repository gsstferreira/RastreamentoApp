package br.usp.pcs2018.rastreamentopacotesapp.Global;


import br.usp.pcs2018.rastreamentopacotesapp.Models.User;

public abstract class Data {

    private static User usuario;

    public static User getUsuario() {
        return usuario;
    }

    public static void setUsuario(User usuario) {
        Data.usuario = usuario;
    }
}
