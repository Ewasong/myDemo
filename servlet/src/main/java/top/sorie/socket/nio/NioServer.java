package top.sorie.socket.nio;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.logging.Handler;

public class NioServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(8888));
        // 非阻塞模式
        server.configureBlocking(false);
        // 注册选择器
        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
        Handler handler = new Handler();
        while (true) {
            if (selector.select(3000) == 0) {
                System.out.println("等待超时");
                continue;
            }
            System.out.println("处理请求");
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                try {
                    if (key.isAcceptable()) {
                        handler.handleAccept(key);
                    }
                    if (key.isReadable()) {
                        handler.handleRead(key);
                    }

                } catch (IOException e) {
                }
                keyIterator.remove();
            }
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class Handler {
        private int bufferSize = 1024;
        private String localCharset = "UTF-8";

        public void handleAccept(SelectionKey key) throws IOException {
            SocketChannel channel = ((ServerSocketChannel)(key.channel())).accept();
            channel.configureBlocking(false);
            channel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
        }
        public void handleRead(SelectionKey key) throws IOException {
            // chanel
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = (ByteBuffer)key.attachment();
            byteBuffer.clear();
            if (channel.read(byteBuffer) == -1) {
                channel.close();
            } else {
                // read state
                byteBuffer.flip();
                String received = Charset.forName(localCharset).decode(byteBuffer).toString();
                System.out.println("received:" + received);
                String sendStr = "Hello world";
                byteBuffer = ByteBuffer.wrap(sendStr.getBytes(localCharset));
                channel.write(byteBuffer);
                channel.close();
            }
        }
    }
}
