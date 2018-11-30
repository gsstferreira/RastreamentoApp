package br.usp.pcs2018.rastreamentopacotesapp.Models;

import java.util.Date;

public class Usuario {

    private String Nome;
    private String UsuarioId;
    private String Email;
    private Date DataCadastro;

    public Usuario(){}


    public String getUsuarioId() {
        return UsuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.UsuarioId = usuarioId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public Date getDataCadastro() {
        return DataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.DataCadastro = dataCadastro;
    }
}
