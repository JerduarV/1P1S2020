/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

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
        LinkedList<Object> l = new LinkedList<>();
        if (valor instanceof Integer) {
            l.add(valor);
            return new VectorArit(TipoPrimitivo.INTEGER, l);
        } else if (valor instanceof String) {
            l.add(valor);
            return new VectorArit(TipoPrimitivo.STRING, l);
        } else if (valor instanceof Boolean) {
            l.add(valor);
            return new VectorArit(TipoPrimitivo.BOOL, l);
        } else if (valor instanceof Double) {
            l.add(valor);
            return new VectorArit(TipoPrimitivo.DOUBLE, l);
        } else if (valor == null) {
            l.add("NULL");
            return new VectorArit(TipoPrimitivo.STRING, l);
        }
        return null;
    }

    @Override
    public void dibujar(String padre) {
        String n = NodoAST.getIdNodo(this.Escapar(this.valor.toString()));
        Interprete.Interprete.Conectar(padre, n);
    }

    private String Escapar(String cadena) {
        cadena = valor.toString().replace("\\", "\\\\");
        cadena = valor.toString().replace("\"", "\\\"");
        cadena = valor.toString().replace("\n", "\\\n");
        return cadena;
    }

}
