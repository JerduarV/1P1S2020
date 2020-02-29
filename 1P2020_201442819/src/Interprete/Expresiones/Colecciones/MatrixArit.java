/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Colecciones;

import Interprete.Expresiones.TipoPrimitivo;
import java.util.LinkedList;

/**
 *
 * @author Jerduar
 */
public class MatrixArit extends Coleccion{

    public MatrixArit(TipoPrimitivo t, LinkedList<Object> v) {
        super(t, v);
    }

    @Override
    public Coleccion copiar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void SetPosicion(int index, Coleccion valor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
