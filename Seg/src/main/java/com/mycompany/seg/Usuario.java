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
class Usuario implements Serializable {

    private String salt;
    private String nome;
    private String senha;

    public Usuario(String salt, String nome, String senha) {
        this.salt = salt;
        this.nome = nome;
        this.senha = senha;
    }

    public Usuario() {
    }

    public String getSalt() {
        return salt;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Usuario) {
            return this.getSalt().equals(((Usuario) obj).getSalt()) && this.getNome().equals(((Usuario) obj).getNome()) && this.getSenha().equals(((Usuario) obj).getSenha());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.salt);
        hash = 53 * hash + Objects.hashCode(this.nome);
        hash = 53 * hash + Objects.hashCode(this.senha);
        return hash;
    }

}
