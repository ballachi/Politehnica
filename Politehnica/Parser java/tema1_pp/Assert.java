
public class Assert implements ProgramPart {

    String code;

    Error err = Error.getInstance();

    Assert(String code) {
        this.code = code;
    }

    @Override
    public int accept(ProgramPartVisitor programPartVisitor) {
        int x = programPartVisitor.visit(this);
        if (x == 0) {
            err.setError(2); // in caz ca conditia nui corecta se seteaza eroarea 
        }
        return 0;

    }


}
