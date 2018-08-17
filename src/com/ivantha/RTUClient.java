package com.ivantha;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * RTUClient instances will connect with the CTCServer.
 */
public class RTUClient {

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Capitalize Client");
    private JTextField dataField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 60);

    public RTUClient() {
        // Layout GUI
        messageArea.setEditable(false);
        frame.getContentPane().add(dataField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Add Listeners
        dataField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield
             * by sending the contents of the text field to the
             * server and displaying the response from the server
             * in the text area.  If the response is "." we exit
             * the whole application, which closes all sockets,
             * streams and windows.
             */
            public void actionPerformed(ActionEvent e) {
                // TODO : Print the output
                // 00.
                // 01.
                // 10.
                // 11.
                out.println(dataField.getText());

                String response;
                try {
                    response = in.readLine();
                    if (response == null || response.equals("-1")) {
                        System.exit(0);
                    }
                    System.out.println(response);
                } catch (IOException ex) {
                    response = "Error: " + ex;
                }
                System.out.println("kkkkkk");
                messageArea.append(response + "\n");
                dataField.selectAll();
            }
        });
    }

    public void connectToServer() throws IOException {
        // Get the server address from a dialog box.
        String serverAddress = JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:",
                "Welcome to the Capitalization Program", JOptionPane.QUESTION_MESSAGE);

        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Consume the initial welcoming messages from the server
        for (int i = 0; i < 3; i++) {
            messageArea.append(in.readLine() + "\n");
        }
    }

    public static void main(String[] args) throws Exception {
        RTUClient client = new RTUClient();
        client.connectToServer();
    }
}
