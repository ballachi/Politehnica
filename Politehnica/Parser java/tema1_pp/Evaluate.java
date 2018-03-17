import java.util.ArrayList;

/**
 * Se face evaluare fiecarui Program
 * @author Marin
 */
public class Evaluate implements ProgramPart , Eval{

    public ArrayList<ProgramPart> program = new ArrayList();
    String code;
    public Evaluate(String code) {
        this.code = code;
    }

    Evaluate() {
    }

    @Override
    public int accept(ProgramPartVisitor programPartVisitor) {
        int x = 0;
        for (int i = 0; i < program.size(); i++) {
            x = program.get(i).accept(programPartVisitor);
        }
        return x;
    }

    @Override
    public ArrayList<ProgramPart> getList() {
        return program;
    }


}
