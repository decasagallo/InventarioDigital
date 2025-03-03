package ec.com.ecuamag.InventarioDigital.service;

import ec.com.ecuamag.InventarioDigital.enums.*;
import ec.com.ecuamag.InventarioDigital.model.*;
import ec.com.ecuamag.InventarioDigital.repository.TroquelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CsvImportService {

    @Autowired
    private TroquelRepository troquelRepository;

    private static final Logger LOGGER = Logger.getLogger(CsvImportService.class.getName());
    private static final String CSV_PATH = "data/DatosInventarioEcuamag.csv"; // Ruta en 'src/main/resources/data/'

    public void importarDatosDesdeCsv() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSV_PATH)) {
            if (inputStream == null) {
                throw new IOException("No se encontró el archivo CSV en los recursos.");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String linea;
            boolean primeraLinea = true; // Para omitir el encabezado

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] registro = linea.split(",");

                // Validar que la fila tenga suficientes columnas
                if (registro.length < 4) { // Se esperan al menos 9 campos
                    LOGGER.log(Level.WARNING, "Fila ignorada por datos incompletos: {0}", linea);
                    continue;
                }

                // Obtener tipo de troquel y crear el objeto correspondiente
                String tipoTroquelStr = registro[7].trim().toUpperCase();
                Troquel troquel = crearTroquelSegunTipo(tipoTroquelStr);

                if (troquel == null) {
                    LOGGER.log(Level.WARNING, "Tipo de troquel desconocido en fila: {0}", linea);
                    continue;
                }

                // Asignar valores generales
                try {
                    troquel.setNumero(Integer.parseInt(registro[0].trim()));
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Número de troquel inválido en la fila: {0}", linea);
                    continue;
                }
                troquel.setTamanioCorteAncho(parseBigDecimal(registro[1]));
                troquel.setTamanioCorteLargo(parseBigDecimal(registro[2]));
                troquel.setAncho(parseBigDecimal(registro[3]));
                troquel.setLargo(parseBigDecimal(registro[4]));
                troquel.setDescripcion(registro[5]);
                troquel.setInventario(parseInventario(registro[6]));
                troquel.setTipo(parseTipoTroquel(tipoTroquelStr)
                        .orElseThrow(() -> new IllegalArgumentException("Tipo de troquel inválido: " + tipoTroquelStr)));

                // Asignar valores específicos por tipo de troquel
                switch (tipoTroquelStr) {
                    case "SOBRE":
                        if (troquel instanceof Sobre sobre) {
                            sobre.setOrientacion(parseOrientacion(registro[8]));
                            sobre.setTipoSolapa(parseTipoSolapa(registro[9]));
                            sobre.setTipoSobre(parseTipoSobre(registro[14]));
                        }
                        break;
                    case "CAJA":
                        if (troquel instanceof Caja caja) {
                            caja.setAlto(parseInteger(registro[13]));
                        }
                        break;
                    case "BOLSA":
                        if (troquel instanceof Bolsa bolsa) {
                            bolsa.setAlto(parseInteger(registro[13]));
                        }
                        break;
                    case "FORMA":
                        if (troquel instanceof Forma forma) {
                            forma.setTipoForma(parseTipoForma(registro[11]));
                        }
                        break;
                    case "CARPETA":
                        if (troquel instanceof Carpeta carpeta) {
                            carpeta.setTipoCarpeta(parseTipoCarpeta(registro[10]));
                        }
                        break;
                    case "PARTE":
                        if (troquel instanceof Parte parte) {
                            parte.setTipoParte(parseTipoParte(registro[12]));
                        }
                        break;
                    case "FUNDA":
                       // Se crea una funda sin atributos adicionales
                        break;
                    default:
                        LOGGER.log(Level.WARNING, "Tipo de troquel desconocido: {0}", tipoTroquelStr);
                        continue;
                }

                // Guardar en la base de datos
                troquelRepository.save(troquel);
                LOGGER.log(Level.INFO, "Troquel guardado: {0}", troquel.getNumero());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al leer el archivo CSV", e);
        }
    }

    private Troquel crearTroquelSegunTipo(String tipo) {
        return switch (tipo) {
            case "SOBRE" -> new Sobre();
            case "CAJA" -> new Caja();
            case "BOLSA" -> new Bolsa();
            case "FORMA" -> new Forma();
            case "CARPETA" -> new Carpeta();
            case "PARTE" -> new Parte();
            case "FUNDA" -> new Funda();
            default -> null;
        };
    }

    private Integer parseInteger(String value) {
        return (value == null || value.trim().isEmpty()) ? null : Integer.parseInt(value.trim());
    }

    private BigDecimal parseBigDecimal(String value) {
        return (value == null || value.isEmpty()) ? null : new BigDecimal(value.trim());
    }

    private Orientacion parseOrientacion(String value) {
        try {
            return Orientacion.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    private TipoSolapa parseTipoSolapa(String value) {
        try {
            return TipoSolapa.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    private TipoCarpeta parseTipoCarpeta(String value) {
        try {
            return TipoCarpeta.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    private Inventario parseInventario(String value) {
        try {
            return Inventario.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private TipoParte parseTipoParte(String value) {
        try {
            return TipoParte.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private TipoForma parseTipoForma(String value) {
        try {
            return TipoForma.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private TipoSobre parseTipoSobre(String value) {
        try {
            return TipoSobre.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Optional<TipoTroquel> parseTipoTroquel(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(TipoTroquel.valueOf(value.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
