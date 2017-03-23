
/**
 *
 * @author Marin
 */
public class Arbore {

    private Arbore st, dr;
    private VariablePart info;
    private final static variabile nodes = variabile.getInstance();       //pentru a avea acces la toate variabilele si a putea extrage varibilele necesare
    private final static Factory fac = new Factory();               //pentru a putea creea variabile noi

    /**
     *
     * @param info primeste variabila
     */
    public Arbore(VariablePart info) {
        st = null;
        dr = null;
        this.info = info;
    }

    /**
     *
     */
    public Arbore() {
    }

    /**
     * Metoda creeaza arborile Inserarea se face RSD Inserarea se face ca pentru
     * fiecare frunza din arbore sa fie variabila dar nu operatie
     *
     * @param exp expresia (prefix)
     * @param vect counter
     * @param root nod
     * @return
     */
    public Arbore creeaza(String exp[], int vect[], Arbore root) {
        if (exp.length != vect[0]) {

            if (root == null || root.info == null) {
                if (exp[vect[0]].equals("+") || exp[vect[0]].equals("-") || exp[vect[0]].equals("*") || exp[vect[0]].equals("/")) {
                    root = new Arbore(fac.getNod("operatie", exp[vect[0]]));
                } else {
                    root = new Arbore(nodes.getNod(exp[vect[0]]));
                }
                vect[0]++;
            }
            if (exp.length != vect[0] && !(exp[vect[0] - 1].equals("+") || exp[vect[0] - 1].equals("-") || exp[vect[0] - 1].equals("*") || exp[vect[0] - 1].equals("/"))) {
                return root;
            } else {
                root.st = creeaza(exp, vect, root.st);
                root.dr = creeaza(exp, vect, root.dr);
            }
        }
        return root;
    }

    /**
     * Metoda caluculeaza arborile 
     * Parcurgerea se face RSD
     * 
     * @return
     */
    public VariablePart calculator() {
        VariablePart result = null;
        if (this != null) {

            if (!(this.info.getValue().equals("-") || this.info.getValue().equals("+")
                    || this.info.getValue().equals("*") || this.info.getValue().equals("/"))) {
                result = this.info;

            } else {
                VariablePart left, right;
                String operator = this.info.getValue();

                left = this.st.calculator();
                right = this.dr.calculator();

                if (left != null && right != null) {
                    if (operator.equals("-")) {
                        result = left.scade(right);
                    } else if (operator.equals("+")) {
                        result = left.aduna(right);
                    } else if (operator.equals("*")) {
                        result = left.inmultire(right);
                    } else if (operator.equals("/")) {
                        result = left.impartire(right);
                    }
                }
            }
        }

        return result;

    }

}
