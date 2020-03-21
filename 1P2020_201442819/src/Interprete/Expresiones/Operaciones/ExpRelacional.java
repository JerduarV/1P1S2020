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
import java.util.LinkedList;
import java.util.Objects;

/**
 * Clase que maneja las opearciones relacionales en el lenguaje
 *
 * @author Jerduar
 */
public class ExpRelacional extends Operacion {

    /**
     * Constructor de las expresiones relacionales, solo pueden ser operaciones
     * binarias
     *
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     * @param izq Operador izquierdo
     * @param der Operador derecho
     * @param o Tipo de la operación
     */
    public ExpRelacional(Integer fila, Integer col, Expresion izq, Expresion der, TipoOpe o) {
        super(fila, col, izq, der, o);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {

        Object val_izq = this.getOp_izq().Resolver(t);
        Object val_der = this.getOp_der().Resolver(t);

        //SI ALGUNA OPERACIÓN DEVUELVE UN ERROR SE RETORNA EL MISMO
        if (val_izq instanceof ErrorCompi || val_der instanceof ErrorCompi) {
            return val_izq instanceof ErrorCompi ? val_izq : val_der;
        }

        //COMPARACION DE DOS VECTORES
        if (val_izq instanceof VectorArit && val_der instanceof VectorArit) {
            VectorArit vector_izq = (VectorArit) val_izq;
            VectorArit vector_der = (VectorArit) val_der;
            return this.ComparacionVectores(vector_izq, vector_der);
        }
        throw new UnsupportedOperationException("Solo aguanto comparación de vectores"); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Función que opera los vectores de tamaño uno
     *
     * @param izq Operador Izquierdo
     * @param der Operador Derecho
     * @return Vector con resultado o ErrorCompi
     */
    private Object ComparacionVectores(VectorArit izq, VectorArit der) {
        LinkedList<Object> l = new LinkedList<>();
        if (izq.isNumerico() && der.isNumerico()) {
            switch (this.getTipo()) {
                case MAYOR:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) > (der.isInteger() ? (Integer) e : (Double) e));
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                l.add((izq.isInteger() ? (Integer) e : (Double) e) > (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            l.add((izq.isInteger() ? (Integer) izq.Acceder(i) : (Double) izq.Acceder(i)) > (der.isInteger() ? (Integer) der.Acceder(i) : (Double) der.Acceder(i)));
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "MayorQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                case MENOR:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) < (der.isInteger() ? (Integer) e : (Double) e));
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                l.add((izq.isInteger() ? (Integer) e : (Double) e) < (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            l.add((izq.isInteger() ? (Integer) izq.Acceder(i) : (Double) izq.Acceder(i)) < (der.isInteger() ? (Integer) der.Acceder(i) : (Double) der.Acceder(i)));
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "MenorQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
                case MENORIGUAL:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) <= (der.isInteger() ? (Integer) e : (Double) e));
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                l.add((izq.isInteger() ? (Integer) e : (Double) e) <= (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            l.add((izq.isInteger() ? (Integer) izq.Acceder(i) : (Double) izq.Acceder(i)) <= (der.isInteger() ? (Integer) der.Acceder(i) : (Double) der.Acceder(i)));
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "MenorIgualQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
                case MAYORIGUAL:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) >= (der.isInteger() ? (Integer) e : (Double) e));
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                l.add((izq.isInteger() ? (Integer) e : (Double) e) >= (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            l.add((izq.isInteger() ? (Integer) izq.Acceder(i) : (Double) izq.Acceder(i)) >= (der.isInteger() ? (Integer) der.Acceder(i) : (Double) der.Acceder(i)));
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "MayorIgualQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
                case IGUALQUE:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) == (der.isInteger() ? (Integer) e : (Double) e));
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                l.add((izq.isInteger() ? (Integer) e : (Double) e) == (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            l.add((izq.isInteger() ? (Integer) izq.Acceder(i) : (Double) izq.Acceder(i)) == (der.isInteger() ? (Integer) der.Acceder(i) : (Double) der.Acceder(i)));
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "IgualQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
                default:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                l.add((izq.isInteger() ? (Integer) izq.Acceder(0) : (Double) izq.Acceder(0)) != (der.isInteger() ? (Integer) e : (Double) e));
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                l.add((izq.isInteger() ? (Integer) e : (Double) e) != (der.isInteger() ? (Integer) der.Acceder(0) : (Double) der.Acceder(0)));
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            l.add((izq.isInteger() ? (Integer) izq.Acceder(i) : (Double) izq.Acceder(i)) != (der.isInteger() ? (Integer) der.Acceder(i) : (Double) der.Acceder(i)));
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "DiferenteQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
            }
        } else if (izq.isString() && der.isString()) {
            switch (this.getTipo()) {
                case IGUALQUE:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                l.add(izq.Acceder(0).toString().equals(e.toString()));
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                l.add(e.toString().equals(der.Acceder(0).toString()));
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            l.add(izq.Acceder(i).toString().equals(der.Acceder(i).toString()));
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "IgualQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
                case DIFERENTE:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                l.add(!izq.Acceder(0).toString().equals(e.toString()));
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                l.add(!e.toString().equals(der.Acceder(0).toString()));
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            l.add(!izq.Acceder(i).toString().equals(der.Acceder(i).toString()));
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "DiferenteQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
                case MAYOR:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                int r = izq.Acceder(0).toString().compareTo(e.toString());
                                l.add(r > 0);
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                int r = e.toString().compareTo(der.Acceder(0).toString());
                                l.add(r > 0);
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            int r = izq.Acceder(i).toString().compareTo(der.Acceder(i).toString());
                            l.add(r > 0);
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "MayorQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
                case MENOR:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                int r = izq.Acceder(0).toString().compareTo(e.toString());
                                l.add(r < 0);
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                int r = e.toString().compareTo(der.Acceder(0).toString());
                                l.add(r < 0);
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            int r = izq.Acceder(i).toString().compareTo(der.Acceder(i).toString());
                            l.add(r < 0);
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "MenorQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
                case MAYORIGUAL:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                int r = izq.Acceder(0).toString().compareTo(e.toString());
                                l.add(r >= 0);
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                int r = e.toString().compareTo(der.Acceder(0).toString());
                                l.add(r >= 0);
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            int r = izq.Acceder(i).toString().compareTo(der.Acceder(i).toString());
                            l.add(r >= 0);
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "MayorIgualQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
                default:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                int r = izq.Acceder(0).toString().compareTo(e.toString());
                                l.add(r <= 0);
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                int r = e.toString().compareTo(der.Acceder(0).toString());
                                l.add(r <= 0);
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            int r = izq.Acceder(i).toString().compareTo(der.Acceder(i).toString());
                            l.add(r <= 0);
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "MenorIgualQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
            }
        } else if (izq.isBool() && der.isBool()) {
            switch (this.getTipo()) {
                case IGUALQUE:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                l.add(Objects.equals((Boolean) izq.Acceder(0), e));
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                l.add(Objects.equals((Boolean) e, (Boolean) der.Acceder(0)));
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            l.add(Objects.equals((Boolean) izq.Acceder(i), (Boolean) der.Acceder(i)));
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "IgualQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
                case DIFERENTE:
                    if (this.UnoVsN(izq, der)) {
                        if (izq.getTamanio() == 1) {
                            for (Object e : der.getValores()) {
                                l.add(!Objects.equals((Boolean) izq.Acceder(0), e));
                            }
                        } else {
                            for (Object e : izq.getValores()) {
                                l.add(!Objects.equals((Boolean) e, (Boolean) der.Acceder(0)));
                            }
                        }
                    } else if (this.NvsN(izq, der)) {
                        for (int i = 0; i < izq.getTamanio(); i++) {
                            l.add(!Objects.equals((Boolean) izq.Acceder(i), (Boolean) der.Acceder(i)));
                        }
                    } else {
                        return VentanaErrores.getVenErrores().AgregarError("Semantico", "IgualQue: Problema con el tamaño de los vectores", this.getFila(), this.getColumna());
                    }
                    return this.GenResultado(izq, der, TipoPrimitivo.BOOL, l);
                    //return new VectorArit(TipoPrimitivo.BOOL, l);
                default:
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "Comparación no valida entre booleanos", this.getFila(), this.getColumna());
            }
        } else {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede comparar " + izq.getTipo_dato() + " con " + der.getTipo_dato(), this.getFila(), this.getColumna());
        }

    }

}
