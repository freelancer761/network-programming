package RMI;

import RMI.ByteService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class rmibyte2 {
    public static void main(String[] args) {
        // Thay mã sinh viên của bạn vào đây
        String studentCode = "B23DCCN334"; 
        String qCode = "tCKfRTf6";
        String host = "36.50.135.242";

        try {
            // Kết nối tới RMI Registry và tìm đối tượng từ xa RMIByteService
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            // a. Nhận mảng dữ liệu byte[] từ server
            byte[] data = service.requestData(studentCode, qCode);
            System.out.println("Dữ liệu nhận được: " + Arrays.toString(data));

            // b. Đếm tần suất và tìm phần tử xuất hiện nhiều nhất (ưu tiên phần tử xuất hiện đầu tiên)
            Map<Byte, Integer> freqMap = new LinkedHashMap<>();
            for (byte b : data) {
                freqMap.put(b, freqMap.getOrDefault(b, 0) + 1);
            }

            byte maxElement = data[0];
            int maxCount = 0;

            for (Map.Entry<Byte, Integer> entry : freqMap.entrySet()) {
                // Dùng dấu '>' để giữ nguyên phần tử xuất hiện đầu tiên nếu có cùng tần suất cao nhất
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    maxElement = entry.getKey();
                }
            }

            // Tạo mảng kết quả: [phần tử xuất hiện nhiều nhất, tần suất xuất hiện]
            byte[] result = new byte[]{maxElement, (byte) maxCount};
            System.out.println("Kết quả gửi đi: " + Arrays.toString(result));

            // c. Gửi mảng kết quả trở lại server
            service.submitData(studentCode, qCode, result);
            System.out.println("Nộp bài thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}