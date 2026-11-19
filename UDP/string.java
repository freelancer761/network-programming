package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class string {
    public static void main(String[] args) {
        // Thông tin cấu hình lấy từ đề bài
        String studentCode = "B23DCCN334"; //
        String qCode = "X0OtnIbg"; //[cite: 3]
        String serverIp = "36.50.135.242"; //[cite: 3]
        int port = 2208; //[cite: 3]

        try {
            // 1. Khởi tạo DatagramSocket
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(5000); // Set timeout 5s
            InetAddress address = InetAddress.getByName(serverIp);

            // ==========================================
            // a. Gửi thông điệp mở đầu lên server
            // ==========================================
            String sendMsg1 = ";" + studentCode + ";" + qCode; //[cite: 3]
            byte[] sendData1 = sendMsg1.getBytes();
            DatagramPacket sendPacket1 = new DatagramPacket(sendData1, sendData1.length, address, port);
            socket.send(sendPacket1);
            System.out.println("Client sent: " + sendMsg1);

            // ==========================================
            // b. Nhận thông điệp từ server
            // ==========================================
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String receivedMsg = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Client received: " + receivedMsg);

            // Tách dữ liệu (Định dạng: requestId;data)[cite: 3]
            String[] parts = receivedMsg.split(";");
            String requestId = parts[0];
            String data = parts[1];

            // ==========================================
            // c. Tìm ký tự xuất hiện nhiều nhất và các vị trí
            // ==========================================
            int[] count = new int[256]; // Mảng đếm tần suất các ký tự
            for (char c : data.toCharArray()) {
                count[c]++;
            }

            char maxChar = data.charAt(0);
            int maxCount = 0;
            
            // Duyệt qua chuỗi để tìm ký tự có số lần lặp lớn nhất xuất hiện đầu tiên
            for (int i = 0; i < data.length(); i++) {
                char c = data.charAt(i);
                if (count[c] > maxCount) {
                    maxCount = count[c];
                    maxChar = c;
                }
            }

            // Tìm các vị trí của ký tự maxChar (vị trí tính từ 1)
            StringBuilder positions = new StringBuilder();
            for (int i = 0; i < data.length(); i++) {
                if (data.charAt(i) == maxChar) {
                    positions.append(i + 1).append(","); 
                }
            }

            // Ghép chuỗi kết quả
            String sendMsg2 = requestId + ";" + maxChar + ":" + positions.toString(); //[cite: 3]

            // Gửi kết quả lên server
            byte[] sendData2 = sendMsg2.getBytes();
            DatagramPacket sendPacket2 = new DatagramPacket(sendData2, sendData2.length, address, port);
            socket.send(sendPacket2);
            System.out.println("Client sent: " + sendMsg2);

            // ==========================================
            // d. Đóng socket và kết thúc
            // ==========================================
            socket.close(); //[cite: 3]

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
