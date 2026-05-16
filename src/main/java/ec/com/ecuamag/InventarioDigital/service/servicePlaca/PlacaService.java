package ec.com.ecuamag.InventarioDigital.service.servicePlaca;

import ec.com.ecuamag.InventarioDigital.model.modelPlaca.Placa;
import ec.com.ecuamag.InventarioDigital.repository.repositoryPlaca.PlacaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
public class PlacaService {

    private final PlacaRepository placaRepository;

    public PlacaService(PlacaRepository placaRepository) {
        this.placaRepository = placaRepository;
    }

    public List<Placa> buscar(String q) {
        if (q == null || q.isBlank()) {
            return placaRepository.findAll(Sort.by("numero"));
        }
        return placaRepository.buscar(q);
    }

    public Placa guardar(Placa placa) {
        return placaRepository.save(placa);
    }

    public Placa actualizar(Long id, Placa datos) {
        Placa placa = placaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la placa seleccionada"));
        placa.setNumero(datos.getNumero());
        placa.setCliente(datos.getCliente());
        placa.setDescripcion(datos.getDescripcion());
        placa.setCantidad(datos.getCantidad());
        return placaRepository.save(placa);
    }

    public Placa eliminar(Long id) {
        Placa placa = placaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la placa seleccionada"));
        placaRepository.delete(placa);
        return placa;
    }

    public int getUltimoNumero() {
        return placaRepository.findTopByOrderByNumeroDesc()
                .map(Placa::getNumero)
                .orElse(0);
    }

    public List<Placa> buscarSugerencias(String cliente, String descripcion) {
        String clienteNormalizado = normalizar(cliente);
        String descripcionNormalizada = normalizar(descripcion);

        if (clienteNormalizado.length() < 3 && descripcionNormalizada.length() < 3) {
            return List.of();
        }

        return placaRepository.findAll().stream()
                .filter(placa -> coincidePlaca(placa, clienteNormalizado, descripcionNormalizada))
                .limit(5)
                .toList();
    }

    public Placa sumarCantidad(Long id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        Placa placa = placaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la placa seleccionada"));
        placa.setCantidad(placa.getCantidad() + cantidad);
        return placaRepository.save(placa);
    }

    private boolean coincidePlaca(Placa placa, String cliente, String descripcion) {
        String clientePlaca = normalizar(placa.getCliente());
        String descripcionPlaca = normalizar(placa.getDescripcion());

        boolean clienteCoincide = cliente.length() >= 3 && esParecido(cliente, clientePlaca);
        boolean descripcionCoincide = descripcion.length() >= 3 && compartePalabra(descripcion, descripcionPlaca);

        return clienteCoincide || descripcionCoincide;
    }

    private boolean compartePalabra(String buscado, String existente) {
        for (String palabra : buscado.split(" ")) {
            if (palabra.length() >= 3 && existente.contains(palabra)) {
                return true;
            }
        }
        return esParecido(buscado, existente);
    }

    private boolean esParecido(String valor, String existente) {
        return existente.contains(valor)
                || valor.contains(existente)
                || distanciaLevenshtein(valor, existente) <= 2;
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return "";
        }

        String sinTildes = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinTildes.trim().replaceAll("\\s+", " ").toUpperCase();
    }

    private int distanciaLevenshtein(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= b.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int costo = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + costo
                );
            }
        }

        return dp[a.length()][b.length()];
    }
}
