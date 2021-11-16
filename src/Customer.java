public class Customer extends User {
    private boolean activationStatus;

    public Customer(){this.activationStatus = false;}

    public Customer(String username,String password,String email,String mobileNumber){
        super(username,password,email,mobileNumber);
        this.activationStatus = false;
    }
}
