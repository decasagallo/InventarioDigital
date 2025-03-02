package ec.com.ecuamag.InventarioDigital.service;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.Orientacion;
import ec.com.ecuamag.InventarioDigital.enums.TipoSolapa;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.Sobre;
import ec.com.ecuamag.InventarioDigital.model.Troquel;
import ec.com.ecuamag.InventarioDigital.repository.TroquelRepository;
import ec.com.ecuamag.InventarioDigital.specification.TroquelSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TroquelService {

    private final TroquelRepository troquelRepository;

    public TroquelService(TroquelRepository troquelRepository) {
        this.troquelRepository = troquelRepository;
    }

    // Método para obtener troqueles filtrados

   // 📌 Filtrar por Inventario (GRANDE o PEQUEÑO) y ordenar por número
    public List<Troquel> filtrarPorInventario(Inventario inventario) {
        return troquelRepository.findByInventarioOrderByNumeroAsc(inventario);
    }

    // 📌 Filtrar por Inventario y TipoTroquel y ordenar por número
    public List<Troquel> filtrarPorInventarioYTipo(Inventario inventario, TipoTroquel tipo) {
        return troquelRepository.findByInventarioAndTipoOrderByNumeroAsc(inventario, tipo);
    }






}
