/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Operaciones;

import Interprete.Expresiones.Expresion;

/**
 * Claes abstracta operación de la que heredan todas las operaciones aritmeticas,
 * lógicas y relacionales
 * @author Jerduar
 */
public abstract class Operacion extends Expresion {

    /**
     * Operador izquierdo
     */
    private final Expresion op_izq;

    /**
     * Operador derecho
     */
    private final Expresion op_der;

    /**
     * Operador unario
     */
    private final Expresion op_unario;
    
    /**
     * Tipo de la operación a resolver
     */
    private final TipoOpe tipo;

    /**
     * Constructor de una operacion binaria
     *
     * @param fila integer fila en la que se encuentra
     * @param col integer columan en la que se encuentra
     * @param izq operador izquierdo
     * @param der operador derecho
     * @param o tipo de operación
     */
    public Operacion(Integer fila, Integer col, Expresion izq, Expresion der, TipoOpe o) {
        super(fila, col);
        this.op_izq = izq;
        this.op_der = der;
        this.tipo = o;
        this.op_unario = null;
    }

    /**
     * Constructor de una operacion unaria (negativo y negacion)
     *
     * @param op_unario unico operando de la operación
     * @param o tipo de la operación
     * @param fila fila en la que se encuentra
     * @param col columna en la que se encuentra
     */
    public Operacion(Expresion op_unario,TipoOpe o, Integer fila, Integer col) {
        super(fila, col);
        this.op_unario = op_unario;
        this.op_der = null;
        this.op_izq = null;
        this.tipo = o;
    }

    /**
     * Retorna el operador izquierdo
     *
     * @return Expresión
     */
    public Expresion getOp_izq() {
        return op_izq;
    }

    /**
     * Retorna el operador derecho
     *
     * @return Expresión
     */
    public Expresion getOp_der() {
        return op_der;
    }

    /**
     * Retorna el operador unario
     *
     * @return Expresión
     */
    public Expresion getOp_unario() {
        return op_unario;
    }

    /**
     * Retorna el tipo de operación que se realiza
     * @return 
     */
    public TipoOpe getTipo() {
        return tipo;
    }

}
