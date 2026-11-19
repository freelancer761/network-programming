package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class datatype {
    public static void main(String[] args) {
        // Thông tin cấu hình từ đề bài
        String studentCode = "B23DCCN334";
        String qCode = "w8tpNGjp";
        String serverIp = "36.50.135.242";
        int port = 2207;

        try {
            // Khởi tạo DatagramSocket
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(5000); // Set timeout 5s để tránh treo chương trình
            InetAddress address = InetAddress.getByName(serverIp);

            // ==========================================
            // a. Gửi thông điệp mở đầu lên server
            // ==========================================
            String sendMsg1 = ";" + studentCode + ";" + qCode;
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

            // Parse dữ liệu server gửi về (Định dạng: requestId;n;A1,A2,...,Am)
            String[] parts = receivedMsg.split(";");
            String requestId = parts[0];
            int n = Integer.parseInt(parts[1]);
            String[] aStr = parts[2].split(",");

            // Dùng mảng boolean để đánh dấu các số đã xuất hiện
            boolean[] isPresent = new boolean[n + 1];
            for (String s : aStr) {
                if (!s.trim().isEmpty()) {
                    int num = Integer.parseInt(s.trim());
                    if (num <= n) {
                        isPresent[num] = true;
                    }
                }
            }

            // ==========================================
            // c. Tìm kiếm các giá trị còn thiếu và gửi lại
            // ==========================================
            List<String> missingNums = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                if (!isPresent[i]) {
                    missingNums.add(String.valueOf(i));
                }
            }

            // Nối danh sách các số còn thiếu bằng dấu phẩy
            String resultNumbers = String.join(",", missingNums);
            String sendMsg2 = requestId + ";" + resultNumbers;

            // Gửi kết quả lên server
            byte[] sendData2 = sendMsg2.getBytes();
            DatagramPacket sendPacket2 = new DatagramPacket(sendData2, sendData2.length, address, port);
            socket.send(sendPacket2);
            System.out.println("Client sent: " + sendMsg2);

            // ==========================================
            // d. Đóng socket và kết thúc
            // ==========================================
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}