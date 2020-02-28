/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete;

import Interprete.Expresiones.Nativas.Print;
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
     * @param t Tabla de Símbolos
     */
    private void GuardarFunciones(TablaSimbolos t){
        this.GuardarFuncionesNativas(t);
        this.getCuerpo().stream().filter((n) -> (n instanceof DecFuncion)).forEachOrdered((n) -> {
            ((DecFuncion) n).GuardarFuncion(t);
        });
    }
    
    private void GuardarFuncionesNativas(TablaSimbolos global){
        
        //CREACIÓN DE FUNCIÓN PRINT
        LinkedList<Declaracion> l_print = new LinkedList<>();
        l_print.add(new Declaracion("print%%param1", -1, -1));
        Print print = new Print(l_print);
        global.GuardarFuncion("print", print);
        
        //RESERVANDO NOMBRE PARA LA FUNCIÓN C
        DecFuncion c = new DecFuncion("c", null, null, -1, -1);
        global.GuardarFuncion("c", c);
        
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
