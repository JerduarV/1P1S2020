/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Colecciones;

import java.util.LinkedList;

/**
 *
 * @author Jerduar
 */
public class ListArit extends Coleccion{

    public ListArit(LinkedList<Object> v) {
        super(v);
    }

    @Override
    public String Typeof() {
        return "LIST";
    }

    @Override
    public Coleccion copiar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
