/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seg;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author phazevedo28
 */
class UsuarioPlano implements Serializable {

   
    private String nome;
    private String senha;

    public UsuarioPlano( String nome, String senha) {
        //this.salt = salt;
        this.nome = nome;
        this.senha = senha;
    }

    public UsuarioPlano() {
    }



    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

  

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UsuarioPlano) {
            return this.getNome().equals(((UsuarioPlano) obj).getNome()) && this.getSenha().equals(((UsuarioPlano) obj).getSenha());
        } else {
            return false;
        }
    }

  

}
