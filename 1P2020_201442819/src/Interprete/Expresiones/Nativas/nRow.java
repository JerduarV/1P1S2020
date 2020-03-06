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
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase para el llamada a la función nativa nRow
 *
 * @author Jerduar
 */
public class nRow extends CallFun {

    /**
     * Constructor de la llamada a la función nativa nRow
     *
     * @param p Lista de parámetros
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public nRow(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("nRow", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "nRow: Se esperaba un parámetro", this.getFila(), this.getColumna());
        }

        Object param1 = this.getParam_act().getFirst().Resolver(t);

        if (param1 instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "nRow: Hubo un error en los parámetros", this.getFila(), this.getColumna());
        }

        if (!(param1 instanceof MatrixArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "nRow: Se esperaba una matriz", this.getFila(), this.getColumna());
        }

        MatrixArit m = (MatrixArit) param1;

        return new VectorArit(TipoPrimitivo.INTEGER, m.getNum_filas());
    }

}
