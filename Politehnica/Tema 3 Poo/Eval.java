/**
 *
 * @author Marin
 */
public class Eval {
    /**
     * Metoda primeste expresia 
     * Apeleaza metoda care transforma expresia din infix in prefix
     * Apeleaza metoda care creeaza arborile
     * Apeleaza metoda care calculeaza arborile 
     * @param line expresia
     * @return rezultatul expresiei
     */
    public static String eval(String line) {

        Arbore arb = new Arbore();
        String[] vect = line.split("\\s+");
        vect[0] = " ";
        vect[vect.length - 1] = vect[vect.length - 1].split(";")[0];
        String expresie = Polish.infixToPrefix(vect);
        vect = expresie.split("\\s+");

        int vec[] = new int[1];
        vec[0] = 0;
        arb = arb.creeaza(vect, vec, arb);

        VariablePart v = (arb.calculator());
        if (v instanceof My_Double) {
            if (v.getNaN()==true) {
                return v.getValue();
            } else {
                return  ""+ Math.round(Double.parseDouble(v.getValue()) * 100.0) / 100.0;
            }
        } else {
            
            return v.getValue();
        }
    }
}
