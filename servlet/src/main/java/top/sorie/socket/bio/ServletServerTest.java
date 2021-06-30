package top.sorie.socket.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServletServerTest {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8888);
            // 等待请求
            Socket socket = server.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = reader.readLine();
            System.out.println(line);

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println("received:" + line);
            printWriter.flush();
            printWriter.close();
            reader.close();
            socket.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
