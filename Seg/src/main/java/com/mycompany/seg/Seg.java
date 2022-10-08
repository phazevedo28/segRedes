/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.seg;

import java.util.ArrayList;

import java.util.Scanner;

public class Seg {

    static ArrayList<Object> usuarios = new ArrayList();

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Digite o salt(teste):");
        String salt = input.next();
        System.out.println("Digite seu nome: ");
        String nomePlano = input.next();
        System.out.println("Digite sua senha: ");
        String senhaPlana = input.next();
        input.close();
        Usuario usuarioAtual = new Usuario(salt, nomePlano, senhaPlana);
        if (estaNaLista(usuarioAtual)) {

        } else {
            cadastrarUsuario(usuarioAtual);
        }
        testeImprimirArquivo();
    }

    public static boolean estaNaLista(Usuario usuarioAtual) {
        usuarios = Empacotamento.lerArquivoBinario("/home/phazevedo28/Documentos/seg");

        int i = 1;
        for (Object item : usuarios) {

            if (usuarioAtual.equals(item)) {
                System.out.println("");
                System.out.println("usuário: " + ((Usuario) item).getNome() + " já cadastrado");
                return true;

            }
        }
        return false;
    }

    public static void cadastrarUsuario(Usuario usuarioAtual) {
        usuarios.add(usuarioAtual);
        Empacotamento.gravarArquivoBinario(usuarios, "/home/phazevedo28/Documentos/seg");
    }

    public static void testeImprimirArquivo() {
        int i = 1;
        for (Object item : usuarios) {

            System.out.println("");
            System.out.printf("Objeto nro....: %d.\n", i++);
            System.out.println(((Usuario) item).getSalt());
            System.out.println(((Usuario) item).getNome());
            System.out.println(((Usuario) item).getSenha());
            System.out.println("");

        }
    }
}
