import java.rmi.Naming;

public class ClienteRMI {

    static int N = 900;
    static int M = 400;
    static int subN = N / 9;

    // Crear matriz Caux
    static float[][][] Caux = new float[81][subN][subN];

    // Matrices auxiliares para división de Matriz A y BT
    static float[][][] subA = new float[9][subN][M];
    static float[][][] subBT = new float[9][subN][M];

    public static void main(String[] args) {

        try {
            InterfaceRMI servidor = (InterfaceRMI) Naming.lookup("rmi://localhost/ServidorRMI");

            // int N = 9;
            // int M = 4;

            // Inicializar matriz A
            float[][] A = new float[N][M];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    A[i][j] = 2 * i + 3 * j;
                }
            }

            // Inicializar matriz B
            float[][] B = new float[M][N];
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    B[i][j] = 3 * i - 2 * j;
                }
            }

            // Obtener matriz transpuesta de B
            float[][] BT = new float[N][M];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    BT[i][j] = B[j][i];
                }
            }

            // Dividir matriz A y B en submatrices
            // int subN = N / 9;
            // float[][][] subA = new float[9][subN][M];
            // float[][][] subBT = new float[9][subN][M];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < subN; j++) {
                    for (int k = 0; k < M; k++) {
                        subA[i][j][k] = A[i * subN + j][k];
                        subBT[i][j][k] = BT[i * subN + j][k];
                    }
                }
            }

            // Se crean las instancias para las InterfacesRMI
            String direccionIP = "rmi://localhost/ServidorRMI";
            String direccionIP2 = "rmi://localhost/ServidorRMI";
            // Se llama al archivo InterfaceRMI
            InterfaceRMI servidor1 = (InterfaceRMI) Naming.lookup(direccionIP);
            InterfaceRMI servidor2 = (InterfaceRMI) Naming.lookup(direccionIP2);

            // Ejecución de Threads en paralelo
            Hilos h = new Hilos(servidor, 0, subN, M);
            Hilos h1 = new Hilos(servidor1, 1, subN, M);
            Hilos h2 = new Hilos(servidor2, 2, subN, M);

            // Se crea una barrera
            h.start();
            h1.start();
            h2.start();
            h.join();
            h1.join();
            h2.join();

            // Imprimir resultado si N=9 y M=4
            if (N == 9 && M == 4) {
                System.out.println("\nMatriz A: \n");
                imprimirMatriz(A);
                System.out.println("\nMatriz B: \n");
                imprimirMatriz(B);
                System.out.println("\nMatriz C: \n");
                imprimirCaux();
            }

            System.out.println("\nChecksum: " + Checksum());

        } catch (Exception e) {
            System.out.println("Excepción en ClienteRMI: " + e);
        }
    }

    static public class Hilos extends Thread {
        private InterfaceRMI servidor;
        private int identificador;
        private int N;
        private int M;

        public Hilos(InterfaceRMI servidor, int identificador, int N, int M) {
            this.servidor = servidor;
            this.identificador = identificador;
            this.N = N;
            this.M = M;
        }

        public void run() {
            try {
                int indice1 = 0;
                int indice2 = 0;
                if (identificador == 0) {
                    // Iterar sobre la primera dimensión de Caux
                    for (int i = 0; i < 27; i++) {
                        // Llamar al método multiplicarMatrices para obtener una matriz de [N/9][N/9]
                        float[][] subC = servidor.multiplicarMatrices(subA[indice1], subBT[indice2], N, M);

                        // Iterar sobre la matriz subC y copiar los valores en la matriz Caux
                        for (int j = 0; j < subC.length; j++) {
                            for (int k = 0; k < subC[j].length; k++) {
                                Caux[i][j][k] = subC[j][k];
                            }
                        }

                        // Actualizar los índices de subA y subB
                        indice2++;
                        if (indice2 == 9) {
                            indice2 = 0;
                            indice1++;
                        }
                    }

                } else if (identificador == 1) {
                    indice1 = 3;
                    indice2 = 0;
                    // Implementar lógica para identificador 1
                    for (int i = 27; i < 54; i++) {
                        // Llamar al método multiplicarMatrices para obtener una matriz de [N/9][N/9]
                        float[][] subC = servidor.multiplicarMatrices(subA[indice1], subBT[indice2], N, M);

                        // Iterar sobre la matriz subC y copiar los valores en la matriz Caux
                        for (int j = 0; j < subC.length; j++) {
                            for (int k = 0; k < subC[j].length; k++) {
                                Caux[i][j][k] = subC[j][k];
                            }
                        }

                        // Actualizar los índices de subA y subB
                        indice2++;
                        if (indice2 == 9) {
                            indice2 = 0;
                            indice1++;
                        }
                    }

                } else if (identificador == 2) {
                    // Implementar lógica para identificador 2
                    indice1 = 6;
                    indice2 = 0;
                    // Implementar lógica para identificador 1
                    for (int i = 54; i < 81; i++) {
                        // Llamar al método multiplicarMatrices para obtener una matriz de [N/9][N/9]
                        float[][] subC = servidor.multiplicarMatrices(subA[indice1], subBT[indice2], N, M);

                        // Iterar sobre la matriz subC y copiar los valores en la matriz Caux
                        for (int j = 0; j < subC.length; j++) {
                            for (int k = 0; k < subC[j].length; k++) {
                                Caux[i][j][k] = subC[j][k];
                            }
                        }

                        // Actualizar los índices de subA y subB
                        indice2++;
                        if (indice2 == 9) {
                            indice2 = 0;
                            indice1++;
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void imprimirMatriz(float[][] matriz) {
        int n = matriz.length;
        int m = matriz[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println();
        }
    }

    private static void imprimirSubmatrices(float[][][] submatrices) {
        int n = submatrices.length;
        int m = submatrices[0].length;
        int p = submatrices[0][0].length;
        for (int i = 0; i < n; i++) {
            System.out.println("Submatriz " + i + ":");
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < p; k++) {
                    System.out.print(submatrices[i][j][k] + "\t");
                }
                System.out.println();
            }
        }
    }

    private static void imprimirSubmatrices3D(float[][][] submatrices) {
        int n = submatrices.length;
        int m = submatrices[0].length;
        int p = submatrices[0][0].length;
        for (int i = 0; i < n; i++) {
            System.out.println("Submatriz " + i + ":");
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < p; k++) {
                    System.out.print(submatrices[i][j][k] + "\t");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    private static void imprimirCaux() {
        int suN = N / 9;
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                float[][] submatrix = Caux[count];
                for (int k = 0; k < suN; k++) {
                    for (int l = 0; l < suN; l++) {
                        System.out.print(submatrix[k][l] + " ");
                    }
                }
                count++;
            }
            System.out.println();
        }
    }

    private static double Checksum() {
        double result = 0.0;
        for (int i = 0; i < 81; i++) {
            for (int j = 0; j < Caux[i].length; j++) {
                for (int k = 0; k < Caux[i][j].length; k++) {
                    result += Caux[i][j][k];
                }
            }
        }
        return result;
    }

}