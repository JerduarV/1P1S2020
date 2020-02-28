/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import Editor.VentanaErrores;
import Interprete.Instrucciones.DecFuncion;
import TablaSimbolos.Simbolo.ROL;
import java.util.Hashtable;

/**
 * Clase para el manejo de los simbolos del lenguaje
 *
 * @author Jerduar
 */
public class TablaSimbolos extends Hashtable {

    /**
     * Tabla de Simbolos padre, es nula cuando el padre es la tabla global
     */
    private final TablaSimbolos padre;

    /**
     * Variable que indica el valor del display para controlar las instrucciones
     * break y continue
     */
    private int display;

    /**
     * Valor que determina si estoy en una función
     */
    private boolean EnFuncion;

    /**
     * Constructor de la tabla de simbolos que recibe como parámetro a su tabla
     * padre
     *
     * @param padre TablaSimbolos superior
     */
    public TablaSimbolos(TablaSimbolos padre) {
        this.padre = padre;
        this.display = padre == null ? 0 : this.padre.display;
        this.EnFuncion = padre == null ? false : this.padre.EnFuncion;
    }

    /**
     * Sete el valor para indicar que ahora se encuentra en una función
     */
    public void EntreEnFuncion() {
        this.EnFuncion = true;
    }

    /**
     * Indica si se encuentra en una función
     *
     * @return valor booleano
     */
    public boolean isEnFuncion() {
        return EnFuncion;
    }

    /**
     * Guarda la variable o le cambia el valor en la tabla de simbolos
     *
     * @param nombre Nombre de la variable a guardar o crear
     * @param valor Valor a asignar
     */
    public void GuardarVariable(String nombre, Object valor) {
        int key = this.getHashCode(nombre, ROL.VARIABLE);
//        this.put(key, valor);
        if (this.BusarVariable(nombre) == null) {
            this.put(key, valor);
        } else {
            this.SetVar(key, valor);
        }
    }

    /**
     * Setea el valor de una variable
     *
     * @param key Llave de la variable
     * @param val Nuevo valor
     */
    private void SetVar(int key, Object val) {
        if (this.containsKey(key)) {
            this.put(key, val);
        } else {
            if (this.padre != null) {
                this.padre.SetVar(key, val);
            } else {
                System.out.println("Que cagada :(");
            }
        }
    }

    /**
     * Método para guardar parámetros en los ámbitos de las funciones
     *
     * @param nombre Nombre del parámetro
     * @param valor Valor que tendrá, puede ser null si el parámetro no tiene
     * valor por defecto
     * @return valor booleano, true si el parámetro se ingresó sin problemas y
     * false si el parámetro ya existía en la tabla
     */
    public boolean GuardarParametro(String nombre, Object valor) {
        int key = this.getHashCode(nombre, ROL.VARIABLE);
        if (this.containsKey(key)) {
            return false;
        } else {
            this.put(key, valor);
        }

        return true;
    }

    /**
     * Función que busca un variable en la tabla se simbolos
     *
     * @param nombre Nombre de la variable a buscar
     * @return Object con el valor de la variable o null de no encontrarla
     */
    public Object BusarVariable(String nombre) {
        int key = this.getHashCode(nombre, ROL.VARIABLE);
        if (this.containsKey(key)) {
            return this.get(key);
        } else if (this.padre != null) {
            return this.padre.BusarVariable(nombre);
        } else {
            return null;
        }
    }

    /**
     * Retorna la llave con la que se guardará o buscará el simbolo en la tabla
     *
     * @param nombre de la variable o función
     * @param r rol del símbolo
     * @return entero llave
     */
    private int getHashCode(String nombre, ROL r) {
        return (r + "$$$" + nombre.toLowerCase()).hashCode();
    }

    /**
     * Método que guarda una función en la tabla de símbolos
     *
     * @param nombre nombre de la función a guardar
     * @param funcion función como tal
     */
    public void GuardarFuncion(String nombre, DecFuncion funcion) {
        int key = this.getHashCode(nombre, ROL.FUNCION);
        if (this.containsKey(key)) {
            VentanaErrores.getVenErrores().AgregarError("Semantico", "Ya existe la función " + nombre, funcion.getFila(), funcion.getColumna());
        } else {
            System.out.println("Se guardó con éxito la función: " + nombre);
            this.put(key, funcion);
        }
    }

    /**
     * Función que retorna la función o null si no la encuentra
     *
     * @param nombre Nombre de la función que se busca
     * @return DecFunción o null si no existe
     */
    public Object BuscarFuncion(String nombre) {
        return this.getGlobal().get(this.getHashCode(nombre, ROL.FUNCION));
    }

    /**
     * Retorna la tabla de simbolos global, usada para el llamado de funciones
     *
     * @return Tabla de símbolos global
     */
    public TablaSimbolos getGlobal() {
        if (this.padre == null) {
            return this;
        } else {
            return this.padre.getGlobal();
        }
    }

    /**
     * Incrementa el display en 1, este método es usado el entrar en
     * instrucciones ciclicas como while, dowhile y for
     */
    public void IncrementarDisplay() {
        this.display++;
    }

    /**
     * Retorna el valor entero del display
     *
     * @return número entero
     */
    public int getDisplay() {
        return display;
    }

}
