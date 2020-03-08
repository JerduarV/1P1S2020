/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Colecciones;

import Interprete.Expresiones.TipoPrimitivo;
import java.util.LinkedList;

/**
 * Clase que se comporta como una matriz de tamanio n x m
 *
 * @author Jerduar
 */
public class MatrixArit extends VectorArit {

    /**
     * Número de filas y columnas que tiene la matriz
     */
    private final Integer num_filas, num_columnas;

    /**
     * Constructor de la matriz
     *
     * @param f Cantidad de filas que posee
     * @param c Cantidad de columnas que posee
     * @param t Tipo de dato que tiene y debe ser primitivo
     * @param v Lista de objetos que tiene como valor
     */
    public MatrixArit(Integer f, Integer c, TipoPrimitivo t, LinkedList<Object> v) {
        super(t, v);
        this.num_filas = f;
        this.num_columnas = c;
    }

    @Override
    public String toString() {
        String cad = "    ";
        for (int i = 0; i < num_columnas; i++) {
            cad += "[" + (i + 1) + "]";
        }
        cad += "\n>>>";
        for (int y = 0; y < this.num_filas; y++) {
            cad += " [" + (y + 1) + "]";
            for (int k = 0; k < this.num_columnas; k++) {
                cad += " " + ( this.isString() ? "\"" + this.AccederMatrix(y, k).toString() + "\"" : this.AccederMatrix(y, k).toString());
            }
            cad += "\n>>>";
        }
        return cad;
    }

    public Object AccederMatrix(int i, int j) {
        int y = j * this.num_filas + i;
        return this.Acceder(y);
    }

    @Override
    public Coleccion copiar() {
        LinkedList<Object> l = new LinkedList<>();
        for (Object o : this.getValores()) {
            l.add(o);
        }
        return new MatrixArit(this.num_filas, this.num_columnas, this.getTipo_dato(), l);
    }

    public Integer getNum_filas() {
        return num_filas;
    }

    public Integer getNum_columnas() {
        return num_columnas;
    }

}
