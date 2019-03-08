/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import com.sun.xml.internal.ws.api.message.saaj.SAAJFactory;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego Castro Freijo
 */
public class Cliente {

    static final String Host = "localhost";
    static final int Puerto = 2000;

    public Cliente() {
        //capturamos datos del teclado
        Scanner teclado = new Scanner(System.in);

        try {
            String respuesta = "";
            //ponemos un bucle que se repite mientras el numero no sea el correcto
            while (!respuesta.equals("El numero secreto correcto")) {
                //me conecto al servidor
                Socket skCliente = new Socket(Host, Puerto);
                //solicita en numero para comprobar si es el correcto
                System.out.print("Ponga el numero para comprobar: ");
                String numero = teclado.nextLine();
                //pregunta al servidor
                DataOutputStream flujo_salida = new DataOutputStream(skCliente.getOutputStream());
                flujo_salida.writeUTF(numero);
                //respuesta del servidor
                DataInputStream flujo_entrada = new DataInputStream(skCliente.getInputStream());
                respuesta = flujo_entrada.readUTF();
                System.out.println(respuesta);
                skCliente.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        new Cliente();
    }

}
