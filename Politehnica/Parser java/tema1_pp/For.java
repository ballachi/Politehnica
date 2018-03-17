public class For implements ProgramPart {

    String code;
    int count;

    For(String code) {
        this.code = code;
    }

    @Override
    public int accept(ProgramPartVisitor programPartVisitor) {
        count = 6;
        String s = getCode();
        (new Assignment(s)).accept(programPartVisitor);
        String _if = getCode();
        String _as = getCode();
        String prg = getCode();
        If i = new If(_if);
        Assignment assignment = new Assignment(_as);
        while ((programPartVisitor.visit(this)) == 1) {
            Eval ev = new NameDecorator(new Evaluate(),prg);
            int x = ev.accept(new ProgramPartExecuteVisitor());
            assignment.accept(programPartVisitor);
        }

        return 0;
    }

    /**
     * Metoda intorce un string cu codul aflat in paranteze  []
     * @return 
     */
    private String getCode() {
        String r = "[";
        int paranteze = 1;
        while (paranteze > 0) {
            r += code.charAt(count);
            if (code.charAt(count) == '[') {
                paranteze++;
            } else if (code.charAt(count) == ']') {
                paranteze--;
            }
            count++;
        }
        count += 2;
        return r;
    }

}
