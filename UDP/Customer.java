package UDP;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 20151107; //
    private String id; //
    private String code; //
    private String name; //[cite: 8]
    private String dayOfBirth; //[cite: 8]
    private String userName; //[cite: 8]

    // Một Hàm khởi tạo với đầy đủ các thuộc tính[cite: 8]
    public Customer(String id, String code, String name, String dayOfBirth, String userName) { 
        this.id = id;
        this.code = code;
        this.name = name;
        this.dayOfBirth = dayOfBirth;
        this.userName = userName;
    }

    // Các hàm Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDayOfBirth() { return dayOfBirth; }
    public void setDayOfBirth(String dayOfBirth) { this.dayOfBirth = dayOfBirth; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
