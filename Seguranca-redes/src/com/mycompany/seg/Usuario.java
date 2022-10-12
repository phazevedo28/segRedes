/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seg;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author phazevedo28
 */
class Usuario implements Serializable {

    private String nome;
    private String senha;
    private byte[] salt;

    public Usuario(String nome, String senha, byte[] salt) {

        this.nome = nome;
        this.senha = senha;
        this.salt = salt;
    }

    Usuario( String nome,  String senha) {
        this.nome = nome;
        this.senha = senha;
    }

    public  String getNome() {
        return nome;
    }

    public  byte[] getSalt() {
        return salt;
    }

    public  String getSenha() {
        return senha;
    }

    public void setNome( String nome) {
        this.nome = nome;
    }

    public void setSenha( String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Usuario) {
            return this.getNome().equals(((Usuario) obj).getNome()) && this.getSenha().equals(((Usuario) obj).getSenha());
        } else {
            return false;
        }
    }
    
    public String toString(){
        return this.getNome() + this.getSenha();
    }

    
}
