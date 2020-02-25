/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

/**
 * Clase que maneja los tipo de índices que existen para acceder a las listas
 *
 * @author Jerduar
 */
public class Indice {

    /**
     * Expresión asociada al índice
     */
    private final Expresion exp;

    /**
     * Tipo de indice [] -> simble : true [[]] -> doble : false
     */
    private final boolean Simple;

    /**
     * Constructor de la clase Indice para un índice simple
     *
     * @param exp Expresión asociada
     */
    public Indice(Expresion exp) {
        this.exp = exp;
        this.Simple = true;
    }

    /**
     * Constructor de la clase Indice para un índice doble
     *
     * @param exp Expresion asociada
     * @param Simple al venir indica que el indice es doble
     */
    public Indice(Expresion exp, boolean Simple) {
        this.exp = exp;
        this.Simple = false;
    }

    /**
     * Retorna la Expresion
     *
     * @return
     */
    public Expresion getExp() {
        return exp;
    }

    /**
     * Retorna si es simple o doble
     *
     * @return
     */
    public boolean isSimple() {
        return Simple;
    }

}
