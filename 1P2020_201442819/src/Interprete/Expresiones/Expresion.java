/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Interprete.NodoAST;
import TablaSimbolos.*;

/**
 * Clase abstracta de la cual heredan todas las demás expresiones y deben implementar
 * su función resolver
 * @author Jerduar
 */
public abstract class Expresion extends NodoAST {
    
    public Expresion(Integer fila, Integer col) {
        super(fila, col);
    }
    
    public abstract Object Resolver(TablaSimbolos t);
    
}
