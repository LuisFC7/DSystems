import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientePUT {
    public static void main(String[] args) {
        // Verificar que los argumentos sean los correctos
        if (args.length != 3) {
            System.err.println("Uso: java ClientePUT <IP del servidor> <puerto> <archivo>");
            return;
        }

        String ipServidor = args[0];
        int puerto = Integer.parseInt(args[1]);
        String archivo = args[2];

        // Verificar que el archivo exista
        Path archivoPath = Paths.get(archivo);
        if (!Files.exists(archivoPath)) {
            System.err.println("ERROR: El archivo " + archivo + " no existe.");
            return;
        }

        try {
            // Leer contenido del archivo
            byte[] contenidoArchivo = Files.readAllBytes(archivoPath);

            // Crear socket y conectarse al servidor
            Socket socket = new Socket(ipServidor, puerto);

            // Obtener flujos de entrada y salida del socket
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // Enviar petición PUT
            String mensaje = "PUT " + archivo + " " + contenidoArchivo.length + "\r\n";
            outputStream.write(mensaje.getBytes(Charset.forName("Cp850")));
            outputStream.flush();

            // Enviar contenido del archivo
            outputStream.write(contenidoArchivo);
            outputStream.flush();

            // Leer respuesta del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("Cp850")));
            String respuesta = reader.readLine();

            if (respuesta == null) {
                System.err.println("ERROR: No se pudo recibir respuesta del servidor.");
            } else if (respuesta.equals("OK")) {
                System.out.println("Archivo fue recibido por el servidor con éxito");
            } else {
                System.err.println("ERROR: El servidor no pudo escribir el archivo en el disco local.");
            }

            // Cerrar socket
            socket.close();

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
