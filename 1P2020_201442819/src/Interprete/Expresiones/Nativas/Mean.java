/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Nativas;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.CallFun;
import Interprete.Expresiones.Colecciones.Coleccion;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase para manejar el llamado a la función nativa Mean
 *
 * @author Jerduar
 */
public class Mean extends CallFun {

    /**
     * Constructor de la clase Mean
     *
     * @param p Lista de parámetros actuales
     * @param fila Fila en la que se encuentra
     * @param col Columana en la que se encuentra
     */
    public Mean(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("Mean", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 1 && this.getParam_act().size() != 2) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "Se esperaban 2 o 1 parámetros", this.getFila(), this.getColumna());
        }

        return this.getParam_act().size() == 1 ? this.Mean(t) : MeanTrim(t);
    }

    /**
     * Promedio con trim
     *
     * @param t Tabla de símbolos
     * @return VectorArit o Errorcompi
     */
    private Object MeanTrim(TablaSimbolos t) {
        Object param1 = this.getParam_act().getFirst().Resolver(t),
                param2 = this.getParam_act().get(1).Resolver(t);

        if (param1 instanceof ErrorCompi || param2 instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mean: Hay error en los parámetros", this.getFila(), this.getColumna());
        }

        VectorArit vector = (VectorArit) param1,
                vector_trim = (VectorArit) param2;

        if (!vector.isNumerico() || !vector_trim.isNumerico()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mean: Se esperaba un vector numérico", this.getFila(), this.getColumna());
        }

        Double suma = .0,
                trim = vector_trim.isInteger() ? (Integer) vector_trim.Acceder(0) : (Double) vector_trim.Acceder(0);
        int contador = 0;

        for (Object v : vector.getValores()) {
            Double i = v instanceof Integer ? (Integer) v : (Double) v;
            if (i < trim) {
                continue;
            }
            suma += i;
            contador++;
        }
        return contador == 0 ? this.Mean(t) : new VectorArit(TipoPrimitivo.NUMERIC, suma / contador);
    }

    /**
     * Solución del promedio sin el parámetro trim
     *
     * @param t Tabla de Simbolos
     * @return VectorArit Double o ErrorCompi
     */
    private Object Mean(TablaSimbolos t) {
        Object param1 = this.getParam_act().getFirst().Resolver(t);

        if (param1 instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mean: Hay error en los parámetros", this.getFila(), this.getColumna());
        }

        if (!(param1 instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mean: Se esperaba un vector numérico", this.getFila(), this.getColumna());
        }

        VectorArit vector = (VectorArit) param1;

        if (!vector.isNumerico()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mean: Se esperaba un vector numérico", this.getFila(), this.getColumna());
        }

        Double suma = .0;

        for (Object v : vector.getValores()) {
            suma += v instanceof Integer ? (Integer) v : (Double) v;
        }

        suma /= vector.getTamanio();

        return new VectorArit(TipoPrimitivo.NUMERIC, suma);
    }

}
