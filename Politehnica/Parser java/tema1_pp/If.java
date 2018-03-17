public class If implements ProgramPart {

    int count;
    String code;

    If(String code) {
        this.code = code;
    }

    @Override
    public int accept(ProgramPartVisitor programPartVisitor) {
        int x = programPartVisitor.visit(this);   //se verifica conditia
        String b;
        count = 5;
        b = getCode();      //se elimina conditia (se incrementeaza count pina la primu program)
        count += 4;
        if (x == 1) {
            b = getCode();  //primu program
            Eval ev = new NameDecorator(new Evaluate(),b); 
            ev.accept(new ProgramPartExecuteVisitor());/// se evalueaza programul
        } else {
            b = getCode();  //se elimina primu program (se incrementeaza count pina la al doilea program)
            count += 4;
            b = getCode();  //al doilea program
            Eval ev = new NameDecorator(new Evaluate(),b);
            ev.accept(new ProgramPartExecuteVisitor());
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
        count--;
        count--;
        return r;
    }



}
