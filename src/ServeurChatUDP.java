import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServeurChatUDP {

    private static final int PORT = 1234;

    private static final List<SocketAddress> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket(null)) {

            socket.bind(new InetSocketAddress(PORT));

            System.out.println("Serveur de chat UDP démarré sur le port " + PORT);

            byte[] buffer = new byte[1024];

            while (true) {

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                SocketAddress sender = new InetSocketAddress(
                        packet.getAddress(),
                        packet.getPort()
                );

                if (!clients.contains(sender)) {
                    clients.add(sender);
                    System.out.println("Nouveau client enregistré : " + sender);
                }

                String message = new String(
                        packet.getData(),
                        packet.getOffset(),
                        packet.getLength(),
                        StandardCharsets.UTF_8
                );

                System.out.println("Reçu de " + sender + " -> " + message);

                for (SocketAddress client : clients) {
                    if (!client.equals(sender)) {

                        DatagramPacket forward = new DatagramPacket(
                                packet.getData(),
                                packet.getLength(),
                                client
                        );

                        socket.send(forward);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}