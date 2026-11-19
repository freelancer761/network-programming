package RMI;
import RMI.ByteService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class rmibyte1 {
    public static void main(String[] args) {
        // Thay đổi mã sinh viên của bạn tại đây
        String studentCode = "B23DCCN334"; 
        String qCode = "iUPM2H69";
        String host = "36.50.135.242";

        try {
            // 1. Kết nối tới RMI Registry trên Server (mặc định cổng 1099)
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            // a. Triệu gọi phương thức requestData để nhận mảng byte[] từ server
            byte[] data = service.requestData(studentCode, qCode);
            System.out.println("Dữ liệu nhận được: " + Arrays.toString(data));

            // b. Tìm phần tử có số lần xuất hiện ít nhất (ưu tiên xuất hiện đầu tiên)
            Map<Byte, Integer> freqMap = new LinkedHashMap<>();
            for (byte b : data) {
                freqMap.put(b, freqMap.getOrDefault(b, 0) + 1);
            }

            byte minElement = data[0];
            int minCount = Integer.MAX_VALUE;

            for (Map.Entry<Byte, Integer> entry : freqMap.entrySet()) {
                // Dùng dấu '<' để giữ lại phần tử xuất hiện đầu tiên nếu có cùng số lần ít nhất
                if (entry.getValue() < minCount) {
                    minCount = entry.getValue();
                    minElement = entry.getKey();
                }
            }

            // Tạo mảng kết quả gồm 2 phần tử: [phần tử xuất hiện ít nhất, số lần xuất hiện]
            byte[] result = new byte[]{minElement, (byte) minCount};
            System.out.println("Kết quả gửi đi: " + Arrays.toString(result));

            // c. Gửi mảng kết quả trở lại server
            service.submitData(studentCode, qCode, result);
            System.out.println("Nộp bài thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}