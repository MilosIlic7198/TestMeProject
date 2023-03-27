/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Milos
 */
public class Server {

    private ServerSocket serverSocket;
    private ExecutorService pool;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.pool = Executors.newCachedThreadPool();
    }

    public void runServer() {
        try {
            System.out.println("Server is running!");
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection established!");
                ClientHandler clientHandler = new ClientHandler(socket);
                pool.submit(clientHandler);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while accepting connection!");
            ex.printStackTrace();
            closeServer(serverSocket);
        } finally {
            pool.shutdown();
        }
    }

    private void closeServer(ServerSocket serverSocket) {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while closing server!");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7007);
        Server server = new Server(serverSocket);
        server.runServer();
    }
}
