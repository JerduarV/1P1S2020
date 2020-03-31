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
                cad += " |" + (this.isString() ? this.AccederMatrix(y, k).toString() : this.AccederMatrix(y, k).toString()) + "|";
            }
            cad += "\n>>>";
        }
        return cad;
    }

    /**
     * Función para acceder a una matriz de la forma [E,E]
     *
     * @param i Índice de las filas
     * @param j Índice de las columnas
     * @return Object
     */
    public Object AccederMatrix(int i, int j) {
        int y = j * this.num_filas + i;
        return this.Acceder(y);
    }

    @Override
    public Coleccion copiar() {
        LinkedList<Object> l = new LinkedList<>();
        for (int i = 0; i < this.getTamanio(); i++) {
            Object o = this.Acceder(i);
            l.add(o);
        }
        return new MatrixArit(this.num_filas, this.num_columnas, this.getTipo_dato(), l);
    }

    /**
     * Retorna el número de filas
     *
     * @return Entero positivo
     */
    public Integer getNum_filas() {
        return num_filas;
    }

    /**
     * Retorna el número de columnas
     *
     * @return Entero positivo
     */
    public Integer getNum_columnas() {
        return num_columnas;
    }

    /**
     * Setea una posición de la matriz usando índice de filas y columnas
     *
     * @param i Índice de las filas
     * @param j Índice de las columnas
     * @param c Nuevo valor a asignar
     */
    public void SetPosicion(int i, int j, Coleccion c) {
        int y = j * this.num_filas + i;
        this.SetPosicion(y, c);
    }

    /**
     * Modificación de una fila completa
     *
     * @param i Índice de la fila a modificar
     * @param c Valor nuevo que se asignará
     */
    public void SetRow(int i, Coleccion c) {
        if (c.getTamanio() == 1) {
            for (int j = 0; j < this.getNum_columnas(); j++) {
                this.SetPosicion(i, j, c);
            }
        } else {
            for (int j = 0; j < this.getNum_columnas(); j++) {
                this.SetPosicion(i, j, new VectorArit(c.getTipo_dato(), c.Acceder(j)));
            }
        }
    }

    /**
     * Modificación de una columna completa
     *
     * @param j Índice de la columna a modificar
     * @param c Valor nuevo que se quiere asignar
     */
    public void SetCol(int j, Coleccion c) {
        if (c.getTamanio() == 1) {
            for (int i = 0; i < this.getNum_filas(); i++) {
                this.SetPosicion(i, j, c);
            }
        } else {
            for (int i = 0; i < this.getNum_filas(); i++) {
                this.SetPosicion(i, j, new VectorArit(c.getTipo_dato(), c.Acceder(j)));
            }
        }
    }

}
