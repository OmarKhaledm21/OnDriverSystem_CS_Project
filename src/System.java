public class System {
    private static System system;

    private System(){}

    public static System getSystem(){
        if(system==null){
            system = new System();
        }
        return system;
    }
}
