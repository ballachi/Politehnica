
/**
 * Vizitor
 *
 * @author Marin
 */
public class ProgramPartExecuteVisitor implements ProgramPartVisitor {

    Variables var = Variables.getInstance();
    String code;  // se va afla codul pt instructiunea respectiva
    int count;

    @Override
    public int visit(If _if) {
        code = _if.code;
        count = 4;
        return _expresie();// se va evalua expresia(conditia)
    }

    @Override
    public int visit(For _for) {
        count = 6;
        code = _for.code;
        String q1 = getCode();
        code = getCode();
        count = 0;
        return _expresie();// se va evalua expresia(conditia)
    }

    @Override
    public int visit(Assert _assert) {

        code = _assert.code;
        count = 9;
        String cond = getCode();
        code = cond;
        count = 0;

        return _expresie();
    }

    @Override
    public int visit(Return _return) {
        count = 8;
        code = _return.code;
        return _expresie();
    }

    @Override
    public int visit(Assignment _assignment) {
        String val = "";
        code = _assignment.code;
        count = 3;
        while (_assignment.code.charAt(count) != ' ') {  // nume variabilei
            val += _assignment.code.charAt(count);
            count++;
        }
        count++;
        int x = _expresie();
        count = 0;
        var.add(val, x);
        return 0;

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
        count += 2;
        return r;
    }

    
    /**
     * Se va evalua expresia 
     * @return 
     */
    int _expresie() {
        String val = "";
        int rs = 0;
        if (code.charAt(count) == '[') {  // in acest caz stiu sigur ca va urma "+ * = < " dearece decoratoru nul va numara ca un program 
            count++;
            char op = code.charAt(count);  //operatorul
            count += 2;
            if (op == '=') {               // daca este egal
                count += 1;
            }
            int x1 = _expresie();          // va intoarce prima valoare 
            int x2 = _expresie();          // va intoarce a doua valoare

            switch (op) {
                case '+':
                    return rs = x1 + x2;
                case '*':
                    return rs = x1 * x2;
                case '=':
                    return (x1 == x2) ? 1 : 0;   //true(1) sau folse(0)
                case '<':
                    return (x1 < x2) ? 1 : 0;
            }

        } else if (code.charAt(count) >= '0' && code.charAt(count) <= '9') {    // daca e cifra 
            while (code.charAt(count) != ']' && code.charAt(count) != ' ') {
                val += code.charAt(count);
                count++;
            }
            rs = Integer.parseInt(val);
            if (code.charAt(count) == ' ') {
                count++;
            } else if (code.length() > count + 4) {
                count += 3;
            }
        } else if (code.charAt(count) >= 'a' && code.charAt(count) >= 'Z') {   //daca e text (atuci e o variabila )
            while (code.charAt(count) != ']' && code.charAt(count) != ' ') {   //scriu numele in val
                val += code.charAt(count);
                count++;
            }

            while ((code.charAt(count) == ' ' || code.charAt(count) == ']') && code.length() != count + 1) {
                count++;
            }
            rs = var.get(val);  //intoarace valoarea

        }
        while (code.length() > count + 1 && (code.charAt(count) == ']' || code.charAt(count) == ' ')) {
            count++;
        }

        return rs;

    }

}
