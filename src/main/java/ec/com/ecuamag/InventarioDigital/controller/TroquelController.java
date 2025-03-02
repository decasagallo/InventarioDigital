package ec.com.ecuamag.InventarioDigital.controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.Orientacion;
import ec.com.ecuamag.InventarioDigital.enums.TipoSolapa;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.Bolsa;
import ec.com.ecuamag.InventarioDigital.model.Caja;
import ec.com.ecuamag.InventarioDigital.model.Sobre;
import ec.com.ecuamag.InventarioDigital.model.Troquel;
import ec.com.ecuamag.InventarioDigital.repository.TroquelRepository;
import ec.com.ecuamag.InventarioDigital.repository.BolsaRepository;
import ec.com.ecuamag.InventarioDigital.repository.CajaRepository;
import ec.com.ecuamag.InventarioDigital.service.SobreService;
import ec.com.ecuamag.InventarioDigital.service.TroquelService;
import ec.com.ecuamag.InventarioDigital.specification.TroquelSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/troqueles")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen

public class TroquelController {
    private final TroquelService troquelService;


    // Inyección por constructor
    public TroquelController(TroquelService troquelService) {
        this.troquelService = troquelService;

    }



   /* private static final String CSV_FILE_PATH = "/data/DatosInventarioEcuamag.csv";  // Ruta interna del recurso

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
    }*/


    // Filtrar por Inventario (GRANDE o PEQUEÑO)
    @GetMapping("/filtrar/inventario")
    public List<Troquel> getTroquelesByInventario(@RequestParam Inventario inventario) {
        return troquelService.filtrarPorInventario(inventario);
    }
    //Filtrar por TipoTroquel dentro de un Inventario
    @GetMapping("/filtrar/inventario-y-tipo")
    public List<Troquel> getTroquelesByInventarioAndTipo(
            @RequestParam Inventario inventario,
            @RequestParam TipoTroquel tipo
    ) {
        return troquelService.filtrarPorInventarioYTipo(inventario, tipo);
    }



    private Integer parseInteger(String value) {
        return (value == null || value.isEmpty()) ? null : Integer.parseInt(value.trim());
    }

    private BigDecimal parseBigDecimal(String value) {
        return (value == null || value.isEmpty()) ? null : new BigDecimal(value.trim());
    }
}
