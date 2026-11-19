package UDP;

import java.io.Serializable; 

public class Student implements Serializable{
    private static final long serialVersionUID = 20171107; //
    private String id; //
    private String code; //[cite: 6]
    private String name; //[cite: 6]
    private String email; //[cite: 6]

    public Student(String id, String code, String name, String email) { //[cite: 6]
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
    }

    public Student(String code) { //[cite: 6]
        this.code = code;
    }

    // Các hàm Getters và Setters để thao tác với đối tượng
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
