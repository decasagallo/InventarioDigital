package ec.com.ecuamag.EcuamagInventario.controller;

import ec.com.ecuamag.EcuamagInventario.model.Troquel;
import ec.com.ecuamag.EcuamagInventario.service.TroquelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/troqueles")
@CrossOrigin(origins = "*")
public class TroquelController {

    @Autowired
    private TroquelService troquelService;

    @GetMapping("/filtrar")
    public List<Troquel> filtrar(
            @RequestParam(required = false) Integer ancho,
            @RequestParam(required = false) Integer alto,
            @RequestParam(required = false) Integer largo
    ) {
        return troquelService.filtrarPorDimensiones(ancho, alto, largo);
    }
}
