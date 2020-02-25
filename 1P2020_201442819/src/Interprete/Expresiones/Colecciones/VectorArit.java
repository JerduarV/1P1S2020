/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Colecciones;

import Interprete.Expresiones.TipoPrimitivo;
import java.util.LinkedList;

/**
 * Clase vector del lenguaje
 *
 * @author Jerduar
 */
public class VectorArit {

    /**
     * Tipo de dato que contiene el vector
     */
    private TipoPrimitivo tipo_dato;

    /**
     * Lista de valores que contiene el vector
     */
    private final LinkedList<Object> valores;

    /**
     * Constructor de la clase vector
     *
     * @param tipo_dato Tipo de dato primitivo que contiene
     * @param valores LinkedList de valores
     */
    public VectorArit(TipoPrimitivo tipo_dato, LinkedList<Object> valores) {
        this.tipo_dato = tipo_dato;
        this.valores = valores;
    }

    /**
     * Retorna el tipo de dato que contiene en su lista
     *
     * @return Tipo de valores que contiene el vector
     */
    public TipoPrimitivo getTipo_dato() {
        return tipo_dato;
    }

    /**
     * Retorna la lista de valores del vector
     *
     * @return LinkedList de valores
     */
    public LinkedList<Object> getValores() {
        return valores;
    }

    /**
     * Retorna el tamanio de la coleccion
     *
     * @return
     */
    public int getTamanio() {
        return this.valores.size();
    }

    /**
     * Función para acceder a un valor del vector
     *
     * @param index índice entero
     * @return Object valor
     */
    public Object Acceder(Integer index) {
        return this.valores.get(index);
    }

    /**
     * Retorna si el Vector es de tipo Entero
     *
     * @return valor booleano
     */
    public boolean isInteger() {
        return this.getTipo_dato() == TipoPrimitivo.INTEGER;
    }

    /**
     * Retorna si el vector es de tipo Double
     *
     * @return valor booleano
     */
    public boolean isDouble() {
        return this.getTipo_dato() == TipoPrimitivo.DOUBLE;
    }

    /**
     * Retorna si el vector es de tipo string
     *
     * @return
     */
    public boolean isString() {
        return this.getTipo_dato() == TipoPrimitivo.STRING;
    }

    /**
     * Retorna si el vector es de tipo Numerico: int o double
     *
     * @return valor booleano
     */
    public boolean isNumerico() {
        return this.isInteger() || this.isDouble();
    }

    /**
     * Retorna si el vector es de tipo Numerico: int o double
     *
     * @return
     */
    public boolean isBool() {
        return this.getTipo_dato() == TipoPrimitivo.BOOL;
    }

    /**
     * Setea un valor nuevo en alguna posición del vector dada por el índice que
     * se envía como parámetro
     *
     * @param index Indice que indica la posición a setear
     * @param nuevo_valor Nuevo valor que se seteará
     */
    public void SetPosicion(int index, VectorArit nuevo_valor) {

        //SI NO TIENEN EL MISMO TIPO DE DATO HACE FALTA CASTEAR PRIMERO
        if (this.tipo_dato != nuevo_valor.getTipo_dato()) {
            this.casteoImplicito(nuevo_valor);
        }

        //SE RELLENA LAS CASILLAS INTERMEDIAS CON EL VALOR POR DEFECTO
        if (index > this.valores.size() - 1) {
            this.RellenarConDefault(index - this.valores.size());
            this.valores.add(nuevo_valor.Acceder(0));
            return;
        }

        //SE ASIGNA EL NUEVO VALOR
        this.valores.set(index, nuevo_valor.Acceder(0));
    }

    /**
     * Método con el que se realizan las validaciones para los casteos implictos
     * en lo elementos del vector
     *
     * @param nuevo_valor Nuevo valor que se cambiará
     */
    private void casteoImplicito(VectorArit nuevo_valor) {
        if (this.isString() || nuevo_valor.isString()) {
            this.tipo_dato = TipoPrimitivo.STRING;
        } else if (this.isDouble()) {
            if (nuevo_valor.isInteger()) {
                nuevo_valor.CasteoIntADouble();
            } else if (nuevo_valor.isBool()) {
                nuevo_valor.casteoBoolACualquierNumerico(this.getTipo_dato());
            }
        } else if (this.isInteger()) {
            if (nuevo_valor.isDouble()) {
                this.CasteoIntADouble();
            } else if (nuevo_valor.isBool()) {
                nuevo_valor.casteoBoolACualquierNumerico(this.getTipo_dato());
            }
        } else if (this.isBool()) {
            this.casteoBoolACualquierNumerico(nuevo_valor.getTipo_dato());
        }
    }

    /**
     * Método que castea todos los elementos del arreglo de entero a double
     */
    public void CasteoIntADouble() {
        this.tipo_dato = TipoPrimitivo.DOUBLE;
        for (int i = 0; i < this.valores.size(); i++) {
            Integer e = (Integer) this.valores.get(i);
            Double nuevo = e.doubleValue();
            this.valores.set(i, nuevo);
        }
    }

    /**
     * Casteo de los elementos booleanos del arreglo a numericos
     *
     * @param t
     */
    public void casteoBoolACualquierNumerico(TipoPrimitivo t) {
        this.tipo_dato = t;
        for (int i = 0; i < this.valores.size(); i++) {
            Boolean e = (Boolean) this.valores.get(i);
            this.valores.set(i, e ? (t == TipoPrimitivo.INTEGER ? 1 : 1.0) : (t == TipoPrimitivo.INTEGER ? 0 : 0.0));
        }
    }

    /**
     * Método que rellena las celdas vacías del arreglo con el valor por defecto
     * de acuerdo al tipo del vector
     *
     * @param cantidad Cantidad de espacios a rellenar
     */
    private void RellenarConDefault(int cantidad) {
        Object defecto;
        switch (this.tipo_dato) {
            case BOOL:
                defecto = false;
                break;
            case DOUBLE:
                defecto = 0.0;
                break;
            case INTEGER:
                defecto = 0;
                break;
            default:
                defecto = "null";
        }

        for (int i = 0; i < cantidad; i++) {
            this.valores.add(defecto);
        }
    }

    @Override
    public String toString() {
        if(this.valores.size() == 1){
            return this.valores.getFirst().toString();
        }else{
            String l = "", aux = "";
            //System.out.println(this.valores.size());
            for(Object e : this.valores){
                l += aux + e.toString();
                aux = ",";
            }
            return "VECTOR[" + l + "]";
        }
    }
    
    

}
