/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.seg;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.security.Security;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;

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

        for (Object item : usuarios) {
            if (usuarioAtual.equals(item)) {
                System.out.println("");
                System.out.println("usuário: " + ((Usuario) item).getNome() + " já cadastrado");
                return true;
            }
        }
        return false;
    }

    public static void cadastrarUsuario(Usuario usuarioAtual) throws NoSuchAlgorithmException {
        byte[] salt = gerarSalt();
         int costParameter = 2048; // exemplo: 2048 (afeta uso de memória e CPU)

        int blocksize = 8; // exemplo: 8

        int parallelizationParam = 1; // exemplo: 1

        byte[] derivedKeyFromScrypt;
        derivedKeyFromScrypt = SCRYPT.useScryptKDF(password.toCharArray(), salt, 
                costParameter,
                blocksize, parallelizationParam);
        usuarios.add(usuarioAtual);
        Empacotamento.gravarArquivoBinario(usuarios, "/home/phazevedo28/Documentos/seg");
    }

    public static byte[] gerarSalt() throws NoSuchAlgorithmException {
         int addProvider;
        addProvider = Security.addProvider(new BouncyCastleFipsProvider());
        if (Security.getProvider("BCFIPS") == null) {
            System.out.println("Bouncy Castle provider NAO disponivel");
        } else {
            System.out.println("Bouncy Castle provider esta disponivel");
        }

        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
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
