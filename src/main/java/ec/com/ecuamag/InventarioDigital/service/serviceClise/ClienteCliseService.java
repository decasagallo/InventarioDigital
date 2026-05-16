package ec.com.ecuamag.InventarioDigital.service.serviceClise;

import ec.com.ecuamag.InventarioDigital.model.modelClise.ClienteClise;
import ec.com.ecuamag.InventarioDigital.repository.repositoryClise.ClienteCliseRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteCliseService {

    private final ClienteCliseRepository repository;

    public ClienteCliseService(ClienteCliseRepository repository) {
        this.repository = repository;
    }

    public List<ClienteClise> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return repository.findAll().stream()
                    .sorted(Comparator.comparing(ClienteClise::getLetra)
                            .thenComparing(ClienteClise::getNumero))
                    .toList();
        }
        return repository.findByNombreClienteContainingIgnoreCase(nombre);
    }

    public ClienteClise agregarClise(String nombreCliente, String tipo) {
        int impresion = "IMPRESION".equalsIgnoreCase(tipo) ? 1 : 0;
        int repujado = "REPUJADO".equalsIgnoreCase(tipo) ? 1 : 0;
        return agregarCantidades(nombreCliente, impresion, repujado);
    }

    public ClienteClise agregarCantidades(String nombreCliente, Integer impresion, Integer repujado) {
        int cantidadImpresion = impresion == null ? 0 : impresion;
        int cantidadRepujado = repujado == null ? 0 : repujado;

        if (cantidadImpresion < 0 || cantidadRepujado < 0) {
            throw new IllegalArgumentException("Las cantidades no pueden ser negativas");
        }

        if (cantidadImpresion == 0 && cantidadRepujado == 0) {
            throw new IllegalArgumentException("Debe ingresar al menos una cantidad");
        }

        String nombreLimpio = limpiarNombre(nombreCliente);
        ClienteClise cliente = buscarPorNombreNormalizado(nombreLimpio)
                .orElseGet(() -> crearNuevoCliente(nombreLimpio));

        cliente.setImpresion(cliente.getImpresion() + cantidadImpresion);
        cliente.setRepujado(cliente.getRepujado() + cantidadRepujado);

        return repository.save(cliente);
    }

    public List<ClienteClise> buscarSugerencias(String nombre) {
        String normalizado = normalizar(nombre);
        if (normalizado.length() < 3) {
            return List.of();
        }

        return repository.findAll().stream()
                .filter(cliente -> !normalizar(cliente.getNombreCliente()).equals(normalizado))
                .filter(cliente -> esParecido(normalizado, normalizar(cliente.getNombreCliente())))
                .sorted(Comparator.comparing(ClienteClise::getNombreCliente))
                .limit(5)
                .toList();
    }

    public ClienteClise actualizar(Long id, ClienteClise datos) {
        ClienteClise clise = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el clisé seleccionado"));

        String letraAnterior = clise.getLetra();

        String nombreLimpio = limpiarNombre(datos.getNombreCliente());
        String nuevaLetra = nombreLimpio.substring(0, 1).toUpperCase();

        clise.setNombreCliente(nombreLimpio);

        if (!nuevaLetra.equals(letraAnterior)) {
            int siguienteNumero = repository.findAll().stream()
                    .filter(c -> c.getLetra().equals(nuevaLetra))
                    .map(ClienteClise::getNumero)
                    .max(Integer::compareTo)
                    .orElse(0) + 1;

            clise.setLetra(nuevaLetra);
            clise.setNumero(siguienteNumero);
        }

        clise.setImpresion(datos.getImpresion());
        clise.setRepujado(datos.getRepujado());

        return repository.save(clise);
    }

    public ClienteClise eliminar(Long id) {
        ClienteClise clise = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el clisé seleccionado"));
        repository.delete(clise);
        return clise;
    }

    private ClienteClise crearNuevoCliente(String nombreCliente) {
        String nombreLimpio = limpiarNombre(nombreCliente);
        String letra = nombreLimpio.substring(0, 1).toUpperCase();

        int siguienteNumero = repository.findAll().stream()
                .filter(c -> c.getLetra().equals(letra))
                .map(ClienteClise::getNumero)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        ClienteClise cliente = new ClienteClise();
        cliente.setNombreCliente(nombreLimpio);
        cliente.setLetra(letra);
        cliente.setNumero(siguienteNumero);
        cliente.setImpresion(0);
        cliente.setRepujado(0);

        return cliente;
    }

    private Optional<ClienteClise> buscarPorNombreNormalizado(String nombre) {
        String nombreNormalizado = normalizar(nombre);
        return repository.findAll().stream()
                .filter(cliente -> normalizar(cliente.getNombreCliente()).equals(nombreNormalizado))
                .findFirst();
    }

    private boolean esParecido(String nombre, String existente) {
        return existente.contains(nombre)
                || nombre.contains(existente)
                || distanciaLevenshtein(nombre, existente) <= 2;
    }

    private String limpiarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }

        String limpio = nombre.trim().replaceAll("\\s+", " ");
        return limpio.substring(0, 1).toUpperCase() + limpio.substring(1);
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
