import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class Cifrador {
    public static Key obtenerClave(String password){
        Key clave = new SecretKeySpec(password.getBytes(), 0, 16, "AES");

        return clave;
    }
    public static void cifrar(Key clave){
        Scanner sc = new Scanner(System.in);

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, clave);

            System.out.println("Introduzca un mensaje");
            String mensaje = sc.nextLine();

            byte[] cipherText = cipher.doFinal(mensaje.getBytes());

            escribirFichero(Base64.getEncoder().encodeToString(cipherText));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
    public static String descifrar(Key clave){
        String textoDescifrado="";
        String mensaje=leerFichero();
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, clave);

            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(mensaje));

             textoDescifrado=new String(plainText);

        } catch (NoSuchAlgorithmException e) {
            System.err.println("No existe el algoritmo especificado");
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            System.err.println("El padding seleccionado no existe");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.err.println("La clave utilizada no es válida");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.err.println("El tamaño del bloque elegido no es correcto");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.err.println("El padding seleccionado no es correcto");
            e.printStackTrace();
        }
        return textoDescifrado;
    }

    /**
     * Método para escribir en un fichero un String recibido por parámetro
     * @param mensaje
     */
    public static void escribirFichero(String mensaje){
        try {
            String filePath = "src/cifrados.txt";
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mensaje);
            bw.newLine();
            bw.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }


    /**
     * Método que leerá un fichero y devolverá un String con el contenido de este
     * @return
     */
    public static String leerFichero() {

        BufferedReader br = null;
        String contenido="";

        try {
            br = new BufferedReader(new FileReader("src/cifrados.txt"));
            Scanner sc = new Scanner(br);

            // Se lee el fichero
            while (sc.hasNext()) {
                contenido = sc.nextLine();
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {

                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return contenido;
    }

}
