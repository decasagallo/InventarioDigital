package ec.com.ecuamag.InventarioDigital.controller;

import ec.com.ecuamag.InventarioDigital.enums.Orientacion;
import ec.com.ecuamag.InventarioDigital.enums.TipoSolapa;
import ec.com.ecuamag.InventarioDigital.model.Sobre;
import ec.com.ecuamag.InventarioDigital.service.SobreService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/sobres")
public class SobreController {

    private final SobreService sobreService;

    public SobreController(SobreService sobreService) {
        this.sobreService = sobreService;
    }

    @GetMapping("/filtrar")
    public List<Sobre> filtrarSobres(
            @RequestParam(required = false) Orientacion orientacion,
            @RequestParam(required = false) TipoSolapa tipoSolapa,
            @RequestParam(required = false) Boolean medioSobre,
            @RequestParam(required = false) BigDecimal ancho,
            @RequestParam(required = false) BigDecimal largo
    ) {
        return sobreService.filtrarSobres(orientacion, tipoSolapa, medioSobre, ancho, largo);
    }
}
