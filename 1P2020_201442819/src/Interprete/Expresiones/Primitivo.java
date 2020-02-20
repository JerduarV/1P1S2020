/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Interprete.Expresiones.Colecciones.VectorArit;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;


/**
 * Clase que se usa para el proceso de los datos primitivos, su función ejecutar
 * devuelve un vector de tamaño uno
 * @author Jerduar
 */
public class Primitivo extends Expresion{

    /**
     * Valor del primitivo
     */
    private final Object valor;
    
    /**
     * Constructor de un dato primitivo
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
        if(valor instanceof Integer){
            l.add(valor);
            return new VectorArit(TipoPrimitivo.ENTERO,l);
        }else if(valor instanceof String){
            l.add(valor);
            return new VectorArit(TipoPrimitivo.CADENA, l);
        }else if(valor instanceof Boolean){
            l.add(valor);
            return new VectorArit(TipoPrimitivo.BOOL, l);
        }else if(valor instanceof Double){
            l.add(valor);
            return new VectorArit(TipoPrimitivo.DECIMAL, l);
        }else if(valor == null){
            l.add(new NULO());
            return new VectorArit(TipoPrimitivo.NULL, l);
        }
        return null;
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}