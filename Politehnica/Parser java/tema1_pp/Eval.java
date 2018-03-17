import java.util.ArrayList;


public interface Eval {
    public int accept(ProgramPartVisitor programPartVisitor);
    public ArrayList<ProgramPart> getList();
    
}
