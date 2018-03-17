
/**
 * Clasa in care setez erorile
 * @author Marin
 */

public class Error {

    static Error err = new Error();
    private int errorA = 0;
    private int errorR = 1;

    private Error() {
    }

    public void setError(int x) {
        if (x == 0) {
            errorR = 0;
        } else {
            errorA = 2;
        }
    }

    public int getError() {
        if (errorR == 1) {
            return 1;
        } else if (errorA == 2) {
            return 2;
        }
        return 0;
    }

    static Error getInstance() {
        return err;
    }
}
