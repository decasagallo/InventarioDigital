package ec.com.ecuamag.InventarioDigital.service.serviceClise;

import ec.com.ecuamag.InventarioDigital.model.modelClise.Clise;
import ec.com.ecuamag.InventarioDigital.repository.repositoryClise.CliseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CliseService {

    private final CliseRepository cliseRepository;

    public CliseService(CliseRepository cliseRepository) {
        this.cliseRepository = cliseRepository;
    }

    public List<Clise> filtrarClises(String letra, Integer numero, String nombre) {
        return cliseRepository.filtrarClises(letra, numero, nombre);
    }

}