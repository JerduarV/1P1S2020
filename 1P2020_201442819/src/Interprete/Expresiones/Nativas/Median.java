/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Nativas;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.CallFun;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Clase para manejar la función nativa de la mediana
 *
 * @author Jerduar
 */
public class Median extends CallFun {

    /**
     * Constructor de la llamada a la función de la Mediana
     *
     * @param p
     * @param fila
     * @param col
     */
    public Median(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("Median", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 1 && this.getParam_act().size() != 2) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "Se esperaban 2 o 1 parámetros", this.getFila(), this.getColumna());
        }

        return this.getParam_act().size() == 1 ? this.Median(t) : MedianTrim(t);
    }

    /**
     * Mediana sin trim
     *
     * @return VectorArit o ErrorCompi
     */
    private Object Median(TablaSimbolos t) {
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

        LinkedList<Double> lista_ordenada = new LinkedList<>();
        for (Object v : vector.getValores()) {
            lista_ordenada.add(vector.isInteger() ? ((Integer) v).doubleValue() : (Double) v);
        }

        Collections.sort(lista_ordenada);

        Double m;
        if (lista_ordenada.size() % 2 != 0) {
            m = lista_ordenada.get((lista_ordenada.size() - 1) / 2);
            Integer i = m.intValue();
            if (vector.isInteger()) {
                return new VectorArit(TipoPrimitivo.INTEGER, i);
            } else {
                return new VectorArit(TipoPrimitivo.DOUBLE, m);
            }
        } else {
            m = ((lista_ordenada.get(lista_ordenada.size() / 2) + lista_ordenada.get(((lista_ordenada.size() / 2) - 1)))) / 2;
        }
        return new VectorArit(TipoPrimitivo.DOUBLE, m);
    }

    private Object MedianTrim(TablaSimbolos t) {
        Object param1 = this.getParam_act().getFirst().Resolver(t),
                param2 = this.getParam_act().get(1).Resolver(t);

        if (param1 instanceof ErrorCompi || param2 instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mean: Hay error en los parámetros", this.getFila(), this.getColumna());
        }

        if (!(param1 instanceof VectorArit && param2 instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mean: Se esperaba un vector", this.getFila(), this.getColumna());
        }

        VectorArit vector = (VectorArit) param1,
                vector_trim = (VectorArit) param2;

        if (!vector.isNumerico() || !vector_trim.isNumerico()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mean: Se esperaba un vector numérico", this.getFila(), this.getColumna());
        }

        Double trim = vector_trim.isInteger() ? (Integer) vector_trim.Acceder(0) : (Double) vector_trim.Acceder(0);

        LinkedList<Double> lista_ordenada = new LinkedList<>();
        for (Object v : vector.getValores()) {
            Double i = v instanceof Integer ? (Integer) v : (Double) v;
            if (i < trim) {
                continue;
            }
            lista_ordenada.add(i);
        }

        Collections.sort(lista_ordenada);

        Double m;
        if (lista_ordenada.size() % 2 != 0) {
            m = lista_ordenada.get((lista_ordenada.size() - 1) / 2);
            Integer i = m.intValue();
            if (vector.isInteger()) {
                return new VectorArit(TipoPrimitivo.INTEGER, i);
            } else {
                return new VectorArit(TipoPrimitivo.DOUBLE, m);
            }
        } else {
            m = ((lista_ordenada.get(lista_ordenada.size() / 2) + lista_ordenada.get(((lista_ordenada.size() / 2) - 1)))) / 2;
        }
        return new VectorArit(TipoPrimitivo.DOUBLE, m);
    }

}
