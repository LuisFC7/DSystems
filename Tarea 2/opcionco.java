import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class opcionco {

    // Código del server TCP
    private static final int PORT = 5000; // Puerto del servidor
    private static final int THREADS = 4; // Cantidad de hilos (uno por matriz a recibir)
    private static int N = 3; // Tamaño de las matrices

    private static double[][] Aaux = new double[N / 3][N];
    private static double[][] B1aux = new double[N / 3][N];
    private static double[][] B2aux = new double[N / 3][N];
    private static double[][] B3aux = new double[N / 3][N];
    private static double[][] C1aux = new double[N / 3][N / 3];
    private static double[][] C2aux = new double[N / 3][N / 3];
    private static double[][] C3aux = new double[N / 3][N / 3];

    public static double[][] mulMat(double[][] A, double[][] B) {
        double[][] aux = new double[N / 3][N / 3];
        for (int i = 0; i < N / 3; i++) {
            for (int j = 0; j < N / 3; j++) {
                double suma = 0;
                for (int k = 0; k < N; k++) {
                    suma += A[i][k] * B[j][k];
                }
                aux[i][j] = suma;
            }
        }

        return aux;
    }

    public static void imprimir(double[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        // private int id;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            // this.id = id;
        }

        @Override
        public void run() {
            try {
                // System.out.println("Hilo " + id + " iniciado");
                System.out.println("Se ha entrado al hilo");
                System.out.println(Aaux.length);
                System.out.println(Aaux[0].length);
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

                // double[][] Auxa1 = (double[][]) inputStream.readObject();
                // double[][] Auxb1 = (double[][]) inputStream.readObject();
                // double[][] Auxb2 = (double[][]) inputStream.readObject();
                // double[][] Auxb3 = (double[][]) inputStream.readObject();
                Aaux = (double[][]) inputStream.readObject();
                B1aux = (double[][]) inputStream.readObject();
                B2aux = (double[][]) inputStream.readObject();
                B3aux = (double[][]) inputStream.readObject();
                imprimir(Aaux);

                C1aux = mulMat(Aaux, B1aux);
                C2aux = mulMat(Aaux, B2aux);
                C3aux = mulMat(Aaux, B3aux);

                outputStream.writeObject(C1aux);
                outputStream.writeObject(C2aux);
                outputStream.writeObject(C3aux);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int nodo;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el valor del nodo: ");
        nodo = scanner.nextInt();

        if (nodo == 0) {
            // Variables para la conexión
            String[] direccionesIP = { "localhost", "localhost", "localhost" };
            int[] puertos = { 1234, 5678, 9012 };
            int puerto = 1234;

            // Variables para las matrices
            // int N = 12;
            double[][] A = new double[N][N];
            double[][] B = new double[N][N];
            double[][] C = new double[N][N];
            double[][] ex = new double[N / 3][N / 3];

            double[][] C1 = new double[N / 3][N / 3];
            double[][] C2 = new double[N / 3][N / 3];
            double[][] C3 = new double[N / 3][N / 3];
            double[][] C4 = new double[N / 3][N / 3];
            double[][] C5 = new double[N / 3][N / 3];
            double[][] C6 = new double[N / 3][N / 3];
            double[][] C7 = new double[N / 3][N / 3];
            double[][] C8 = new double[N / 3][N / 3];
            double[][] C9 = new double[N / 3][N / 3];

            // Inicialización de matrices A y B
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    A[i][j] = 2 * i + j;
                    B[i][j] = 3 * i - j;
                }
            }

            // Transposición de la matriz B
            for (int i = 0; i < N; i++) {
                for (int j = i + 1; j < N; j++) {
                    double temp = B[i][j];
                    B[i][j] = B[j][i];
                    B[j][i] = temp;
                }
            }

            imprimir(A);
            imprimir(B);
            // División de matrices A y B en matrices A1, A2, A3, B1, B2, B3
            double[][] A1 = new double[N / 3][N];
            double[][] A2 = new double[N / 3][N];
            double[][] A3 = new double[N / 3][N];
            double[][] B1 = new double[N / 3][N];
            double[][] B2 = new double[N / 3][N];
            double[][] B3 = new double[N / 3][N];

            for (int i = 0; i < N / 3; i++) {
                for (int j = 0; j < N; j++) {
                    A1[i][j] = A[i][j];
                    A2[i][j] = A[i + N / 3][j];
                    A3[i][j] = A[i + 2 * N / 3][j];

                    B1[i][j] = B[i][j];
                    B2[i][j] = B[i + N / 3][j];
                    B3[i][j] = B[i + 2 * N / 3][j];
                }
            }

            // Conexión a los nodos
            try {
                Socket[] sockets = new Socket[3];
                for (int i = 0; i < 3; i++) {
                    sockets[i] = new Socket(direccionesIP[i], puertos[i]);
                    ObjectOutputStream out = new ObjectOutputStream(sockets[i].getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(sockets[i].getInputStream());

                    // Envío de las matrices al nodo correspondiente
                    if (i == 0) {
                        out.writeObject(A1);
                        out.writeObject(B1);
                        out.writeObject(B2);
                        out.writeObject(B3);
                    } else if (i == 1) {
                        out.writeObject(A2);
                        out.writeObject(B1);
                        out.writeObject(B2);
                        out.writeObject(B3);
                    } else {
                        out.writeObject(A3);
                        out.writeObject(B1);
                        out.writeObject(B2);
                        out.writeObject(B3);
                    }

                    // Recepción de las matrices serializadas
                    if (i == 0) {
                        C1 = (double[][]) in.readObject();
                        C2 = (double[][]) in.readObject();
                        C3 = (double[][]) in.readObject();
                        // Object receivedMatrix1 = in.readObject();
                        // Object receivedMatrix2 = in.readObject();
                        // Object receivedMatrix3 = in.readObject();

                        // Comprobación de las matrices recibidas
                        // if (Arrays.deepEquals((double[][]) ex, C1) &&
                        // Arrays.deepEquals((double[][]) receivedMatrix2, C2) &&
                        // Arrays.deepEquals((double[][]) receivedMatrix3, C3)) {
                        // System.out.println("Matrices C1, C2 y C3 recibidas correctamente");
                        // }
                        imprimir(C1);
                        imprimir(C2);
                        imprimir(C3);

                    } else if (i == 1) {
                        C4 = (double[][]) in.readObject();
                        C5 = (double[][]) in.readObject();
                        C6 = (double[][]) in.readObject();

                        // Comprobación de las matrices recibidas
                        // if (Arrays.deepEquals((double[][]) receivedMatrix1, C4) &&
                        // Arrays.deepEquals((double[][]) receivedMatrix2, C5) &&
                        // Arrays.deepEquals((double[][]) receivedMatrix3, C6)) {
                        // System.out.println("Matrices C4, C5 y C6 recibidas correctamente");
                        // }
                    } else {
                        C7 = (double[][]) in.readObject();
                        C8 = (double[][]) in.readObject();
                        C9 = (double[][]) in.readObject();
                        // Object receivedMatrix1 = in.readObject();
                        // Object receivedMatrix2 = in.readObject();
                        // Object receivedMatrix3 = in.readObject();

                        // Comprobación de las matrices recibidas
                        // if (Arrays.deepEquals((double[][]) receivedMatrix1, C7) &&
                        // Arrays.deepEquals((double[][]) receivedMatrix2, C8) &&
                        // Arrays.deepEquals((double[][]) receivedMatrix3, C9)) {
                        // System.out.println("Matrices C7, C8 y C9 recibidas correctamente");
                        // }
                    }
                }

                int n = N / 3;
                // Concatenación de las matrices C1, C2 y C3
                for (int i = 0; i < n; i++) {
                    System.arraycopy(C1[i], 0, C[i], 0, n);
                    System.arraycopy(C2[i], 0, C[i], n, n);
                    System.arraycopy(C3[i], 0, C[i], 2 * n, n);
                }
                // Concatenación de las matrices C4, C5 y C6
                for (int i = 0; i < n; i++) {
                    System.arraycopy(C4[i], 0, C[n + i], 0, n);
                    System.arraycopy(C5[i], 0, C[n + i], n, n);
                    System.arraycopy(C6[i], 0, C[n + i], 2 * n, n);
                }
                // Concatenación de las matrices C7, C8 y C9
                for (int i = 0; i < n; i++) {
                    System.arraycopy(C7[i], 0, C[2 * n + i], 0, n);
                    System.arraycopy(C8[i], 0, C[2 * n + i], n, n);
                    System.arraycopy(C9[i], 0, C[2 * n + i], 2 * n, n);
                }

                // Operación checksum
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        sum += C[i][j];
                    }
                }

                // Llamada al método imprimir() con la matriz A
                System.out.println("Matriz A:");
                imprimir(A);

                // Llamada al método imprimir() con la matriz B
                System.out.println("Matriz B:");
                imprimir(B);

                // Llamada al método imprimir() con la matriz C
                System.out.println("Matriz C:");
                imprimir(C);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            // Código para los NODOS 1,2 Y 3
            try {
                System.out.println("Ingresa el puerto a conectar: ");
                int pto = scanner.nextInt();
                ServerSocket serverSocket = new ServerSocket(pto);
                System.out.println("Servidor iniciado en el puerto " + pto);
                // for (int i = 0; i < THREADS; i++) {
                Thread thread = new Thread(new ClientHandler(serverSocket.accept()));
                thread.start();
                // }
                // Thread thread = new Thread(new ClientHandler(serverSocket.accept()));
                // thread.start();
                // serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
