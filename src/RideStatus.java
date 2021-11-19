public enum RideStatus {
    PENDING,
    IN_PROGRESS,
    FINISHED;
    @Override
    public String toString() {
        return this.name();
    }
}
