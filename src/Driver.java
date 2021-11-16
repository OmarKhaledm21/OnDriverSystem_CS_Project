import java.util.Dictionary;

public class Driver extends User{
    private String nationalID;
    private String licenseNumber;
    private double averageRating;
    private boolean activationStatus;

    public Driver(){
        this.activationStatus = false;
    }

    public Driver(String username,String password,String email,String mobileNumber,String nationalID,String licenseNumber){
        super(username,password,email,mobileNumber);
        this.nationalID = nationalID;
        this.licenseNumber = licenseNumber;
        this.activationStatus = false;
    }

    ///////////////////////////////////// Getters and Setters /////////////////////////////////////

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public boolean isActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(boolean activationStatus) {
        this.activationStatus = activationStatus;
    }
}
