import java.io.*;
import java.net.*;

public class Client {
    private static final String CONFIG_FILE = "server_info.dat";
    private static String serverAddress = "localhost";
    private static int port = 1234;

    public static void main(String[] args) {
        loadServerInfo();
        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            String question;
            while ((question = in.readLine()) != null) {
                if (question.startsWith("QUESTION:")) {
                    System.out.println("Question: " + question.substring(9));
                    String answer = consoleInput.readLine();
                    out.println(answer.trim()); 
                } else if (question.startsWith("FEEDBACK:")) {
                    System.out.println(question.substring(9));
                } else if (question.startsWith("SCORE:")) {
                    System.out.println("Your final score is: " + question.substring(6));
                    break;
                }
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadServerInfo() {
        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE))) {
            serverAddress = br.readLine();
            port = Integer.parseInt(br.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Using default server settings (localhost:1234)");
        }
    }
}
