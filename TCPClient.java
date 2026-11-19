import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPClient {
    public static void main(String[] args) {
        String serverIp = "36.50.135.242";
        int serverPort = 2208;
        String studentCode = "B23DCCN334";
        String qCode = "4Mwb8I9k";
        try (Socket socket = new Socket(serverIp, serverPort);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String initialMessage = studentCode + ";" + qCode;
            out.write(initialMessage);
            out.newLine(); 
            out.flush();  
            System.out.println("Đã gửi: " + initialMessage);
            String response = in.readLine();
            System.out.println("Server trả về: " + response);

            if (response != null && !response.isEmpty()) {
                String[] domains = response.split(",");
                List<String> eduDomains = new ArrayList<>();

                for (String domain : domains) {
                    String cleanDomain = domain.trim();
                    if (cleanDomain.endsWith(".edu")) {
                        eduDomains.add(cleanDomain);
                    }
                }
                String finalAnswer = String.join(", ", eduDomains);
                out.write(finalAnswer);
                out.newLine();
                out.flush();
                System.out.println("Đã gửi kết quả: " + finalAnswer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
