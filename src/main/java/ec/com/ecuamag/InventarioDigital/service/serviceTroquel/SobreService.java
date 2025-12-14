package ec.com.ecuamag.InventarioDigital.service.serviceTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.Orientacion;
import ec.com.ecuamag.InventarioDigital.enums.TipoSobre;
import ec.com.ecuamag.InventarioDigital.enums.TipoSolapa;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Sobre;
import ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel.SobreRepository;
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
