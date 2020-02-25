/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.VectorArit;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 *
 * @author Jerduar
 */
public class AccesoGet extends Expresion {

    /**
     * Identificador de la variable a accesar
     */
    private final Identificador id;

    /**
     * Lista de indices para acceder a la estructura
     */
    private final LinkedList<Indice> lista_index;

    /**
     * Constructor de un acceso para obtener un valor de la estructura
     *
     * @param id Identificador de la variable
     * @param l Lista de índices
     */
    public AccesoGet(Identificador id, LinkedList<Indice> l) {
        super(id.getFila(), id.getColumna());
        this.id = id;
        this.lista_index = l;
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        Object o = id.Resolver(t);

        //SI NO SE ECONTRÓ LA VARIABLE SE RETORNA EL ERROR
        if (o instanceof ErrorCompi) {
            return o;
        }

        if (o instanceof VectorArit) {
            return AccesoGetVector((VectorArit) o, t);
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Identificador getId() {
        return id;
    }

    public LinkedList<Indice> getLista_index() {
        return lista_index;
    }

    /**
     * Acceso a un vector primitivo
     *
     * @param vector Vector a acceder
     * @param t Tabla de símbolos
     * @return Vector con resultado
     */
    private Object AccesoGetVector(VectorArit vector, TablaSimbolos t) {
        if (this.lista_index.size() > 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede acceder más de una vez al vector", this.getFila(), this.getColumna());
        }

        if (!this.lista_index.getFirst().isSimple()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "En los vectores solo se pueden hacer accesos simples", this.getFila(), this.getColumna());
        }

        Object i = this.lista_index.getFirst().getExp().Resolver(t);

        if (i instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Hubo un error en los indices", this.getFila(), this.getColumna());
        }
        VectorArit index;

        if (i instanceof VectorArit) {
            index = (VectorArit) i;
        } else {
            throw new UnsupportedOperationException("No he validado que pasa cuando vienen otras cosas diferentes a vectores en el indice -> ACCESO GET");
        }

        if (!index.isInteger()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Se esperaba un entero en el índice", this.getFila(), this.getColumna());
        }

        LinkedList<Object> l = new LinkedList<>();
        Integer y = (Integer) index.Acceder(0);

        if (y < 1 || y > vector.getTamanio()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indice fuera de rango", this.getFila(), this.getColumna());
        }

        l.add(vector.Acceder(y - 1));
        return new VectorArit(vector.getTipo_dato(), l);
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}