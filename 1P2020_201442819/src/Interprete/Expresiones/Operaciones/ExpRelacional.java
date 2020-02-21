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
 * Clase que maneja las opearciones relacionales en el lenguaje
 *
 * @author Jerduar
 */
public class ExpRelacional extends Operacion {

    /**
     * Constructor de las expresiones relacionales, solo pueden ser operaciones
     * binarias
     *
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     * @param izq Operador izquierdo
     * @param der Operador derecho
     * @param o Tipo de la operación
     */
    public ExpRelacional(Integer fila, Integer col, Expresion izq, Expresion der, TipoOpe o) {
        super(fila, col, izq, der, o);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {

        Object val_izq = this.getOp_izq().Resolver(t);
        Object val_der = this.getOp_der().Resolver(t);

        //SI ALGUNA OPERACIÓN DEVUELVE UN ERROR SE RETORNA EL MISMO
        if (val_izq instanceof ErrorCompi || val_der instanceof ErrorCompi) {
            return val_izq instanceof ErrorCompi ? val_izq : val_der;
        }

        //COMPARACION DE DOS VECTORES
        if (val_izq instanceof VectorArit && val_der instanceof VectorArit) {
            VectorArit vector_izq = (VectorArit) val_izq;
            VectorArit vector_der = (VectorArit) val_der;

            //COMPARACION DE DOS VECTORES DE TAMANIO 1
            if (vector_izq.getTamanio() == 1 && vector_der.getTamanio() == 1) {
                return this.ComparacionBaseVectores(vector_izq, vector_der);
            }
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Función que opera los vectores de tamaño uno
     * @param izq Operador Izquierdo
     * @param der Operador Derecho
     * @return Vector con resultado o ErrorCompi
     */
    private Object ComparacionBaseVectores(VectorArit izq, VectorArit der) {
        LinkedList<Object> l = new LinkedList<>();
        if (izq.isNumerico() && der.isNumerico()) {
            switch (this.getTipo()) {
                case MAYOR:
                    l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) > (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                    return new VectorArit(TipoPrimitivo.BOOL, l);
                case MENOR:
                    l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) < (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                    return new VectorArit(TipoPrimitivo.BOOL, l);
                case MENORIGUAL:
                    l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) <= (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                    return new VectorArit(TipoPrimitivo.BOOL, l);
                case MAYORIGUAL:
                    l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) >= (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                    return new VectorArit(TipoPrimitivo.BOOL, l);
                case IGUALQUE:
                    l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) == (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                    return new VectorArit(TipoPrimitivo.BOOL, l);
                default:
                    l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) != (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                    return new VectorArit(TipoPrimitivo.BOOL, l);
            }
        } else if (izq.isString() && der.isString()) {
            switch (this.getTipo()) {
                case IGUALQUE:
                    l.add(izq.Acceder(0).toString().equals(der.Acceder(0).toString()));
                    return new VectorArit(TipoPrimitivo.BOOL, l);
                case DIFERENTE:
                    l.add(!izq.Acceder(0).toString().equals(der.Acceder(0).toString()));
                    return new VectorArit(TipoPrimitivo.BOOL, l);
                case MAYOR:
                    l.add(izq.Acceder(0).toString().hashCode() > der.Acceder(0).toString().hashCode());
                    return new VectorArit(TipoPrimitivo.BOOL, l);
                case MENOR:
                    l.add(izq.Acceder(0).toString().hashCode() < der.Acceder(0).toString().hashCode());
                    return new VectorArit(TipoPrimitivo.BOOL, l);
                case MAYORIGUAL:
                    l.add(izq.Acceder(0).toString().hashCode() >= der.Acceder(0).toString().hashCode());
                    return new VectorArit(TipoPrimitivo.BOOL, l);
                default:
                    l.add(izq.Acceder(0).toString().hashCode() <= der.Acceder(0).toString().hashCode());
                    return new VectorArit(TipoPrimitivo.BOOL, l);
            }
        } else {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede comparar " + izq.getTipo_dato() + " con " + der.getTipo_dato(), this.getFila(), this.getColumna());
        }

    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
