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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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

    public Cliente() {
        Scanner teclado = new Scanner(System.in);
        try {
            Socket skCliente = new Socket(Host, puerto);
            DataInputStream flujo_entrada = new DataInputStream(skCliente.getInputStream());
            DataOutputStream flujo_salida = new DataOutputStream(skCliente.getOutputStream());
            ObjectInputStream flujo_entra_objeto = new ObjectInputStream(skCliente.getInputStream());

            System.out.print("Ponga el usuario(Diego):");
            String usuario = teclado.nextLine();
            System.out.print("Ponga la contraseña(1234):");
            String contraseña = teclado.nextLine();
            flujo_salida.writeUTF(usuario + "/" + contraseña);

            if (flujo_entrada.readUTF().equals("Correcto")) {
                System.out.println("-------------------------------------------------------------------------");
                Boolean existe = false;

                System.out.print("Ponga el directorio a examinar:");
                String directorio = teclado.nextLine();
                flujo_salida.writeUTF(directorio);

                if (flujo_entrada.readUTF().equals("existente")) {
                    File[] archivos = (File[]) flujo_entra_objeto.readObject();
                    for (int x = 0; x < archivos.length; x++) {
                        System.out.println(archivos[x].getName());
                    }
                } else {
                    System.out.println("No se puede encontrar la ruta");
                }

            } else {
                System.out.println("Identificacion incorrecta");
            }

            skCliente.close();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }

}
