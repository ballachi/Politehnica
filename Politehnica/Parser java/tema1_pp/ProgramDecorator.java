
public abstract class ProgramDecorator implements Eval {

    protected Eval ev;

    public ProgramDecorator(Eval ev) {
        this.ev = ev;
    }

    
}
