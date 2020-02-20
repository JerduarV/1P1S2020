/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Interprete.ErrorCompi;
import Interprete.Expresiones.Expresion;
import TablaSimbolos.TablaSimbolos;

/**
 *
 * @author Jerduar
 */
public class Asignacion extends Instruccion{
    
    /**
     * Identificador de la variable a crear o modificar
     */
    private final String identificador;
    
    /**
     * Expresión a asignar
     */
    private final Expresion exp;

    /**
     * Constructor de la clase Asignación que funciona a su vez como declaración
     * de nuevas variables
     * @param fila en la que se encuentra
     * @param col columna en la que se encuentra
     * @param id Identificador de la variable
     * @param e Expresión que se va asignar
     */
    public Asignacion(Integer fila, Integer col, String id, Expresion e) {
        super(fila, col);
        this.identificador = id;
        this.exp = e;
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        Object valor = this.exp.Resolver(t);
        if(valor instanceof ErrorCompi){
            return valor;
        }else{
            t.GuardarVariable(identificador, valor);
            return null;
        }
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
