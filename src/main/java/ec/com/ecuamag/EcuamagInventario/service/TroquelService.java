package ec.com.ecuamag.EcuamagInventario.service;

import ec.com.ecuamag.EcuamagInventario.model.Troquel;
import ec.com.ecuamag.EcuamagInventario.repository.TroquelRepository;
import ec.com.ecuamag.EcuamagInventario.specification.TroquelSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TroquelService {

    @Autowired
    private TroquelRepository troquelRepository;

    public List<Troquel> filtrarPorDimensiones(Integer ancho, Integer alto, Integer largo) {
        Specification<Troquel> spec = TroquelSpecification.filtrarPorDimensiones(ancho, alto, largo);
        return troquelRepository.findAll(spec);
    }
}
