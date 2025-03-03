package ec.com.ecuamag.InventarioDigital.service;

import ec.com.ecuamag.InventarioDigital.enums.*;
import ec.com.ecuamag.InventarioDigital.model.Sobre;
import ec.com.ecuamag.InventarioDigital.repository.SobreRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SobreService {

    private final SobreRepository sobreRepository;

    public SobreService(SobreRepository sobreRepository) {
        this.sobreRepository = sobreRepository;
    }

    public List<Sobre> filtrarSobres(TipoSobre tipoSobre, Orientacion orientacion, TipoSolapa tipoSolapa,
                                      BigDecimal ancho, BigDecimal largo, Inventario inventario) {
        return sobreRepository.filtrarSobres(tipoSobre, orientacion, tipoSolapa, ancho, largo, inventario);
    }
}
