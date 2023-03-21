import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Nuevo {
    static int opcion;
    static int N;
    static double[][] A;
    static double[][] B;
    static double[][] C;
    static Object obj = new Object();
    static String host;
    ///
    static double[][] A1;
    static double[][] A2;
    static double[][] A3;
    static double[][] B1;
    static double[][] B2;
    static double[][] B3;
    //
    static double[][] C1 = new double[N / 3][N / 3];
    static double[][] C2 = new double[N / 3][N / 3];
    static double[][] C3 = new double[N / 3][N / 3];
    static double[][] C4 = new double[N / 3][N / 3];
    static double[][] C5 = new double[N / 3][N / 3];
    static double[][] C6 = new double[N / 3][N / 3];
    static double[][] C7 = new double[N / 3][N / 3];
    static double[][] C8 = new double[N / 3][N / 3];
    static double[][] C9 = new double[N / 3][N / 3];

    public static void imprimirMatriz(double[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println(); // Salto de línea para separar las filas
        }
    }

    public static void concatenarMatrices() {
        // Concatenar las primeras tres matrices en la primera fila de la matriz final
        for (int i = 0; i < N / 3; i++) {
            for (int j = 0; j < N / 3; j++) {
                C[i][j] = C1[i][j];
                C[i][j + N / 3] = C2[i][j];
                C[i][j + 2 * N / 3] = C3[i][j];
            }
        }

        // Concatenar las siguientes tres matrices en la segunda fila de la matriz final
        for (int i = 0; i < N / 3; i++) {
            for (int j = 0; j < N / 3; j++) {
                C[i + N / 3][j] = C4[i][j];
                C[i + N / 3][j + N / 3] = C5[i][j];
                C[i + N / 3][j + 2 * N / 3] = C6[i][j];
            }
        }

        // Concatenar las últimas tres matrices en la tercera fila de la matriz final
        for (int i = 0; i < N / 3; i++) {
            for (int j = 0; j < N / 3; j++) {
                C[i + 2 * N / 3][j] = C7[i][j];
                C[i + 2 * N / 3][j + N / 3] = C8[i][j];
                C[i + 2 * N / 3][j + 2 * N / 3] = C9[i][j];
            }
        }
    }

    public static double[][] multiplicarMatriz(double[][] A, double[][] B) {
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

        return C;
    }

    public static double checkSum(double[][] matriz) {
        double sum = 0;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                sum += matriz[i][j];
            }
        }
        return sum;
    }

    public static double[][] recibir(DataInputStream entrada, int filas, int columnas) throws IOException {
        double[][] auxiliar = new double[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                auxiliar[i][j] = entrada.readDouble();
                System.out.println(auxiliar[i][j]);
            }
        }
        return auxiliar;
    }

    // static void enviar(DataOutputStream salida, double[][] matriz, int filas, int
    // columnas) {
    // try {
    // for (int i = 0; i < filas; i++) {
    // for (int j = 0; j < columnas; j++) {
    // salida.writeDouble(matriz[i][j]);
    // }
    // }
    // salida.flush();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // // return matriz;
    // }
    static void enviar(DataOutputStream salida, double[][] M, int fil, int col) throws IOException {
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < fil; j++) {
                // salida.writeFloat(M[i][j]);
                salida.writeDouble(M[i][j]);
            }
        }
        salida.flush();
    }

    static class Worker extends Thread {
        Socket conexion;

        public Worker(Socket conexion) {
            this.conexion = conexion;
        }

        public void run() {
            try {
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                // Modificación aqui
                int nod = entrada.readInt();
                System.out.println("Se ha establecido la conexión desde el nodo " + nod);
                System.out.println(A1.length);

                switch (nod) {
                    case 1:
                        System.out.println("Entro");
                        System.out.println(A1[0][1]);
                        for (int i = 0; i < N / 3; i++) {
                            for (int j = 0; j < N; j++) {
                                // salida.writeFloat(M[i][j]);
                                salida.writeDouble(A1[i][j]);
                                System.out.println(A1[i][j]);
                                salida.flush();
                            }
                        }

                        enviar(salida, A1, A1.length, A1[0].length);
                        enviar(salida, B1, B1.length, B1[0].length);
                        enviar(salida, B2, B2.length, B2[0].length);
                        enviar(salida, B3, B3.length, B3[0].length);
                        break;
                    case 2:
                        enviar(salida, A2, A2.length, A2[0].length);
                        enviar(salida, B1, B1.length, B1[0].length);
                        enviar(salida, B2, B2.length, B2[0].length);
                        enviar(salida, B3, B3.length, B3[0].length);
                        break;
                    case 3:
                        enviar(salida, A3, A3.length, A3[0].length);
                        enviar(salida, B1, B1.length, B1[0].length);
                        enviar(salida, B2, B2.length, B2[0].length);
                        enviar(salida, B3, B3.length, B3[0].length);
                        break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }

                synchronized (obj) {
                    switch (nod) {
                        case 1:
                            C1 = recibir(entrada, N / 3, N / 3);
                            C2 = recibir(entrada, N / 3, N / 3);
                            C3 = recibir(entrada, N / 3, N / 3);

                            break;
                        case 2:
                            C4 = recibir(entrada, N / 3, N / 3);
                            C5 = recibir(entrada, N / 3, N / 3);
                            C6 = recibir(entrada, N / 3, N / 3);
                            break;
                        case 3:
                            C7 = recibir(entrada, N / 3, N / 3);
                            C8 = recibir(entrada, N / 3, N / 3);
                            C9 = recibir(entrada, N / 3, N / 3);
                            break;
                    }
                }

                salida.close();
                entrada.close();
                conexion.close();
            } catch (IOException e) {
                System.out.println("Error al establecer la conexión desde el nodo ");
            }

        }

    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Seleccione una opcion:");
            System.out.println("0. Nodo 0.");
            System.out.println("1. Nodo 1.");
            System.out.println("2. Nodo 2.");
            System.out.println("3. Nodo 3.");

            int nodo = scanner.nextInt();

            if (nodo == 0) {
                System.out.println("Ingrese el numero de puerto al que desea conectar:");
                int puerto = scanner.nextInt();
                // Aquí se podría hacer algo con el puerto ingresado

                System.out.println("Ingrese el tamano de la matriz:");
                N = scanner.nextInt();

                // Aquí se podrían hacer las operaciones que requieren la matriz y las demás
                // variables
                A = new double[N][N];
                B = new double[N][N];
                C = new double[N][N];

                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        A[i][j] = 2 * i + j;
                        B[i][j] = 3 * i - j;
                    }
                }

                // Transponer la matriz B
                for (int i = 0; i < N; i++) {
                    for (int j = i + 1; j < N; j++) {
                        double temp = B[i][j];
                        B[i][j] = B[j][i];
                        B[j][i] = temp;
                    }
                }

                A1 = new double[N / 3][N];
                A2 = new double[N / 3][N];
                A3 = new double[N / 3][N];
                B1 = new double[N / 3][N];
                B2 = new double[N / 3][N];
                B3 = new double[N / 3][N];

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

                ServerSocket serverSocket = new ServerSocket(puerto);
                Worker[] workers = new Worker[4];

                // Ciclo for para crear las conexiones y los trabajadores
                for (int i = 0; i < 3; i++) {
                    // Crear una variable de tipo Socket y aceptar una conexión entrante
                    Socket conexion = serverSocket.accept();

                    // Crear un objeto de la clase Worker con la conexión aceptada
                    workers[i] = new Worker(conexion);

                    // Iniciar el hilo del worker
                    workers[i].start();
                }

                // Esperar a que todos los hilos terminen
                for (int i = 0; i < 3; i++) {
                    try {
                        workers[i].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Matriz A: ");
                imprimirMatriz(A);
                System.out.println("Matriz B: ");
                imprimirMatriz(B);
                System.out.println("Matriz C: ");
                imprimirMatriz(C);
                System.out.println("Checksum: ");
                double result = checkSum(C);
                break;
            } else if (nodo >= 1 && nodo <= 3) {
                System.out.println("Ingrese el host al que desea conectar:");
                host = scanner.next();

                System.out.println("Ingrese el numero de puerto al que desea conectar:");
                int puerto = scanner.nextInt();
                // Aquí se podría hacer algo con el host y puerto ingresados

                // Declarar una variable de tipo Socket y conectar a un host y puerto
                Socket conexion = new Socket(host, puerto);

                // Declarar dos variables de tipo DataInputStream y DataOutputStream
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());

                // AQUI SE HACE MODIFICACION
                salida.writeInt(nodo);
                // Declarar 4 matrices de tipo double de tamaño [N/3][N]
                double[][] Aaux = new double[N / 3][N];
                double[][] B1aux = new double[N / 3][N];
                double[][] B2aux = new double[N / 3][N];
                double[][] B3aux = new double[N / 3][N];

                // Leer los valores de las matrices desde el flujo de entrada
                Aaux = recibir(entrada, N / 3, N);
                B1aux = recibir(entrada, N / 3, N);
                B2aux = recibir(entrada, N / 3, N);
                B3aux = recibir(entrada, N / 3, N);

                double[][] Caux1 = multiplicarMatriz(Aaux, B1aux);
                double[][] Caux2 = multiplicarMatriz(Aaux, B2aux);
                double[][] Caux3 = multiplicarMatriz(Aaux, B3aux);

                enviar(salida, Caux1, N / 3, N / 3);
                enviar(salida, Caux2, N / 3, N / 3);
                enviar(salida, Caux3, N / 3, N / 3);

                entrada.close();
                salida.close();
                conexion.close();
                // Aquí se podrían hacer las operaciones que requieren la matriz y las demás
                // variables
                break;
            } else {
                System.out.println("Opcion no valida. Las opciones van desde 0 hasta 3.");
            }
        }

        // Aquí se podrían seguir haciendo operaciones con las variables declaradas
        // fuera del main
    }
}
