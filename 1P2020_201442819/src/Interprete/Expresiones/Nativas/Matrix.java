/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Nativas;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.CallFun;
import Interprete.Expresiones.Colecciones.MatrixArit;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase para el manejo del llamado al constructor de las matrices
 *
 * @author Jerduar
 */
public class Matrix extends CallFun {

    /**
     * Constructor de la clase Matrix
     *
     * @param p Lista de parámetros
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public Matrix(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("Matrix", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 3) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Matrix: Se esperaban 3 parámetros", this.getFila(), this.getColumna());
        }

        Object param1 = this.getParam_act().getFirst().Resolver(t),
                param2 = this.getParam_act().get(1).Resolver(t),
                param3 = this.getParam_act().get(2).Resolver(t);

        if (param1 instanceof ErrorCompi || param2 instanceof ErrorCompi || param3 instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Matrix: Hubo un error en los parámetros", this.getFila(), this.getColumna());
        }

        if (!(param1 instanceof VectorArit && param2 instanceof VectorArit && param3 instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Matrix: Se esperaban 3 vectores primitivos " + param1.getClass(), this.getFila(), this.getColumna());
        }

        VectorArit v1 = (VectorArit) ((VectorArit) param1).copiar(),
                v2 = (VectorArit) param2,
                v3 = (VectorArit) param3;

        if (!(v2.isInteger() && v3.isInteger())) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Matrix: Los tamaños deben ser enteros", this.getFila(), this.getColumna());
        }
        int h = (Integer) v2.Acceder(0) * (Integer) v3.Acceder(0);

        LinkedList<Object> valores = new LinkedList<>();
        for (int i = 0; i < h;) {
            for (int y = 0; i < h && y < v1.getTamanio(); y++) {
                valores.add(v1.Acceder(y));
                i++;
            }
        }
        return new MatrixArit((Integer) v2.Acceder(0), (Integer) v3.Acceder(0), v1.getTipo_dato(), valores);
    }

}
