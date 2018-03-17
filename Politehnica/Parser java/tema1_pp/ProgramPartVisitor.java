public interface ProgramPartVisitor {

    public int visit(If _if);

    public int visit(For _for);

    public int visit(Assert _assert);

    public int visit(Return _return);

    public int visit(Assignment _assignment);
}
