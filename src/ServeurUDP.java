import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ServeurUDP {

    private static final int PORT = 1234;

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket(null)) {

            socket.bind(new InetSocketAddress(PORT));

            System.out.println("Serveur UDP démarré sur le port " + PORT);

            byte[] buffer = new byte[1024];

            while (true) {

                // استقبال datagramme
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(
                        packet.getData(),
                        packet.getOffset(),
                        packet.getLength(),
                        StandardCharsets.UTF_8
                );

                String ip = packet.getAddress().getHostAddress();
                int port = packet.getPort();

                System.out.println("Message reçu de " + ip + ":" + port + " -> " + message);
            }

        } catch (IOException e) {
            System.err.println("Erreur serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}