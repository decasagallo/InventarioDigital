package ec.com.ecuamag.InventarioDigital.service.serviceTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.Orientacion;
import ec.com.ecuamag.InventarioDigital.enums.TipoForma;
import ec.com.ecuamag.InventarioDigital.enums.TipoSobre;
import ec.com.ecuamag.InventarioDigital.enums.TipoSolapa;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.*;
import ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel.TroquelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CsvImportTroquelService {

    private static final Logger LOGGER = Logger.getLogger(CsvImportTroquelService.class.getName());
    private static final String CSV_PATH = "data/DatosInventarioEcuamagTroqueles.csv";

    // CSV:
    // inventario,numero,sufijo,tipo,corteAncho,corteLargo,ancho,largo,alto,descripcion,tipoSobre,orientacion,tipoSolapa,tipoForma
    private static final int COL_INVENTARIO = 0;
    private static final int COL_NUMERO = 1;
    private static final int COL_SUFIJO = 2;
    private static final int COL_TIPO_TROQUEL = 3;
    private static final int COL_TAMANIO_CORTE_ANCHO = 4;
    private static final int COL_TAMANIO_CORTE_LARGO = 5;
    private static final int COL_ANCHO = 6;
    private static final int COL_LARGO = 7;
    private static final int COL_ALTO = 8;
    private static final int COL_DESCRIPCION = 9;
    private static final int COL_TIPO_SOBRE = 10;
    private static final int COL_ORIENTACION = 11;
    private static final int COL_TIPO_SOLAPA = 12;
    private static final int COL_TIPO_FORMA = 13;

    @Autowired
    private TroquelRepository troquelRepository;

    public void importarDatosDesdeCsv() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSV_PATH)) {
            if (inputStream == null) {
                throw new IOException("No se encontro el archivo CSV en los recursos.");
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String linea;
                boolean primeraLinea = true;

                while ((linea = br.readLine()) != null) {
                    if (primeraLinea) {
                        primeraLinea = false;
                        continue;
                    }

                    procesarLinea(linea);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al leer el archivo CSV", e);
        }
    }

    private void procesarLinea(String linea) {
        String[] registro = linea.split(",", -1);

        if (registro.length < 10
                || get(registro, COL_INVENTARIO) == null
                || get(registro, COL_NUMERO) == null
                || get(registro, COL_TIPO_TROQUEL) == null
                || get(registro, COL_DESCRIPCION) == null) {
            LOGGER.log(Level.WARNING, "Fila ignorada por campos obligatorios faltantes: {0}", linea);
            return;
        }

        String tipoTroquelStr = get(registro, COL_TIPO_TROQUEL).toUpperCase();
        Troquel troquel = crearTroquelSegunTipo(tipoTroquelStr);

        if (troquel == null) {
            LOGGER.log(Level.WARNING, "Tipo de troquel desconocido en fila: {0}", linea);
            return;
        }

        try {
            troquel.setInventario(parseInventario(get(registro, COL_INVENTARIO)));
            troquel.setTipo(parseTipoTroquel(tipoTroquelStr)
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de troquel invalido: " + tipoTroquelStr)));

            troquel.setNumero(Integer.parseInt(get(registro, COL_NUMERO)));

            String sufijo = get(registro, COL_SUFIJO);
            troquel.setSufijo(sufijo == null ? "" : sufijo.toUpperCase());

            troquel.setDescripcion(get(registro, COL_DESCRIPCION));
            troquel.setTamanioCorteAncho(parseBigDecimal(get(registro, COL_TAMANIO_CORTE_ANCHO)));
            troquel.setTamanioCorteLargo(parseBigDecimal(get(registro, COL_TAMANIO_CORTE_LARGO)));
            troquel.setAncho(parseBigDecimal(get(registro, COL_ANCHO)));
            troquel.setLargo(parseBigDecimal(get(registro, COL_LARGO)));

            asignarCamposPorTipo(troquel, tipoTroquelStr, registro);

            troquelRepository.save(troquel);

            LOGGER.log(Level.INFO, "Troquel guardado: {0}", troquel.getNumeroCompleto());

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al procesar fila: " + linea, e);
        }
    }

    private void asignarCamposPorTipo(Troquel troquel, String tipoTroquelStr, String[] registro) {
        switch (tipoTroquelStr) {
            case "SOBRE" -> {
                Sobre sobre = (Sobre) troquel;
                sobre.setTipoSobre(parseTipoSobre(get(registro, COL_TIPO_SOBRE)));
                sobre.setOrientacion(parseOrientacion(get(registro, COL_ORIENTACION)));
                sobre.setTipoSolapa(parseTipoSolapa(get(registro, COL_TIPO_SOLAPA)));
            }

            case "CAJA" -> ((Caja) troquel).setAlto(parseBigDecimal(get(registro, COL_ALTO)));

            case "BOLSA" -> ((Bolsa) troquel).setAlto(parseBigDecimal(get(registro, COL_ALTO)));

            case "FORMA" -> ((Forma) troquel).setTipoForma(parseTipoForma(get(registro, COL_TIPO_FORMA)));

            case "CARPETA", "FUNDA" -> {
                // No tienen campos extra.
            }

            default -> LOGGER.log(Level.WARNING, "Tipo de troquel no manejado especificamente: {0}", tipoTroquelStr);
        }
    }

    private String get(String[] registro, int index) {
        return (registro.length > index && registro[index] != null && !registro[index].trim().isBlank())
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
            default -> null;
        };
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return (value == null || value.isBlank()) ? null : new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Orientacion parseOrientacion(String value) {
        try {
            return value == null ? null : Orientacion.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private TipoSolapa parseTipoSolapa(String value) {
        try {
            return value == null ? null : TipoSolapa.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private Inventario parseInventario(String value) {
        try {
            return value == null ? null : Inventario.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private TipoForma parseTipoForma(String value) {
        try {
            return value == null ? null : TipoForma.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private TipoSobre parseTipoSobre(String value) {
        try {
            return value == null ? null : TipoSobre.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private Optional<TipoTroquel> parseTipoTroquel(String value) {
        try {
            return (value == null || value.isBlank())
                    ? Optional.empty()
                    : Optional.of(TipoTroquel.valueOf(value.toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}