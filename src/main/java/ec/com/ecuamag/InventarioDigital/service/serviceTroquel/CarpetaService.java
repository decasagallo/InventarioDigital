package ec.com.ecuamag.InventarioDigital.service.serviceTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Carpeta;
import ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel.CarpetaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CarpetaService {

    private final CarpetaRepository carpetaRepository;

    public CarpetaService(CarpetaRepository carpetaRepository) {
        this.carpetaRepository = carpetaRepository;
    }

    public List<Carpeta> filtrarCarpetas( BigDecimal ancho, BigDecimal largo, Inventario inventario) {
        return carpetaRepository.filtrarCarpetas( ancho, largo, inventario);
    }
}
