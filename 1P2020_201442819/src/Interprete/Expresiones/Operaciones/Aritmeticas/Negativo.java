/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Operaciones.Aritmeticas;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.Operaciones.Operacion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase que maneja la operación de negar un número
 *
 * @author Jerduar
 */
public class Negativo extends Operacion {

    /**
     * constructor de la clase negativo
     *
     * @param op_unario Operador único
     * @param fila fila en la que se encuentra
     * @param col columana en la que se encuentra
     */
    public Negativo(Expresion op_unario, Integer fila, Integer col) {
        super(op_unario, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        Object val_unico = this.getOp_unario().Resolver(t);

        if (val_unico instanceof ErrorCompi) {
            return val_unico;
        }

        //SUMA DE DOS VECTORES
        if (val_unico instanceof VectorArit) {
            VectorArit vector_unico = (VectorArit) val_unico;

            //SUMA DE DOS VECTORES DE TAMANIO 1
            if (vector_unico.getTamanio() == 1) {
                return NegativoVectoresBase(vector_unico);
            }
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Hace negativo el vector
     * @param vector
     * @return 
     */
    private Object NegativoVectoresBase(VectorArit vector) {
        LinkedList<Object> l = new LinkedList<>();
        switch (vector.getTipo_dato()) {
            case DECIMAL:
                l.add(-1 * (Integer) vector.Acceder(0));
                return new VectorArit(TipoPrimitivo.DECIMAL, l);
            case ENTERO:
                l.add(-1 * (Integer) vector.Acceder(0));
                return new VectorArit(TipoPrimitivo.ENTERO, l);
            default:
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede volver negativo " + vector.getTipo_dato(), this.getFila(), this.getColumna());
        }
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
