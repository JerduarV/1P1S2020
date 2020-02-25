/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Nativas;

import Editor.VentanaEditor;
import Interprete.Instrucciones.DecFuncion;
import Interprete.Instrucciones.Declaracion;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 *
 * @author Jerduar
 */
public class Print extends DecFuncion {

    public Print(LinkedList<Declaracion> lista_param) {
        super("print", lista_param, null, 0, 0);
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        Object valor = t.BusarVariable("print%%param1");
        VentanaEditor.ImprimirEnConsoloa(valor.toString());
        return null;
    }

}
