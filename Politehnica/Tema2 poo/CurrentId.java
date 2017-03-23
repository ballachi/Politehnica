
/**
 *
 * @author Marin
 */
public class CurrentId {

    private static CurrentId instance = null;
    private static int id;

    private CurrentId() {
    }

    public static CurrentId getInstanceId() {
        if (instance == null) {
            instance = new CurrentId();
        }
        return instance;
    }
    
    public int getId(){
        return id;
    }
    
    public int newGetId(){
        return id++;
    }
    
    public void set(int id){
        CurrentId.id = id;
    }

}
