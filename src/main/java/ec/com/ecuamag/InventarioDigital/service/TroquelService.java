package ec.com.ecuamag.InventarioDigital.service;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.Troquel;
import ec.com.ecuamag.InventarioDigital.repository.TroquelRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TroquelService {

    private final TroquelRepository troquelRepository;

    public TroquelService(TroquelRepository troquelRepository) {
        this.troquelRepository = troquelRepository;
    }

    public List<Troquel> getTroquelesPorInventario(Inventario inventario) {
        return troquelRepository.findByInventario(inventario, Sort.by("numero").ascending());
    }

    public List<Troquel> getTroquelesPorInventarioYTipo(Inventario inventario, TipoTroquel tipo) {
        return troquelRepository.findByInventarioAndTipo(inventario, tipo, Sort.by("numero").ascending());
    }

    public List<Troquel> getTodosLosTroqueles() {
        return troquelRepository.findAll(Sort.by("numero").ascending());
    }

    public List<Troquel> buscarPorDescripcion(String descripcion) {
        return troquelRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }


}
