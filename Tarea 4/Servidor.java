import java.net.*;
import java.io.*;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class Servidor {

    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        int port = 8000;
        System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "12345678");
        System.setProperty("javax.net.ssl.trustStore", "keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "12345678");

        ServerSocket serverSocket = null;

        try {
            SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory
                    .getDefault();
            serverSocket = sslServerSocketFactory.createServerSocket(PORT);

            System.out.println("Esperando por conexiones...");
        } catch (IOException e) {
            System.err.println("Error al crear el socket del servidor: " + e.getMessage());
            System.exit(1);
        }

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostName()
                        + " en el puerto " + clientSocket.getPort());
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            } catch (IOException e) {
                System.err.println("Error al aceptar la conexión del cliente: " + e.getMessage());
            }
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "Cp850"));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "Cp850"),
                        true);

                String inputLine = in.readLine();
                String[] tokens = inputLine.split(" ");

                if (tokens[0].equals("GET")) {
                    String fileName = tokens[1];
                    File file = new File(fileName);
                    if (file.exists() && file.isFile() && file.canRead()) {
                        out.println("OK");
                        // long sic = file.length();
                        String realSize = Long.toString(file.length());
                        out.println(realSize);
                        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                            String line;
                            while ((line = fileReader.readLine()) != null) {
                                out.println(line);
                            }
                        }
                    } else {
                        out.println("ERROR");
                    }
                } else if (tokens[0].equals("PUT")) {
                    String fileName = tokens[1];
                    long fileSize = Long.parseLong(tokens[2]);
                    File file = new File(fileName);
                    try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
                        for (int i = 0; i < fileSize; i++) {
                            fileWriter.write(in.read());
                        }
                        out.println("OK");
                    } catch (IOException e) {
                        System.err.println("Error al escribir en el archivo " + fileName + ": " + e.getMessage());
                        out.println("ERROR");
                    }
                } else {
                    out.println("ERROR");
                }

                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error al manejar la conexión del cliente: " + e.getMessage());
            }
        }
    }
}
