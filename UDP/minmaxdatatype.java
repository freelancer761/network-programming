package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class minmaxdatatype {
    public static void main(String[] args) {
        // Thông tin cấu hình từ đề bài
        String studentCode = "B23DCCN334"; //
        String qCode = "bE0nAfxp"; //
        String serverIp = "36.50.135.242"; //[cite: 4]
        int port = 2207; //[cite: 4]

        try {
            // Khởi tạo DatagramSocket
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(5000); 
            InetAddress address = InetAddress.getByName(serverIp);

            // ==========================================
            // a. Gửi thông điệp mở đầu lên server
            // ==========================================
            String sendMsg1 = ";" + studentCode + ";" + qCode; //[cite: 4]
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

            // Tách dữ liệu (Định dạng: requestId;a1,a2,...,a50)
            String[] parts = receivedMsg.split(";");
            String requestId = parts[0];
            String[] numbersStr = parts[1].split(",");

            // ==========================================
            // c. Tìm giá trị lớn nhất (max) và nhỏ nhất (min)
            // ==========================================
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            for (String s : numbersStr) {
                if (!s.trim().isEmpty()) {
                    int num = Integer.parseInt(s.trim());
                    if (num > max) {
                        max = num;
                    }
                    if (num < min) {
                        min = num;
                    }
                }
            }

            // Ghép chuỗi kết quả (Định dạng: requestId;max,min)
            String sendMsg2 = requestId + ";" + max + "," + min; //[cite: 4]

            // Gửi kết quả lên server
            byte[] sendData2 = sendMsg2.getBytes();
            DatagramPacket sendPacket2 = new DatagramPacket(sendData2, sendData2.length, address, port);
            socket.send(sendPacket2);
            System.out.println("Client sent: " + sendMsg2);

            // ==========================================
            // d. Đóng socket và kết thúc
            // ==========================================
            socket.close(); //[cite: 4]

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
