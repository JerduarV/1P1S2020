/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneradorFlexCup;

/**
 *
 * @author Jerson Villatoro
 */
public class Generador {

    public static void main(String[] args) {
        try {
            String ruta = "src/GeneradorFlexCup";
            String rutaInt = "src/GramaticaFlexCup";
            String opcFlex[] = {ruta + "/Lexico.flex", "-d", rutaInt};
            jflex.Main.generate(opcFlex);
            String opcCUP[] = {"-destdir", rutaInt, "-parser", "Sintactico","-symbols", "sym", ruta + "/Sintactico.cup"};
            java_cup.Main.main(opcCUP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
