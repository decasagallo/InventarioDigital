package ec.com.ecuamag.InventarioDigital.service.serviceTroquel;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import ec.com.ecuamag.InventarioDigital.enums.*;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.*;
import ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel.TroquelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CsvImportTroquelService {

    private static final Logger LOGGER =
            Logger.getLogger(CsvImportTroquelService.class.getName());

    private static final String CSV_PATH =
            "data/DatosInventarioEcuamagTroqueles.csv";

    // Posiciones de las columnas del nuevo CSV
    private static final int COL_INVENTARIO = 0;
    private static final int COL_NUMERO = 1;
    private static final int COL_TIPO_TROQUEL = 2;
    private static final int COL_TAMANIO_CORTE_ANCHO = 3;
    private static final int COL_TAMANIO_CORTE_LARGO = 4;
    private static final int COL_ANCHO = 5;
    private static final int COL_LARGO = 6;
    private static final int COL_ALTO = 7;
    private static final int COL_DESCRIPCION = 8;
    private static final int COL_TIPO_SOBRE = 9;
    private static final int COL_ORIENTACION = 10;
    private static final int COL_TIPO_SOLAPA = 11;
    private static final int COL_TIPO_FORMA = 12;
    private static final int COL_SUFIJO = 13;

    @Autowired
    private TroquelRepository troquelRepository;

    public void importarDatosDesdeCsv() {

        int guardados = 0;
        int ignorados = 0;
        int errores = 0;

        try (InputStream inputStream =
                     getClass().getClassLoader().getResourceAsStream(CSV_PATH)) {

            if (inputStream == null) {
                throw new IOException(
                        "No se encontró el archivo CSV en los recursos."
                );
            }

            try (CSVReader reader = new CSVReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                // Saltar encabezados
                reader.readNext();

                String[] registro;

                while ((registro = reader.readNext()) != null) {

                    try {

                        if (procesarRegistro(registro)) {
                            guardados++;
                        } else {
                            ignorados++;
                        }

                    } catch (Exception e) {

                        errores++;

                        LOGGER.log(
                                Level.WARNING,
                                "Error al procesar fila: "
                                        + Arrays.toString(registro),
                                e
                        );
                    }
                }
            }

        } catch (IOException | CsvValidationException e) {

            LOGGER.log(
                    Level.SEVERE,
                    "Error al leer el archivo CSV",
                    e
            );
        }

        LOGGER.info("====================================");
        LOGGER.info("RESUMEN DE IMPORTACIÓN DE TROQUELES");
        LOGGER.info("Guardados: " + guardados);
        LOGGER.info("Ignorados: " + ignorados);
        LOGGER.info("Errores: " + errores);
        LOGGER.info("====================================");
    }

    private boolean procesarRegistro(String[] registro) {

        // Validar campos obligatorios
        if (registro.length != 14
                || get(registro, COL_INVENTARIO) == null
                || get(registro, COL_NUMERO) == null
                || get(registro, COL_TIPO_TROQUEL) == null
                || get(registro, COL_DESCRIPCION) == null) {

            LOGGER.log(
                    Level.WARNING,
                    "Fila ignorada por campos faltantes: {0}",
                    Arrays.toString(registro)
            );

            return false;
        }

        Inventario inventario =
                parseInventario(get(registro, COL_INVENTARIO));

        int numero =
                Integer.parseInt(get(registro, COL_NUMERO));

        String sufijo = get(registro, COL_SUFIJO);

        sufijo = sufijo == null
                ? ""
                : sufijo.toUpperCase(Locale.ROOT);

        // Evitar registros duplicados
        if (troquelRepository.buscarPorNumeroCompleto(
                inventario, numero, sufijo).isPresent()) {

            LOGGER.log(
                    Level.INFO,
                    "Troquel ya existente, se omite: {0}{1}",
                    new Object[]{numero, sufijo}
            );

            return false;
        }

        String tipoTroquelStr =
                get(registro, COL_TIPO_TROQUEL)
                        .toUpperCase(Locale.ROOT);

        Troquel troquel =
                crearTroquelSegunTipo(tipoTroquelStr);

        // Campos generales
        troquel.setInventario(inventario);

        troquel.setTipo(
                TipoTroquel.valueOf(tipoTroquelStr)
        );

        troquel.setNumero(numero);
        troquel.setSufijo(sufijo);

        troquel.setDescripcion(
                get(registro, COL_DESCRIPCION)
        );

        // Dimensiones de corte
        troquel.setTamanioCorteAncho(
                parseBigDecimal(
                        get(registro, COL_TAMANIO_CORTE_ANCHO)
                )
        );

        troquel.setTamanioCorteLargo(
                parseBigDecimal(
                        get(registro, COL_TAMANIO_CORTE_LARGO)
                )
        );

        // Dimensiones del troquel
        troquel.setAncho(
                parseBigDecimal(get(registro, COL_ANCHO))
        );

        troquel.setLargo(
                parseBigDecimal(get(registro, COL_LARGO))
        );

        // Asignar atributos específicos
        asignarCamposPorTipo(
                troquel, tipoTroquelStr, registro
        );

        // Guardar en PostgreSQL
        troquelRepository.save(troquel);

        LOGGER.log(
                Level.INFO,
                "Troquel guardado: {0}",
                troquel.getNumeroCompleto()
        );

        return true;
    }

    private void asignarCamposPorTipo(
            Troquel troquel,
            String tipoTroquelStr,
            String[] registro) {

        switch (tipoTroquelStr) {

            case "SOBRE" -> {

                Sobre sobre = (Sobre) troquel;

                TipoSobre tipoSobre =
                        parseTipoSobre(
                                get(registro, COL_TIPO_SOBRE)
                        );

                if (tipoSobre == null) {
                    throw new IllegalArgumentException(
                            "Debe especificar el tipo de sobre"
                    );
                }

                sobre.setTipoSobre(tipoSobre);

                switch (tipoSobre) {

                    case RECTANGULAR -> {

                        Orientacion orientacion =
                                parseOrientacion(
                                        get(registro, COL_ORIENTACION)
                                );

                        TipoSolapa tipoSolapa =
                                parseTipoSolapa(
                                        get(registro, COL_TIPO_SOLAPA)
                                );

                        if (orientacion == null) {
                            throw new IllegalArgumentException(
                                    "El sobre rectangular requiere orientación"
                            );
                        }

                        if (tipoSolapa == null) {
                            throw new IllegalArgumentException(
                                    "El sobre rectangular requiere solapa"
                            );
                        }

                        sobre.setOrientacion(orientacion);
                        sobre.setTipoSolapa(tipoSolapa);
                    }

                    case CUADRADO -> {

                        TipoSolapa tipoSolapa =
                                parseTipoSolapa(
                                        get(registro, COL_TIPO_SOLAPA)
                                );

                        if (tipoSolapa == null) {
                            throw new IllegalArgumentException(
                                    "El sobre cuadrado requiere solapa"
                            );
                        }

                        sobre.setOrientacion(null);
                        sobre.setTipoSolapa(tipoSolapa);
                    }

                    case MEDIO_SOBRE -> {

                        sobre.setOrientacion(null);
                        sobre.setTipoSolapa(null);
                    }
                }
            }

            case "CAJA" -> {

                Caja caja = (Caja) troquel;

                caja.setAlto(
                        parseBigDecimal(get(registro, COL_ALTO))
                );
            }

            case "BOLSA" -> {

                Bolsa bolsa = (Bolsa) troquel;

                bolsa.setAlto(
                        parseBigDecimal(get(registro, COL_ALTO))
                );
            }

            case "FORMA" -> {

                Forma forma = (Forma) troquel;

                forma.setTipoForma(
                        parseTipoForma(get(registro, COL_TIPO_FORMA))
                );
            }

            case "CARPETA", "FUNDA" -> {
                // No tienen atributos adicionales
            }

            default -> throw new IllegalArgumentException(
                    "Tipo de troquel desconocido: " + tipoTroquelStr
            );
        }
    }

    private String get(String[] registro, int index) {

        return (registro.length > index
                && registro[index] != null
                && !registro[index].trim().isBlank())

                ? registro[index].trim()
                : null;
    }

    private Troquel crearTroquelSegunTipo(String tipo) {

        return switch (tipo) {

            case "SOBRE" -> new Sobre();
            case "CAJA" -> new Caja();
            case "BOLSA" -> new Bolsa();
            case "FORMA" -> new Forma();
            case "CARPETA" -> new Carpeta();
            case "FUNDA" -> new Funda();

            default -> throw new IllegalArgumentException(
                    "Tipo de troquel inválido: " + tipo
            );
        };
    }

    private BigDecimal parseBigDecimal(String value) {

        return value == null
                ? null
                : new BigDecimal(value);
    }

    private Orientacion parseOrientacion(String value) {

        return value == null
                ? null
                : Orientacion.valueOf(
                value.toUpperCase(Locale.ROOT)
        );
    }

    private TipoSolapa parseTipoSolapa(String value) {

        return value == null
                ? null
                : TipoSolapa.valueOf(
                value.toUpperCase(Locale.ROOT)
        );
    }

    private Inventario parseInventario(String value) {

        return Inventario.valueOf(
                value.toUpperCase(Locale.ROOT)
        );
    }

    private TipoForma parseTipoForma(String value) {

        return value == null
                ? null
                : TipoForma.valueOf(
                value.toUpperCase(Locale.ROOT)
        );
    }

    private TipoSobre parseTipoSobre(String value) {

        return value == null
                ? null
                : TipoSobre.valueOf(
                value.toUpperCase(Locale.ROOT)
        );
    }
}