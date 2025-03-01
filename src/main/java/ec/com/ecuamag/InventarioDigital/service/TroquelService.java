package ec.com.ecuamag.InventarioDigital.service;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.Troquel;
import ec.com.ecuamag.InventarioDigital.repository.TroquelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TroquelService {

    @Autowired
    private TroquelRepository troquelRepository;

    // Obtener troqueles filtrados por inventario (grande o pequeño) y dimensiones
    public List<Troquel> obtenerTroquelesPorInventario(String inventario) {
        // Verificamos si el inventario es "GRANDE" o "PEQUEÑO" y hacemos la consulta correspondiente
        if (inventario.equalsIgnoreCase("GRANDE")) {
            return troquelRepository.findByInventarioOrderByNumeroAsc(Inventario.GRANDE);
        } else if (inventario.equalsIgnoreCase("PEQUEÑO")) {
            return troquelRepository.findByInventarioOrderByNumeroAsc(Inventario.PEQUENO);
        }
        // Si no es válido, devolvemos una lista vacía o manejamos el error de alguna otra manera
        return new ArrayList<>();
    }
}