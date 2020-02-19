/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete;

import Editor.VentanaErrores;
import GramaticaFlexCup.Lexico;
import GramaticaFlexCup.Sintactico;
import TablaSimbolos.TablaSimbolos;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Clase para interpretar el lenguaje usando el árbol generado
 *
 * @author Jerduar
 */
public class Interprete {

    public void Interpretar(String contenido){
        Sintactico pars;
        VentanaErrores.getVenErrores().LimpiarTabla();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("temp_file.txt"));
            bw.write(contenido);
            bw.close();
            pars = new Sintactico(new Lexico(new FileReader("temp_file.txt")));
            pars.parse();

            NodoAST a = pars.getAST();
            if (a != null) {
                
                TablaSimbolos t = new TablaSimbolos(null);
                //a.Ejecutar(t);
            } else {
                //System.err.println("HUBIERON PROBLEMAS :(");
            }

        } catch (Exception ex) {
            System.err.println("Error fatal en compilación de entrada " + ex.toString());
        }
    }
}
