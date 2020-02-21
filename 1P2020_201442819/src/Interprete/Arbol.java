/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete;

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
        return this.Recorrer(t);
    }
    
    /**
     * Método que recorre el árbol buscando las funciones para ser guardadas
     * @param t Tabla de Símbolos
     */
    private void GuardarFunciones(TablaSimbolos t){
        throw new UnsupportedOperationException("Todavía no puedo guardar funciones");
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}