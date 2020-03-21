/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;

/**
 * clase que se usa en el envio de valores por defecto en los llamados a
 * funciones
 *
 * @author Jerduar
 */
public class ValorDefault extends Expresion {

    /**
     * Constructor de la clase valor por defecto
     *
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public ValorDefault(Integer fila, Integer col) {
        super(fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        return this;
    }

    @Override
    public void dibujar(String padre) {
        String n = NodoAST.getIdNodo("default");
        Interprete.Interprete.Conectar(padre, n);
    }

}
