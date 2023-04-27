import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote {
    public float[][] multiplicarMatrices(float[][] A, float[][] B, int N, int M)
            throws RemoteException;

    public int impt(float[][] mat, int N, int M) throws RemoteException;
    // public float[][] multiplicarMatrices(float[][][] A, float[][][] B, int N, int
    // M, int indiceA, int indiceB)
    // throws RemoteException;
}