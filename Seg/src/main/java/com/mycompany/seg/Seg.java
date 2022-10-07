/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.seg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author phazevedo28
 */
public class Seg {

    public static void main(String[] args) {

        ArrayList<Object> usuarios = new ArrayList();
        Scanner input = new Scanner(System.in);
        System.out.println("Digite o salt(teste):");
        String salt = input.next();
        System.out.println("Digite seu nome: ");
        String nomePlano = input.next();
        System.out.println("Digite sua senha: ");
        String senhaPlana = input.next();
        input.close();
        usuarios = Empacotamento.lerArquivoBinario("/home/phazevedo28/Documentos/seg");
        Usuario usuarioAtual = new Usuario(salt,nomePlano,senhaPlana);
        int i = 1;
        for (Object item : usuarios) {
            
            if(usuarioAtual.equals(item)){
                System.out.println("usuário:"+((Usuario) item).getNome()+"já cadastrado");
            }
            System.out.printf("Ficha nro....: %d.\n", i++);
            // ((Pessoa)item) - implementa o mecanismo de downcast, ou seja,
            //                  o objeto "item" declarado a partir da classe
            //                  base "Object" é referenciado como um objeto "Pessoa"
            System.out.println(((Usuario) item).getSalt());
            System.out.println(((Usuario) item).getNome());
            System.out.println(((Usuario) item).getSenha());

        }
        
        usuarios.add(usuarioAtual);
        Empacotamento.gravarArquivoBinario(usuarios, "/home/phazevedo28/Documentos/seg");
        /*usuarios = Empacotamento.lerArquivoBinario("/home/phazevedo28/Documentos/seg");

        int i = 1;
        for (Object item : usuarios) {
            System.out.printf("Ficha nro....: %d.\n", i++);
            // ((Pessoa)item) - implementa o mecanismo de downcast, ou seja,
            //                  o objeto "item" declarado a partir da classe
            //                  base "Object" é referenciado como um objeto "Pessoa"
            System.out.println(((Usuario) item).getSalt());
            System.out.println(((Usuario) item).getNome());
            System.out.println(((Usuario) item).getSenha());

        }

          File file = new File("/home/phazevedo28/Documentos/seg");
        Scanner scFile = new Scanner(file);
        try {
            scFile = new Scanner(file);
            while (scFile.hasNextLine()) {
                System.out.println(scFile.nextLine());
                List<File> usuarios = new ArrayList();
                usuarios.add(file);
                for(File usuario:usuarios){
                System.out.println(usuario);}

            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scFile.close();
        }*/
    }

}
