/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import com.oracle.jrockit.jfr.DataType;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author Diego Castro Freijo
 */
public class Servidor extends Thread {

    static final int puerto = 2000;
    Socket skCliente;
    int numeroSecreto;

    public Servidor(Socket skCliente, int numeroSecreto) {
        this.skCliente = skCliente;
        this.numeroSecreto=numeroSecreto;
    }

    public static void main(String[] args) {

        //Creamos un numero aleatorio
        int numeroSecreto = (int) (Math.random() * 100) + 1;
        System.out.println("El numero secreto es: " + numeroSecreto);

        try {
            //iniciamos la escuncha en el puerto 2000
            ServerSocket skServidor = new ServerSocket(puerto);           
            //ponemos un bucle que se repite
            while (true) {
                //aceptamos la conexion que proviene del cliente
                Socket skCliente = skServidor.accept();
                //atiento el cliente con un thread
                new Servidor(skCliente, numeroSecreto).start();              
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        try {
            DataInputStream flujo_entrada = new DataInputStream(skCliente.getInputStream());
            DataOutputStream flujo_salida = new DataOutputStream(skCliente.getOutputStream());

            //Pregunta del cliente            
            int numeroRecibido = Integer.valueOf(flujo_entrada.readUTF());
            //conprobamos el numero del cliente
             String respuesta = "";
            if (numeroRecibido > numeroSecreto) {
                respuesta = "El numero secreto es menor";
            }
            if (numeroRecibido < numeroSecreto) {
                respuesta = "El numero secreto es Mayor";
            }
            if (numeroRecibido == numeroSecreto) {
                respuesta = "El numero secreto correcto";
            }
            //respondemos al cliente
            flujo_salida.writeUTF(respuesta);
            //cerramos el socket del cliente
            skCliente.close();

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
