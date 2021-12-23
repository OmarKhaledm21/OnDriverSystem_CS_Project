public abstract class RideEvent {
    protected Ride ride;

    RideEvent(Ride ride){
        this.ride=ride;
    }

    public Ride getRide(){
        return this.ride;
    }
}
