package br.usp.pcs2018.rastreamentopacotesapp.Global;


import br.usp.pcs2018.rastreamentopacotesapp.Models.Usuario;

public abstract class Data {

    private static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Data.usuario = usuario;
    }
}
