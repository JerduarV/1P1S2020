/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Interprete.Expresiones.Expresion;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase para el manejo de los casos dentro de un switch
 *
 * @author Jerduar
 */
public class Case extends Instruccion {
    
    /**
     * Variable que contiene la expresión asociada al case
     */
    private final Expresion e;

    /**
     * Constructor de la clase case
     * @param e Expresion de control del case
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encuentra
     * @param cuerpo Cuerpo del switch
     */
    public Case(Expresion e, LinkedList<NodoAST> cuerpo, Integer fila, Integer col) {
        super(fila, col, cuerpo);
        this.e = e;
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        return this.Recorrer(t);
    }

    @Override
    public void dibujar(String padre) {
        String n = NodoAST.getIdNodo("CASE");
        Interprete.Interprete.Conectar(padre, n);
        this.e.dibujar(n);
        this.DibujarCuerpo(n);
    }

    public Expresion getE() {
        return e;
    }

}
