/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete;

import Editor.VentanaErrores;
import GramaticaFlexCup.Lexico;
import GramaticaFlexCup.Sintactico;
import GramaticaJavaCC.Gramatica;
import GramaticaJavaCC.ParseException;
import GramaticaJavaCC.TokenMgrError;
import TablaSimbolos.TablaSimbolos;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para interpretar el lenguaje usando el árbol generado
 *
 * @author Jerduar
 */
public class Interprete {

    /**
     * Variable en la que se guarda el texto del dot con el que se generará el
     * árbol
     */
    private static String dot;

    /**
     * Método que inicia el análisis de la entrada usando las herramientas Flex
     * y Cup
     *
     * @param contenido Cadena de entrada
     */
    public void InterpretarFlexCup(String contenido) {
        Sintactico pars;
        VentanaErrores.getVenErrores().LimpiarTabla();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("temp_file.txt"));
            bw.write(contenido);
            bw.close();
            pars = new Sintactico(new Lexico(new FileReader("temp_file.txt")));
            pars.parse();

            Arbol a = pars.getAST();
            if (a != null) {
                this.ConstruirDot(a);
                TablaSimbolos global = new TablaSimbolos(null);
                a.Ejecutar(global);
            } else {
                System.err.println("HUBIERON PROBLEMAS :(");
            }

        } catch (Exception ex) {
            System.err.println("Error fatal en compilación de entrada " + ex.toString() + "\n" + ex.getMessage());
        }
    }

    /**
     * Método que inicia el análisis de la entrada usando JavaCC
     *
     * @param contenido Cadena de entrada
     */
    public void InterpretarJavaCC(String contenido) {
        try {
            Gramatica parser = new Gramatica(new BufferedReader(new StringReader(contenido)));
            Arbol a = parser.INI();
            this.ConstruirDot(a);
            TablaSimbolos global = new TablaSimbolos(null);
            a.Ejecutar(global);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        } catch (TokenMgrError e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Inicia con la escritura del dot que es usado para dibujar el árbol
     *
     * @param tree Árbol a dibujar
     */
    private void ConstruirDot(Arbol tree) {
        dot = "digraph G{\n\tnode[shape=\"box\"];\n";
        tree.dibujar(null);
        dot += "}\n";
        this.DibujarArbol();
    }

    /**
     * Función que se usa para declarar el nodo en el dot
     *
     * @param id Identificador del nodo
     * @param label Etiqueta que usará
     */
    public static void DeclararNodo(String id, String label) {
        appendDot("\t\"" + id + "\" [label = \"" + label + "\"];\n");
    }

    /**
     * Método que se usa para conectar dos nodos en el dot
     *
     * @param padre Nodo padre
     * @param hijo Nodo hijo
     */
    public static void Conectar(String padre, String hijo) {
        appendDot("\t\"" + padre + "\" -> \"" + hijo + "\";\n");
    }

    /**
     * Método para concatenar con la variable dot
     *
     * @param cad String que se concatenará
     */
    public static void appendDot(String cad) {
        dot = dot.concat(cad);
    }

    /**
     * Método para dibujar el árbol y generar la imagen
     */
    public void DibujarArbol() {
        BufferedWriter bw = null;
        String dir = System.getProperty("user.dir");
        try {
            //System.out.println(dot);
            bw = new BufferedWriter(new FileWriter("ArbolDot.dot"));
            bw.write(dot);
            bw.close();
            String[] cmd = new String[5];
            cmd[0] = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot";
            cmd[1] = "-Tpng";
            cmd[2] = dir + "\\ArbolDot.dot";
            cmd[3] = "-o";
            cmd[4] = dir + "\\Arbol.png";
            //System.out.println(cmd[4]);
            //Invocamos nuestra clase
            Runtime rt = Runtime.getRuntime();
            //Ahora ejecutamos como lo hacemos en consola
            rt.exec(cmd);
        } catch (IOException ex) {
            Logger.getLogger(Interprete.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Interprete.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
