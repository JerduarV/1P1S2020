/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;

/**
 * Clase que se usa para el proceso de los datos primitivos, su función ejecutar
 * devuelve un vector de tamaño uno
 *
 * @author Jerduar
 */
public class Primitivo extends Expresion {

    /**
     * Valor del primitivo
     */
    private final Object valor;

    /**
     * Constructor de un dato primitivo
     *
     * @param fila integer fila en la que se encuentra
     * @param col integer columna en la que se encuentra
     * @param valor Object valor que posee
     */
    public Primitivo(Integer fila, Integer col, Object valor) {
        super(fila, col);
        this.valor = valor;
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (valor instanceof Integer) {
            return new VectorArit(TipoPrimitivo.INTEGER, this.valor);
        } else if (valor instanceof String) {
            return new VectorArit(TipoPrimitivo.STRING, this.valor);
        } else if (valor instanceof Boolean) {
            return new VectorArit(TipoPrimitivo.BOOL, this.valor);
        } else if (valor instanceof Double) {
            return new VectorArit(TipoPrimitivo.DOUBLE, this.valor);
        } else if (valor == null) {
            return new VectorArit(TipoPrimitivo.STRING, "NULL");
        }
        return null;
    }

    @Override
    public void dibujar(String padre) {
        String n = NodoAST.getIdNodo(this.Escapar(this.valor.toString()));
        Interprete.Interprete.Conectar(padre, n);
    }

    /**
     * Función usada para el dibujo del árbol en el caso de que los datos sean
     * de tipo string
     *
     * @param cadena String que se recibe
     * @return cadena
     */
    private String Escapar(String cadena) {
        cadena = cadena.replace("\\", "\\\\");
        cadena = cadena.replace("\"", "\\\"");
        cadena = cadena.replace("\n", "\\\\n");
        cadena = cadena.replace("\t", "\\\\t");
        cadena = cadena.replace("\r", "\\\\r");
        return cadena;
    }

}
