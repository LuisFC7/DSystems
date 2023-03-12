//Tarea 1. Sistema Distribuido que verifica si un número es primo.
//Flores Castro Luis Antonio.
//Servidor B

import java.io.*;
import java.net.*;
import javax.sound.sampled.Port;

class ServidorB {

    // Declaracion de objetos y variables estaticas para el uso de synchronized
    static Object obj = new Object();
    static int deteccion;

    static class Worker extends Thread {
        Socket conexion;
        long NUMERO;
        long NUMERO_INICIAL;
        long NUMERO_FINAL;

        Worker(Socket conexion, long NUMERO, long NUMERO_INICIAL, long NUMERO_FINAL) {
            this.conexion = conexion;
            this.NUMERO = NUMERO;
            this.NUMERO_INICIAL = NUMERO_INICIAL;
            this.NUMERO_FINAL = NUMERO_FINAL;
        }

        public void run() {
            try {
                // entrada y salida de datos
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                System.out.println("\nInstancia.");
                // Primero se envian los datos númericos a los servidores
                System.out.println("\t" + NUMERO + ", " + NUMERO_INICIAL + ", " + NUMERO_FINAL);
                salida.writeLong(NUMERO);
                salida.writeLong(NUMERO_INICIAL);
                salida.writeLong(NUMERO_FINAL);
                // Se realiza la sincronización de los hilos para que no existe error en los
                // resultados
                synchronized (obj) {
                    byte[] buffer = new byte[9];
                    // Aqui se puede llamar a la función hecha por nosotros o el método
                    // perteneciente a la clase InputStream
                    entrada.read(buffer, 0, 9);
                    String respuesta = new String(buffer, "UTF-8");
                    if (respuesta.startsWith("NO")) {
                        // Entonces es primo y la variable deteccion debe de igualarse a 3 que es el
                        // número de instancias de SA
                        System.out.println("\nEnviado desde el servidor A: " + respuesta);
                        deteccion++;
                    } else {
                        // No es primo
                        System.out.println("\nEnviado desde el servidor A: " + respuesta);
                    }

                }
                // Cerrar la conexion
                conexion.close();

            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket servidor = new ServerSocket(5000);
        System.out.println("\nEsperando conexi\u00f3n....");
        for (;;) {
            Socket conexion = servidor.accept();
            System.out.println("\nConexi\u00f3n establecida.");
            // Se realiza la lectura de las respuestas obtenidas por Server A

            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            long k;
            // Primero se lee el número que manda el cliente
            long NUMERO = entrada.readLong();
            k = NUMERO / 3;
            System.out.println("\n\nSe ha recibido el n\u00famero:  " + NUMERO);
            System.out.println("Enviando a los servidores...");

            // Se crean las intancias de conexion para los servidores A
            Socket conexionS1 = new Socket("localhost", 5001);
            Socket conexionS2 = new Socket("localhost", 5002);
            Socket conexionS3 = new Socket("localhost", 5003);

            // Ahora se pasan los números a enviar mediante el constructor Worker
            Worker serA1 = new Worker(conexionS1, NUMERO, 2, k);
            Worker serA2 = new Worker(conexionS2, NUMERO, k + 1, 2 * k);
            Worker serA3 = new Worker(conexionS3, NUMERO, 2 * k + 1, NUMERO - 1);

            // Se inician los hilos y las barreras
            serA1.start();
            serA1.join();
            serA2.start();
            serA2.join();
            serA3.start();
            serA3.join();

            if (deteccion == 3)
                // System.out.println("ES PRIMO");
                // Aqui se envia el string al cliente
                salida.write("ES PRIMO".getBytes());
            if (deteccion < 3)
                // System.out.println("NO ES PRIMO");
                salida.write("NO ES PRIMO".getBytes());
            // reiniciamos el indicador
            deteccion = 0;

            System.out.println("\nEsperando conexi\u00f3n...");

            // Quitar en caso de que afecte funcionamiento
            conexionS1.close();
            conexionS2.close();
            conexionS3.close();
            conexion.close();
        }
    }

}
