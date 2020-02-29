/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Colecciones;

import Interprete.Expresiones.TipoPrimitivo;
import java.util.LinkedList;

/**
 * Clase para el manejo de listas
 *
 * @author Jerduar
 */
public class ListArit extends Coleccion {

    /**
     * Constructor de la clase ListArit
     *
     * @param v Lista de Objetos que contiene la Lista que puede ser homogeneo
     */
    public ListArit(LinkedList<Object> v) {
        super(TipoPrimitivo.LIST, v);
    }

    @Override
    public Coleccion copiar() {
        LinkedList<Object> l = new LinkedList<>();
        for (Object o : this.getValores()) {
            l.add(o);
        }
        return new ListArit(l);
    }

    @Override
    public String toString() {
        String l = "", aux = "";
        for (Object e : this.getValores()) {
            l += aux + e.toString();
            aux = ",";
        }
        return "LIST(" + l + ")";
    }

}
