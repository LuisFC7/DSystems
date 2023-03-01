import java.io.*;
import java.net.*;

class Asin extends Thread {
    // La variable a la que los dos threads acceden debe ser estatica
    // ya que si no se declara asi los threads crean un variable por cada uno.
    static long n;
    static Object obj = new Object();

    public void run() {
        for (int i = 0; i < 100000; i++)
            synchronized (obj) {
                n++;
            }
    }

    public static void main(String[] args) throws Exception {
        Asin t1 = new Asin();
        Asin t2 = new Asin();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(n);
    }

}
