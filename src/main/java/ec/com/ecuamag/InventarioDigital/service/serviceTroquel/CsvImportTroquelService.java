package ec.com.ecuamag.InventarioDigital.service.serviceTroquel;

import ec.com.ecuamag.InventarioDigital.enums.*;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.*;
import ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel.SobreRepository;
import ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel.TroquelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CsvImportTroquelService {

    @Autowired
    private TroquelRepository troquelRepository;

    @Autowired
    private SobreRepository sobreRepository;

    private static final Logger LOGGER = Logger.getLogger(CsvImportTroquelService.class.getName());
    private static final String CSV_PATH = "data/DatosInventarioEcuamagTroqueles.csv";

    public void importarDatosDesdeCsv() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSV_PATH)) {
            if (inputStream == null) {
                throw new IOException("No se encontró el archivo CSV en los recursos.");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] registro = linea.split(",");

                // Validar los obligatorios: TipoTroquel (2), Numero (1), Descripcion (8)
                if (registro.length < 9 || get(registro, 1) == null || get(registro, 2) == null || get(registro, 8) == null) {
                    LOGGER.log(Level.WARNING, "Fila ignorada por campos obligatorios faltantes: {0}", linea);
                    continue;
                }

                String tipoTroquelStr = get(registro, 2).toUpperCase();
                Troquel troquel = crearTroquelSegunTipo(tipoTroquelStr);

                if (troquel == null) {
                    LOGGER.log(Level.WARNING, "Tipo de troquel desconocido en fila: {0}", linea);
                    continue;
                }

                try {
                    troquel.setInventario(parseInventario(get(registro, 0)));
                    troquel.setTipo(parseTipoTroquel(tipoTroquelStr)
                            .orElseThrow(() -> new IllegalArgumentException("Tipo de troquel inválido: " + tipoTroquelStr)));
                    troquel.setNumero(Integer.parseInt(get(registro, 1)));
                    troquel.setDescripcion(get(registro, 8));
                    troquel.setTamanioCorteAncho(parseBigDecimal(get(registro, 3)));
                    troquel.setTamanioCorteLargo(parseBigDecimal(get(registro, 4)));
                    troquel.setAncho(parseBigDecimal(get(registro, 5)));
                    troquel.setLargo(parseBigDecimal(get(registro, 6)));

                    switch (tipoTroquelStr) {
                        case "SOBRE" -> {
                            if (troquel instanceof Sobre sobre) {
                                sobre.setTipoSobre(parseTipoSobre(get(registro, 9)));
                                sobre.setOrientacion(parseOrientacion(get(registro, 10)));
                                sobre.setTipoSolapa(parseTipoSolapa(get(registro, 11)));
                            }
                        }
                        case "CAJA" -> {
                            if (troquel instanceof Caja caja) {
                                caja.setAlto(parseBigDecimal(get(registro, 7)));
                            }
                        }
                        case "BOLSA" -> {
                            if (troquel instanceof Bolsa bolsa) {
                                bolsa.setAlto(parseBigDecimal(get(registro, 7)));
                            }
                        }
                        case "FORMA" -> {
                            if (troquel instanceof Forma forma) {
                                forma.setTipoForma(parseTipoForma(get(registro, 12)));
                            }
                        }
                        case "CARPETA", "FUNDA" -> {
                            // No hay campos adicionales
                        }
                        default -> LOGGER.log(Level.WARNING, "Tipo de troquel no manejado específicamente: {0}", tipoTroquelStr);
                    }

                    troquelRepository.save(troquel);
                    LOGGER.log(Level.INFO, "Troquel guardado: {0}", troquel.getNumero());

                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error al procesar fila: " + linea, e);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al leer el archivo CSV", e);
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
            return Orientacion.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private TipoSolapa parseTipoSolapa(String value) {
        try {
            return TipoSolapa.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private Inventario parseInventario(String value) {
        try {
            return (value == null) ? null : Inventario.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private TipoForma parseTipoForma(String value) {
        try {
            return TipoForma.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private TipoSobre parseTipoSobre(String value) {
        try {
            return TipoSobre.valueOf(value.toUpperCase());
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
