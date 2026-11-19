package RMI;

import RMI.CharacterService;
import java.net.URI;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class rmicharacter1 {
    public static void main(String[] args) {
        // Thay mã sinh viên của bạn vào đây
        String studentCode = "B23DCCN334"; 
        String qCode = "Xxb5KgCF";
        String host = "36.50.135.242";

        try {
            // Kết nối tới RMI Registry và lấy đối tượng RMICharacterService
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            // a. Nhận chuỗi ngẫu nhiên từ server
            String input = service.requestCharacter(studentCode, qCode);
            System.out.println("Chuỗi nhận được: " + input);

            // b. Mã hóa URL theo đúng quy tắc phần đường dẫn của URI trong đề bài
            String encoded = new URI(null, null, input, null).toASCIIString();
            System.out.println("Chuỗi sau khi mã hóa: " + encoded);

            // c. Gửi chuỗi đã mã hóa trở lại server
            service.submitCharacter(studentCode, qCode, encoded);
            System.out.println("Nộp bài thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
