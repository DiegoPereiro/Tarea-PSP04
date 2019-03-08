/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego Castro Freijo
 */
public class Servidor extends Thread {

    static final int Puerto = 1500;
    Socket skCliente;

    public Servidor(Socket skCliente) {
        this.skCliente = skCliente;//
    }

    public static void main(String[] args) {

        try {
            //Poner el servidor en escucha
            ServerSocket serverSocket = new ServerSocket(Puerto);
            while (true) {
                //Aceptamos las conexiones entrantes
                Socket skCliente = serverSocket.accept();
                //atiento el cliente con un thread
                new Servidor(skCliente).start();
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
            File archvo = new File(flujo_entrada.readUTF());
            String respuesta = null;
        if (!archvo.exists()) {
            flujo_salida.writeUTF("no existe");
        } else {
            flujo_salida.writeUTF("existe");

            DataInputStream input;
            BufferedInputStream bis;
            BufferedOutputStream bos;
            int in;
            byte[] byteArray;

            try {
                bis = new BufferedInputStream(new FileInputStream(archvo));
                bos = new BufferedOutputStream(skCliente.getOutputStream());
                //Enviamos el nombre del fichero
                DataOutputStream dos = new DataOutputStream(skCliente.getOutputStream());
                dos.writeUTF(archvo.getName());
                //Enviamos el fichero
                byteArray = new byte[8192];
                while ((in = bis.read(byteArray)) != -1) {
                    bos.write(byteArray, 0, in);
                }
                bis.close();
                bos.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
