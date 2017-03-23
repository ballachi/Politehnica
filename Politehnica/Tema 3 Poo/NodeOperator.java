
/**
 *
 * @author Marin
 */
public class NodeOperator implements VariablePart {

    String value;

    NodeOperator(String string) {
        value = string;
    }

    /**
     *
     * @return semnul
     */
    @Override
    public String getValue() {
        return "" + value;
    }

    @Override
    public VariablePart scade(VariablePart var2) {
        return null;
    }

    @Override
    public VariablePart aduna(VariablePart var2) {
        return null;
    }

    @Override
    public VariablePart inmultire(VariablePart var2) {
        return null;
    }

    @Override
    public VariablePart impartire(VariablePart var2) {
        return null;
    }

    @Override
    public boolean getNaN() {
        return false;
    }

    @Override
    public void setNaN() {
    }

}
