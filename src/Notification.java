public abstract class Notification {
    private Ride ride;

    public Notification(Ride ride){
        this.ride = ride;
    }
    public Ride getRide() {
        return ride;
    }
}
