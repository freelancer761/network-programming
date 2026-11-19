package UDP;

import UDP.Customer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class customerobject {
    public static void main(String[] args) {
        String studentCode = "B23DCCN334"; 
        String qCode = "gE0mRZwf"; //[cite: 8]
        String serverIp = "36.50.135.242"; //[cite: 8]
        int port = 2209; //[cite: 8]

        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(5000);
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
            // b. Nhận thông điệp chứa Object từ server
            // ==========================================
            byte[] receiveData = new byte[2048];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            // Trích xuất 8 byte đầu chứa requestId[cite: 8]
            String requestId = new String(receiveData, 0, 8); 
            
            // Đọc đối tượng Customer từ các byte còn lại[cite: 8]
            ByteArrayInputStream bais = new ByteArrayInputStream(receiveData, 8, receivePacket.getLength() - 8);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Customer customer = (Customer) ois.readObject();
            System.out.println("Received Original Name: " + customer.getName());
            System.out.println("Received Original DOB: " + customer.getDayOfBirth());

            // ==========================================
            // c. Xử lý dữ liệu đối tượng
            // ==========================================
            String[] nameWords = customer.getName().trim().split("\\s+");
            
            // 1. Tạo Username: các chữ cái đầu in thường + tên cuối in thường[cite: 8]
            StringBuilder usernameBuilder = new StringBuilder();
            for (int i = 0; i < nameWords.length - 1; i++) {
                usernameBuilder.append(nameWords[i].substring(0, 1).toLowerCase());
            }
            String lastName = nameWords[nameWords.length - 1].toLowerCase();
            usernameBuilder.append(lastName);
            customer.setUserName(usernameBuilder.toString());

            // 2. Chuẩn hóa Tên: TÊN CUỐI IN HOA, Các Từ Đầu Viết Hoa Chữ Cái Đầu[cite: 8]
            StringBuilder normalizedName = new StringBuilder();
            normalizedName.append(lastName.toUpperCase()).append(",");
            for (int i = 0; i < nameWords.length - 1; i++) {
                String word = nameWords[i].toLowerCase();
                normalizedName.append(" ").append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
            }
            customer.setName(normalizedName.toString().trim()); // Gán lại tên mới

            // 3. Chuẩn hóa Ngày sinh: mm-dd-yyyy -> dd/mm/yyyy[cite: 8]
            String[] dobParts = customer.getDayOfBirth().split("-");
            if (dobParts.length == 3) {
                // dobParts[0] là mm, dobParts[1] là dd, dobParts[2] là yyyy
                String newDob = dobParts[1] + "/" + dobParts[0] + "/" + dobParts[2];
                customer.setDayOfBirth(newDob);
            }

            System.out.println("Modified Name: " + customer.getName());
            System.out.println("Modified DOB: " + customer.getDayOfBirth());
            System.out.println("Generated Username: " + customer.getUserName());

            // ==========================================
            // d. Đóng gói và gửi lại Server
            // ==========================================
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(requestId.getBytes()); // Ghi 8 byte requestId vào đầu mảng[cite: 8]
            
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(customer); // Ghi đối tượng Customer đã sửa đổi[cite: 8]
            oos.flush();
            
            byte[] sendData2 = baos.toByteArray();
            DatagramPacket sendPacket2 = new DatagramPacket(sendData2, sendData2.length, address, port);
            socket.send(sendPacket2);
            System.out.println("Client sent updated Object back to server.");

            // ==========================================
            // Đóng socket và kết thúc chương trình[cite: 9]
            // ==========================================
            socket.close(); 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
