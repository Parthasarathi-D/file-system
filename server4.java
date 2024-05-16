/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cnproject;

/**
 *
 * @author Parthasarathi D
 */
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server4 {
    private static final String FILES_DIRECTORY = "server_files/";
    private static ServerGUI serverGui;

    public static void main(String[] args) {
        // Create an instance of ServerGUI
        SwingUtilities.invokeLater(() -> serverGui = new ServerGUI());

        ExecutorService executorService = Executors.newFixedThreadPool(5); // Handles up to 5 clients concurrently

        try {
            ServerSocket serverSocket = new ServerSocket(2222);
            appendToLog("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                appendToLog("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to append logs to the ServerGUI
    private static void appendToLog(String log) {
        if (serverGui != null) {
            serverGui.appendToLog(log);
        }
    }

    static class ServerGUI extends JFrame {
        private JTextArea logTextArea;

        public ServerGUI() {
            setTitle("Server Log");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            logTextArea = new JTextArea();
            logTextArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(logTextArea);

            getContentPane().add(scrollPane);
            setVisible(true);
        }

        public void appendToLog(String log) {
            logTextArea.append(log + "\n");
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

                while (true) {
                    int choice = dis.readInt();

                    switch (choice) {
                        case 1:
                            receiveFile(dis);
                            break;
                        case 2:
                            sendFile(dis, dos);
                            break;
                        case 3:
                            return;
                        default:
                            appendToLog("Invalid choice.");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       private void receiveFile(DataInputStream dis) throws IOException {
            String fileName = dis.readUTF();
            long fileSize = dis.readLong();

            System.out.println("Receiving file: " + fileName);
            String path="D:\\webtech lab project\\cnproject\\files\\";
            FileOutputStream fos = new FileOutputStream(path+fileName);
            byte[] buffer = new byte[4096];
            int read;
            long remaining = fileSize;
            while ((read = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {
                fos.write(buffer, 0, read);
                remaining -= read;
            }
            fos.close();

            System.out.println("File received: " + fileName);
        }

        private void sendFile(DataInputStream dis, DataOutputStream dos) throws IOException {
            String fileName = dis.readUTF();
            File fileToSend = new File("D:\\webtech lab project\\cnproject\\files\\"+fileName);

            if (!fileToSend.exists()) {
                dos.writeLong(-1); // Indicates file not found to client
                return;
            }

            dos.writeLong(fileToSend.length());

            FileInputStream fis = new FileInputStream(fileToSend);
            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, read);
            }
            fis.close();

            System.out.println("File sent: " + fileName);
            dos.flush();
        }

        // Additional helper methods and functionalities for the ClientHandler class
    }
}
