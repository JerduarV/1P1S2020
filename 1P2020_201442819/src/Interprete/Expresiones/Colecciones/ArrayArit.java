/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Colecciones;

import Interprete.Expresiones.TipoPrimitivo;
import Utileria.ValorArit;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Clase que simula el comportamiento de arreglos de N dimensiones
 *
 * @author Jerduar
 */
public class ArrayArit extends Coleccion {

    /**
     * Lista de dimensiones del ArrayArit
     */
    private final LinkedList<Integer> lista_dim;

    /**
     * Constructor del array
     *
     * @param t Tipo de dato que contiene el arreglo (primitivo o lista)
     * @param v Lista de valores
     * @param Dimensiones Lista de tamaños de las dimensiones
     */
    public ArrayArit(TipoPrimitivo t, LinkedList<Object> v, LinkedList<Integer> Dimensiones) {
        super(t, v);
        this.lista_dim = Dimensiones;
    }

    @Override
    public Coleccion copiar() {
        LinkedList<Object> nueva = new LinkedList<>();
        for (Object j : this.getValores()) {
            Coleccion a = (Coleccion) j;
            nueva.add(a.copiar());
        }
        return new ArrayArit(this.getTipo_dato(), nueva, this.getLista_dim());
    }

    @Override
    public void SetPosicion(int index, Coleccion valor) {
        if (valor.getTipo_dato() == this.getTipo_dato()) {
            this.getValores().set(index, valor);
        } else {
            System.out.println(valor.getTipo_dato() + " " + this.getTipo_dato());
            this.Casteo(valor, index);
        }
    }

    public void Casteo(Coleccion valor, int index) {
        if (this.isList() || valor.isList()) {
            System.out.println("LISTA");
            if (!this.isList()) {
                this.CastToList();
            } else {
                System.out.println("ENTRO ACA");
                this.SetPosicion(index, new ListArit(new ValorArit(valor)));
                return;
            }
        } else if (this.isString() || valor.isString()) {
            if (!this.isString()) {
                this.CastToString();
            } else {
                valor.setTipo_dato(TipoPrimitivo.STRING);
            }
        } else if (this.isDouble()) {
            if (valor.isInteger()) {
                valor.CasteoIntADouble();
            } else {
                valor.casteoBoolToDouble();
            }
        } else if (this.isInteger()) {
            if (valor.isBool()) {
                valor.casteoBoolToInt();
            } else {
                this.CasteoIntADouble();
            }
        } else if (this.isBool()) {
            if (valor.isInteger()) {
                this.casteoBoolToInt();
            } else {
                this.casteoBoolToDouble();
            }
        }
        this.SetPosicion(index, valor);
    }

    @Override
    public void casteoBoolToDouble() {
        this.setTipo_dato(TipoPrimitivo.NUMERIC);
        for (Object v : this.getValores()) {
            Coleccion c = (Coleccion) v;
            if (c.isDouble()) {
                continue;
            }
            c.casteoBoolToDouble();
        }
    }

    @Override
    public void casteoBoolToInt() {
        this.setTipo_dato(TipoPrimitivo.INTEGER);
        for (Object v : this.getValores()) {
            Coleccion c = (Coleccion) v;
            if (c.isInteger()) {
                continue;
            }
            c.casteoBoolToInt();
        }
    }

    @Override
    public void CasteoIntADouble() {
        this.setTipo_dato(TipoPrimitivo.NUMERIC);
        for (Object v : this.getValores()) {
            Coleccion c = (Coleccion) v;
            if (c.isDouble()) {
                continue;
            }
            c.CasteoIntADouble();
        }
    }

    private void CastToString() {
        this.setTipo_dato(TipoPrimitivo.STRING);
        for (Object v : this.getValores()) {
            Coleccion c = (Coleccion) v;
            c.setTipo_dato(TipoPrimitivo.STRING);
        }
    }

    private void CastToList() {
        //System.out.println("cast");
        for (int i = 0; i < this.getTamanio(); i++) {
            this.getValores().set(i, new ListArit(new ValorArit(this.Acceder(i))));
        }
        this.setTipo_dato(TipoPrimitivo.LIST);
    }

    @Override
    public String toString() {
        String cad;
        switch (this.getCantidadDims()) {
            case 1:
                cad = "";
                String aux = "";
                for (Object v : this.getValores()) {
                    cad += aux + v.toString();
                    aux = ",";
                }
                return cad;
            case 2:
                return this.toStringCuadrado(0);
            default:
                return this.toStringNDims();
        }
    }

    /**
     * Función que devuelve la representación en cadena de un array de N
     * dimensiones con N > 2
     *
     * @return Cadena
     */
    private String toStringNDims() {
        String cad = "";
        int cuadrado = 0;
        int s = 1;
        LinkedList<Integer> lista = new LinkedList<>();
        for (int r = 2; r < this.lista_dim.size(); r++) {
            lista.add(1);//[5][5][7][4][4] -> 1,1,1 -> 1,1,2 
        }
        
        for (int k = 2; k < this.lista_dim.size(); k++) {
            s *= this.lista_dim.get(k);
        }
        for (int y = 0; y < s; y++) {
            cad += "\n>>>";
//            for (Integer i : lista) {
//                cad += ", " + i;
//            }
            cad += "\n";
            cad += this.toStringCuadrado(cuadrado);
            cuadrado += this.lista_dim.get(1) * this.lista_dim.get(0);
            this.Contador(lista);
        }
        return cad;
    }

    private void Contador(LinkedList<Integer> lista) {
        for (int y = lista.size() - 1; y >= 0; y--) {
            //System.out.println(y  + " == " + this.lista_dim.get(y + 2));
            //array(2,2,3,3,4)
            //1,1,1 -> 1,1,2 ... 1,1,4 -> 1,2,1-> 1,3,4... 2
            if (!Objects.equals(lista.get(y), this.lista_dim.get(y + 2))) {
                lista.set(y, lista.get(y) + 1);
                for (int k = y + 1; k < lista.size(); k++) {
                    lista.set(k, 1);
                }
                return;
            }
        }
    }

    private String toStringCuadrado(int suma) {
        //System.out.println("llamada cuadrado");
        String cad = "    ";
        for (int i = 0; i < this.getLista_dim().get(1); i++) {
            cad += "[" + (i + 1) + "]";
        }
        cad += "\n>>>";
        for (int y = 0; y < this.getLista_dim().get(0); y++) {
            cad += " [" + (y + 1) + "]";
            for (int k = 0; k < this.getLista_dim().get(1); k++) {
                int index = k * this.getLista_dim().get(0) + y + suma;
                cad += " |" + this.Acceder(index).toString()  + "|";
            }
            cad += "\n>>>";
        }
        return cad;
    }

    /**
     * Setea un valor dentro del array
     *
     * @param indices Lista de índices
     * @param valor_nuevo Valor nuevo a setear
     */
    public void SetPosicion(LinkedList<Integer> indices, Coleccion valor_nuevo) {
        int indice_real = this.getIndiceReal(indices);
        this.SetPosicion(indice_real, valor_nuevo);
    }

    /**
     * Función para acceder a una dirección del arreglo
     *
     * @param indices Lista de enteros que representan los índices
     * @return Object con el valor que contiene el arreglo
     */
    public Object Acceso(LinkedList<Integer> indices) {
        return this.Acceder(this.getIndiceReal(indices));
    }

    /**
     * Calcula el índice real de acuerdo al mapeo lexicográfico
     *
     * @param indices
     * @return
     */
    public int getIndiceReal(LinkedList<Integer> indices) {
        int indice_real = 0;
        for (int i = 0; i < indices.size(); i++) {
            int indice = indices.get(i);
            for (int y = i - 1; y >= 0; y--) {
                indice *= this.lista_dim.get(y);
            }
            indice_real += indice;
        }
        return indice_real;
    }

    /**
     * Función que retorna el número de dimensiones que tiene el array
     *
     * @return Entero positivo
     */
    public int getCantidadDims() {
        return this.lista_dim.size();
    }

    /**
     * Función que retorna la lista de dimensiones del array
     *
     * @return
     */
    public LinkedList<Integer> getLista_dim() {
        return lista_dim;
    }

    /**
     * Retorna si el arreglo es de tipo primitivo
     *
     * @return valor booleano
     */
    public boolean isPrimitivo() {
        return !this.isList();
    }
}
