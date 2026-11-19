package UDP;

import UDP.Student;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class object {
    public static void main(String[] args) {
        // Thông tin cấu hình từ đề bài
        String studentCode = "B23DCCN334"; 
        String qCode = "LtAxYJLN"; //[cite: 6]
        String serverIp = "36.50.135.242"; //[cite: 6]
        int port = 2209; //[cite: 6]

        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(5000);
            InetAddress address = InetAddress.getByName(serverIp);

            // ==========================================
            // a. Gửi thông điệp mở đầu lên server
            // ==========================================
            String sendMsg1 = ";" + studentCode + ";" + qCode; //[cite: 6]
            byte[] sendData1 = sendMsg1.getBytes();
            DatagramPacket sendPacket1 = new DatagramPacket(sendData1, sendData1.length, address, port);
            socket.send(sendPacket1);
            System.out.println("Client sent: " + sendMsg1);

            // ==========================================
            // b. Nhận thông điệp chứa Object từ server
            // ==========================================
            byte[] receiveData = new byte[2048]; // Buffer lớn hơn cho Object
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            // Tách 8 byte đầu lấy requestId và phần còn lại chứa Object[cite: 6]
            String requestId = new String(receiveData, 0, 8); //[cite: 6]
            
            ByteArrayInputStream bais = new ByteArrayInputStream(receiveData, 8, receivePacket.getLength() - 8);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Student student = (Student) ois.readObject();
            System.out.println("Received Student Name: " + student.getName());

            // ==========================================
            // c. Xử lý chuẩn hóa chuỗi tên và tạo email
            // ==========================================
            String[] words = student.getName().trim().split("\\s+");
            StringBuilder normalizedName = new StringBuilder();
            StringBuilder emailPrefix = new StringBuilder();

            // Lấy tên (từ cuối cùng) làm tiền tố email[cite: 6]
            String lastName = words[words.length - 1].toLowerCase();
            emailPrefix.append(lastName);

            for (int i = 0; i < words.length; i++) {
                String word = words[i].toLowerCase();
                // Chuẩn hóa tên: Chữ cái đầu viết hoa, phần còn lại viết thường[cite: 6]
                String formattedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
                normalizedName.append(formattedWord);
                
                if (i < words.length - 1) {
                    normalizedName.append(" ");
                    // Lấy các chữ cái đầu của họ và tên đệm ghép vào email[cite: 6]
                    emailPrefix.append(word.charAt(0));
                }
            }

            // Gán lại dữ liệu cho đối tượng[cite: 6]
            student.setName(normalizedName.toString());
            student.setEmail(emailPrefix.toString() + "@ptit.edu.vn"); //[cite: 6]
            System.out.println("Normalized Name: " + student.getName());
            System.out.println("Generated Email: " + student.getEmail());

            // Đóng gói dữ liệu gửi lên Server (8 bytes requestId + Object byte array)[cite: 6]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(requestId.getBytes()); // Ghi 8 byte requestId[cite: 6]
            
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(student); // Ghi Object đã cập nhật[cite: 6]
            oos.flush();
            
            byte[] sendData2 = baos.toByteArray();
            DatagramPacket sendPacket2 = new DatagramPacket(sendData2, sendData2.length, address, port);
            socket.send(sendPacket2);
            System.out.println("Client sent response Object back to server.");

            // ==========================================
            // d. Đóng socket
            // ==========================================
            socket.close(); //[cite: 6]

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
