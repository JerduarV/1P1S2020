/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Operaciones.Aritmeticas;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.Operaciones.Operacion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase que maneja la lógica de la potencia en el lenguaje
 * @author Jerduar
 */
public class Potencia extends Operacion{

    /**
     * constructor de la clase potencia
     * @param fila fila en la que se encuentra
     * @param col columna en la que se encuentra
     * @param izq operador izquierdo
     * @param der operador derecho
     */
    public Potencia(Integer fila, Integer col, Expresion izq, Expresion der) {
        super(fila, col, izq, der);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        Object val_izq = this.getOp_izq().Resolver(t);
        Object val_der = this.getOp_der().Resolver(t);

        //SI ALGUNA OPERACIÓN DEVUELVE UN ERROR SE RETORNA EL MISMO
        if (val_izq instanceof ErrorCompi || val_der instanceof ErrorCompi) {
            return val_izq instanceof ErrorCompi ? val_izq : val_der;
        }

        //SUMA DE DOS VECTORES
        if (val_izq instanceof VectorArit && val_der instanceof VectorArit) {
            VectorArit vector_izq = (VectorArit) val_izq;
            VectorArit vector_der = (VectorArit) val_der;

            //SUMA DE DOS VECTORES DE TAMANIO 1
            if (vector_izq.getTamanio() == 1 && vector_der.getTamanio() == 1) {
                return PotenciaVectoresBase(vector_izq, vector_der);
            }
        }
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Función para calcular la potencia vector de tamanio uno
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return Vector de tamanio uno
     */
    private Object PotenciaVectoresBase(VectorArit vector_izq, VectorArit vector_der) {
        LinkedList<Object> l = new LinkedList<>();
        if (null == vector_izq.getTipo_dato()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular la potencia " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
        } else //ENTERO
        {
            switch (vector_izq.getTipo_dato()) {
                case ENTERO:
                    switch (vector_der.getTipo_dato()) {
                        //ENTERO ^ ENTERO
                        case ENTERO: {
                            l.add(Math.pow((Integer) vector_izq.Acceder(0),(Integer) vector_der.Acceder(0)));
                            return new VectorArit(TipoPrimitivo.DECIMAL, l);
                        }
                        //ENTERO ^ DOUBLE
                        case DECIMAL: {
                            l.add(Math.pow((Integer) vector_izq.Acceder(0),(Double) vector_der.Acceder(0)));
                            return new VectorArit(TipoPrimitivo.DECIMAL, l);
                        }
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular mla potencia integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }
                case DECIMAL:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE ^ DOUBLE
                        case DECIMAL:
                            l.add(Math.pow((Double) vector_izq.Acceder(0),(Double) vector_der.Acceder(0)));
                            return new VectorArit(TipoPrimitivo.DECIMAL, l);
                        //DOUBLE ^ ENTERO
                        case ENTERO:
                            l.add(Math.pow((Double) vector_izq.Acceder(0),(Integer) vector_der.Acceder(0)));
                            return new VectorArit(TipoPrimitivo.DECIMAL, l);
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular mla potencia double con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }
                default:
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular mla potencia " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
            }
        }
    }
    
}
