import java.io.*;
import java.net.*;
import java.text.BreakIterator;
import java.util.*;

class Cliente {

    public static void main(String[] args) {
        try {
            Socket conexion = new Socket("localhost", 5000);

            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());

            Boolean verificacion = true;
            Scanner datos = new Scanner(System.in);
            long dato;
            while (verificacion) {
                System.out.println("Ingresa el n\u00famero a verificar si es primo o no: \n");
                dato = datos.nextLong();
                if (dato <= 1) {
                    System.out.println("El n\u00famero debe ser mayor a uno.\n");
                } else {
                    verificacion = false;
                    // Aqui se debe ingresar el dato o definirlo en el programa
                    // Aqui se da el envío
                    salida.writeLong(dato);

                    // Aqui se recibe el mensaje string del servidor
                    byte[] buffer = new byte[11];
                    entrada.read(buffer, 0, 11);
                    System.out.println("\n");
                    System.out.println(new String(buffer, "UTF-8"));

                    Thread.sleep(1000);
                    conexion.close();
                }
            }
            // // Aqui se debe ingresar el dato o definirlo en el programa
            // // Aqui se da el envío
            // salida.writeLong(dato);

            // // Aqui se recibe el mensaje string del servidor
            // byte[] buffer = new byte[11];
            // entrada.read(buffer, 0, 11);
            // System.out.println("\n");
            // System.out.println(new String(buffer, "UTF-8"));

            // Thread.sleep(1000);
            // conexion.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
