/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Interprete.NodoAST;

/**
 * Clase que maneja los tipo de índices que existen para acceder a las listas
 *
 * @author Jerduar
 */
public class Indice {

    public enum TipoIndice {
        SIMPLE,
        DOBLE,
        MATRIX,
        MATRIX_COL,
        MATRIX_ROW
    }

    /**
     * Expresión asociada al índice, la exp2 se usa para el índice de tipo
     * matrizs
     */
    private final Expresion exp, exp2;

    /**
     * Tipo de indice [] -> simble : true [[]] -> doble : false
     */
    private final TipoIndice tipo;

    /**
     * Constructor de la clase Indice para un índice simple
     *
     * @param exp Expresión asociada
     */
    public Indice(Expresion exp) {
        this.exp = exp;
        this.tipo = TipoIndice.SIMPLE;
        this.exp2 = null;
    }

    /**
     * Constructor de la clase Indice para un índice doble
     *
     * @param exp Expresion asociada
     * @param t Tipo de Indice que se contruye
     */
    public Indice(Expresion exp, TipoIndice t) {
        this.exp = exp;
        this.tipo = t;
        this.exp2 = null;
    }

    /**
     * Constructor para un índice de tipo matrix (con fila y columna)
     *
     * @param e Expresión para las filas
     * @param e2 Expresión para las columnas
     */
    public Indice(Expresion e, Expresion e2) {
        this.tipo = TipoIndice.MATRIX;
        this.exp = e;
        this.exp2 = e2;
    }

    /**
     * Retorna la Expresion
     *
     * @return Expresión
     */
    public Expresion getExp() {
        return exp;
    }

    /**
     * Retorna si es simple o doble
     *
     * @return Valor booleano
     */
    public boolean isSimple() {
        return this.tipo == TipoIndice.SIMPLE;
    }

    /**
     * Retorna la expresión 2 que debe ser el índice de las columnas en un
     * matriz
     *
     * @return Expresión
     */
    public Expresion getExp2() {
        return exp2;
    }

    /**
     * Retorna si un indice es doble
     *
     * @return valor booleano
     */
    public boolean isDoble() {
        return this.tipo == TipoIndice.DOBLE;
    }

    /**
     * Retorna el tipo de Indice
     *
     * @return TipoIndice
     */
    public TipoIndice getTipo() {
        return tipo;
    }

    public void Dibujar(String padre) {
        String label = "I";
        if (null == this.getTipo()) {
            label = "[I,I]";
        } else {
            switch (this.getTipo()) {
                case DOBLE:
                    label = "[[" + label + "]]";
                    break;
                case SIMPLE:
                    label = "[" + label + "]";
                    break;
                case MATRIX_ROW:
                    label = "[I,]";
                    break;
                case MATRIX_COL:
                    label = "[,I]";
                    break;
                default:
                    label = "[I,I]";
                    break;
            }
        }
        String n = NodoAST.getIdNodo(label);
        Interprete.Interprete.Conectar(padre, n);
        
        this.exp.dibujar(n);
        
        if(this.exp2 != null){
            this.exp2.dibujar(n);
        }
    }

}
