package RMI;

import RMI.DataService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class rmidata1 {
    public static void main(String[] args) {
        // Thay mã sinh viên của bạn vào đây
        String studentCode = "B23DCCN334"; 
        String qCode = "aRUfaszP";
        String host = "36.50.135.242";

        try {
            // Kết nối tới RMI Registry và lấy đối tượng RMIDataService
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            DataService service = (DataService) registry.lookup("RMIDataService");

            // b. Nhận mảng số nguyên từ server
            Object response = service.requestData(studentCode, qCode);
            int[] arr = toIntArray(response);
            System.out.println("Mảng nhận được: " + Arrays.toString(arr));

            // c. Tìm các vị trí đỉnh cục bộ (chỉ số bắt đầu từ 1)
            List<Integer> peaks = new ArrayList<>();
            int n = arr.length;

            if (n == 1) {
                peaks.add(1);
            } else if (n > 1) {
                for (int i = 0; i < n; i++) {
                    if (i == 0) {
                        if (arr[i] > arr[i + 1]) {
                            peaks.add(i + 1);
                        }
                    } else if (i == n - 1) {
                        if (arr[i] > arr[i - 1]) {
                            peaks.add(i + 1);
                        }
                    } else {
                        if (arr[i] > arr[i - 1] && arr[i] > arr[i + 1]) {
                            peaks.add(i + 1);
                        }
                    }
                }
            }

            System.out.println("Các vị trí đỉnh cục bộ: " + peaks);

            // d. Gửi List<Integer> các vị trí đỉnh cục bộ lên server
            service.submitData(studentCode, qCode, peaks);
            System.out.println("Nộp bài thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm hỗ trợ chuyển đổi dữ liệu Object từ server sang mảng int[] an toàn
    private static int[] toIntArray(Object obj) {
        if (obj instanceof int[]) {
            return (int[]) obj;
        } else if (obj instanceof Integer[]) {
            Integer[] wrapperArr = (Integer[]) obj;
            int[] res = new int[wrapperArr.length];
            for (int i = 0; i < wrapperArr.length; i++) {
                res[i] = wrapperArr[i];
            }
            return res;
        } else if (obj instanceof List<?>) {
            List<?> list = (List<?>) obj;
            int[] res = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                res[i] = ((Number) list.get(i)).intValue();
            }
            return res;
        }
        throw new IllegalArgumentException("Kiểu dữ liệu không hợp lệ: " + obj.getClass());
    }
}