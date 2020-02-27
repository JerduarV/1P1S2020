/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Editor.VentanaErrores;
import TablaSimbolos.TablaSimbolos;
import Utileria.Retorno;

/**
 * Clase para realizar el comporatmiento de un retorno dentro del lenguaje
 *
 * @author Jerduar
 */
public class Return extends Expresion {

    /**
     * Expresión del retorno que puede ser nula
     */
    private final Expresion e;

    /**
     * Constructor de la clase Return
     *
     * @param e Expresión asociada
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encuentra
     */
    public Return(Expresion e, Integer fila, Integer col) {
        super(fila, col);
        this.e = e;
    }

    /**
     * Constructor de un retorno sin expresión asociada
     *
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public Return(Integer fila, Integer col) {
        super(fila, col);
        this.e = null;
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (!t.isEnFuncion()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Retorno fuera del ambiente de función", this.getFila(), this.getColumna());
        } else {
            return this.e == null ? new Retorno(null) : new Retorno(this.e.Resolver(t));
        }
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
