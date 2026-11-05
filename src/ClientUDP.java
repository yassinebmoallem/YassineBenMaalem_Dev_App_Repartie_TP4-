import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientUDP {

    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {

        try (
                Scanner scanner = new Scanner(System.in);
                DatagramSocket socket = new DatagramSocket()
        ) {

            System.out.print("Entrez votre nom d'utilisateur : ");
            String username = scanner.nextLine().trim();

            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");

            System.out.println("Client prêt. Tapez un message puis Entrée.");
            System.out.println("Tapez 'exit' pour quitter.");

            while (true) {

                String text = scanner.nextLine();

                if (text.equalsIgnoreCase("exit")) {
                    System.out.println("Fermeture du client...");
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

        } catch (IOException e) {
            System.err.println("Erreur client : " + e.getMessage());
            e.printStackTrace();
        }
    }
}