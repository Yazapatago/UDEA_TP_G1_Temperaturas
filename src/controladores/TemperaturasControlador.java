package controladores;

import modelos.Temperatura;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import servicios.TemperaturasServicio;
import vistas.TemperaturasVista;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TemperaturasControlador {

    private final TemperaturasVista vista;
    private List<Temperatura> temperaturas;

    public TemperaturasControlador(TemperaturasVista vista) {
        this.vista = vista;

        this.vista.setGraficarClick(evento -> graficar());
        this.vista.setConsultarExtremosClick(evento -> consultarExtremos());
        cargarDatos();
    }

    private void cargarDatos() {
        String nombreArchivo = System.getProperty("user.dir") + "/src/datos/Temperaturas.csv";
        temperaturas = TemperaturasServicio.getDatos(nombreArchivo);
    }

    private void graficar() {
        LocalDate desde = vista.getFechaDesde();
        LocalDate hasta = vista.getFechaHasta();

        var datosFiltrados = TemperaturasServicio.filtrar(temperaturas, desde, hasta);
        var promedios = TemperaturasServicio.getPromediosPorCiudad(datosFiltrados);

        DefaultCategoryDataset datosGrafica = new DefaultCategoryDataset();
        promedios.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(promedio -> datosGrafica.addValue(promedio.getValue(),
                        "Temperatura promedio", promedio.getKey()));

        JFreeChart graficador = ChartFactory.createBarChart(
                "Temperatura promedio por ciudad",
                "Ciudad",
                "Temperatura promedio (°C)",
                datosGrafica
        );

        ChartPanel pnlGraficador = new ChartPanel(graficador);
        pnlGraficador.setPreferredSize(new Dimension(500, 270));
        vista.graficar(pnlGraficador);
        vista.activarPestaña(0);
    }

    private void consultarExtremos() {
        LocalDate fecha = vista.getFecha();
        var temperaturasFecha = TemperaturasServicio.filtrar(temperaturas, fecha);

        if (temperaturasFecha.isEmpty()) {
            vista.mostrarSinDatos(fecha);
        } else {
            vista.mostrarExtremos(fecha,
                    TemperaturasServicio.getMasCalurosa(temperaturasFecha),
                    TemperaturasServicio.getMenosCalurosa(temperaturasFecha));
        }
        vista.activarPestaña(1);
    }
}
