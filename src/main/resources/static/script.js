// Determina la URL base de la API dependiendo del entorno (local o producción)
const API_BASE_URL = window.location.hostname === "127.0.0.1" || window.location.hostname === "localhost"
    ? "http://127.0.0.1:8080"
    : "https://inventariodigital.onrender.com";

// Espera que el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", function () {
    // Referencias a elementos del DOM
    const tipoTroquelSelect = document.getElementById("tipoTroquel");
    const inventarioSelect = document.getElementById("inventario");
    const tipoSobreSelect = document.getElementById("tipoSobre");
    const tipoSobreContainer = document.getElementById("tipoSobre-container");
    const orientacionContainer = document.getElementById("orientacion-container");
    const tipoSolapaSelect = document.getElementById("tipoSolapa");
    const tipoSolapaContainer = document.getElementById("tipoSolapa-container");
    const tamaniosContainer = document.getElementById("tamanios-container");
    const tableBody = document.querySelector("#troqueles-table tbody");
    const columnaTipoTroquel = document.querySelector("#troqueles-table th:nth-child(4)");
    const listaSelector = document.getElementById("listaSelector");
    const troquelesSection = document.getElementById("troquelesSection");
    const placasSection = document.getElementById("placasSection");
    const clisesSection = document.getElementById("clisesSection");
    const descripcionInput = document.getElementById("descripcion");
    const tipoCarpetaContainer = document.getElementById("tipoCarpeta-container");
    const tipoCarpetaSelect = document.getElementById("tipoCarpeta");

    // Muestra/oculta filtros según el tipo de troquel seleccionado
    function toggleFilters() {
        const tipo = tipoTroquelSelect.value;

        // Siempre ocultamos todo al inicio
        tipoSobreContainer.classList.add("hidden");
        orientacionContainer.classList.add("hidden");
        tipoSolapaContainer.classList.add("hidden");
        tamaniosContainer.classList.add("hidden");
        document.getElementById("alto-container").classList.add("hidden");
        document.getElementById("tipoForma-container").classList.add("hidden");

        // Ahora según el tipo mostramos lo necesario
        if (tipo === "SOBRE") {
            tipoSobreContainer.classList.remove("hidden");
            tipoSolapaContainer.classList.remove("hidden");
            tamaniosContainer.classList.remove("hidden");

            updateOrientacionVisibility(); // Ver si se muestra orientación
        } else if (tipo === "CARPETA" || tipo === "FUNDA" || tipo === "BOLSA" || tipo === "CAJA") {
            tamaniosContainer.classList.remove("hidden");

            if (tipo === "BOLSA" || tipo === "CAJA") {
                tamaniosContainer.classList.remove("hidden");
                document.getElementById("alto-container").classList.remove("hidden");
            }
        } else if (tipo === "FORMA") {
            tamaniosContainer.classList.remove("hidden");
            document.getElementById("tipoForma-container").classList.remove("hidden"); // Mostrar tipoForma
        }

        toggleTipoTroquelColumn();
    }

    // Controla visibilidad del filtro de orientación basado en tipo de sobre
    function updateOrientacionVisibility() {
        const tipoSobre = tipoSobreSelect.value;
        const orientacionInput = document.getElementById("orientacion");

        if (tipoSobre === "RECTANGULAR" || tipoSobre === "MEDIO_SOBRE") {
            orientacionContainer.classList.remove("hidden");
        } else {
            orientacionContainer.classList.add("hidden");
            orientacionInput.value = ""; // Limpiar valor de orientación
        }

        toggleTipoTroquelColumn();
    }

    // Muestra u oculta la columna de tipo de troquel en la tabla
    function toggleTipoTroquelColumn() {
        const inventario = inventarioSelect.value;
        const tipoTroquel = tipoTroquelSelect.value;

        if (inventario === "GRANDE" || inventario === "PEQUENO") {
            if (tipoTroquel === "TODOS") {
                columnaTipoTroquel.style.display = "";
                document.querySelectorAll("#troqueles-table td:nth-child(4)").forEach(td => td.style.display = "");
            } else {
                columnaTipoTroquel.style.display = "none";
                document.querySelectorAll("#troqueles-table td:nth-child(4)").forEach(td => td.style.display = "none");
            }
        } else {
            columnaTipoTroquel.style.display = "none";
            document.querySelectorAll("#troqueles-table td:nth-child(4)").forEach(td => td.style.display = "none");
        }
    }

    // Muestra solo la sección seleccionada: Troqueles, Placas o Clises
    function toggleSectionVisibility() {
        const selectedOption = listaSelector.value;

        troquelesSection.classList.toggle("hidden", selectedOption !== "TROQUELES");
        placasSection.classList.toggle("hidden", selectedOption !== "PLACAS");
        clisesSection.classList.toggle("hidden", selectedOption !== "CLISES");

        if (selectedOption === "CLISES") {
            fetchClises();
        } else if (selectedOption === "PLACAS") {
            fetchPlacas();
        } else {
            // TROQUELES
            fetchData();
        }
    }

    // Fetch troqueles normales
    function fetchTroqueles() {
        const inventario = inventarioSelect.value;
        const tipo = tipoTroquelSelect.value;

        let url = `${API_BASE_URL}/api/troqueles/filtrar/inventario?inventario=${inventario}`;
        if (tipo !== "TODOS") {
            url = `${API_BASE_URL}/api/troqueles/filtrar/inventario-y-tipo?inventario=${inventario}&tipo=${tipo}`;
        }

        fetch(url)
            .then(response => response.ok ? response.json() : Promise.reject(response.statusText))
            .then(data => mostrarResultados(data))
            .catch(error => mostrarError(error));
    }

    // Fetch sobres con filtros
    function fetchSobres() {
        const inventario = inventarioSelect.value;
        const tipoSobre = tipoSobreSelect.value;
        const orientacion = document.getElementById("orientacion").value;
        const tipoSolapa = tipoSolapaSelect.value;
        const ancho = document.getElementById("ancho").value;
        const largo = document.getElementById("largo").value;

        let url = `${API_BASE_URL}/api/sobres/filtrar?inventario=${inventario}`;
        if (tipoSobre) url += `&tipoSobre=${tipoSobre}`;
        if (orientacion) url += `&orientacion=${orientacion}`;
        if (tipoSolapa) url += `&tipoSolapa=${tipoSolapa}`;
        if (ancho) url += `&ancho=${ancho}`;
        if (largo) url += `&largo=${largo}`;

        console.log("URL sobres:", url);

        fetch(url)
            .then(response => response.json())
            .then(data => mostrarResultados(data, "SOBRE"))
            .catch(error => console.error("Error en sobres:", error));
    }

    // Fetch carpetas con filtros
    function fetchCarpetas() {
        const inventario = inventarioSelect.value;
        const ancho = document.getElementById("ancho").value;
        const largo = document.getElementById("largo").value;

        let url = `${API_BASE_URL}/api/carpetas/filtrar?inventario=${inventario}`;
        if (ancho) url += `&ancho=${ancho}`;
        if (largo) url += `&largo=${largo}`;

        console.log("URL carpetas:", url);

        fetch(url)
            .then(response => response.json())
            .then(data => mostrarResultados(data, "CARPETA"))
            .catch(error => console.error("Error en Carpetas:", error));
    }

    // Fetch fundas con filtros
    function fetchFundas() {
        const inventario = inventarioSelect.value;
        const ancho = document.getElementById("ancho").value;
        const largo = document.getElementById("largo").value;

        let url = `${API_BASE_URL}/api/fundas/filtrar?inventario=${inventario}`;
        if (ancho) url += `&ancho=${ancho}`;
        if (largo) url += `&largo=${largo}`;

        console.log("URL fundas:", url);

        fetch(url)
            .then(response => response.json())
            .then(data => mostrarResultados(data, "FUNDA"))
            .catch(error => console.error("Error en Fundas:", error));
    }

    // Fetch bolsas con filtros
    function fetchBolsas() {
        const inventario = inventarioSelect.value;
        const ancho = document.getElementById("ancho").value;
        const largo = document.getElementById("largo").value;
        const alto = document.getElementById("alto") ? document.getElementById("alto").value : "";

        let url = `${API_BASE_URL}/api/bolsas/filtrar?`;

        const params = new URLSearchParams();

        if (inventario) params.append("inventario", inventario);
        if (ancho) params.append("ancho", ancho);
        if (largo) params.append("largo", largo);
        if (alto) params.append("alto", alto);

        url += params.toString();

        console.log("URL bolsas:", url);

        fetch(url)
            .then(response => response.json())
            .then(data => mostrarResultados(data, "BOLSA"))
            .catch(error => console.error("Error en Bolsas:", error));
    }

    // Fetch cajas con filtros
    function fetchCajas() {
        const inventario = inventarioSelect.value;
        const ancho = document.getElementById("ancho").value;
        const largo = document.getElementById("largo").value;
        const alto = document.getElementById("alto") ? document.getElementById("alto").value : "";

        let url = `${API_BASE_URL}/api/cajas/filtrar?inventario=${inventario}`;
        if (ancho) url += `&ancho=${ancho}`;
        if (largo) url += `&largo=${largo}`;
        if (alto) url += `&alto=${alto}`;
        console.log("URL cajas:", url);

        fetch(url)
            .then(response => response.json())
            .then(data => mostrarResultados(data, "CAJA"))
            .catch(error => console.error("Error en Cajas:", error));
    }

    function fetchFormas() {
        const tipoForma = document.getElementById("tipoForma").value;
        const ancho = document.getElementById("ancho").value;
        const largo = document.getElementById("largo").value;
        const inventario = inventarioSelect.value;

        let url = `${API_BASE_URL}/api/formas/filtrar?`;
        const params = new URLSearchParams();

        if (inventario) params.append("inventario", inventario);
        if (tipoForma) params.append("tipoForma", tipoForma);
        if (ancho) params.append("ancho", ancho);
        if (largo) params.append("largo", largo);

        url += params.toString();
        console.log("URL formas:", url);

        fetch(url)
            .then(response => response.json())
            .then(data => mostrarResultados(data, "FORMA"))
            .catch(error => console.error("Error en Formas:", error));
    }

    // Muestra resultados en la tabla
    function mostrarResultados(data, tipo) {
        tableBody.innerHTML = "";
        if (!data || data.length === 0) {
            const nombreTipo = (tipo ? tipo.toLowerCase() : "troquel");
            let mensaje = `No se encontraron ${nombreTipo}s con esas características`;

            tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">${mensaje}</td></tr>`;
            return;
        }
        data.forEach(item => {
            tableBody.innerHTML += `
                <tr>
                    <td>${item.numero}</td>
                    <td>${item.tamanioCorteAncho} x ${item.tamanioCorteLargo}</td>
                    <td>${item.ancho} x ${item.largo}${item.alto ? ' x ' + item.alto : ''}</td>
                    <td>${item.tipo}</td>
                    <td>${item.descripcion}</td>
                </tr>`;
        });
        toggleTipoTroquelColumn();
    }

    // Muestra mensaje de error si falla la solicitud
    function mostrarError(error) {
        tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">Error en la solicitud: ${error}</td></tr>`;
    }

    // Dispatcher para decidir si cargar sobres o troqueles
    function fetchData() {
        tipoTroquelSelect.value === "SOBRE" ? fetchSobres() : fetchTroqueles();
    }

    // Dispatcher para carpetas
    function fetchCarpetaData() {
        tipoTroquelSelect.value === "CARPETA" ? fetchCarpetas() : fetchTroqueles();
    }

    // Dispatcher para fundas
    function fetchFundaData() {
        tipoTroquelSelect.value === "CARPETA" ? fetchFundas() : fetchTroqueles();
    }

    // Dispatcher para cajas
    function fetchCajasData() {
        tipoTroquelSelect.value === "CAJA" ? fetchCajas() : fetchTroqueles();
    }

    // Dispatcher para formas
    function fetchFormaData() {
        tipoTroquelSelect.value === "FORMA" ? fetchFormas() : fetchTroqueles();
    }

    // ✅ Filtro por descripción / búsqueda (Troqueles o Clises)
    descripcionInput.addEventListener("input", function () {

        // ✅ Si estás en CLISES, usa el backend para filtrar por nombre
        if (listaSelector.value === "CLISES") {
            fetchClises();
            return;
        }

        if (listaSelector.value === "PLACAS") {
            fetchPlacas();
            return;
        }

        // 👇 Si NO estás en CLISES, filtra Troqueles como antes
        const filtro = descripcionInput.value.trim().toLowerCase();
        const filas = document.querySelectorAll("#troqueles-table tbody tr");
        const mensajeNoResultados = document.getElementById("mensaje-no-resultados");
        if (mensajeNoResultados) mensajeNoResultados.remove();
        let encontrado = false;

        filas.forEach(fila => {
            const descripcion = fila.cells[4].textContent.toLowerCase();
            if (descripcion.includes(filtro)) {
                fila.style.display = "";
                encontrado = true;
            } else {
                fila.style.display = "none";
            }
        });

        if (!encontrado) {
            const mensaje = document.createElement("tr");
            mensaje.id = "mensaje-no-resultados";
            mensaje.innerHTML = `<td colspan="5" style="text-align:center; color:red;">No se encontraron coincidencias</td>`;
            tableBody.appendChild(mensaje);
        } else if (mensajeNoResultados) {
            mensajeNoResultados.remove();
        }
    });

    // Eventos principales de filtro y fetch
    inventarioSelect.addEventListener("change", fetchData);
    inventarioSelect.addEventListener("change", fetchCarpetaData);
    tipoTroquelSelect.addEventListener("change", () => {
        toggleFilters();
        fetchData();
        fetchCarpetaData();
        fetchFundaData();
        fetchCajas();
        fetchCajasData();
    });
    document.getElementById("orientacion").addEventListener("change", fetchData);
    tipoSobreSelect.addEventListener("change", () => { updateOrientacionVisibility(); fetchData(); });

    document.getElementById("alto").addEventListener("input", () => {
        if (tipoTroquelSelect.value === "BOLSA") {
            fetchBolsas();
        }
    });
    document.getElementById("alto").addEventListener("input", () => {
        if (tipoTroquelSelect.value === "CAJA") {
            fetchCajas();
        }
    });

    document.getElementById("ancho").addEventListener("input", () => {
        const tipo = tipoTroquelSelect.value;
        if (tipo === "SOBRE") {
            fetchSobres();
        } else if (tipo === "CARPETA") {
            fetchCarpetas();
        } else if (tipo === "FUNDA") {
            fetchFundas();
        } else if (tipo === "BOLSA") {
            fetchBolsas();
        } else if (tipo === "CAJA") {
            fetchCajas();
        } else if (tipo === "FORMA") {
            fetchFormas();
        }
    });

    document.getElementById("largo").addEventListener("input", () => {
        const tipo = tipoTroquelSelect.value;
        if (tipo === "SOBRE") {
            fetchSobres();
        } else if (tipo === "CARPETA") {
            fetchCarpetas();
        } else if (tipo === "FUNDA") {
            fetchFundas();
        } else if (tipo === "BOLSA") {
            fetchBolsas();
        } else if (tipo === "CAJA") {
            fetchCajas();
        } else if (tipo === "FORMA") {
            fetchFormas();
        }
    });

    document.getElementById("tipoForma").addEventListener("change", () => {
        if (tipoTroquelSelect.value === "FORMA") {
            fetchFormas();
        }
    });

    document.getElementById("alto").value = "";
    document.getElementById("tipoForma").value = "";

    tipoSolapaSelect.addEventListener("change", fetchData);
    listaSelector.addEventListener("change", toggleSectionVisibility);

    // Reinicia todos los filtros al cambiar inventario
    inventarioSelect.addEventListener("change", () => {
        tipoTroquelSelect.value = "TODOS";
        tipoTroquelSelect.dispatchEvent(new Event("change"));

        // Limpiar otros filtros
        tipoSobreSelect.value = "";
        document.getElementById("orientacion").value = "";
        tipoSolapaSelect.value = "";
        document.getElementById("ancho").value = "";
        document.getElementById("largo").value = "";
        document.getElementById("alto").value = "";
        document.getElementById("tipoForma").value = "";

        // Ocultar todos los filtros
        tipoSobreContainer.classList.add("hidden");
        orientacionContainer.classList.add("hidden");
        tipoSolapaContainer.classList.add("hidden");
        tamaniosContainer.classList.add("hidden");
        document.getElementById("alto-container").classList.add("hidden");
        document.getElementById("tipoForma-container").classList.add("hidden");
        tipoCarpetaContainer.classList.add("hidden");
    });

    // Carga inicial de datos
    fetchData();
    toggleSectionVisibility();


                                 // CLISES


    function mostrarClises(data) {
        const tbody = document.querySelector("#clises-table tbody");
        tbody.innerHTML = "";

        data.forEach(c => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${c.nombreCliente}</td>
                <td>${c.letra}</td>
                <td>${c.numero}</td>
                <td>${c.impresion}</td>
                <td>${c.repujado}</td>
            `;
            tbody.appendChild(tr);
        });
    }

    function fetchClises() {
        const nombre = descripcionInput.value.trim();

        let url = `${API_BASE_URL}/api/clises/buscar`;
        const params = new URLSearchParams();

        if (nombre) params.append("nombre", nombre);

        if (params.toString()) {
            url += `?${params.toString()}`;
        }

        fetch(url)
            .then(response => response.json())
            .then(data => mostrarClises(data))
            .catch(error => console.error("Error al obtener clises:", error));
    }

                                                 //PLACAS
        function mostrarPlacas(data) {
            const tbody = document.querySelector("#placas-table tbody");
            tbody.innerHTML = "";

            if (!data || data.length === 0) {
                tbody.innerHTML = `<tr><td colspan="4" style="text-align:center; color:red;">No se encontraron placas</td></tr>`;
                return;
            }

            data.forEach(p => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
            <td>${p.numero}</td>
            <td>${p.cliente}</td>
            <td>${p.descripcion}</td>
            <td>${p.cantidad}</td>
        `;
                tbody.appendChild(tr);
            });
        }

        function fetchPlacas() {
            const q = descripcionInput.value.trim();

            let url = `${API_BASE_URL}/api/placas/buscar`;
            const params = new URLSearchParams();

            // Como tu input es uno solo, lo usamos para buscar por cliente o por descripción
            if (q) {
                if (q) params.append("q", q);

            }

            if (params.toString()) {
                url += `?${params.toString()}`;
            }

            fetch(url)
                .then(response => response.json())
                .then(data => mostrarPlacas(data))
                .catch(error => console.error("Error al obtener placas:", error));
        }


});
