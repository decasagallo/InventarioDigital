package ec.com.ecuamag.InventarioDigital.controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ec.com.ecuamag.InventarioDigital.enums.*;
import ec.com.ecuamag.InventarioDigital.model.*;
import ec.com.ecuamag.InventarioDigital.repository.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class CSVImportController {

    private final SobreRepository sobreRepository;
    private final BolsaRepository bolsaRepository;
    private final CajaRepository cajaRepository;
    private final CarpetaRepository carpetaRepository;
    private final FormaRepository formaRepository;
    private final ParteRepository parteRepository;
    private final FundaRepository fundaRepository;

    public CSVImportController(SobreRepository sobreRepository, BolsaRepository bolsaRepository, CajaRepository cajaRepository,
                               CarpetaRepository carpetaRepository, FormaRepository formaRepository, ParteRepository parteRepository,
                               FundaRepository fundaRepository) {
        this.sobreRepository = sobreRepository;
        this.bolsaRepository = bolsaRepository;
        this.cajaRepository = cajaRepository;
        this.carpetaRepository = carpetaRepository;
        this.formaRepository = formaRepository;
        this.parteRepository = parteRepository;
        this.fundaRepository = fundaRepository;
    }

    @PostMapping("/importar-csv")
    public String importarCSV() {
        try {
            // Leer archivo desde resources
            ClassPathResource resource = new ClassPathResource("data/DatosInventarioEcuamag.csv");
            CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()));

            List<String[]> registros = reader.readAll();
            registros.remove(0); // Eliminar encabezado

            for (String[] registro : registros) {
                if (registro.length < 8) continue; // Saltar filas incompletas

                int numero = Integer.parseInt(registro[0]);
                BigDecimal tamanioCorteAncho = parseBigDecimal(registro[1]);
                BigDecimal tamanioCorteLargo = parseBigDecimal(registro[2]);
                BigDecimal ancho = parseBigDecimal(registro[3]);
                BigDecimal largo = parseBigDecimal(registro[4]);
                String descripcion = registro[5];
                Inventario inventario = Inventario.valueOf(registro[6].toUpperCase());
                TipoTroquel tipo = TipoTroquel.valueOf(registro[7].toUpperCase());

                // Procesar según el tipo de troquel
                switch (tipo) {
                    case SOBRE:
                        Sobre sobre = new Sobre();
                        sobre.setNumero(numero);
                        sobre.setTamanioCorteAncho(tamanioCorteAncho);
                        sobre.setTamanioCorteLargo(tamanioCorteLargo);
                        sobre.setAncho(ancho);
                        sobre.setLargo(largo);
                        sobre.setDescripcion(descripcion);
                        sobre.setInventario(inventario);
                        sobre.setTipo(tipo);
                        sobre.setTipoSobre(TipoSobre.valueOf(registro[8].toUpperCase()));

                        if (sobre.getTipoSobre() == TipoSobre.RECTANGULAR) {
                            sobre.setOrientacion(Orientacion.valueOf(registro[9].toUpperCase()));
                        }
                        sobre.setTipoSolapa(TipoSolapa.valueOf(registro[10].toUpperCase()));
                        sobreRepository.save(sobre);
                        break;

                    case BOLSA:
                        Bolsa bolsa = new Bolsa();
                        bolsa.setNumero(numero);
                        bolsa.setTamanioCorteAncho(tamanioCorteAncho);
                        bolsa.setTamanioCorteLargo(tamanioCorteLargo);
                        bolsa.setAncho(ancho);
                        bolsa.setLargo(largo);
                        bolsa.setDescripcion(descripcion);
                        bolsa.setInventario(inventario);
                        bolsa.setTipo(tipo);
                        bolsa.setAlto(parseInteger(registro[8]));
                        bolsaRepository.save(bolsa);
                        break;

                    case CAJA:
                        Caja caja = new Caja();
                        caja.setNumero(numero);
                        caja.setTamanioCorteAncho(tamanioCorteAncho);
                        caja.setTamanioCorteLargo(tamanioCorteLargo);
                        caja.setAncho(ancho);
                        caja.setLargo(largo);
                        caja.setDescripcion(descripcion);
                        caja.setInventario(inventario);
                        caja.setTipo(tipo);
                        caja.setAlto(parseInteger(registro[8]));
                        cajaRepository.save(caja);
                        break;

                    case CARPETA:
                        Carpeta carpeta = new Carpeta();
                        carpeta.setNumero(numero);
                        carpeta.setTamanioCorteAncho(tamanioCorteAncho);
                        carpeta.setTamanioCorteLargo(tamanioCorteLargo);
                        carpeta.setAncho(ancho);
                        carpeta.setLargo(largo);
                        carpeta.setDescripcion(descripcion);
                        carpeta.setInventario(inventario);
                        carpeta.setTipo(tipo);
                        carpeta.setTipoCarpeta(TipoCarpeta.valueOf(registro[8].toUpperCase()));
                        carpetaRepository.save(carpeta);
                        break;

                    case FORMA:
                        Forma forma = new Forma();
                        forma.setNumero(numero);
                        forma.setTamanioCorteAncho(tamanioCorteAncho);
                        forma.setTamanioCorteLargo(tamanioCorteLargo);
                        forma.setAncho(ancho);
                        forma.setLargo(largo);
                        forma.setDescripcion(descripcion);
                        forma.setInventario(inventario);
                        forma.setTipo(tipo);
                        forma.setTipoForma(TipoForma.valueOf(registro[8].toUpperCase()));
                        formaRepository.save(forma);
                        break;

                    case PARTE:
                        Parte parte = new Parte();
                        parte.setNumero(numero);
                        parte.setTamanioCorteAncho(tamanioCorteAncho);
                        parte.setTamanioCorteLargo(tamanioCorteLargo);
                        parte.setAncho(ancho);
                        parte.setLargo(largo);
                        parte.setDescripcion(descripcion);
                        parte.setInventario(inventario);
                        parte.setTipo(tipo);
                        parte.setTipoParte(TipoParte.valueOf(registro[8].toUpperCase()));
                        parteRepository.save(parte);
                        break;

                    case FUNDA:
                        Funda funda = new Funda();
                        funda.setNumero(numero);
                        funda.setTamanioCorteAncho(tamanioCorteAncho);
                        funda.setTamanioCorteLargo(tamanioCorteLargo);
                        funda.setAncho(ancho);
                        funda.setLargo(largo);
                        funda.setDescripcion(descripcion);
                        funda.setInventario(inventario);
                        funda.setTipo(tipo);
                        fundaRepository.save(funda);
                        break;
                }
            }
            return "Datos importados correctamente.";
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return "Error al importar el archivo CSV.";
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        return value == null || value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value);
    }

    private Integer parseInteger(String value) {
        return value == null || value.isEmpty() ? null : Integer.parseInt(value);
    }
}
