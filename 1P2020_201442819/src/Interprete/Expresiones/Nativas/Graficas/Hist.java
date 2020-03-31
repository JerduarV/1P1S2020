/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Nativas.Graficas;

import Editor.VentanaEditor;
import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.CallFun;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

/**
 * Clase para la llamada a la función nativa Hist
 *
 * @author Jerduar
 */
public class Hist extends CallFun {

    private static int i = 0;

    /**
     * Constructor de la clase para la llamada a la función hist
     *
     * @param p Lista de parámetros actuales
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public Hist(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("Hist", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 3) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Hist: Se esperaban 3 parámetros", this.getFila(), this.getColumna());
        }

        Object param1 = this.getParam_act().getFirst().Resolver(t),
                param2 = this.getParam_act().get(1).Resolver(t),
                param3 = this.getParam_act().get(2).Resolver(t);

        if (param1 instanceof ErrorCompi || param2 instanceof ErrorCompi || param3 instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Hist: Error en los parámetros", this.getFila(), this.getColumna());
        }
        if (!(param1 instanceof VectorArit && param2 instanceof VectorArit && param3 instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Hist: Se esperaban 3 vectores primitivos", this.getFila(), this.getColumna());
        }
        VectorArit datos = (VectorArit) param1,
                lb = (VectorArit) param2,
                titulo = (VectorArit) param3;

        if (!datos.isNumerico() || !(lb.isString() && titulo.isString())) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Hist: Se esperaban un vector numerico y dos strings", this.getFila(), this.getColumna());
        }

        double[] data = new double[datos.getTamanio()];
        for (int y = 0; y < data.length; y++) {
            data[y] = datos.isInteger() ? (Integer) datos.Acceder(y) : (Double) datos.Acceder(y);
        }

        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        dataset.addSeries("Hist", data,(int)Math.sqrt(data.length));

        JFreeChart chart = ChartFactory.createHistogram(titulo.Acceder(0).toString(), lb.Acceder(0).toString(), "", dataset, PlotOrientation.VERTICAL, false, true, true);
        int width = 640, height = 480;
        File pie = new File("Graficas\\Hist_" + titulo.Acceder(0).toString() + i + ".jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(pie, chart, width, height);
        } catch (IOException ex) {
            Logger.getLogger(Hist.class.getName()).log(Level.SEVERE, null, ex);
        }
        VentanaEditor.InsertarGrafica("Graficas\\Hist_" + titulo.Acceder(0).toString() + i + ".jpeg", titulo.Acceder(0).toString());
        i++;
        return new VectorArit(TipoPrimitivo.STRING, "null");
    }

}
