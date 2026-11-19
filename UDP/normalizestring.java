package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class normalizestring {
    public static void main(String[] args) {
        // Thông tin cấu hình từ đề bài
        String studentCode = "B23DCCN334"; //
        String qCode = "xpacj67C"; //
        String serverIp = "36.50.135.242"; //
        int port = 2208; //[cite: 5]

        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(5000);
            InetAddress address = InetAddress.getByName(serverIp);

            // ==========================================
            // a. Gửi thông điệp mở đầu lên server
            // ==========================================
            String sendMsg1 = ";" + studentCode + ";" + qCode; //[cite: 5]
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

            // Tách dữ liệu (Định dạng: requestId;data)
            String[] parts = receivedMsg.split(";"); //[cite: 5]
            String requestId = parts[0];
            String data = parts[1];

            // ==========================================
            // c. Xử lý chuẩn hóa chuỗi
            // ==========================================
            // Loại bỏ khoảng trắng thừa ở hai đầu và tách các từ dựa trên khoảng trắng
            String[] words = data.trim().split("\\s+");
            StringBuilder normalizedData = new StringBuilder();

            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                if (word.length() > 0) {
                    // Ký tự đầu tiên in hoa, các ký tự còn lại in thường[cite: 5]
                    normalizedData.append(word.substring(0, 1).toUpperCase());
                    if (word.length() > 1) {
                        normalizedData.append(word.substring(1).toLowerCase());
                    }
                    // Thêm khoảng trắng giữa các từ
                    if (i < words.length - 1) {
                        normalizedData.append(" ");
                    }
                }
            }

            // Ghép chuỗi kết quả
            String sendMsg2 = requestId + ";" + normalizedData.toString(); //[cite: 5]

            // Gửi kết quả lên server
            byte[] sendData2 = sendMsg2.getBytes();
            DatagramPacket sendPacket2 = new DatagramPacket(sendData2, sendData2.length, address, port);
            socket.send(sendPacket2);
            System.out.println("Client sent: " + sendMsg2);

            // ==========================================
            // d. Đóng socket và kết thúc chương trình
            // ==========================================
            socket.close(); //[cite: 5]

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
