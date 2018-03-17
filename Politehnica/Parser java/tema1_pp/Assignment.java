public class Assignment implements ProgramPart {

    String code;

    public Assignment(String code) {
        this.code = code;
    }

    @Override
    public int accept(ProgramPartVisitor programPartVisitor) {
        return programPartVisitor.visit(this);

    }


}
