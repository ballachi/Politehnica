
/**
 *
 * @author Marin
 */
public class My_String implements VariablePart {

    private final String value;

    /**
     *
     * @param value valoarea
     */
    public My_String(String value) {
        this.value = value;
    }

    /**
     *
     * @return valoarea
     */
    @Override
    public String getValue() {
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

            if (var2.getNaN()) {
                return fac.getNod("string", value);
            }

            if (Integer.parseInt(var2.getValue()) > value.length()) {
                return fac.getNod("string", "");
            }
            if (Integer.parseInt(var2.getValue()) < 0) {

                String a = value;
                for (int i = 0; i > Integer.parseInt(var2.getValue()); i--) {
                    a += "#";
                }
                return fac.getNod("string", a);
            }

            return fac.getNod("string", value.substring(0, value.length() - Integer.parseInt(var2.getValue())));
        } else if (var2 instanceof My_Double) {
            if (var2.getNaN()) {
                v = fac.getNod("double", 0.0);
                v.setNaN();
                return v;
            }
            return fac.getNod("double", value.length() - Double.parseDouble(var2.getValue()));
        } else if (var2 instanceof My_String) {
            return fac.getNod("int", value.length() - var2.getValue().length());
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
            return fac.getNod("string", value + var2.getValue());
        } else if (var2 instanceof My_Double) {
            if (var2.getNaN()) {
                v = fac.getNod("string", value + "NaN");
                return v;
            }
            return fac.getNod("string", value + Math.round(Double.parseDouble(var2.getValue()) * 100.0) / 100.0);
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
        Factory fac = new Factory();
        VariablePart v;
        if (var2 instanceof My_Int) {
            String a = "";
            for (int i = 0; i < Integer.parseInt(var2.getValue()); i++) {
                a += value;
            }
            return fac.getNod("string", a);
        } else if (var2 instanceof My_Double) {
            if (var2.getNaN()) {
                v = fac.getNod("double", 0.0);
                v.setNaN();
                return v;
            }
            return fac.getNod("double", value.length() * Double.parseDouble(var2.getValue()));
        } else if (var2 instanceof My_String) {
            return fac.getNod("int", value.length() * var2.getValue().length());
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
            if (Integer.parseInt(var2.getValue()) <= 0) {
                return this;
            }
            return fac.getNod("string", value.substring(0, value.length() / Integer.parseInt(var2.getValue())));
        } else if (var2 instanceof My_Double) {
            if (var2.getNaN()) {
                v = fac.getNod("double", 0.0);
                v.setNaN();
                return v;
            }
            return fac.getNod("double", value.length() / Double.parseDouble(var2.getValue()));
        } else if (var2 instanceof My_String) {
            return fac.getNod("int", value.length() / var2.getValue().length());
        }
        return null;
    }

    /**
     * Pentru string nu se poate seta NaN
     *
     * @return false
     */
    @Override
    public boolean getNaN() {
        return false;
    }

    /**
     * Pentru string nu se poate seta NaN
     */
    @Override
    public void setNaN() {
    }

}
