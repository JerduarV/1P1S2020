/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Colecciones;

import Interprete.Expresiones.TipoPrimitivo;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        String cad = "";
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

    private String toStringNDims() {
        String cad = "";
        int cuadrado = 0;
        int s = 1;
        LinkedList<Integer> lista = new LinkedList<>();
        for (int r = 2; r < this.lista_dim.size(); r++) {
            lista.add(1);
        }
        for (int k = 2; k < this.lista_dim.size(); k++) {
            s *= this.lista_dim.get(k);
        }
        for (int y = 0; y < s; y++) {
            cad += "\n>>> , ";
            for (Integer i : lista) {
                cad += ", " + i;
            }
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
                cad += " " + this.Acceder(index).toString();
            }
            cad += "\n>>>";
        }
        return cad;
    }

    /**
     * Función para acceder a una dirección del arreglo
     *
     * @param indices Lista de enteros que representan los índices
     * @return Object con el valor que contiene el arreglo
     */
    public Object Acceso(LinkedList<Integer> indices) {
        int indice_real = 0;
        for (int i = 0; i < indices.size(); i++) {
            int indice = indices.get(i);
            for (int y = i - 1; y >= 0; y--) {
                indice *= this.lista_dim.get(y);
            }
            indice_real += indice;
        }
        return this.Acceder(indice_real);
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
}
