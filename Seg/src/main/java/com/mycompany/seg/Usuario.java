/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seg;

import java.io.Serializable;

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


}
