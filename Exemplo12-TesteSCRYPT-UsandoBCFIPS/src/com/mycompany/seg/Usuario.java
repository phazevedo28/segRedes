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

    private byte[] nome;
    private byte[] senha;
    private byte[] salt;

    public Usuario(byte[] nome, byte[] senha, byte[] salt) {

        this.nome = nome;
        this.senha = senha;
        this.salt = salt;
    }

    public Usuario() {
    }

    Usuario(byte[] nome, byte[] senha) {
        this.nome = nome;
        this.senha = senha;
    }

    public byte[] getNome() {
        return nome;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getSenha() {
        return senha;
    }

    public void setNome(byte[] nome) {
        this.nome = nome;
    }

    public void setSenha(byte[] senha) {
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

    public String toString() {
        String imp = Hex.encodeHexString(this.nome) + "" + "" + Hex.encodeHexString(this.senha);
        return imp;
    }

}
