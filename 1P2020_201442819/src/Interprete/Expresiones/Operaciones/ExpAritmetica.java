/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Operaciones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import Utileria.ValorArit;
import java.util.LinkedList;

/**
 * Clase que maneja la lógica de las expresiones aritméticas
 *
 * @author Jerduar
 */
public class ExpAritmetica extends Operacion {

    /**
     * Constructor de una expresión aritmética binaria
     *
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     * @param izq operador izquierdo
     * @param der operador derecho
     * @param o Tipo de la operación
     */
    public ExpAritmetica(Integer fila, Integer col, Expresion izq, Expresion der, TipoOpe o) {
        super(fila, col, izq, der, o);
    }

    /**
     * Constructor de una expresión aritmética unaria (negativo)
     *
     * @param op_unario Operador único
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public ExpAritmetica(Expresion op_unario, Integer fila, Integer col) {
        super(op_unario, TipoOpe.NEGATIVO, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getOp_unario() != null) {
            Object val_unico = this.getOp_unario().Resolver(t);

            if (val_unico instanceof ErrorCompi) {
                return val_unico;
            }

            //NEGATIVO DE UN VECTOR
            if (val_unico instanceof VectorArit) {
                VectorArit vector_unico = (VectorArit) val_unico;
                return NegativoVectores(vector_unico);
            }
        } else { //SI ES UNA OPERACIÓN BINARIA
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

                return OperacionesBasicas(vector_izq, vector_der);

            }
        }

        return VentanaErrores.getVenErrores().AgregarError("Semantico", "Operación no soportada", this.getFila(), this.getColumna());
    }

    /**
     * Realiza las opearciones artiméticas con los vectores de tamanio 1
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return Vector con resultado
     */
    private Object OperacionesBasicas(VectorArit vector_izq, VectorArit vector_der) {
        switch (this.getTipo()) {
            case SUMA:
                return this.SumarVectores(vector_izq, vector_der);
            case RESTA:
                return this.RestaVectores(vector_izq, vector_der);
            case MULT:
                return this.MultiplicacionVectores(vector_izq, vector_der);
            case DIV:
                return this.DivisionVectores(vector_izq, vector_der);
            case MOD:
                return this.ModuloVectoresBase(vector_izq, vector_der);
            case POT:
                return this.PotenciaVectores(vector_izq, vector_der);
            default:
                System.out.println("NO SE QUE MIERDA HACES EN LA CLASE EXPARITMETICA");
                return null;
        }
    }

    /**
     * Función para sumar vector de tamanio uno
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return Vector de tamanio uno
     */
    private Object SumarVectores(VectorArit vector_izq, VectorArit vector_der) {
        LinkedList<Object> l = new LinkedList<>();
        if (null == vector_izq.getTipo_dato()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede sumar" + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
        } else //ENTERO
        {
            switch (vector_izq.getTipo_dato()) {
                case INTEGER:
                    switch (vector_der.getTipo_dato()) {
                        //ENTERO + ENTERO
                        case INTEGER: {
                            //System.out.println(vector_izq.Acceder(0).getClass() + " " + vector_der.Acceder(0).getClass());
                            //l.add((Integer) vector_izq.Acceder(0) + (Integer) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Integer) vector_izq.Acceder(0) + (Integer) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Integer) ((ValorArit) e).getVal() + (Integer) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Integer) vector_izq.Acceder(i) + (Integer) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Suma: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.INTEGER, l);
                            //return new VectorArit(TipoPrimitivo.INTEGER, l);
                        }
                        //ENTERO + DOUBLE
                        case NUMERIC: {
                            //l.add((Integer) vector_izq.Acceder(0) + (Double) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Integer) vector_izq.Acceder(0) + (Double) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Integer) ((ValorArit) e).getVal() + (Double) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Integer) vector_izq.Acceder(i) + (Double) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Suma: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                            //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        //ENTERO + CADENA
                        case STRING:
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add(vector_izq.Acceder(0).toString() + e.toString());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add(e.toString() + vector_der.Acceder(0).toString());
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add(vector_izq.Acceder(i).toString() + vector_der.Acceder(i).toString());
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Suma: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.STRING, l);
                        //return new VectorArit(TipoPrimitivo.STRING, l);

                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede sumar integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case NUMERIC:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE + DOUBLE
                        case NUMERIC:
                            //l.add((Double) vector_izq.Acceder(0) + (Double) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Double) vector_izq.Acceder(0) + (Double) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Double) ((ValorArit) e).getVal() + (Double) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Double) vector_izq.Acceder(i) + (Double) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Suma: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE + ENTERO
                        case INTEGER:
                            //l.add((Double) vector_izq.Acceder(0) + (Integer) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Double) vector_izq.Acceder(0) + (Integer) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Double) ((ValorArit) e).getVal() + (Integer) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Double) vector_izq.Acceder(i) + (Integer) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Suma: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE + CADENA
                        case STRING:
                            //l.add(vector_izq.Acceder(0).toString() + vector_der.Acceder(0).toString());
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add(vector_izq.Acceder(0).toString() + e.toString());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add(e.toString() + vector_der.Acceder(0).toString());
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add(vector_izq.Acceder(i).toString() + vector_der.Acceder(i).toString());
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Suma: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.STRING, l);
                        //return new VectorArit(TipoPrimitivo.STRING, l);

                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede sumar double con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }


                case BOOL:
                    switch (vector_der.getTipo_dato()) {
                        //BOOL + STRING
                        case STRING:
                            //l.add(vector_izq.Acceder(0).toString() + vector_der.Acceder(0).toString());
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add(vector_izq.Acceder(0).toString() + e.toString());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add(e.toString() + vector_der.Acceder(0).toString());
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add(vector_izq.Acceder(i).toString() + vector_der.Acceder(i).toString());
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Suma: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.STRING, l);
                        //return new VectorArit(TipoPrimitivo.STRING, l);
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede sumar bool con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }
                case STRING:
                    //CADENA + CUALQUIER OTRO
                    if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                        if (vector_izq.getTamanio() == 1) {
                            for (Object e : vector_der.getValores()) {
                                l.add(vector_izq.Acceder(0).toString() + e.toString());
                            }
                        } else {
                            for (Object e : vector_izq.getValores()) {
                                l.add(e.toString() + vector_der.Acceder(0).toString());
                            }
                        }
                    } else if (this.NvsN(vector_izq, vector_der)) {
                        for (int i = 0; i < vector_izq.getTamanio(); i++) {
                            l.add(vector_izq.Acceder(i).toString() + vector_der.Acceder(i).toString());
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "Suma: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.STRING, l);
                //return new VectorArit(TipoPrimitivo.STRING, l);

                default:
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede sumar" + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
            }
        }
    }

    /**
     * Función para restar vector de tamanio uno
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return Vector de tamanio uno
     */
    private Object RestaVectores(VectorArit vector_izq, VectorArit vector_der) {
        LinkedList<Object> l = new LinkedList<>();
        if (null == vector_izq.getTipo_dato()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede restar " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
        } else //ENTERO
        {
            switch (vector_izq.getTipo_dato()) {
                case INTEGER:
                    switch (vector_der.getTipo_dato()) {
                        //ENTERO - ENTERO
                        case INTEGER: {
                            //l.add((Integer) vector_izq.Acceder(0) - (Integer) vector_der.Acceder(0));
                            //VECTOR[1] * VECTOR[1]
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Integer) vector_izq.Acceder(0) - (Integer) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Integer) ((ValorArit) e).getVal() - (Integer) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Integer) vector_izq.Acceder(i) - (Integer) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Resta: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.INTEGER, l);
                            //return new VectorArit(TipoPrimitivo.INTEGER, l);
                        }
                        //ENTERO - DOUBLE
                        case NUMERIC: {
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Integer) vector_izq.Acceder(0) - (Double) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Integer) ((ValorArit) e).getVal() - (Double) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Integer) vector_izq.Acceder(i) - (Double) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Resta: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        }
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede restar integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case NUMERIC:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE - DOUBLE
                        case NUMERIC:
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Double) vector_izq.Acceder(0) - (Double) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Double) ((ValorArit) e).getVal() - (Double) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Double) vector_izq.Acceder(i) - (Double) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Resta: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE - ENTERO
                        case INTEGER:
                            //l.add((Double) vector_izq.Acceder(0) - (Integer) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Double) vector_izq.Acceder(0) - (Integer) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Double) ((ValorArit) e).getVal() - (Integer) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Double) vector_izq.Acceder(i) - (Integer) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Resta: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede restar double con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }


                default:
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede restar " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
            }
        }
    }

    /**
     * Función para multiplicar vectores
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return VectorArit o Error
     */
    private Object MultiplicacionVectores(VectorArit vector_izq, VectorArit vector_der) {
        LinkedList<Object> l = new LinkedList<>();
        if (null == vector_izq.getTipo_dato()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede multiplicar " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
        } else //ENTERO
        {
            switch (vector_izq.getTipo_dato()) {
                case INTEGER:
                    switch (vector_der.getTipo_dato()) {
                        //ENTERO * ENTERO
                        case INTEGER: {
                            //VECTOR[1] * VECTOR[1]
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Integer) vector_izq.Acceder(0) * (Integer) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Integer) ((ValorArit) e).getVal() * (Integer) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Integer) vector_izq.Acceder(i) * (Integer) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mult: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.INTEGER, l);
                            //return new VectorArit(TipoPrimitivo.INTEGER, l);
                        }
                        //ENTERO * DOUBLE
                        case NUMERIC: {
                            //VECTOR[1] * VECTOR[1]
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Integer) vector_izq.Acceder(0) * (Double) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Integer) ((ValorArit) e).getVal() * (Double) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Integer) vector_izq.Acceder(i) * (Double) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mult: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                            //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede multiplicar integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case NUMERIC:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE * DOUBLE
                        case NUMERIC:
                            //l.add((Double) vector_izq.Acceder(0) * (Double) vector_der.Acceder(0));
                            //VECTOR[1] * VECTOR[1]
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Double) vector_izq.Acceder(0) * (Double) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Double) ((ValorArit) e).getVal() * (Double) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Double) vector_izq.Acceder(i) * (Double) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mult: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE * ENTERO
                        case INTEGER:
                            //l.add((Double) vector_izq.Acceder(0) * (Integer) vector_der.Acceder(0));
                            //VECTOR[1] * VECTOR[1]
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Double) vector_izq.Acceder(0) * (Integer) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Double) ((ValorArit) e).getVal() * (Integer) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Double) vector_izq.Acceder(i) * (Integer) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Mult: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede multiplicar double con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }


                default:
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede multiplicar " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
            }
        }
    }

    /**
     * Función para calcular el modulo vector de tamanio uno
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return Vector de tamanio uno
     */
    private Object ModuloVectoresBase(VectorArit vector_izq, VectorArit vector_der) {
        LinkedList<Object> l = new LinkedList<>();
        if (null == vector_izq.getTipo_dato()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular modulo " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
        } else //ENTERO
        {
            switch (vector_izq.getTipo_dato()) {
                case INTEGER:
                    switch (vector_der.getTipo_dato()) {
                        //ENTERO %% ENTERO
                        case INTEGER: {
                            //l.add((Integer) vector_izq.Acceder(0) % (Integer) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Integer) vector_izq.Acceder(0) % (Integer) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Integer) ((ValorArit) e).getVal() % (Integer) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Integer) vector_izq.Acceder(i) % (Integer) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Modulo: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.INTEGER, l);
                            //return new VectorArit(TipoPrimitivo.INTEGER, l);
                        }
                        //ENTERO %% DOUBLE
                        case NUMERIC: {
                            //l.add((Integer) vector_izq.Acceder(0) % (Double) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Integer) vector_izq.Acceder(0) % (Double) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Integer) ((ValorArit) e).getVal() % (Double) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Integer) vector_izq.Acceder(i) % (Double) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Modulo: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                            //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular modulo integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case NUMERIC:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE %% DOUBLE
                        case NUMERIC:
                            //l.add((Double) vector_izq.Acceder(0) % (Double) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Double) vector_izq.Acceder(0) % (Double) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Double) ((ValorArit) e).getVal() % (Double) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Double) vector_izq.Acceder(i) % (Double) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Modulo: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE %% ENTERO
                        case INTEGER:
                            //l.add((Double) vector_izq.Acceder(0) % (Integer) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Double) vector_izq.Acceder(0) % (Integer) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Double) ((ValorArit) e).getVal() % (Integer) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Double) vector_izq.Acceder(i) % (Integer) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Modulo: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular modulo double con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                default:
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular modulo " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
            }
        }
    }

    /**
     * Función para dividir vectores
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return VectorArit
     */
    private Object DivisionVectores(VectorArit vector_izq, VectorArit vector_der) {
        LinkedList<Object> l = new LinkedList<>();
        if (null == vector_izq.getTipo_dato()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede dividir " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
        } else //ENTERO
        {
            switch (vector_izq.getTipo_dato()) {
                case INTEGER:
                    switch (vector_der.getTipo_dato()) {
                        //ENTERO / ENTERO
                        case INTEGER: {
                            //l.add((Integer) vector_izq.Acceder(0) / (Integer) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Integer) vector_izq.Acceder(0) / (Integer) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Integer) ((ValorArit) e).getVal() / (Integer) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Integer) vector_izq.Acceder(i) / (Integer) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "División: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.INTEGER, l);
                            //return new VectorArit(TipoPrimitivo.INTEGER, l);
                        }
                        //ENTERO / DOUBLE
                        case NUMERIC: {
                            //l.add((Integer) vector_izq.Acceder(0) / (Double) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Integer) vector_izq.Acceder(0) / (Double) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Integer) ((ValorArit) e).getVal() / (Double) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Integer) vector_izq.Acceder(i) / (Double) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "División: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                            //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede dividir integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case NUMERIC:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE / DOUBLE
                        case NUMERIC:
                            //l.add((Double) vector_izq.Acceder(0) / (Double) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Double) vector_izq.Acceder(0) / (Double) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Double) ((ValorArit) e).getVal() / (Double) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Double) vector_izq.Acceder(i) / (Double) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "División: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE / ENTERO
                        case INTEGER:
                            //l.add((Double) vector_izq.Acceder(0) / (Integer) vector_der.Acceder(0));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add((Double) vector_izq.Acceder(0) / (Integer) ((ValorArit) e).getVal());
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add((Double) ((ValorArit) e).getVal() / (Integer) vector_der.Acceder(0));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add((Double) vector_izq.Acceder(i) / (Integer) vector_der.Acceder(i));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "División: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede dividir double con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }


                default:
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede dividir " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
            }
        }
    }

    /**
     * Función para calcular la potencia vectores
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return VectorArit
     */
    private Object PotenciaVectores(VectorArit vector_izq, VectorArit vector_der) {
        LinkedList<Object> l = new LinkedList<>();
        if (null == vector_izq.getTipo_dato()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular la potencia " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
        } else //ENTERO
        {
            switch (vector_izq.getTipo_dato()) {
                case INTEGER:
                    switch (vector_der.getTipo_dato()) {
                        //ENTERO ^ ENTERO
                        case INTEGER: {
                            //l.add(Math.pow((Integer) vector_izq.Acceder(0), (Integer) vector_der.Acceder(0)));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add(Math.pow((Integer) vector_izq.Acceder(0), (Integer) ((ValorArit) e).getVal()));
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add(Math.pow((Integer) ((ValorArit) e).getVal(), (Integer) vector_der.Acceder(0)));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add(Math.pow((Integer) vector_izq.Acceder(i), (Integer) vector_der.Acceder(i)));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Potencia: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                            //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        //ENTERO ^ DOUBLE
                        case NUMERIC: {
                            //l.add(Math.pow((Integer) vector_izq.Acceder(0), (Double) vector_der.Acceder(0)));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add(Math.pow((Integer) vector_izq.Acceder(0), (Double) ((ValorArit) e).getVal()));
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add(Math.pow((Integer) ((ValorArit) e).getVal(), (Double) vector_der.Acceder(0)));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add(Math.pow((Integer) vector_izq.Acceder(i), (Double) vector_der.Acceder(i)));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Potencia: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                            //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular mla potencia integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case NUMERIC:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE ^ DOUBLE
                        case NUMERIC:
                            //l.add(Math.pow((Double) vector_izq.Acceder(0), (Double) vector_der.Acceder(0)));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add(Math.pow((Double) vector_izq.Acceder(0), (Double) ((ValorArit) e).getVal()));
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add(Math.pow((Double) ((ValorArit) e).getVal(), (Double) vector_der.Acceder(0)));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add(Math.pow((Double) vector_izq.Acceder(i), (Double) vector_der.Acceder(i)));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Potencia: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE ^ ENTERO
                        case INTEGER:
                            //l.add(Math.pow((Double) vector_izq.Acceder(0), (Integer) vector_der.Acceder(0)));
                            if (this.UnoVsN(vector_izq, vector_der)) {//VECTOR[1] * V[n] || V[n] * V[1]
                                if (vector_izq.getTamanio() == 1) {
                                    for (Object e : vector_der.getValores()) {
                                        l.add(Math.pow((Double) vector_izq.Acceder(0), (Integer) ((ValorArit) e).getVal()));
                                    }
                                } else {
                                    for (Object e : vector_izq.getValores()) {
                                        l.add(Math.pow((Double) ((ValorArit) e).getVal(), (Integer) vector_der.Acceder(0)));
                                    }
                                }
                            } else if (this.NvsN(vector_izq, vector_der)) {
                                for (int i = 0; i < vector_izq.getTamanio(); i++) {
                                    l.add(Math.pow((Double) vector_izq.Acceder(i), (Integer) vector_der.Acceder(i)));
                                }
                            } else {
                                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Potencia: Hay problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                            }
                            return this.GenResultado(vector_izq, vector_der, TipoPrimitivo.NUMERIC, l);
                        //return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular mla potencia double con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }


                default:
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular mla potencia " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
            }
        }
    }

    /**
     * Hace negativo el vector
     *
     * @param vector Vector a volver negativo
     * @return VectorArit o Error
     */
    private Object NegativoVectores(VectorArit vector) {
        LinkedList<Object> l = new LinkedList<>();
        switch (vector.getTipo_dato()) {
            case NUMERIC:
                if (vector.getTamanio() == 1) {
                    l.add(-1 * (Double) vector.Acceder(0));
                } else {
                    for (Object e : vector.getValores()) {
                        l.add(-1 * (Double) ((ValorArit) e).getVal());
                    }
                }
                return this.GenResultado(vector, l);

            case INTEGER:
                if (vector.getTamanio() == 1) {
                    l.add(-1 * (Integer) vector.Acceder(0));
                } else {
                    for (Object e : vector.getValores()) {
                        l.add(-1 * (Integer) ((ValorArit) e).getVal());
                    }
                }
                return this.GenResultado(vector, l);

            default:
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede volver negativo " + vector.getTipo_dato(), this.getFila(), this.getColumna());
        }
    }

}
