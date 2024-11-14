import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 1234;
    private static final List<String[]> questions = new ArrayList<>();
    private static final ExecutorService pool = Executors.newFixedThreadPool(5); // 다중 클라이언트 지원

    public static void main(String[] args) {
        loadQuestions();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadQuestions() {
        questions.add(new String[]{"Capital of South Korea?", "Seoul"});
        questions.add(new String[]{"4 * 3 = ?", "12"});
        questions.add(new String[]{"Color of the sky?", "Blue"});
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private int score = 0;
    
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
    
        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                
                for (String[] q : questions) {
                    out.println("QUESTION:" + q[0]);
                    String answer = in.readLine().trim();
    
                    System.out.println("Client Answer: '" + answer + "'");
                    System.out.println("Correct Answer: '" + q[1] + "'");
    
                    if (answer.equalsIgnoreCase(q[1].trim())) {
                        out.println("FEEDBACK:Correct");
                        score++;
                    } else {
                        out.println("FEEDBACK:Incorrect");
                    }
                }
                out.println("SCORE:" + score);
                System.out.println("Client disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


