
/**
 *
 * @author Marin
 */
public class My_Int implements VariablePart {

    private boolean NaN = false;
    private final int value;

    /**
     *
     * @param value valoarea
     */
    public My_Int(int value) {
        this.value = value;
    }

    /**
     *
     * @return valoarea
     */
    @Override
    public String getValue() {
        if (NaN) {
            return "NaN";
        }
        return "" + value;
    }

    /**
     * Se verifica tipul var2 si se face operatia respectiva pt tip
     *
     * @param var2
     * @return
     */
    @Override
    public VariablePart scade(VariablePart var2) {
        Factory fac = new Factory();
        VariablePart v;
        if (var2 instanceof My_Int) {
            if (var2.getNaN() || this.getNaN()) {
                v = fac.getNod("int", 0);
                v.setNaN();
                return v;
            }
            return fac.getNod("int", value - Integer.parseInt(var2.getValue()));
        } else if (var2 instanceof My_Double) {
            if (var2.getNaN() || this.getNaN()) {
                v = fac.getNod("double", 0.0);
                v.setNaN();
                return v;
            }
            return fac.getNod("double", value - Double.parseDouble(var2.getValue()));
        } else if (var2 instanceof My_String) {
            if (this.getNaN()) {
                v = fac.getNod("int", 0);
                v.setNaN();
                return v;
            }
            return fac.getNod("int", value - var2.getValue().length());
        }
        return null;

    }

    /**
     * Se verifica tipul var2 si se face operatia respectiva pt tip
     *
     * @param var2
     * @return
     */
    @Override
    public VariablePart aduna(VariablePart var2) {
        Factory fac = new Factory();
        VariablePart v;
        if (var2 instanceof My_Int) {
            if (var2.getNaN() || this.getNaN()) {
                v = fac.getNod("int", 0);
                v.setNaN();
                return v;
            }
            return fac.getNod("int", value + Integer.parseInt(var2.getValue()));
        } else if (var2 instanceof My_Double) {
            if (var2.getNaN() || this.getNaN()) {
                v = fac.getNod("double", 0.0);
                v.setNaN();
                return v;
            }
            return fac.getNod("double", value + Double.parseDouble(var2.getValue()));
        } else if (var2 instanceof My_String) {
            return fac.getNod("string", value + var2.getValue());
        }
        return null;
    }

    /**
     * Se verifica tipul var2 si se face operatia respectiva pt tip
     *
     * @param var2
     * @return
     */
    @Override
    public VariablePart inmultire(VariablePart var2) {
        VariablePart v;
        Factory fac = new Factory();
        if (var2 instanceof My_Int) {
            if (var2.getNaN() || this.getNaN()) {
                v = fac.getNod("int", 0);
                v.setNaN();
                return v;
            }
            return fac.getNod("int", value * Integer.parseInt(var2.getValue()));
        } else if (var2 instanceof My_Double) {
            if (var2.getNaN() || this.getNaN()) {
                v = fac.getNod("double", 0.0);
                v.setNaN();
                return v;
            }
            return fac.getNod("double", value * Double.parseDouble(var2.getValue()));
        } else if (var2 instanceof My_String) {
            String a = "";
            for (int i = 0; i < value; i++) {
                a += var2.getValue();
            }
            return fac.getNod("string", a);
        }
        return null;
    }

    /**
     * Se verifica tipul var2 si se face operatia respectiva pt tip
     *
     * @param var2
     * @return
     */
    @Override
    public VariablePart impartire(VariablePart var2) {
        Factory fac = new Factory();
        VariablePart v;
        if (var2 instanceof My_Int) {
            if (var2.getNaN() || this.getNaN() || Integer.parseInt(var2.getValue()) == 0) {
                v = fac.getNod("int", 0);
                v.setNaN();
                return v;
            }
            return fac.getNod("int", value / Integer.parseInt(var2.getValue()));
        } else if (var2 instanceof My_Double) {
            if (var2.getNaN() || this.getNaN() || Double.parseDouble(var2.getValue()) == 0) {
                v = fac.getNod("double", 0.0);
                v.setNaN();
                return v;
            }
            return fac.getNod("double", value / Double.parseDouble(var2.getValue()));
        } else if (var2 instanceof My_String) {
            if (this.getNaN() || var2.getValue().length() == 0) {
                v = fac.getNod("int", 0);
                v.setNaN();
                return v;
            }
            return fac.getNod("int", value / var2.getValue().length());
        }
        return null;
    }
    
    /**
     *
     * @return true daca este setat NaN
     */
    @Override
    public boolean getNaN() {
        return NaN;
    }
    /**
     * Seteaza NaN
     */
    @Override
    public void setNaN() {
        NaN = true;
    }

}
