import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tema1_PP {

    static char exp[];
    Variables var = Variables.getInstance();

    public static void main(String[] args) {
        String line;
        String code = "";
        BufferedReader Reader;
        BufferedWriter writer = null;
        Variables.nameFile(args[1]);
        try {
            writer = new BufferedWriter(new FileWriter(args[1]));
            String name = args[0];
            FileInputStream file = new FileInputStream(name);
            Reader = new BufferedReader(new InputStreamReader(file));

            while ((line = Reader.readLine()) != null) {
                code += line;
            }
            code = code.replaceAll("\\s+", " ");                //suprascriu "\n" si "\t" cu spatiu
            Eval ev = new NameDecorator(new Evaluate(), code);  // decorator care primeste ca parametri  Evaluate() si codul
            int x = ev.accept(new ProgramPartExecuteVisitor());
            int err = (Error.getInstance()).getError(); //pt verificarea de erori
            switch (err) {
                case 0:             // 0  nu a fost nici o eroare
                    writer.write("" + x);
                    break;
                case 1:     
                    writer.write("Missing return");
                    break;
                case 2:
                    writer.write("Assert failed");
                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            System.out.println("Error");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }

    }

}
