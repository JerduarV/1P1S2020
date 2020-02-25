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

                //NEGATIVO DE UN VECTOR DE TAMANIO 1
                if (vector_unico.getTamanio() == 1) {
                    return NegativoVectoresBase(vector_unico);
                }
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

                //SUMA DE DOS VECTORES DE TAMANIO 1
                if (vector_izq.getTamanio() == 1 && vector_der.getTamanio() == 1) {
                    return this.OperacionesBasicas(vector_izq, vector_der);
                }
            }
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Realiza las opearciones artiméticas con los vectores de tamanio 1
     *
     * @param izq operador izquierdo
     * @param der operador derecho
     * @return Vector con resultado
     */
    private Object OperacionesBasicas(VectorArit izq, VectorArit der) {
        switch (this.getTipo()) {
            case SUMA:
                return this.SumarVectoresBase(izq, der);
            case RESTA:
                return this.RestaVectoresBase(izq, der);
            case MULT:
                return this.MultiplicacionVectoresBase(izq, der);
            case DIV:
                return this.DivisionVectoresBase(izq, der);
            case MOD:
                return this.ModuloVectoresBase(izq, der);
            case POT:
                return this.PotenciaVectoresBase(izq, der);
            default:
                System.out.println("NO SE QUE MIERDA HACES EN LA CLASE EXPARITMETICA");
                return null;
        }
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Función para sumar vector de tamanio uno
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return Vector de tamanio uno
     */
    private Object SumarVectoresBase(VectorArit vector_izq, VectorArit vector_der) {
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
                            l.add((Integer) vector_izq.Acceder(0) + (Integer) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.INTEGER, l);
                        }
                        //ENTERO + DOUBLE
                        case DOUBLE: {
                            l.add((Integer) vector_izq.Acceder(0) + (Double) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        //ENTERO + CADENA
                        case STRING:
                            l.add(vector_izq.Acceder(0).toString() + vector_der.Acceder(0).toString());
                            return new VectorArit(TipoPrimitivo.STRING, l);

                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede sumar integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case DOUBLE:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE + DOUBLE
                        case DOUBLE:
                            l.add((Double) vector_izq.Acceder(0) + (Double) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE + ENTERO
                        case INTEGER:
                            l.add((Double) vector_izq.Acceder(0) + (Integer) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE + CADENA
                        case STRING:
                            l.add(vector_izq.Acceder(0).toString() + vector_der.Acceder(0).toString());
                            return new VectorArit(TipoPrimitivo.STRING, l);

                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede sumar double con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case BOOL:
                    switch (vector_der.getTipo_dato()) {
                        //BOOL + STRING
                        case STRING:
                            l.add(vector_izq.Acceder(0).toString() + vector_der.Acceder(0).toString());
                            return new VectorArit(TipoPrimitivo.STRING, l);

                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede sumar bool con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }
                case STRING:
                    //CADENA + CUALQUIER OTRO
                    l.add(vector_izq.Acceder(0).toString() + vector_der.Acceder(0).toString());
                    return new VectorArit(TipoPrimitivo.STRING, l);

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
    private Object RestaVectoresBase(VectorArit vector_izq, VectorArit vector_der) {
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
                            l.add((Integer) vector_izq.Acceder(0) - (Integer) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.INTEGER, l);
                        }
                        //ENTERO - DOUBLE
                        case DOUBLE: {
                            l.add((Integer) vector_izq.Acceder(0) - (Double) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede restar integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case DOUBLE:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE - DOUBLE
                        case DOUBLE:
                            l.add((Double) vector_izq.Acceder(0) - (Double) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE - ENTERO
                        case INTEGER:
                            l.add((Double) vector_izq.Acceder(0) - (Integer) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede restar double con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                default:
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede restar " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
            }
        }
    }

    /**
     * Función para multiplicar vector de tamanio uno
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return Vector de tamanio uno
     */
    private Object MultiplicacionVectoresBase(VectorArit vector_izq, VectorArit vector_der) {
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
                            l.add((Integer) vector_izq.Acceder(0) * (Integer) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.INTEGER, l);
                        }
                        //ENTERO * DOUBLE
                        case DOUBLE: {
                            l.add((Integer) vector_izq.Acceder(0) * (Double) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede multiplicar integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case DOUBLE:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE * DOUBLE
                        case DOUBLE:
                            l.add((Double) vector_izq.Acceder(0) * (Double) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE * ENTERO
                        case INTEGER:
                            l.add((Double) vector_izq.Acceder(0) * (Integer) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
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
                            l.add((Integer) vector_izq.Acceder(0) % (Integer) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.INTEGER, l);
                        }
                        //ENTERO %% DOUBLE
                        case DOUBLE: {
                            l.add((Integer) vector_izq.Acceder(0) % (Double) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular modulo integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case DOUBLE:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE %% DOUBLE
                        case DOUBLE:
                            l.add((Double) vector_izq.Acceder(0) % (Double) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE %% ENTERO
                        case INTEGER:
                            l.add((Double) vector_izq.Acceder(0) % (Integer) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular modulo double con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                default:
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular modulo " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
            }
        }
    }

    /**
     * Función para multiplicar vector de tamanio uno
     *
     * @param vector_izq operador izquierdo
     * @param vector_der operador derecho
     * @return Vector de tamanio uno
     */
    private Object DivisionVectoresBase(VectorArit vector_izq, VectorArit vector_der) {
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
                            l.add((Integer) vector_izq.Acceder(0) / (Integer) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.INTEGER, l);
                        }
                        //ENTERO / DOUBLE
                        case DOUBLE: {
                            l.add((Integer) vector_izq.Acceder(0) / (Double) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede dividir integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case DOUBLE:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE / DOUBLE
                        case DOUBLE:
                            l.add((Double) vector_izq.Acceder(0) / (Double) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE / ENTERO
                        case INTEGER:
                            l.add((Double) vector_izq.Acceder(0) / (Integer) vector_der.Acceder(0));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede dividir double con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                default:
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede dividir " + vector_izq.getTipo_dato() + " con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
            }
        }
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
                case INTEGER:
                    switch (vector_der.getTipo_dato()) {
                        //ENTERO ^ ENTERO
                        case INTEGER: {
                            l.add(Math.pow((Integer) vector_izq.Acceder(0), (Integer) vector_der.Acceder(0)));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        //ENTERO ^ DOUBLE
                        case DOUBLE: {
                            l.add(Math.pow((Integer) vector_izq.Acceder(0), (Double) vector_der.Acceder(0)));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        }
                        default:
                            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede calcular mla potencia integer con " + vector_der.getTipo_dato(), this.getFila(), this.getColumna());
                    }

                case DOUBLE:
                    switch (vector_der.getTipo_dato()) {
                        //DOUBLE ^ DOUBLE
                        case DOUBLE:
                            l.add(Math.pow((Double) vector_izq.Acceder(0), (Double) vector_der.Acceder(0)));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
                        //DOUBLE ^ ENTERO
                        case INTEGER:
                            l.add(Math.pow((Double) vector_izq.Acceder(0), (Integer) vector_der.Acceder(0)));
                            return new VectorArit(TipoPrimitivo.DOUBLE, l);
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
     * @param vector
     * @return
     */
    private Object NegativoVectoresBase(VectorArit vector) {
        LinkedList<Object> l = new LinkedList<>();
        switch (vector.getTipo_dato()) {
            case DOUBLE:
                l.add(-1 * (Integer) vector.Acceder(0));
                return new VectorArit(TipoPrimitivo.DOUBLE, l);

            case INTEGER:
                l.add(-1 * (Integer) vector.Acceder(0));
                return new VectorArit(TipoPrimitivo.INTEGER, l);

            default:
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede volver negativo " + vector.getTipo_dato(), this.getFila(), this.getColumna());
        }
    }
}
