/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public class ClientHandler implements Runnable {

    private static ArrayList<ClientHandler> listOfUsers = new ArrayList<>();
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            listOfUsers.add(this);
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        String dbChange;
        while (!socket.isClosed()) {
            try {
                dbChange = in.readUTF();
                notifyAllUsers(dbChange);
            } catch (IOException ex) {
                close(socket, in, out);
                break;
            }
        }
    }

    private void notifyAllUsers(String change) {
        try {
            for (ClientHandler user : listOfUsers) {
                user.out.writeUTF(change);
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void close(Socket socket, DataInputStream in, DataOutputStream out) {
        removeUser();
        try {
            if (socket != null) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private void removeUser() {
        System.out.println(listOfUsers.remove(this));
    }
}
