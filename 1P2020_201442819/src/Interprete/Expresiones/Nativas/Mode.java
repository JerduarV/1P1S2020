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
import TablaSimbolos.TablaSimbolos;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Clase para manejo de la función nativa median
 *
 * @author Jerduar
 */
public class Mode extends CallFun {

    /**
     * Constructor del llamado a la función de la media
     *
     * @param p
     * @param fila
     * @param col
     */
    public Mode(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("Mode", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 1 && this.getParam_act().size() != 2) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "Se esperaban 2 o 1 parámetros", this.getFila(), this.getColumna());
        }

        return this.getParam_act().size() == 1 ? this.Mode(t) : ModeTrim(t);
    }

    /**
     * Moda con trim
     *
     * @param t Tabla de símbolos
     * @return VectorArit o ErrorCompi
     */
    private Object ModeTrim(TablaSimbolos t) {
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

        HashMap h = new HashMap();
        LinkedList<Object> l = new LinkedList<>();
        for (Object v : vector.getValores()) {
            Double i = v instanceof Integer ? (Integer) v : (Double) v;
            if (i < trim) {
                continue;
            }
            if (h.containsKey(v)) {
                h.put(v, ((Integer) h.get(v)) + 1);
            } else {
                h.put(v, 1);
                l.add(v);
            }
        }

        if (l.isEmpty()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mode: No hay moda por el trim", this.getFila(), this.getColumna());
        }

        Object v = l.getFirst();
        for (int y = 1; y < l.size(); y++) {
            //System.out.println(h.get(v) + " " + h.get(l.get(y)));
            if ((Integer) h.get(v) < (Integer) h.get(l.get(y))) {
                v = l.get(y);
                //System.out.println("v " + v);
            }
        }
        return new VectorArit(vector.getTipo_dato(), v);
    }

    /**
     * Mode sin trim
     *
     * @param t Tabla de símbolos
     * @return VectorArit o ErrorCompi
     */
    public Object Mode(TablaSimbolos t) {
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

        HashMap h = new HashMap();
        LinkedList<Object> l = new LinkedList<>();
        for (Object v : vector.getValores()) {
            if (h.containsKey(v)) {
                h.put(v, ((Integer) h.get(v)) + 1);
            } else {
                h.put(v, 1);
                l.add(v);
            }
        }

        Object v = l.getFirst();
        for (int y = 1; y < l.size(); y++) {
            //System.out.println(h.get(v) + " " + h.get(l.get(y)));
            if ((Integer) h.get(v) < (Integer) h.get(l.get(y))) {
                v = l.get(y);
                //System.out.println("v " + v);
            }
        }
        return new VectorArit(vector.getTipo_dato(), v);
    }

}
