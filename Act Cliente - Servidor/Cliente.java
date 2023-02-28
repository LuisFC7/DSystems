import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;

public class Cliente {

    // Método read asegura el envío completo de datos
    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
        while (longitud > 0) {
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }

    public static void main(String args[]) {

        try {
            // Creacion del socket
            Socket conexion = new Socket("localhost", 5000);

            // Creación de streams
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());

            // Envío de datos mediante método Write Int
            salida.writeInt(123);
            salida.writeDouble(1234567890.1234567890);
            salida.write("hola".getBytes());

            // Creación de la cadena de caracteres.
            byte[] buffer = new byte[4];
            read(entrada, buffer, 0, 4);
            System.out.println(new String(buffer, "UTF-8"));

            // Envío de numeros punto flotantes
            ByteBuffer b = ByteBuffer.allocate(5 * 8);
            b.putDouble(1.1);
            b.putDouble(1.2);
            b.putDouble(1.3);
            b.putDouble(1.4);
            b.putDouble(1.5);

            byte[] a = b.array();
            salida.write(a);

            // Terminación de programa.
            Thread.sleep(1000);
            conexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}