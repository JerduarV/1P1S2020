/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utileria;

import Interprete.Expresiones.Colecciones.Coleccion;
import Interprete.Expresiones.Expresion;
import TablaSimbolos.TablaSimbolos;

/**
 * Clase para realizar una asignación con acceso en el for
 * @author Jerduar
 */
public class DumbExpresion extends Expresion{
    
    private final Coleccion col;

    public DumbExpresion(Coleccion c, Integer fila, Integer col) {
        super(fila, col);
        this.col = c;
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        return this.col;
    }

    @Override
    public void dibujar(String padre) {
    }
    
}
