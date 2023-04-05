import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.net.ssl.SSLSocketFactory;

public class ClienteGET {
    public static void main(String[] args) {
        // Verificamos que los argumentos necesarios hayan sido proporcionados
        if (args.length != 3) {
            System.out.println("Debe proporcionar la dirección IP del servidor, el puerto y el nombre del archivo");
            return;
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String fileName = args[2];

        System.setProperty("javax.net.ssl.trustStore",
                "C:/Users/Lenovo/Documents/Semestre 23-2/Sistemas Distribuidos/Tareas/Tarea 4/keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "12345678");

        // Creamos el socket y nos conectamos al servidor
        try {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            Socket socket = sslSocketFactory.createSocket(serverIP, serverPort);
            // Creamos los streams de entrada y salida para comunicarnos con el servidor
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            // Enviamos la petición GET al servidor seguida del nombre del archivo
            String request = "GET " + fileName + "\n";
            out.write(request.getBytes(Charset.forName("Cp850")));

            // Leemos la respuesta del servidor
            byte[] buffer = new byte[8192];
            int bytesRead = in.read(buffer);
            String response = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);

            // Creamos un Scanner para leer el tamaño esperado del archivo
            // DataInputStream dataIn = new DataInputStream(in);
            Scanner scanner = new Scanner(in);
            String str = scanner.nextLine();
            // Leemos el tamaño esperado del archivo
            long expectedSize = Long.parseLong(str);

            // Verificamos si la respuesta es OK
            if (response.trim().equals("OK")) {
                // Creamos el archivo y escribimos los datos recibidos del servidor
                File file = new File(fileName);
                FileOutputStream fileOut = new FileOutputStream(file);
                int count;
                long totalBytesRead = 0; // Llevamos la cuenta de los bytes leídos
                while (totalBytesRead < expectedSize && (count = in.read(buffer)) > 0) {
                    fileOut.write(buffer, 0, count);
                    totalBytesRead += count; // Actualizamos la cuenta de bytes leídos
                }
                fileOut.close();

                System.out.println("Archivo recibido exitosamente");
            } else {
                System.out.println("ERROR: el archivo no ha sido recibido exitosamente");
            }

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}