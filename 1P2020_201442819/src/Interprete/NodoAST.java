/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete;

/**
 * Clase abstracta nodo de la cual heredan expresión e instrucción
 *
 * @author Jerduar
 */
public abstract class NodoAST {

    /**
     * Variable que se usa para construir el nombre de los nodos
     */
    private static int contadorNodo = 0;

    /**
     * Fila en la que se encuentra úbicado en la entrada
     */
    private final Integer fila;

    /**
     * Columna en la que se encuentra ubicado en la entrada
     */
    private final Integer columna;

    /**
     * Construcción del NodoAST
     *
     * @param fila fila del archivo
     * @param col columna del archivo
     */
    public NodoAST(Integer fila, Integer col) {
        this.fila = fila;
        this.columna = col;
    }

    /**
     * Metodo usado para generar el codigo graphviz para el reporte del AST
     *
     * @param padre Nodo padre
     */
    public abstract void dibujar(String padre);

    /**
     * Retorna la fila de la ubicación
     *
     * @return int fila
     */
    public int getFila() {
        return this.fila;
    }

    /**
     * Retorna la columna de la ubicación
     *
     * @return int columna
     */
    public int getColumna() {
        return this.columna;
    }

    /**
     * Genera identificador para los nodos
     *
     * @param label Etiqueta para crear el nodo
     * @return String identificador
     */
    public static String getIdNodo(String label) {
        String n = "nodo" + contadorNodo++;
        Interprete.DeclararNodo(n, label);
        return n;
    }
}
