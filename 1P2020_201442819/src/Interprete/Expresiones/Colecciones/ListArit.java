/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Colecciones;

import Interprete.Expresiones.TipoPrimitivo;
import Utileria.ValorArit;
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
        for (int i = 0; i < this.getValores().size(); i++) {
            this.getValores().set(i, new ValorArit(this.getValores().get(i)));
        }
    }

    /**
     * Constructor de una lista con un valor
     *
     * @param v
     */
    public ListArit(ValorArit v) {
        super(TipoPrimitivo.LIST, new LinkedList<>());
        this.getValores().add(v);
    }

    @Override
    public Coleccion copiar() {
        LinkedList<Object> l = new LinkedList<>();
        for (Object o : this.getValores()) {
            l.add(((ValorArit) o).getVal());
        }
        return new ListArit(l);
    }

    /**
     * Sobre escritura del método toString para el vector
     *
     * @return String
     */
    @Override
    public String toString() {
        String l = "", aux = "";
        for (Object e : this.getValores()) {
            l += aux + e.toString();
            aux = ",";
        }
        return "LIST(" + l + ")";
    }

    /**
     * Función que seteal la posición en una lista
     *
     * @param index
     * @param valor
     */
    @Override
    public void SetPosicion(int index, Coleccion valor) {
        if (index > this.getValores().size() - 1) {
            this.RellenarConDefault(index - this.getValores().size());
            this.getValores().add(new ValorArit(valor));
            return;
        }
        //SE ASIGNA EL NUEVO VALOR
        this.getValores().set(index, valor);
    }

    /**
     * Rellena la lista con el valor por defecto que en este caso es null
     *
     * @param cantidad cantidad de veces que tendrá que hacerlo
     */
    private void RellenarConDefault(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            this.getValores().add(new ValorArit(new VectorArit(TipoPrimitivo.STRING, "null")));
        }
    }

    @Override
    public Object Acceder(Integer index) {
        ValorArit v = (ValorArit) super.Acceder(index); //To change body of generated methods, choose Tools | Templates.
        return v.getVal();
    }

}
