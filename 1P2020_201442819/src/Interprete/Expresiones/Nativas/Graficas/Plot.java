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
import Interprete.Expresiones.Colecciones.MatrixArit;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import java.awt.BasicStroke;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Clase que maneja el llamada a la función Plot que realiza una gráfica de
 * línes
 *
 * @author Jerduar
 */
public class Plot extends CallFun {

    private static int i = 0;

    /**
     * Constructor de la llamada a la función plot
     *
     * @param p Lista de parámetros actuales
     * @param fila Fila en la que se encuentra
     * @param col Columana en la que se encuentra
     */
    public Plot(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("Plot", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 5) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Line: Se esperaban 5 parámetros", this.getFila(), this.getColumna());
        }
        Object param1 = this.getParam_act().getFirst().Resolver(t),
                param2 = this.getParam_act().get(1).Resolver(t),
                param3 = this.getParam_act().get(2).Resolver(t),
                param4 = this.getParam_act().get(3).Resolver(t),
                param5 = this.getParam_act().get(4).Resolver(t);

        if (param1 instanceof ErrorCompi || param2 instanceof ErrorCompi || param3 instanceof ErrorCompi || param4 instanceof ErrorCompi || param5 instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Line: Error en los parámetros", this.getFila(), this.getColumna());
        }
        if (!(param1 instanceof MatrixArit && param2 instanceof VectorArit && param3 instanceof VectorArit && param4 instanceof VectorArit || param5 instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Line: Se esperaban 3 vectores primitivos", this.getFila(), this.getColumna());
        }

        VectorArit last_param = (VectorArit) param5;

        if (last_param.isNumerico()) {
            return this.GraficaDispersion(param1, param2, param3, param4, param5);
        }

        VectorArit type = (VectorArit) param2,
                lbx = (VectorArit) param3,
                lby = (VectorArit) param4,
                titulo = (VectorArit) param5,
                datos = (VectorArit) param1;

        if (!datos.isNumerico() || !(type.isString() && lby.isString() && lbx.isString() && titulo.isString())) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Line: Se esperaban un vector numerico y cuatro strings", this.getFila(), this.getColumna());
        }

        String nombre = titulo.Acceder(0).toString(),
                tipo = type.Acceder(0).toString();

        final XYSeries firefox = new XYSeries("Line");
        for (int x = 0; x < datos.getTamanio(); x++) {
            firefox.add(x + 1, datos.isInteger() ? (Integer) datos.Acceder(x) : (Double) datos.Acceder(x));
        }

        XYSeriesCollection dataset = new XYSeriesCollection(firefox);

        switch (tipo.toLowerCase()) {
            case "o":
                this.GraficaO(dataset, nombre, lbx.Acceder(0).toString(), lby.Acceder(0).toString());
                break;
            case "l":
                this.GraficaL(dataset, nombre, lbx.Acceder(0).toString(), lby.Acceder(0).toString());
                break;
            case "p":
                this.GraficaP(dataset, nombre, lbx.Acceder(0).toString(), lby.Acceder(0).toString());
                break;
            default:
                VentanaErrores.getVenErrores().AgregarError("Warning", "Line: El parámetro de tipo no coincide con ninguno conocido", this.getFila(), this.getColumna());
                this.GraficaO(dataset, nombre, lbx.Acceder(0).toString(), lby.Acceder(0).toString());
        }
        return new VectorArit(TipoPrimitivo.STRING, "null");
    }

    /**
     * Función que construye un diagrama de diespersión
     *
     * @param parametros Lista de parámetros necesarios para su construcción
     * @return Null o ErrorCompi
     */
    private Object GraficaDispersion(Object... parametros) {
        VectorArit datos = (VectorArit) parametros[0],
                titulo = (VectorArit) parametros[1],
                lbx = (VectorArit) parametros[2],
                lby = (VectorArit) parametros[3],
                ylim = (VectorArit) parametros[4];

        if (!datos.isNumerico() || !ylim.isNumerico() || !(lby.isString() && lbx.isString() && titulo.isString())) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Plot: Se esperaban un vector numerico y cuatro strings", this.getFila(), this.getColumna());
        }

        if (ylim.getTamanio() != 2) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Plot: El límete debe tener dos valores", this.getFila(), this.getColumna());
        }

        Double lim_inferios = ylim.isInteger() ? (Integer) ylim.Acceder(0) : (Double) ylim.Acceder(0),
                lim_superior = ylim.isInteger() ? (Integer) ylim.Acceder(1) : (Double) ylim.Acceder(1);

        if (lim_inferios > lim_superior || Objects.equals(lim_inferios, lim_superior)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Plot: Error en los límites", this.getFila(), this.getColumna());
        }

        final XYSeries firefox = new XYSeries("Line");
        for (int x = 0; x < datos.getTamanio(); x++) {
            Double dato = datos.isInteger() ? (Integer) datos.Acceder(x) : (Double) datos.Acceder(x);
            if (dato >= lim_inferios && dato <= lim_superior) {
                firefox.add(x + 1, dato);
            }
        }

        String nombre = titulo.Acceder(0).toString();
        XYDataset dataset = new XYSeriesCollection(firefox);

        JFreeChart chart = ChartFactory.createScatterPlot(nombre, lbx.Acceder(0).toString(), lby.Acceder(0).toString(), dataset, PlotOrientation.VERTICAL, true, true, true);
        int width = 640, height = 480;
        File LineChart = new File("Graficas\\PLOT_1" + nombre + i + ".jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(LineChart, chart, width, height);
        } catch (IOException ex) {
            Logger.getLogger(Plot.class.getName()).log(Level.SEVERE, null, ex);
        }
        VentanaEditor.InsertarGrafica("Graficas\\PLOT_1" + nombre + i + ".jpeg", nombre);
        i++;
        return null;
    }

    /**
     * Método que dibuja una gráfica de Lineas sin puntos
     *
     * @param dataset Conjunto de datos
     * @param titulo Título de la gráfica
     * @param lbx Etiqueta para el eje X
     * @param lby Etiqueta para el eje Y
     */
    private void GraficaL(XYSeriesCollection dataset, String titulo, String lbx, String lby) {
        JFreeChart chart = ChartFactory.createXYLineChart(titulo, lbx, lby, dataset, PlotOrientation.VERTICAL, false, false, false);
        int width = 640, height = 480;
        File LineChart = new File("Graficas\\PLOT_2" + titulo + i + ".jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(LineChart, chart, width, height);
        } catch (IOException ex) {
            Logger.getLogger(Plot.class.getName()).log(Level.SEVERE, null, ex);
        }
        VentanaEditor.InsertarGrafica("Graficas\\PLOT_2" + titulo + i + ".jpeg", titulo);
        i++;
    }

    /**
     * Método que dibuja una gráfica de Lineas y Puntos
     *
     * @param dataset Conjunto de datos
     * @param titulo Título de la gráfica
     * @param lbx Etiqueta para el eje X
     * @param lby Etiqueta para el eje Y
     */
    private void GraficaO(XYSeriesCollection dataset, String titulo, String lbx, String lby) {
        JFreeChart chart = ChartFactory.createXYLineChart(titulo, lbx, lby, dataset, PlotOrientation.VERTICAL, false, false, false);

        final XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        int width = 640, height = 480;
        File LineChart = new File("Graficas\\PLOT_3" + titulo + i + ".jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(LineChart, chart, width, height);
        } catch (IOException ex) {
            Logger.getLogger(Plot.class.getName()).log(Level.SEVERE, null, ex);
        }
        VentanaEditor.InsertarGrafica("Graficas\\PLOT_3" + titulo + i + ".jpeg", titulo);
        i++;
    }

    /**
     * Método que dibuja una gráfica de Puntos
     *
     * @param dataset Conjunto de datos
     * @param titulo Título de la gráfica
     * @param lbx Etiqueta para el eje X
     * @param lby Etiqueta para el eje Y
     */
    private void GraficaP(XYSeriesCollection dataset, String titulo, String lbx, String lby) {
        JFreeChart chart = ChartFactory.createXYLineChart(titulo, lbx, lby, dataset, PlotOrientation.VERTICAL, false, false, false);

        final XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(0.0f));
        plot.setRenderer(renderer);

        int width = 640, height = 480;
        File LineChart = new File("Graficas\\PLOT_4" + titulo + i + ".jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(LineChart, chart, width, height);
        } catch (IOException ex) {
            Logger.getLogger(Plot.class.getName()).log(Level.SEVERE, null, ex);
        }
        VentanaEditor.InsertarGrafica("Graficas\\PLOT_4" + titulo + i + ".jpeg", titulo);
        i++;
    }

}
