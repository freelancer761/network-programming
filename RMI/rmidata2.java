package RMI;

import RMI.DataService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Locale;

public class rmidata2 {
    public static void main(String[] args) {
        // Thay mã sinh viên của bạn vào đây
        String studentCode = "B23DCCN334"; 
        String qCode = "q6Z7OGAv";
        String host = "36.50.135.242";

        try {
            // Kết nối tới RMI Registry và lấy đối tượng RMIDataService
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            DataService service = (DataService) registry.lookup("RMIDataService");

            // b. Nhận chuỗi CSV các số thực từ server
            String csvData = (String) service.requestData(studentCode, qCode);
            System.out.println("Dữ liệu nhận được: " + csvData);

            // Chuyển chuỗi CSV thành mảng double[]
            String[] parts = csvData.trim().split(",");
            int n = parts.length;
            double[] nums = new double[n];
            double sum = 0;

            for (int i = 0; i < n; i++) {
                nums[i] = Double.parseDouble(parts[i].trim());
                sum += nums[i];
            }

            // c. Tính giá trị trung bình (average)
            double avg = sum / n;

            // Tính độ lệch chuẩn trên toàn bộ dãy (population standard deviation: chia cho n)
            double varianceSum = 0;
            for (double x : nums) {
                varianceSum += Math.pow(x - avg, 2);
            }
            double stddev = Math.sqrt(varianceSum / n);

            // Sắp xếp tăng dần và tìm phân vị 95 tại vị trí ceil(n * 0.95) - 1
            Arrays.sort(nums);
            int p95Index = (int) Math.ceil(n * 0.95) - 1;
            double p95 = nums[p95Index];

            // d. Định dạng kết quả làm tròn 02 chữ số thập phân (dùng Locale.US để giữ dấu chấm '.')
            String result = String.format(Locale.US, "average=%.2f;stddev=%.2f;p95=%.2f", avg, stddev, p95);
            System.out.println("Kết quả gửi đi: " + result);

            // Gửi kết quả trở lại server
            service.submitData(studentCode, qCode, result);
            System.out.println("Nộp bài thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
