/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Operaciones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase que maneja las operaciones lógicas en el lenguaje
 *
 * @author Jerduar
 */
public class ExpLogica extends Operacion {

    /**
     * Constructor de operaciones lógicas binarias
     *
     * @param fila fila en la que se encuentra
     * @param col columna en la que se encuentra
     * @param izq operador izquierdo
     * @param der operador derecho
     * @param o Tipo de la operación
     */
    public ExpLogica(Integer fila, Integer col, Expresion izq, Expresion der, TipoOpe o) {
        super(fila, col, izq, der, o);
    }

    /**
     * Constructor de operaciones lógicas unarias (negación)
     *
     * @param op_unario operador único
     * @param fila fila en la que se encuentra
     * @param col columan en la que se encuentra
     */
    public ExpLogica(Expresion op_unario, Integer fila, Integer col) {
        super(op_unario, TipoOpe.NOT, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getOp_unario() != null) {
            Object val_unico = this.getOp_unario().Resolver(t);

            //SI RESULTA UN ERROR
            if (val_unico instanceof ErrorCompi) {
                return val_unico;
            }

            //NEGACIÓN DE UN VECTOR
            if (val_unico instanceof VectorArit) {
                VectorArit vector_unico = (VectorArit) val_unico;

                if (!vector_unico.isBool()) {
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "Solo se puede negar valores booleanos", this.getFila(), this.getColumna());
                }

                //NEGACIÓN DE UN VECTOR DE TAMANIO 1
                if (vector_unico.getTamanio() == 1) {
                    return OperacionLogicaBase(vector_unico, null);
                }
            }
        } else { //SI ES UNA OPERACIÓN BINARIA
            Object val_izq = this.getOp_izq().Resolver(t);
            Object val_der = this.getOp_der().Resolver(t);

            //SI ALGUNA OPERACIÓN DEVUELVE UN ERROR SE RETORNA EL MISMO
            if (val_izq instanceof ErrorCompi || val_der instanceof ErrorCompi) {
                return val_izq instanceof ErrorCompi ? val_izq : val_der;
            }

            //OPERACION DE DOS VECTORES
            if (val_izq instanceof VectorArit && val_der instanceof VectorArit) {
                VectorArit vector_izq = (VectorArit) val_izq;
                VectorArit vector_der = (VectorArit) val_der;

                if (!vector_izq.isBool() || !vector_der.isBool()) {
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "Solo se pueden operar valores booleanos", this.getFila(), this.getColumna());
                }

                //SUMA DE DOS VECTORES DE TAMANIO 1
                if (vector_izq.getTamanio() == 1 && vector_der.getTamanio() == 1) {
                    return this.OperacionLogicaBase(vector_izq, vector_der);
                }
            }
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Función para realizar las operaciones lógicas entre vectores de tamanio 1
     *
     * @param izq operador izquierdo
     * @param der operador derecho
     * @return VectorArit con el resultado
     */
    private Object OperacionLogicaBase(VectorArit izq, VectorArit der) {
        LinkedList<Object> l = new LinkedList<>();
        switch (this.getTipo()) {
            case AND:
                l.add((Boolean) izq.Acceder(0) && (Boolean) der.Acceder(0));
                break;
            case OR:
                l.add((Boolean) izq.Acceder(0) || (Boolean) der.Acceder(0));
                break;
            case NOT:
                l.add(!(Boolean) izq.Acceder(0));
                break;
        }
        return new VectorArit(TipoPrimitivo.BOOL, l);
    }

}
