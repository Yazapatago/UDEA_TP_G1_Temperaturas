package vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import datechooser.beans.DateChooserCombo;
import modelos.Temperatura;
import org.jfree.chart.ChartPanel;

public class TemperaturasVista extends JFrame {

    private DateChooserCombo dccDesde, dccHasta, dccFecha;
    private JTabbedPane tpTemperaturas;
    private JPanel pnlGrafica;
    private JPanel pnlExtremos;

    private JButton btnGraficar, btnConsultarExtremos;

    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TemperaturasVista() {

        setTitle("Temperaturas de Ciudades");
        setSize(750, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JToolBar tb = new JToolBar();

        btnGraficar = new JButton();
        btnGraficar.setIcon(new ImageIcon(getClass().getResource("/iconos/Grafica.png")));
        btnGraficar.setToolTipText("Gráfica de temperatura promedio por ciudad en el rango de fechas");
        tb.add(btnGraficar);

        btnConsultarExtremos = new JButton();
        btnConsultarExtremos.setIcon(new ImageIcon(getClass().getResource("/iconos/Datos.png")));
        btnConsultarExtremos.setToolTipText("Ciudad más y menos calurosa en la fecha");
        tb.add(btnConsultarExtremos);

        JPanel pnlTemperaturas = new JPanel();
        pnlTemperaturas.setLayout(new BoxLayout(pnlTemperaturas, BoxLayout.Y_AXIS));

        JPanel pnlDatosProceso = new JPanel();
        pnlDatosProceso.setPreferredSize(new Dimension(pnlDatosProceso.getWidth(), 50));
        pnlDatosProceso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlDatosProceso.setLayout(null);

        JLabel lblDesde = new JLabel("Desde");
        lblDesde.setBounds(10, 10, 50, 25);
        pnlDatosProceso.add(lblDesde);

        dccDesde = new DateChooserCombo();
        dccDesde.setBounds(60, 10, 110, 25);
        pnlDatosProceso.add(dccDesde);

        JLabel lblHasta = new JLabel("Hasta");
        lblHasta.setBounds(190, 10, 50, 25);
        pnlDatosProceso.add(lblHasta);

        dccHasta = new DateChooserCombo();
        dccHasta.setBounds(240, 10, 110, 25);
        pnlDatosProceso.add(dccHasta);

        JLabel lblFecha = new JLabel("Fecha a consultar");
        lblFecha.setBounds(380, 10, 130, 25);
        pnlDatosProceso.add(lblFecha);

        dccFecha = new DateChooserCombo();
        dccFecha.setBounds(510, 10, 110, 25);
        pnlDatosProceso.add(dccFecha);

        pnlGrafica = new JPanel();
        JScrollPane spGrafica = new JScrollPane(pnlGrafica);

        pnlExtremos = new JPanel();

        tpTemperaturas = new JTabbedPane();
        tpTemperaturas.addTab("Gráfica", spGrafica);
        tpTemperaturas.addTab("Temperaturas extremas", pnlExtremos);

        pnlTemperaturas.add(pnlDatosProceso);
        pnlTemperaturas.add(tpTemperaturas);

        getContentPane().add(tb, BorderLayout.NORTH);
        getContentPane().add(pnlTemperaturas, BorderLayout.CENTER);

    }

    public LocalDate getFechaDesde() {
        return dccDesde.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getFechaHasta() {
        return dccHasta.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getFecha() {
        return dccFecha.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setGraficarClick(ActionListener evento) {
        btnGraficar.addActionListener(evento);
    }

    public void setConsultarExtremosClick(ActionListener evento) {
        btnConsultarExtremos.addActionListener(evento);
    }

    public void activarPestaña(int posicion) {
        if (posicion >= 0 && posicion < tpTemperaturas.getTabCount()) {
            tpTemperaturas.setSelectedIndex(posicion);
        }
    }

    public void graficar(ChartPanel pnlGraficador) {
        pnlGrafica.removeAll();
        pnlGrafica.setLayout(new BorderLayout());
        pnlGrafica.add(pnlGraficador, BorderLayout.CENTER);
        pnlGrafica.revalidate();
    }

    public void mostrarExtremos(LocalDate fecha, Temperatura masCalurosa, Temperatura menosCalurosa) {
        pnlExtremos.removeAll();
        pnlExtremos.setLayout(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        agregarFila(gbc, 0, "Fecha", fecha.format(formatoFecha));
        agregarFila(gbc, 1, "Ciudad más calurosa",
                masCalurosa.getCiudad() + " (" + String.format("%.1f", masCalurosa.getTemperatura()) + " °C)");
        agregarFila(gbc, 2, "Ciudad menos calurosa",
                menosCalurosa.getCiudad() + " (" + String.format("%.1f", menosCalurosa.getTemperatura()) + " °C)");

        pnlExtremos.revalidate();
        pnlExtremos.repaint();
    }

    public void mostrarSinDatos(LocalDate fecha) {
        pnlExtremos.removeAll();
        pnlExtremos.setLayout(new GridBagLayout());
        pnlExtremos.add(new JLabel("No hay temperaturas registradas para la fecha " + fecha.format(formatoFecha)));
        pnlExtremos.revalidate();
        pnlExtremos.repaint();
    }

    private void agregarFila(GridBagConstraints gbc, int fila, String titulo, String valor) {
        gbc.gridy = fila;
        gbc.gridx = 0;
        pnlExtremos.add(new JLabel(titulo), gbc);
        gbc.gridx = 1;
        pnlExtremos.add(new JLabel(valor), gbc);
    }
}
