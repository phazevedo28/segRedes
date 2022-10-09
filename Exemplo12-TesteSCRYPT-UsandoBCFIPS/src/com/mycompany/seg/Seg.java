/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.seg;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.security.Security;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import org.apache.commons.codec.DecoderException;

public class Seg {

    static int costParameter = 2048; // exemplo: 2048 (afeta uso de memória e CPU)

    static int blocksize = 8; // exemplo: 8

    static int parallelizationParam = 1;

    private static final int MAC_SIZE = 128; // in bits

    // AES-GCM parameters
    public static final int AES_KEY_SIZE = 128; // in bits
    public static final int GCM_NONCE_LENGTH = 12; // in bytes
    public static final int GCM_TAG_LENGTH = 16; // in bytes

    static Integer iteracoes = 1000;

    static ArrayList<Object> usuarios = new ArrayList();

    public static void main(String[] args) throws NoSuchAlgorithmException {
        instanciaSecurityProvider();
        Scanner input = new Scanner(System.in);
        /*System.out.println("Digite o salt(teste):");
        String salt = input.next();*/
        System.out.println("Digite seu nome: ");
        String nomePlano = input.next();
        System.out.println("Digite sua senha: ");
        String senhaPlana = input.next();
        input.close();
        UsuarioPlano usuarioAtual = new UsuarioPlano(nomePlano, senhaPlana);
        boolean estaNalista = estaNaLista(usuarioAtual);
        if (estaNalista) {
            System.out.println("");
            System.out.println("Usuário já cadastrado");
        } else {
            cadastrarUsuario(usuarioAtual);
        }
        testeImprimirArquivo();
    }

    public static boolean estaNaLista(UsuarioPlano usuarioAtual) {

        usuarios = Empacotamento.lerArquivoBinario("/home/phazevedo28/Documentos/seg");

        for (Object item : usuarios) {

            try {
                System.out.println("O valor do Salt para na comparacao(estaNaLista) é: " + ((Usuario) item).getSalt());
                byte[] derivedKeyFromScrypt;
                String nome = usuarioAtual.getNome();
                derivedKeyFromScrypt = SCRYPT.useScryptKDF(nome.toCharArray(), ((Usuario) item).getSalt(),
                        costParameter,
                        blocksize, parallelizationParam);
                String IV = gerarIV(usuarioAtual.getSenha(), Hex.encodeHexString(((Usuario) item).getSalt()), iteracoes);

                Usuario usuario = new Usuario(derivedKeyFromScrypt, gerarGCM(((Usuario) item).getSalt(), usuarioAtual.getSenha(), IV));
                System.out.println("nome do Usuario para na comparacao após instanciar (estaNaLista) é: " + usuario.getNome());
                System.out.println("senha do Usuario para na comparacao após instanciar (estaNaLista) é: " + gerarGCM(((Usuario) item).getSalt(), usuarioAtual.getSenha(), IV));
                System.out.println("");
                System.out.println("");
                /* System.out.println("");
                System.out.println("comparar com: " + usuario);*/
                if (usuario.equals(item)) {
                    return true;
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchProviderException ex) {
                Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DecoderException ex) {
                Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidAlgorithmParameterException ex) {
                Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public static void cadastrarUsuario(UsuarioPlano usuarioAtual) throws NoSuchAlgorithmException {
        byte[] salt = gerarSalt();
        byte[] derivedKeyFromScrypt;
        String nome = usuarioAtual.getNome();
        derivedKeyFromScrypt = SCRYPT.useScryptKDF(nome.toCharArray(), salt,
                costParameter,
                blocksize, parallelizationParam);
        String IV = gerarIV(usuarioAtual.getSenha(), Hex.encodeHexString(salt), iteracoes);
        try {
            Usuario usuario = new Usuario(derivedKeyFromScrypt, gerarGCM(salt, usuarioAtual.getSenha(), IV), salt);
            System.out.println("salt do Usuario dps de instanciar objeto cadastro: " + usuario.getSalt());
            System.out.println("O usuario cadastrado é: " + usuario);
            usuarios.add(usuario);
            System.out.println("salta do Usuario dps de adicionar na lista: " + ((Usuario) usuarios.get(usuarios.size() - 1)).getSalt());
            System.out.println("");
            System.out.println("");
            System.out.println("");
            Empacotamento.gravarArquivoBinario(usuarios, "/home/phazevedo28/Documentos/seg");
            /*System.out.println("");
            System.out.println("Chave derivada usando scrypt: ");
            System.out.println(Hex.encodeHexString(derivedKeyFromScrypt));*/
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DecoderException ex) {
            Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Seg.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*System.out.println("");
        System.out.println("Chave derivada usando scrypt: ");
        System.out.println(Hex.encodeHexString(derivedKeyFromScrypt));*/
        Empacotamento.gravarArquivoBinario(usuarios, "/home/phazevedo28/Documentos/seg");
    }

    public static byte[] gerarSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        /*String valorSalt = "53efb4b1157fccdb9902676329debc52";
        byte[] salt = null;
        try {
            salt = Hex.decodeHex(valorSalt.toCharArray());
        } catch (DecoderException ex) {

        }*/
        System.out.println("O valor do Salt no método gerar é: " + salt);
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

    private static void instanciaSecurityProvider() {
        int addProvider;
        addProvider = Security.addProvider(new BouncyCastleFipsProvider());
        if (Security.getProvider("BCFIPS") == null) {
            System.out.println("Bouncy Castle provider NAO disponivel");
        } else {
            System.out.println("Bouncy Castle provider esta disponivel");
        }
    }

    public static String gerarIV(
            String password, String salt, Integer iterations) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(),
                salt.getBytes(), iterations, 128);
        SecretKeyFactory pbkdf2 = null;
        String derivedPass = null;
        try {
            pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512", "BCFIPS");
            SecretKey sk = pbkdf2.generateSecret(spec);
            derivedPass = Hex.encodeHexString(sk.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return derivedPass;
    }

    public static byte[] gerarGCM(byte[] saltKey, String senhaPlana, String IV) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, DecoderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] senha;
        senha = senhaPlana.getBytes();
        System.out.println("Msg = " + senhaPlana);
        byte[] iV;
        iV = IV.getBytes();

        Key key;
        Cipher in, out;

        in = Cipher.getInstance("AES/GCM/NoPadding", "BCFIPS");
        out = Cipher.getInstance("AES/GCM/NoPadding", "BCFIPS");

        key = new SecretKeySpec(saltKey, "AES");

        GCMParameterSpec​ gcmParameters = new GCMParameterSpec​(MAC_SIZE, iV);

        //in.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(N));
        in.init(Cipher.ENCRYPT_MODE, key, gcmParameters);

        byte[] enc = in.doFinal(senha);

        System.out.println("Msg cifrada = " + Hex.encodeHexString(enc));

        out.init(Cipher.DECRYPT_MODE, key, gcmParameters);
        byte[] out2;
        out2 = out.doFinal(enc);

        System.out.println("Msg decifrada = " + Utils.toString(out2));

        return enc;
    }

   /* public static byte[] gerarMsgCifrada(byte[] saltKey, String senhaPlana, String IV) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, DecoderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] senha;
        senha = senhaPlana.getBytes();
        System.out.println("Msg = " + senhaPlana);
        byte[] iV;
        iV = IV.getBytes();

        Key key;
        Cipher in, out;

        in = Cipher.getInstance("AES/GCM/NoPadding", "BCFIPS");
        out = Cipher.getInstance("AES/GCM/NoPadding", "BCFIPS");

        key = new SecretKeySpec(saltKey, "AES");

        GCMParameterSpec​ gcmParameters = new GCMParameterSpec​(MAC_SIZE, iV);

        //in.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(N));
        in.init(Cipher.ENCRYPT_MODE, key, gcmParameters);

        byte[] enc = in.doFinal(senha);

        System.out.println("Msg cifrada = " + Hex.encodeHexString(enc));
        return enc;
    } */
    
   /* public static String gerarMsgDecifrada(byte[] saltKey, String senhaPlana, String IV) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, DecoderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] senha;
        senha = senhaPlana.getBytes();
        System.out.println("Msg = " + senhaPlana);
        byte[] iV;
        iV = IV.getBytes();

        Key key;
        Cipher in, out;

        in = Cipher.getInstance("AES/GCM/NoPadding", "BCFIPS");
        out = Cipher.getInstance("AES/GCM/NoPadding", "BCFIPS");

        key = new SecretKeySpec(saltKey, "AES");

        GCMParameterSpec​ gcmParameters = new GCMParameterSpec​(MAC_SIZE, iV);

        byte[] enc = in.doFinal(senha);
       
        out.init(Cipher.DECRYPT_MODE, key, gcmParameters);
        byte[] out2;
        out2 = out.doFinal(enc);

        System.out.println("Msg decifrada = " + Utils.toString(out2));

        return Utils.toString(out2);
    }*/
}
