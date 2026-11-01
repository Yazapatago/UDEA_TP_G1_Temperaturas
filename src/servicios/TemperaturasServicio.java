package servicios;

import modelos.Temperatura;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemperaturasServicio {

    public static List<Temperatura> getDatos(String nombreArchivo) {
        var formatoFecha = DateTimeFormatter.ofPattern("d/M/yyyy");
        try {
            Stream<String> lineasArchivo = Files.lines(Paths.get(nombreArchivo));
            return lineasArchivo.skip(1)
                    .map(linea -> linea.split(","))
                    .map(textos -> new Temperatura(textos[0],
                            LocalDate.parse(textos[1], formatoFecha),
                            Double.parseDouble(textos[2])
                    ))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static List<Temperatura> filtrar(List<Temperatura> temperaturas,
                                            LocalDate desde, LocalDate hasta) {
        return temperaturas.stream()
                .filter(temperatura -> !temperatura.getFecha().isBefore(desde) &&
                        !temperatura.getFecha().isAfter(hasta)
                )
                .collect(Collectors.toList());
    }

    public static List<Temperatura> filtrar(List<Temperatura> temperaturas, LocalDate fecha) {
        return temperaturas.stream()
                .filter(temperatura -> temperatura.getFecha().equals(fecha))
                .collect(Collectors.toList());
    }

    public static Map<String, Double> getPromediosPorCiudad(List<Temperatura> temperaturas) {
        return temperaturas.stream()
                .collect(Collectors.groupingBy(Temperatura::getCiudad,
                        Collectors.averagingDouble(Temperatura::getTemperatura)));
    }

    public static Temperatura getMasCalurosa(List<Temperatura> temperaturas) {
        return temperaturas.stream()
                .max(Comparator.comparingDouble(Temperatura::getTemperatura))
                .orElse(null);
    }

    public static Temperatura getMenosCalurosa(List<Temperatura> temperaturas) {
        return temperaturas.stream()
                .min(Comparator.comparingDouble(Temperatura::getTemperatura))
                .orElse(null);
    }
}
