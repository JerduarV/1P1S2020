/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Operaciones;

import Interprete.Expresiones.Expresion;
import TablaSimbolos.TablaSimbolos;

/**
 *
 * @author Jerduar
 */
public class ExpLogica extends Operacion{

    public ExpLogica(Integer fila, Integer col, Expresion izq, Expresion der, TipoOpe o) {
        super(fila, col, izq, der, o);
    }

    public ExpLogica(Expresion op_unario, TipoOpe o, Integer fila, Integer col) {
        super(op_unario, o, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
