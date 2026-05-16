package ec.com.ecuamag.InventarioDigital.service.serviceTroquel;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Troquel;
import ec.com.ecuamag.InventarioDigital.repository.repositoryTroquel.TroquelRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    public List<Troquel> filtrarTroquelesGenerico(Inventario inventario, TipoTroquel tipo, BigDecimal ancho, BigDecimal largo) {
        return troquelRepository.filtrarTroquelesGenerico(inventario, tipo, ancho, largo);
    }

    public Troquel guardarTroquel(Troquel troquel) {
        String sufijo = troquel.getSufijo();

        if (sufijo == null) {
            sufijo = "";
        }

        sufijo = sufijo.trim().toUpperCase();
        troquel.setSufijo(sufijo);
        final String sufijoFinal = sufijo;
        boolean existeDuplicado = troquelRepository.findByInventario(
                troquel.getInventario(),
                Sort.by("numero").ascending()
        ).stream().anyMatch(t -> {
            String sufijoExistente = t.getSufijo() == null ? "" : t.getSufijo().trim().toUpperCase();

            boolean mismoNumero = t.getNumero() == troquel.getNumero();
            boolean mismoSufijo = sufijoExistente.equals(sufijoFinal);
            boolean mismoRegistro = troquel.getId() != null && t.getId().equals(troquel.getId());

            return mismoNumero && mismoSufijo && !mismoRegistro;
        });

        if (existeDuplicado) {
            String numeroCompleto = troquel.getNumero() + sufijo;
            throw new IllegalArgumentException(
                    "Ya existe un troquel con el número " + numeroCompleto + " en este inventario"
            );
        }

        return troquelRepository.save(troquel);
    }

    public Troquel getPorId(Long id) {
        return troquelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el troquel seleccionado"));
    }

    public Troquel eliminar(Long id) {
        Troquel troquel = getPorId(id);
        troquelRepository.delete(troquel);
        return troquel;
    }

    public int getUltimoNumeroPorInventario(Inventario inventario) {
        return troquelRepository.findTopByInventarioOrderByNumeroDesc(inventario)
                .map(Troquel::getNumero)
                .orElse(0);
    }
}