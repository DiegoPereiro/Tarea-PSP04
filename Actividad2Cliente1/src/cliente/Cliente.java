/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego Castro Freijo
 */
public class Cliente {

    static final int puerto = 1500;
    static final String Host = "localhost";
    String archivoOrigen;
    final String rutaArchivoDestino = "";

    public Cliente() {
        Scanner teclado=new Scanner(System.in);
        
        System.out.print("Ponga el archivo a solicitar al servidor: ");
        archivoOrigen=teclado.nextLine(); //F:\Pictures\apuntes_64.png   
        

        String respuesta = "";
        try {
            Socket clienteSocket = new Socket(Host, puerto);
            DataOutputStream flujo_salida = new DataOutputStream(clienteSocket.getOutputStream());
            flujo_salida.writeUTF(archivoOrigen);

            //respuesta del servidor
            DataInputStream flujo_entrada = new DataInputStream(clienteSocket.getInputStream());
            respuesta = flujo_entrada.readUTF();

            switch (respuesta) {
                case "existe":

                    int in;
                    //Buffer de 1024 bytes
                    byte[] receivedData = new byte[1024];
                    BufferedInputStream bis = new BufferedInputStream(clienteSocket.getInputStream());
                    DataInputStream dis = new DataInputStream(clienteSocket.getInputStream());
                    //Recibimos el nombre del fichero                    
                    String nombreArchivo = rutaArchivoDestino + dis.readUTF();
                    //Para guardar fichero recibido
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(nombreArchivo));
                    while ((in = bis.read(receivedData)) != -1) {
                        bos.write(receivedData, 0, in);
                    }
                    bos.close();
                    dis.close();
                    System.out.println("Archivo "+nombreArchivo +" recibido correctamente");

                    break;
                case "no existe":
                    System.out.println("El archivo seleccionado no existe");
                    break;
            }

            clienteSocket.close();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }

}
