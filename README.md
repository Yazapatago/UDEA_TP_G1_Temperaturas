# Examen 3 - Temperaturas de Ciudades

Aplicación en Java con Swing que carga desde un archivo CSV las temperaturas
diarias registradas en distintas ciudades del país y permite:

* Seleccionar un rango de fechas y ver una gráfica de barras con la temperatura
  promedio de cada ciudad que tenga datos en ese rango.
* Seleccionar una fecha y ver cuál fue la ciudad más calurosa y la menos calurosa.

Toda la información de las colecciones se procesa bajo el paradigma funcional.

El proyecto parte del ejercicio de Cambio de Monedas trabajado en clase y conserva
su misma estructura y su misma forma de construir la interfaz.

## Cómo ejecutar

La clase principal es `src/App.java`. Se abre la carpeta del proyecto en el editor
y se ejecuta `App.main()` desde la raíz del proyecto, porque el archivo de datos se
busca en `src/datos/Temperaturas.csv` a partir de esa carpeta.

## Librerías

Las librerías van en la carpeta `lib` y deben agregarse a las dependencias del
proyecto:

* `DateChooser.jar`: selector de fechas.
* `jfreechart-1.5.4.jar`: generación de la gráfica.
* `jcommon-1.0.24.jar`: dependencia de JFreeChart.

El `.gitignore` excluye los archivos `.jar`, por lo que no se suben al repositorio.

## Estructura

```
src/
├── App.java                                clase principal
├── modelos/
│   └── Temperatura.java                    ciudad, fecha y temperatura de un registro
├── servicios/
│   └── TemperaturasServicio.java           carga y procesamiento funcional de los datos
├── controladores/
│   └── TemperaturasControlador.java
├── vistas/
│   └── TemperaturasVista.java
├── iconos/
│   ├── Grafica.png
│   └── Datos.png
└── datos/
    └── Temperaturas.csv
```

## Uso

1. Elegir las fechas **Desde** y **Hasta** y presionar el botón de la gráfica. Se
   muestra la pestaña **Gráfica** con una barra por cada ciudad.
2. Elegir la **Fecha a consultar** y presionar el segundo botón. Se muestra la
   pestaña **Temperaturas extremas** con la ciudad más calurosa y la menos
   calurosa. Si la fecha no tiene registros, se informa en la misma pestaña.

El archivo de datos tiene registros del 01/01/2024 al 10/01/2024.

## Cómo se cumple cada requisito

**Cargar los datos desde el archivo CSV.** `TemperaturasServicio.getDatos` lee las
líneas del archivo, omite el encabezado con `skip`, separa cada línea con `map` y
crea los objetos `Temperatura` antes de recolectarlos con `collect`.

**Gráfica de barras con el promedio por ciudad.** `filtrar` deja solo los registros
del rango de fechas con `filter`. `getPromediosPorCiudad` agrupa los registros por
ciudad y calcula el promedio con `Collectors.groupingBy` y
`Collectors.averagingDouble`. El controlador ordena las ciudades con `sorted` y
llena los datos de la gráfica con `forEach`, y la gráfica se construye con
`ChartFactory.createBarChart`.

**Ciudad más calurosa y menos calurosa.** `filtrar` deja solo los registros de la
fecha consultada. `getMasCalurosa` y `getMenosCalurosa` usan las funciones
reductoras `max` y `min` comparando por la temperatura. Si dos ciudades tienen la
misma temperatura se muestra una de ellas.
