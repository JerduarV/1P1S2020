/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase para el manejo de Default del switch
 * @author Jerduar
 */
public class DefaultSw extends Instruccion{

    /**
     * Constructor de la clase para la instrucción Default del switch
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     * @param cuerpo Cuerpo de instrucciones del default
     */
    public DefaultSw(LinkedList<NodoAST> cuerpo, Integer fila, Integer col) {
        super(fila, col, cuerpo);
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        return this.Recorrer(t);
    }

    @Override
    public void dibujar(String padre) {
        String n = NodoAST.getIdNodo("DEFAULT");
        Interprete.Interprete.Conectar(padre, n);
        this.DibujarCuerpo(n);
    }
    
}
