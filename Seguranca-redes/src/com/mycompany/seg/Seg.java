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
import org.apache.commons.codec.binary.Base32;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.crypto.Mac;

public class Seg {

    static ArrayList<Object> usuarios = new ArrayList();

    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
        instanciaSecurityProvider();
        Scanner input = new Scanner(System.in);
        System.out.println("Digite seu nome: ");
        String nomePlano = input.next();
        System.out.println("Digite sua senha: ");
        String senhaPlana = input.next();
        input.close();
        Usuario usuarioAtual = new Usuario(nomePlano, senhaPlana);
        Usuario daLista = estaNaLista(usuarioAtual);
        if (daLista != null) {
            Usuario usuarioAtualAtualizado = gerarNomeESenhaNaoPlanos(usuarioAtual, daLista.getSalt());
            segundoFatorDeAutenticacao(usuarioAtualAtualizado, daLista);
            System.out.println("");
            System.out.println("Usuário já cadastrado");

        } else {
            Usuario paraCadastro = gerarNomeESenhaNaoPlanos(usuarioAtual, gerarSalt());
            cadastrarUsuario(paraCadastro);
        }
        testeImprimirArquivo();
    }

    public static Usuario estaNaLista(Usuario usuarioAtual) {
        usuarios = Empacotamento.lerArquivoBinario("/home/phazevedo28/Documentos/seg");
        for (Object daLista : usuarios) {
            Usuario usuario = gerarNomeESenhaNaoPlanos(usuarioAtual, ((Usuario) daLista).getSalt());
            System.out.println("O valor do Salt para comparacao(estaNaLista) é: " + Hex.encodeHexString(((Usuario) daLista).getSalt()));
            System.out.println("");
            System.out.println("");
            if (((Usuario) daLista).equals(usuario)) {

                return ((Usuario) daLista);
            }
        }
        return null;
    }

    public static Usuario gerarNomeESenhaNaoPlanos(Usuario usuarioAtual, byte[] salt) {
        Integer iteracoes = 1000;
        int costParameter = 2048; // exemplo: 2048 (afeta uso de memória e CPU)
        int blocksize = 8; // exemplo: 8
        int parallelizationParam = 1;
        Usuario usuario = null;
        byte[] derivedKeyFromScrypt;
        String nome = usuarioAtual.getNome();
        derivedKeyFromScrypt = SCRYPT.useScryptKDF(nome.toCharArray(), salt,
                costParameter,
                blocksize, parallelizationParam);
        String IV = gerarIV(usuarioAtual.getSenha(), Hex.encodeHexString(salt), iteracoes);

        try {
            usuario = new Usuario(Hex.encodeHexString(derivedKeyFromScrypt), Hex.encodeHexString(gerarSenha(salt, usuarioAtual.getSenha(), IV)), salt);
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

        return usuario;
    }

    public static void segundoFatorDeAutenticacao(Usuario comparador, Usuario daLista) throws InterruptedException {
        Integer iteracoes = 5000;
        System.out.println("Usuario Atual:  " + comparador);
        System.out.println("Usuario da lista:  " + daLista);
        System.out.println("");
        System.out.println("Nome do usuario atual " + comparador.getNome());
        System.out.println("Senha do usuario atual " + comparador.getSenha());
        System.out.println("");
        if (daLista.equals(comparador)) {
            String masterKey = gerarMasterKey(Hex.encodeHexString(daLista.getSalt()), comparador.getSenha(), iteracoes);
            String codigoServer = getTOTPCode(masterKey);
            // Thread.sleep(9000);
            String codigoCliente = getTOTPCode(masterKey);
            if (codigoServer.equals(codigoCliente)) {
                System.out.println("");
                System.out.println("Segundo fator de autenticação validado com sucesso");
            }
        }
    }

    public static void cadastrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        Empacotamento.gravarArquivoBinario(usuarios, "/home/phazevedo28/Documentos/seg");
    }

    public static byte[] gerarSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        System.out.println("O valor do Salt no método gerar é: " + Hex.encodeHexString(salt));
        return salt;
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

    public static String gerarMasterKey(
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

    public static byte[] gerarSenha(byte[] saltKey, String senhaPlana, String IV) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, DecoderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        int MAC_SIZE = 128; // in bits
        int AES_KEY_SIZE = 128; // in bits
        int GCM_NONCE_LENGTH = 12; // in bytes
        int GCM_TAG_LENGTH = 16; // in bytes
        byte[] senha;
        senha = senhaPlana.getBytes();
        byte[] iV;
        iV = IV.getBytes();
        Key key;
        Cipher in, out;
        in = Cipher.getInstance("AES/GCM/NoPadding", "BCFIPS");
        out = Cipher.getInstance("AES/GCM/NoPadding", "BCFIPS");
        key = new SecretKeySpec(saltKey, "AES");
        GCMParameterSpec​ gcmParameters = new GCMParameterSpec​(MAC_SIZE, iV);
        in.init(Cipher.ENCRYPT_MODE, key, gcmParameters);
        byte[] enc = in.doFinal(senha);
        out.init(Cipher.DECRYPT_MODE, key, gcmParameters);
        byte[] out2;
        out2 = out.doFinal(enc);

        return enc;
    }

    public static String getTOTPCode(String secretKey) {
        try {
            long value = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / TimeUnit.SECONDS.toMillis(30);
            final byte[] key = new Base32().decode("the_password".toUpperCase(Locale.US));
            final var data = new byte[8];
            for (int i = 8; i-- > 0; value >>>= 8) {
                data[i] = (byte) value;
            }
            final var signKey = new SecretKeySpec(key, "HmacSHA256"); // would like to change here to "HmacSHA256"
            final var mac = Mac.getInstance("HmacSHA256"); // would like to change here to "HmacSHA256"
            mac.init(signKey);
            final String hashString = new String(new Hex().encode(mac.doFinal(data)));
            final var offset = Integer.parseInt(hashString.substring(hashString.length() - 1), 16);
            final var truncatedHash = hashString.substring(offset * 2, offset * 2 + 8);
            final var finalHash = String.valueOf(Integer.parseUnsignedInt(truncatedHash, 16) & 0x7FFFFFFF);
            final var finalHashCut = finalHash.substring(finalHash.length() - 6);
            System.out.println("o TOTP gerado com  HmacSHA256 é:  " + finalHashCut);
            return finalHashCut;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            //LOGGER.warn("", e);
            return "";
        }
    }

    public static void testeImprimirArquivo() {

        int i = 1;
        for (Object item : usuarios) {
            System.out.println("");
            System.out.printf("Objeto nro....: %d.\n", i++);
            System.out.println("Salt:  " + Hex.encodeHexString(((Usuario) item).getSalt()));
            System.out.println("Nome:  " + ((Usuario) item).getNome());
            System.out.println("Senha:  " + ((Usuario) item).getSenha());
            System.out.println("");

        }
    }
}
