import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientChatUDP {

    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {

        try {

            Scanner scanner = new Scanner(System.in);

            System.out.print("Entrez votre nom d'utilisateur : ");
            String username = scanner.nextLine().trim();

            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");

            DatagramSocket socket = new DatagramSocket();

            System.out.println("Client démarré. Tapez vos messages.");
            System.out.println("Tapez 'exit' pour quitter.");

            Thread receptionThread = new Thread(() -> {

                byte[] buffer = new byte[1024];

                while (!socket.isClosed()) {

                    try {

                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        String message = new String(
                                packet.getData(),
                                packet.getOffset(),
                                packet.getLength(),
                                StandardCharsets.UTF_8
                        );

                        System.out.println("\n" + message);
                        System.out.print("> ");

                    } catch (IOException e) {

                        if (!socket.isClosed()) {
                            System.err.println("Erreur réception : " + e.getMessage());
                        }

                        break;
                    }
                }
            });

            receptionThread.setDaemon(true);
            receptionThread.start();

            while (true) {

                String text = scanner.nextLine();

                if (text.equalsIgnoreCase("exit")) {
                    break;
                }

                String message = "[" + username + "]: " + text;

                byte[] data = message.getBytes(StandardCharsets.UTF_8);

                DatagramPacket packet = new DatagramPacket(
                        data,
                        data.length,
                        serverAddress,
                        SERVER_PORT
                );

                socket.send(packet);
            }

            socket.close();
            scanner.close();

            System.out.println("Client fermé.");

        } catch (IOException e) {
            System.err.println("Erreur client : " + e.getMessage());
            e.printStackTrace();
        }
    }
}