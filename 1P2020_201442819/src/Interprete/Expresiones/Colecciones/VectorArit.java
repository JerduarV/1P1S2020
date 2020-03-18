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
public class VectorArit extends Coleccion {

    /**
     * Constructor de la clase vector
     *
     * @param tipo_dato Tipo de dato primitivo que contiene
     * @param valores LinkedList de valores
     */
    public VectorArit(TipoPrimitivo tipo_dato, LinkedList<Object> valores) {
        super(tipo_dato, valores);
    }

    /**
     * Constructor que se usa cuando se quiere crear un nuevo vector con un
     * elemento
     *
     * @param tipo_dato Tipo de dato del vector
     * @param valor valor
     */
    public VectorArit(TipoPrimitivo tipo_dato, Object valor) {
        super(tipo_dato, new LinkedList<>());
        this.getValores().add(valor);
    }

    /**
     * Setea un valor nuevo en alguna posición del vector dada por el índice que
     * se envía como parámetro
     *
     * @param index Indice que indica la posición a setear
     * @param valor Nuevo valor que se seteará
     */
    @Override
    public void SetPosicion(int index, Coleccion valor) {

        VectorArit nuevo_valor = (VectorArit) valor;

        //SI NO TIENEN EL MISMO TIPO DE DATO HACE FALTA CASTEAR PRIMERO
        if (this.getTipo_dato() != nuevo_valor.getTipo_dato()) {
            this.casteoImplicito(nuevo_valor);
        }

        //SE RELLENA LAS CASILLAS INTERMEDIAS CON EL VALOR POR DEFECTO
        if (index > this.getValores().size() - 1 && !(this instanceof MatrixArit)) {
            this.RellenarConDefault(index - this.getValores().size());
            this.getValores().add(nuevo_valor.Acceder(0));
            return;
        }

        //SE ASIGNA EL NUEVO VALOR
        this.getValores().set(index, nuevo_valor.Acceder(0));
    }

    /**
     * Método con el que se realizan las validaciones para los casteos implictos
     * en lo elementos del vector
     *
     * @param nuevo_valor Nuevo valor que se cambiará
     */
    private void casteoImplicito(Coleccion nuevo_valor) {
        if (this.isString() || nuevo_valor.isString()) {
            this.setTipo_dato(TipoPrimitivo.STRING);
        } else if (this.isDouble()) {
            if (nuevo_valor.isInteger()) {
                nuevo_valor.CasteoIntADouble();
            } else if (nuevo_valor.isBool()) {
                nuevo_valor.casteoBoolToDouble();
            }
        } else if (this.isInteger()) {
            if (nuevo_valor.isDouble()) {
                this.CasteoIntADouble();
            } else if (nuevo_valor.isBool()) {
                nuevo_valor.casteoBoolToInt();
            }
        } else if (this.isBool()) {
            if (nuevo_valor.isDouble()) {
                this.CasteoIntADouble();
            } else {
                this.casteoBoolToInt();
            }
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
        switch (this.getTipo_dato()) {
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
            this.getValores().add(defecto);
        }
    }

    @Override
    public String toString() {
        if (this.getValores().size() == 1) {
            return this.getValores().getFirst().toString();
        } else {
            String l = "", aux = "";
            for (Object e : this.getValores()) {
                l += aux + (this.isString() ? "\"" + e.toString() + "\"" : e.toString());
                aux = ",";
            }
            return "[" + l + "]";
        }
    }

    /**
     * Función que crea una copia de los elementos que hay en la lista del
     * vector
     *
     * @return Nuevo VectorArit
     */
    @Override
    public Coleccion copiar() {
        LinkedList<Object> l = new LinkedList<>();
        for (Object o : this.getValores()) {
            l.add(o);
        }
        return new VectorArit(this.getTipo_dato(), l);
    }

}
