public class Return implements ProgramPart {

    String code;
    static int ret = 0;

    Return(String code) {
        this.code = code;
    }

    @Override
    public int accept(ProgramPartVisitor programPartVisitor) {
        int x = programPartVisitor.visit(this);
        (Error.getInstance()).setError(0); // se seteaza ca am trecut prin return
        return x;

    }



}
