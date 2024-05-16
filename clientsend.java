/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class clientsend {
    public static void sendFileToServer(DataOutputStream dos) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File fileToSend = fileChooser.getSelectedFile();
            dos.writeUTF(fileToSend.getName());
            dos.writeLong(fileToSend.length());

            FileInputStream fis = new FileInputStream(fileToSend);
            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, read);
            }
            fis.close();

            System.out.println("File sent to server: " + fileToSend.getName());
            dos.flush();
        } else {
            System.out.println("No file selected.");
        }
    }
}
