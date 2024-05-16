/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;

/**
 *
 * @author Parthasarathi D
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class client extends JFrame {
    private JTextField choiceField;
    private JTextArea outputArea;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public client() {
        setTitle("Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        choiceField = new JTextField();
        JButton sendButton = new JButton("ENTER ");
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        panel.add(choiceField, BorderLayout.NORTH);
        panel.add(sendButton, BorderLayout.CENTER);
        panel.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        add(panel);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int choice = Integer.parseInt(choiceField.getText());
                    dos.writeInt(choice);

                    switch (choice) {
                        case 1:
                            clientsend.sendFileToServer(dos);
                            outputArea.append("file sent successfully..\n");
                            break;
                        case 2:
                            clientrec.receiveFileFromServer( dis, dos);
                            outputArea.append("file received successfully..\n");
                            break;
                        case 3:
                            outputArea.append("Terminating the process.\n");
                            socket.close();
                            break;
                        default:
                            outputArea.append("Invalid choice. Please enter a valid option.\n");
                            break;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        try {
            socket = new Socket("localhost", 2222);
            outputArea.append("Connected to server.\n");
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new client().setVisible(true);
            }
        });
    }
}
