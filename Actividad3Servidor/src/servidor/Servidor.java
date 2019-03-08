/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego Castro Freijo
 */
public class Servidor extends Thread {
    
    static final int Puerto = 1500;
    Socket skCliente;
    
    private final String usuario = "Diego";
    private final String contraseña = "1234";
    
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
            ObjectOutputStream flujo_salida_objetos = new ObjectOutputStream(skCliente.getOutputStream());
            
            String[] identificacion = flujo_entrada.readUTF().split("/");
            
            if (usuario.equals(identificacion[0]) && contraseña.equals(identificacion[1])) {
                flujo_salida.writeUTF("Correcto");                
                while (true) {                    
                    File directorio = new File(flujo_entrada.readUTF());
                    if (directorio.exists()) {
                        flujo_salida.writeUTF("existente");                        
                        File[] fichero = directorio.listFiles();
                        flujo_salida_objetos.writeObject(fichero);
                    } else {
                        flujo_salida.writeUTF("inexistente");
                    }
                }
                
            } else {
                flujo_salida.writeUTF("incorrecto");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
