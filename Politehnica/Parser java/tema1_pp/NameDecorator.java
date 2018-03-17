import java.util.ArrayList;

/**
 * Se va facea parsarea codului se va creea obiecte si se va pune in lista
 *
 * @author Marin
 */
public class NameDecorator extends ProgramDecorator {

    String code;
    int count;
    int nrParanteze;

    public NameDecorator(Eval programPart, String code) {
        super(programPart);
        this.code = code;
    }

    
    //Se va facea parsarea codului se va creea obiecte si se va pune in lista
    @Override
    public int accept(ProgramPartVisitor programPartVisitor) {
        ArrayList<ProgramPart> program = ev.getList();
        count++;
        nrParanteze++;
        while (nrParanteze > 0) {
            if (code.charAt(count) == '[') {
                nrParanteze++;
            } else if (code.charAt(count) == ']') {
                nrParanteze--;
            } else if (code.charAt(count) == '=') {
                String s = getCode();
                program.add(new Assignment(s));

            } else if (code.charAt(count) == 'r' && code.charAt(count + 1) == 'e' && code.charAt(count + 2) == 't') {
                String s = getCode();
                program.add(new Return(s));

            } else if (code.charAt(count) == 'i' && code.charAt(count + 1) == 'f') {
                String s = getCode();
                program.add(new If(s));

            } else if (code.charAt(count) == 'a' && code.charAt(count + 1) == 's') {
                String s = getCode();
                program.add(new Assert(s));

            } else if (code.charAt(count) == 'f') {
                String s = getCode();
                program.add(new For(s));
            }
            count++;
        }

        int x = ev.accept(programPartVisitor);
        return x;
    }

    public ArrayList<ProgramPart> getList() {
        return null;
    }

    /**
     * Metoda intorce un string cu codul aflat in paranteze []
     *
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
