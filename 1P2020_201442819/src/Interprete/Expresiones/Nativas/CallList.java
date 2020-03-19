/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Nativas;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.CallFun;
import Interprete.Expresiones.Colecciones.ArrayArit;
import Interprete.Expresiones.Colecciones.Coleccion;
import Interprete.Expresiones.Colecciones.ListArit;
import Interprete.Expresiones.Colecciones.MatrixArit;
import Interprete.Expresiones.Expresion;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase para la llamada al constructor de una lista
 *
 * @author Jerduar
 */
public class CallList extends CallFun {

    /**
     * Constructor de la llamada a la función List
     *
     * @param p Lista de expresiones con el contenido de la lista
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encuentra
     */
    public CallList(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("list", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        LinkedList<Object> lista = new LinkedList<>();

        for (Expresion e : this.getParam_act()) {
            Object r = e.Resolver(t);

            if (r instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en la lista de parámetros", this.getFila(), this.getColumna());
            }

            Coleccion c = (Coleccion) r;

            if (r instanceof MatrixArit || r instanceof ArrayArit) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Las listas solo aceptan listas o vectores primitivos", this.getFila(), this.getColumna());
            }

            lista.add(c.copiar());
        }

        return new ListArit(lista);

    }

}
