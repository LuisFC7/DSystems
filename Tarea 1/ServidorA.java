//Tarea 1. Sistema Distribuido que verifica si un número es primo.
//Flores Castro Luis Antonio.
//Servidor A

import java.io.*;
import java.net.*;

class ServidorA {

    static class Worker extends Thread {
        Socket conexion;

        Worker(Socket conexion) {
            this.conexion = conexion;
        }

        // Método para verificar si NÚMERO divide entre el rango
        public boolean dividir(long NUMERO, long NUMERO_INICIAL, long NUMERO_FINAL) {
            boolean flag = false;
            for (long i = NUMERO_INICIAL; i <= NUMERO_FINAL; i++)
                if (NUMERO % i == 0)
                    flag = true;

            return flag;
        }

        public void run() {
            try {
                // entrada y salida de datos
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                // Dato tipo long de tamaño de 8 bytes = 64 bits
                // lectura de datos
                long NUMERO = entrada.readLong();
                long NUMERO_INICIAL = entrada.readLong();
                long NUMERO_FINAL = entrada.readLong();
                System.out.println("Instancia: " + NUMERO + ", " + NUMERO_INICIAL + ", " + NUMERO_FINAL);

                // Aqui se regresa el mensaje
                if (dividir(NUMERO, NUMERO_INICIAL, NUMERO_FINAL)) {
                    // envio de datos
                    salida.write("DIVIDE".getBytes());
                } else {
                    salida.write("NO DIVIDE".getBytes());
                }

                conexion.close();

            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception, InterruptedException {
        // Se crean las 3 instancias del servidor A
        // ServerSocket servidor = new ServerSocket(5001);
        // ServerSocket servidor = new ServerSocket(5002);
        ServerSocket servidor = new ServerSocket(5003);
        for (;;) {
            Socket conexion = servidor.accept();
            Worker w = new Worker(conexion);
            w.start();
            Thread.sleep(100000);
            conexion.close();
        }
    }
}