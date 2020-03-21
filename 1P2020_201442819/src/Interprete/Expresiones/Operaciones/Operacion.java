/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Operaciones;

import Interprete.Expresiones.Colecciones.MatrixArit;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.TipoPrimitivo;
import Interprete.NodoAST;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Claes abstracta operación de la que heredan todas las operaciones
 * aritmeticas, lógicas y relacionales
 *
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
    public Operacion(Expresion op_unario, TipoOpe o, Integer fila, Integer col) {
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
     *
     * @return
     */
    public TipoOpe getTipo() {
        return tipo;
    }

    /**
     * Determina si la operación es entre un vector de tamaño 1 y otro de tamaño
     * n donde n es mayor a 1
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return valor booleano
     */
    protected boolean UnoVsN(VectorArit vector_izq, VectorArit vector_der) {
        return vector_izq.getTamanio() == 1 && vector_der.getTamanio() > vector_izq.getTamanio() || vector_der.getTamanio() == 1 && vector_izq.getTamanio() > vector_der.getTamanio();
    }

    /**
     * Determina si la operación es entre dos vectores de tamaño n
     *
     * @param izq operador izquierdo
     * @param der operador derecho
     * @return valor booleano
     */
    protected boolean NvsN(VectorArit izq, VectorArit der) {
        if (izq instanceof MatrixArit && der instanceof MatrixArit) {
            return Objects.equals(((MatrixArit) izq).getNum_columnas(), ((MatrixArit) der).getNum_columnas()) && Objects.equals(((MatrixArit) izq).getNum_filas(), ((MatrixArit) der).getNum_filas());
        }
        return izq.getTamanio() == der.getTamanio();
    }

    /**
     * Genera el resultado en función si hay una matrix en la operación
     *
     * @param izq Operador Izquierdo
     * @param der Operador derecho
     * @param t Tipo de dato
     * @param l Lista de valores
     * @return VectorArit: VectorArit o MatrixArit dependiendo de los operadores
     */
    protected VectorArit GenResultado(VectorArit izq, VectorArit der, TipoPrimitivo t, LinkedList<Object> l) {
        if (izq instanceof MatrixArit || der instanceof MatrixArit) {
            int f = izq instanceof MatrixArit ? ((MatrixArit) izq).getNum_filas() : ((MatrixArit) der).getNum_filas();
            int c = izq instanceof MatrixArit ? ((MatrixArit) izq).getNum_columnas() : ((MatrixArit) der).getNum_columnas();

            return new MatrixArit(f, c, t, l);
        }

        return new VectorArit(t, l);
    }

    /**
     * Genera el resultado en función si el vector es una matrix o no para un
     * operación unaria
     *
     * @param vector Operador único
     * @param l Lista de valores
     * @return VectorArit o MatrixArit
     */
    protected VectorArit GenResultado(VectorArit vector, LinkedList<Object> l) {
        if (vector instanceof MatrixArit) {
            return new MatrixArit(((MatrixArit) vector).getNum_filas(), ((MatrixArit) vector).getNum_columnas(), vector.getTipo_dato(), l);
        }

        return new VectorArit(vector.getTipo_dato(), l);
    }

    @Override
    public void dibujar(String padre) {
        String n = NodoAST.getIdNodo(this.getSimbolo());
        Interprete.Interprete.Conectar(padre, n);
        if (this.op_unario != null) {
            this.op_unario.dibujar(n);
        } else {
            this.op_izq.dibujar(n);
            this.op_der.dibujar(n);
        }
    }

    private String getSimbolo() {
        switch (this.getTipo()) {
            case AND:
                return "&";
            case OR:
                return "|";
            case DIFERENTE:
                return "!=";
            case DIV:
                return "/";
            case IGUALQUE:
                return "==";
            case MAYOR:
                return ">";
            case MAYORIGUAL:
                return ">=";
            case MENOR:
                return "<";
            case MENORIGUAL:
                return "<=";
            case MOD:
                return "%";
            case MULT:
                return "*";
            case RESTA:
            case NEGATIVO:
                return "-";
            case NOT:
                return "!";
            case POT:
                return "^";
            default:
                return "+";
        }
    }

}
