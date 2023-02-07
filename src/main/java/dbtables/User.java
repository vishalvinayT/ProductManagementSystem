package dbtables;

public class User {
    public String name; //Not NUll
    public String email; // Not Null
    public String phone=null;
    public String country; //Not Null
    public String street; // Not NUll
    public String pincode;//Not NUll

    public User(){}
    public User(String name, String email, String phone, String country, String street, String pincode){
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.country=country;
        this.street=street;
        this.pincode=pincode;
    }

}
