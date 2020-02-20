/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

/**
 * Clase Simbolo usada para guardar dentro de la tabla de símbolos todo aquello
 * en el lenguaje que tiene nombre propio
 * @author Jerduar
 */
public class Simbolo {
    
    /**
     * Enumerador que contiene los posibles roles que puede tener un simbolo
     */
    public enum ROL{
        VARIABLE,
        FUNCION
    }
    
    /**
     * Nombre propio del simbolo
     */
    private final String nombre;
    
    /**
     * Rol del símbolo creado
     */
    private final ROL rol;
    
    /**
     * Valor del simbolo, puede ser un valor de variable o una función como tal
     */
    private Object valor;

    /**
     * Constructor de la clase símbolo
     * @param nombre Nombre propio del símbolo
     * @param rol Rol que cumple el simbolo, sea variable o función
     * @param valor Valor que se le da
     */
    public Simbolo(String nombre, ROL rol, Object valor) {
        this.nombre = nombre;
        this.rol = rol;
        this.valor = valor;
    }
    
    
    
}
