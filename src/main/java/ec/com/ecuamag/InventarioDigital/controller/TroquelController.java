package ec.com.ecuamag.InventarioDigital.controller;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.Bolsa;
import ec.com.ecuamag.InventarioDigital.model.Caja;
import ec.com.ecuamag.InventarioDigital.model.Troquel;
import ec.com.ecuamag.InventarioDigital.repository.TroquelRepository;
import ec.com.ecuamag.InventarioDigital.repository.BolsaRepository;
import ec.com.ecuamag.InventarioDigital.repository.CajaRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/troqueles")
@CrossOrigin(origins = "*")
public class TroquelController {

    @Autowired
    private TroquelRepository troquelRepository;

    @Autowired
    private BolsaRepository bolsaRepository;

    @Autowired
    private CajaRepository cajaRepository;

    private static final String CSV_FILE_PATH = "/data/DatosInventarioEcuamag.csv";  // Ruta interna del recurso

    @PostMapping("/importar")
    public String importarCSV() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSV_FILE_PATH)) {
            if (inputStream == null) {
                return "Error: No se encontró el archivo CSV en los recursos.";
            }

            // Crear el CSVReader a partir del InputStream
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            List<String[]> registros = reader.readAll();
            registros.remove(0); // Eliminar la fila de encabezados

            for (String[] registro : registros) {
                if (registro.length < 7) continue; // Saltar filas incompletas

                int numero = Integer.parseInt(registro[0]);
                BigDecimal tamanioCorteAncho = parseBigDecimal(registro[1]);
                BigDecimal tamanioCorteLargo = parseBigDecimal(registro[2]);
                BigDecimal ancho = parseBigDecimal(registro[3]);
                BigDecimal largo = parseBigDecimal(registro[4]);
                String descripcion = registro[5];
                Inventario inventario = Inventario.valueOf(registro[6].toUpperCase());
                TipoTroquel tipo = TipoTroquel.valueOf(registro[7].toUpperCase());

                if (tipo == TipoTroquel.BOLSA || tipo == TipoTroquel.CAJA) {
                    Integer alto = parseInteger(registro[8]); // Solo bolsas y cajas tienen alto

                    if (tipo == TipoTroquel.BOLSA) {
                        Bolsa bolsa = new Bolsa();
                        bolsa.setNumero(numero);
                        bolsa.setTamanioCorteAncho(tamanioCorteAncho);
                        bolsa.setTamanioCorteLargo(tamanioCorteLargo);
                        bolsa.setAncho(ancho);
                        bolsa.setLargo(largo);
                        bolsa.setAlto(alto);
                        bolsa.setDescripcion(descripcion);
                        bolsa.setInventario(inventario);
                        bolsa.setTipo(tipo);
                        bolsaRepository.save(bolsa);
                    } else {
                        Caja caja = new Caja();
                        caja.setNumero(numero);
                        caja.setTamanioCorteAncho(tamanioCorteAncho);
                        caja.setTamanioCorteLargo(tamanioCorteLargo);
                        caja.setAncho(ancho);
                        caja.setLargo(largo);
                        caja.setAlto(alto);
                        caja.setDescripcion(descripcion);
                        caja.setInventario(inventario);
                        caja.setTipo(tipo);
                        cajaRepository.save(caja);
                    }
                } else {
                    Troquel troquel = new Troquel(){}; // Instancia genérica para troqueles
                    troquel.setNumero(numero);
                    troquel.setTamanioCorteAncho(tamanioCorteAncho);
                    troquel.setTamanioCorteLargo(tamanioCorteLargo);
                    troquel.setAncho(ancho);
                    troquel.setLargo(largo);
                    troquel.setDescripcion(descripcion);
                    troquel.setInventario(inventario);
                    troquel.setTipo(tipo);
                    troquelRepository.save(troquel);
                }
            }

            return "Datos importados correctamente.";
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return "Error al importar el archivo CSV.";
        }
    }

    // Modificado para filtrar por inventario y tipo
    @GetMapping("/filtrar")
    public List<Troquel> obtenerTroquelesPorInventarioYTipo(@RequestParam String inventario, @RequestParam(required = false) String tipo) {
        Inventario inventarioEnum = Inventario.valueOf(inventario.toUpperCase());

        // Si tipo es null o vacío, no filtrar por tipo
        if (tipo == null || tipo.isEmpty()) {
            return troquelRepository.findByInventarioOrderByNumeroAsc(inventarioEnum);
        } else {
            TipoTroquel tipoEnum = TipoTroquel.valueOf(tipo.toUpperCase());
            return troquelRepository.findByInventarioAndTipoOrderByNumeroAsc(inventarioEnum, tipoEnum);
        }
    }

    private Integer parseInteger(String value) {
        return (value == null || value.isEmpty()) ? null : Integer.parseInt(value.trim());
    }

    private BigDecimal parseBigDecimal(String value) {
        return (value == null || value.isEmpty()) ? null : new BigDecimal(value.trim());
    }
}
