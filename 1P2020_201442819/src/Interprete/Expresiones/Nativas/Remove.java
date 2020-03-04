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
import java.util.LinkedList;

/**
 * Clase para manejar la función nativa remove
 *
 * @author Jerduar
 */
public class Remove extends CallFun {

    /**
     * Constructor de la llamada a la función nativa Remove
     *
     * @param p Lista de parámetros actuales
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encuentra
     */
    public Remove(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("Remove", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 2) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Remove: Se esperaban 2 parámetros de tipo string", this.getFila(), this.getColumna());
        }

        Object param1 = this.getParam_act().getFirst().Resolver(t);

        Object param2 = this.getParam_act().get(1).Resolver(t);

        if (param1 instanceof ErrorCompi || param2 instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Remove: Hubo errores en los parámetros", this.getFila(), this.getColumna());
        }

        if (!(param1 instanceof VectorArit) || !(param2 instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Remove: Se esperaban 2 vectores string", this.getFila(), this.getColumna());
        }

        VectorArit v1 = (VectorArit) param1, v2 = (VectorArit) param2;

        if (v1.getTamanio() != 1 || v2.getTamanio() != 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Remove: Los vectores string deben tener tamanio 1", this.getFila(), this.getColumna());
        }
        
        String cad = v1.Acceder(0).toString().replaceAll(v2.Acceder(0).toString(), "");
        
        return new VectorArit(TipoPrimitivo.STRING, cad);
    }

}
