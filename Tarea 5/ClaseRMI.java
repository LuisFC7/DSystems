import java.rmi.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClaseRMI extends UnicastRemoteObject implements InterfaceRMI {
    private static final long serialVersionUID = 1L;

    public ClaseRMI() throws RemoteException {
        super();
    }

    public int impt(float[][] mat, int N, int M) throws RemoteException {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                System.out.println(mat[i][j]);
            }
        }
        return M;
    }

    public float[][] multiplicarMatrices(float[][] A, float[][] B, int N, int M) {
        float[][] aux = new float[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int op = 0;
                for (int k = 0; k < M; k++) {
                    op += A[i][k] * B[j][k];
                }
                aux[i][j] = op;
            }
        }

        return aux;
    }

}