import java.io.*;
import java.net.*;

public class TicTacToeClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public TicTacToeClient() throws IOException {
        socket = new Socket("localhost", 5909);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(message);
                if (message.contains("Enter your move")) {
                    BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
                    out.println(keyboardInput.readLine());
                }
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Server is not responding.");
        } catch (IOException e) {
            System.err.println("Connection to the server lost.");
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Client socket closed.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TicTacToeClient client = new TicTacToeClient();
    }
}