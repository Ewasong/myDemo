package top.sorie.socket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClientTest {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8888);
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println("wwwwww");
            pw.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = reader.readLine();
            System.out.println(line);
            pw.close();
            reader.close();;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
