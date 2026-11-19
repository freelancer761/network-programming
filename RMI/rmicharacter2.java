package RMI;

import RMI.CharacterService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class rmicharacter2 {
    public static void main(String[] args) {
        // Thay mã sinh viên của bạn vào đây
        String studentCode = "B23DCCN334"; 
        String qCode = "ugxVhud5";
        String host = "36.50.135.242";

        try {
            // Kết nối tới RMI Registry và lấy đối tượng RMICharacterService
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            // a. Nhận chuỗi số thập phân từ server
            String input = service.requestCharacter(studentCode, qCode);
            System.out.println("Chuỗi nhận được: " + input);

            // b. Chuyển đổi số thập phân sang số La Mã (Roman)
            int number = Integer.parseInt(input.trim());
            String roman = intToRoman(number);
            System.out.println("Số La Mã chuyển đổi: " + roman);

            // c. Gửi chuỗi số La Mã đã chuyển đổi trở lại server
            service.submitCharacter(studentCode, qCode, roman);
            System.out.println("Nộp bài thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm chuyển đổi số nguyên sang số La Mã
    private static String intToRoman(int num) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                sb.append(symbols[i]);
            }
        }
        return sb.toString();
    }
}