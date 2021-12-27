public abstract class User {
    private String username;
    private String password;
    private String birthDay;
    private String email;
    private String mobileNumber;
    protected int status;

    public User(){
        this.username = null;
        this.password = null;
        this.email = null;
        this.mobileNumber = null;
        this.status=0;
    }

    public User(String username,String password,String email,String mobileNumber,int status, String date){
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.birthDay = date;
        this.status = status;
    }

    ///////////////////////////////////// Getters and Setters /////////////////////////////////////

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public abstract void displayMenu();
}
