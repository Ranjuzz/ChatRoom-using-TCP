package Persons;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;

public class Ranjith extends JFrame implements Runnable, ActionListener {

    private JTextField textField;
    private JTextArea textArea;
    private JButton sendButton;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public Ranjith() {
        setTitle("Sanjay Chat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        textField = new JTextField(30);
        textArea = new JTextArea(25, 25);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        JPanel inputPanel = new JPanel();
        inputPanel.add(textField);
        inputPanel.add(sendButton);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        setVisible(true);

        try {
            socket = new Socket("localhost", 1200);

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread chatThread = new Thread(this);
        chatThread.setDaemon(true);
        chatThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            sendMessage();
        }
    }

    private void sendMessage() {
        String message = textField.getText().trim();
        if (!message.isEmpty()) {
            textArea.append("You: " + message + "\n");
            textField.setText("");

            try {
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = dataInputStream.readUTF();
                textArea.append("Sanjay: " + message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Ranjith();
    }
}
