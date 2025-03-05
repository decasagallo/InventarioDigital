package ec.com.ecuamag.InventarioDigital.service;

import ec.com.ecuamag.InventarioDigital.enums.*;
import ec.com.ecuamag.InventarioDigital.model.Carpeta;
import ec.com.ecuamag.InventarioDigital.repository.CarpetaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CarpetaService {

    private final CarpetaRepository carpetaRepository;

    public CarpetaService(CarpetaRepository carpetaRepository) {
        this.carpetaRepository = carpetaRepository;
    }

    public List<Carpeta> filtrarCarpetas(TipoCarpeta tipoCarpeta, BigDecimal ancho, BigDecimal largo, Inventario inventario) {
        return carpetaRepository.filtrarCarpetas(tipoCarpeta, ancho, largo, inventario);
    }
}
