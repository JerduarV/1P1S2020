/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete;

import Interprete.Expresiones.Nativas.Print;
import Interprete.Expresiones.Nativas.ToUpperCase;
import Interprete.Instrucciones.DecFuncion;
import Interprete.Instrucciones.Declaracion;
import Interprete.Instrucciones.Instruccion;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase árbol que dará inicio a la ejecución del archivo de entrada
 *
 * @author Jerduar
 */
public class Arbol extends Instruccion {

    /**
     * Constructor del árbol AST
     *
     * @param cuerpo Lista de Instrucciones del archivo de entrada
     */
    public Arbol(LinkedList<NodoAST> cuerpo) {
        super(1, 1, cuerpo);
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        this.GuardarFunciones(t);
        return this.Recorrer(t);
    }

    /**
     * Método que recorre el árbol buscando las funciones para ser guardadas
     *
     * @param t Tabla de Símbolos
     */
    private void GuardarFunciones(TablaSimbolos t) {
        this.GuardarFuncionesNativas(t);
        this.getCuerpo().stream().filter((n) -> (n instanceof DecFuncion)).forEachOrdered((n) -> {
            ((DecFuncion) n).GuardarFuncion(t);
        });
    }

    /**
     * Reserva los nombres de las funciones nativas
     *
     * @param global tabla de símbolos global
     */
    private void GuardarFuncionesNativas(TablaSimbolos global) {

        //CREACIÓN DE FUNCIÓN PRINT
        LinkedList<Declaracion> l_print = new LinkedList<>();
        l_print.add(new Declaracion("print%%param1", -1, -1));
        Print print = new Print(l_print);
        global.GuardarFuncion("print", print);

        //CREACIÓN DE FUNCIÓN TOUPPERCASE
        DecFuncion ToUpperCase = new DecFuncion("touppercase", null, null, -1, -1);
        global.GuardarFuncion("touppercase", ToUpperCase);

        //RESERVANDO NOMBRE PARA LA FUNCIÓN C
        DecFuncion c = new DecFuncion("c", null, null, -1, -1);
        global.GuardarFuncion("c", c);

        //RESERVANDO EL CONSTRUCTOR PARA LIST
        DecFuncion list = new DecFuncion("list", null, null, -1, -1);
        global.GuardarFuncion("list", list);

        //RERVANDO EL NOMBRE DE LA FUNCIÓN TYPEOF
        DecFuncion typeof = new DecFuncion("typeof", null, null, -1, -1);
        global.GuardarFuncion("typeof", typeof);

        //RERVANDO EL NOMBRE DE LA FUNCIÓN LENGTH
        DecFuncion length = new DecFuncion("length", null, null, -1, -1);
        global.GuardarFuncion("length", length);

        //RERVANDO EL NOMBRE DE LA FUNCIÓN STRINGLENGTH
        DecFuncion stringlength = new DecFuncion("stringlength", null, null, -1, -1);
        global.GuardarFuncion("stringlength", stringlength);
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
