package ec.com.ecuamag.InventarioDigital.controller;

import ec.com.ecuamag.InventarioDigital.enums.*;
import ec.com.ecuamag.InventarioDigital.model.Sobre;
import ec.com.ecuamag.InventarioDigital.service.SobreService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sobres")
@CrossOrigin(origins = "*")
public class SobreController {

    private final SobreService sobreService;

    public SobreController(SobreService sobreService) {
        this.sobreService = sobreService;
    }

    @GetMapping("/filtrar")
    public List<Sobre> filtrarSobres(
            @RequestParam(required = false) TipoSobre tipoSobre,
            @RequestParam(required = false) Orientacion orientacion,
            @RequestParam(required = false) TipoSolapa tipoSolapa,
            @RequestParam(required = false) BigDecimal ancho,
            @RequestParam(required = false) BigDecimal largo,
            @RequestParam(required = false) Inventario inventario) {  // 👈 Agregado Inventario

        return sobreService.filtrarSobres(tipoSobre, orientacion, tipoSolapa, ancho, largo, inventario);
    }

}
