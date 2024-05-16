/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class clientrec {
    public static void receiveFileFromServer(DataInputStream dis, DataOutputStream dos) throws IOException {
        JFrame frame = new JFrame("File Receiver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Enter the file name to receive from server:");
        JTextField fileNameField = new JTextField();
        JButton receiveButton = new JButton("Receive");

        panel.add(label);
        panel.add(fileNameField);
        panel.add(receiveButton);

        frame.add(panel);
        frame.setVisible(true);

        receiveButton.addActionListener(e -> {
            try {
                String fileToReceive = fileNameField.getText();
                dos.writeUTF(fileToReceive);

                long fileSize = dis.readLong();
                if (fileSize == -1) {
                    System.out.println("File not found on server.");
                    return;
                }

                System.out.println("Receiving file from server...");

                FileOutputStream fos = new FileOutputStream("received_" + fileToReceive);
                byte[] buffer = new byte[4096];
                int read;
                long remaining = fileSize;
                while ((read = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {
                    fos.write(buffer, 0, read);
                    remaining -= read;
                }
                fos.close();

                System.out.println("File received from server: " + fileToReceive);

                frame.dispose(); // Close the GUI after receiving the file
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
