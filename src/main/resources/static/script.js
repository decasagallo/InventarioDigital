// Determina la URL base de la API dependiendo del entorno (local o producción)
const API_BASE_URL = window.location.hostname === "127.0.0.1" || window.location.hostname === "localhost"
    ? "http://localhost:8080"
    : "https://inventariodigital-1.onrender.com";

// Espera que el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", function () {
    let authToken = localStorage.getItem("authToken") || "";
    let authUsername = localStorage.getItem("authUsername") || "";

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
    const openTroquelModalButton = document.getElementById("openTroquelModal");
    const troquelModalOverlay = document.getElementById("troquelModalOverlay");
    const closeTroquelModalButton = document.getElementById("closeTroquelModal");
    const cancelTroquelModalButton = document.getElementById("cancelTroquelModal");
    const troquelForm = document.getElementById("troquelForm");
    const modalInventario = document.getElementById("modalInventario");
    const modalTipoTroquel = document.getElementById("modalTipoTroquel");
    const modalNumero = document.getElementById("modalNumero");
    const modalAlto = document.getElementById("modalAlto");
    const modalAltoContainer = document.getElementById("modalAltoContainer");
    const modalSobreFields = document.getElementById("modalSobreFields");
    const modalTipoSobre = document.getElementById("modalTipoSobre");
    const modalTipoSolapa = document.getElementById("modalTipoSolapa");
    const modalOrientacion = document.getElementById("modalOrientacion");
    const modalOrientacionContainer = document.getElementById("modalOrientacionContainer");
    const modalFormaFields = document.getElementById("modalFormaFields");
    const modalTipoForma = document.getElementById("modalTipoForma");
    const ultimoTroquelText = document.getElementById("ultimoTroquel");
    const numeroSugeridoText = document.getElementById("numeroSugerido");
    const troquelFormMessage = document.getElementById("troquelFormMessage");
    const openPlacaModalButton = document.getElementById("openPlacaModal");
    const placaModalOverlay = document.getElementById("placaModalOverlay");
    const closePlacaModalButton = document.getElementById("closePlacaModal");
    const cancelPlacaModalButton = document.getElementById("cancelPlacaModal");
    const placaForm = document.getElementById("placaForm");
    const placaNumero = document.getElementById("placaNumero");
    const placaCantidad = document.getElementById("placaCantidad");
    const placaCliente = document.getElementById("placaCliente");
    const placaDescripcion = document.getElementById("placaDescripcion");
    const placaSeleccionadaId = document.getElementById("placaSeleccionadaId");
    const placaClientesList = document.getElementById("placaClientesList");
    const placaSuggestions = document.getElementById("placaSuggestions");
    const ultimaPlacaText = document.getElementById("ultimaPlaca");
    const numeroPlacaSugeridoText = document.getElementById("numeroPlacaSugerido");
    const placaFormMessage = document.getElementById("placaFormMessage");
    const openCliseModalButton = document.getElementById("openCliseModal");
    const cliseModalOverlay = document.getElementById("cliseModalOverlay");
    const closeCliseModalButton = document.getElementById("closeCliseModal");
    const cancelCliseModalButton = document.getElementById("cancelCliseModal");
    const cliseForm = document.getElementById("cliseForm");
    const cliseNombreCliente = document.getElementById("cliseNombreCliente");
    const cliseImpresion = document.getElementById("cliseImpresion");
    const cliseRepujado = document.getElementById("cliseRepujado");
    const cliseClientesList = document.getElementById("cliseClientesList");
    const cliseSuggestions = document.getElementById("cliseSuggestions");
    const cliseFormMessage = document.getElementById("cliseFormMessage");
    const openLoginModalButton = document.getElementById("openLoginModal");
    const logoutButton = document.getElementById("logoutButton");
    const authUser = document.getElementById("authUser");
    const loginModalOverlay = document.getElementById("loginModalOverlay");
    const closeLoginModalButton = document.getElementById("closeLoginModal");
    const cancelLoginModalButton = document.getElementById("cancelLoginModal");
    const loginForm = document.getElementById("loginForm");
    const loginUsername = document.getElementById("loginUsername");
    const loginPassword = document.getElementById("loginPassword");
    const loginMessage = document.getElementById("loginMessage");

    function authHeaders(extraHeaders = {}) {
        return authToken ? { ...extraHeaders, "X-Auth-Token": authToken } : extraHeaders;
    }

    function estaAutenticado() {
        return Boolean(authToken);
    }

    function actualizarVistaAuth() {
        const autenticado = estaAutenticado();
        openLoginModalButton.classList.toggle("hidden", autenticado);
        logoutButton.classList.toggle("hidden", !autenticado);
        authUser.classList.toggle("hidden", !autenticado);
        authUser.textContent = autenticado ? authUsername : "";
        document.querySelectorAll(".auth-required").forEach(element => {
            element.classList.toggle("hidden", !autenticado);
        });
        toggleSectionVisibility();
    }

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
        openTroquelModalButton.classList.toggle("hidden", selectedOption !== "TROQUELES" || !estaAutenticado());

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
                    <td>${item.numero}${item.sufijo ? item.sufijo : ""}</td>
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

    function abrirModalTroquel() {
        troquelForm.reset();
        modalInventario.value = inventarioSelect.value;
        modalTipoTroquel.value = "";
        troquelFormMessage.textContent = "";
        troquelFormMessage.classList.remove("success");
        actualizarCamposPorTipoModal();
        actualizarNumeroSugerido();
        troquelModalOverlay.classList.remove("hidden");
        modalTipoTroquel.focus();
    }

    function cerrarModalTroquel() {
        troquelModalOverlay.classList.add("hidden");
    }

    function actualizarCamposPorTipoModal() {
        const tipo = modalTipoTroquel.value;
        const requiereAlto = tipo === "BOLSA" || tipo === "CAJA";
        const esSobre = tipo === "SOBRE";
        const esForma = tipo === "FORMA";

        modalAltoContainer.classList.toggle("hidden", !requiereAlto);
        modalAlto.required = requiereAlto;
        if (!requiereAlto) {
            modalAlto.value = "";
        }

        modalSobreFields.classList.toggle("hidden", !esSobre);
        modalTipoSobre.required = esSobre;
        modalTipoSolapa.required = esSobre;
        if (!esSobre) {
            modalTipoSobre.value = "";
            modalTipoSolapa.value = "";
            modalOrientacion.value = "";
        }

        modalFormaFields.classList.toggle("hidden", !esForma);
        modalTipoForma.required = esForma;
        if (!esForma) {
            modalTipoForma.value = "";
        }

        actualizarOrientacionModal();
    }

    function actualizarOrientacionModal() {
        const mostrarOrientacion = modalTipoTroquel.value === "SOBRE" &&
            (modalTipoSobre.value === "RECTANGULAR" || modalTipoSobre.value === "MEDIO_SOBRE");

        modalOrientacionContainer.classList.toggle("hidden", !mostrarOrientacion);
        modalOrientacion.required = mostrarOrientacion;
        if (!mostrarOrientacion) {
            modalOrientacion.value = "";
        }
    }

    function actualizarNumeroSugerido() {
        fetch(`${API_BASE_URL}/api/troqueles/ultimo-numero?inventario=${modalInventario.value}`)
            .then(response => response.ok ? response.json() : Promise.reject(response.statusText))
            .then(ultimoNumero => {
                const sugerido = Number(ultimoNumero) + 1;
                ultimoTroquelText.textContent = ultimoNumero;
                numeroSugeridoText.textContent = sugerido;
                modalNumero.value = sugerido;
            })
            .catch(() => {
                ultimoTroquelText.textContent = "0";
                numeroSugeridoText.textContent = "1";
                modalNumero.value = "1";
            });
    }

    function obtenerValorDecimal(id) {
        const valor = document.getElementById(id).value;
        return valor === "" ? null : Number(valor);
    }

    function guardarTroquel(event) {
        event.preventDefault();
        troquelFormMessage.textContent = "";
        troquelFormMessage.classList.remove("success");

        const payload = {
            inventario: modalInventario.value,
            tipo: modalTipoTroquel.value,
            numero: Number(modalNumero.value),
            sufijo: document.getElementById("modalSufijo").value.trim(),
            descripcion: document.getElementById("modalDescripcion").value.trim(),
            tamanioCorteAncho: obtenerValorDecimal("modalCorteAncho"),
            tamanioCorteLargo: obtenerValorDecimal("modalCorteLargo"),
            ancho: obtenerValorDecimal("modalCerradoAncho"),
            largo: obtenerValorDecimal("modalCerradoLargo"),
            alto: obtenerValorDecimal("modalAlto"),
            tipoSobre: modalTipoSobre.value || null,
            orientacion: modalOrientacion.value || null,
            tipoSolapa: modalTipoSolapa.value || null,
            tipoForma: modalTipoForma.value || null
        };

        fetch(`${API_BASE_URL}/api/troqueles`, {
            method: "POST",
            headers: authHeaders({ "Content-Type": "application/json" }),
            body: JSON.stringify(payload)
        })
            .then(response => response.ok ? response.json() : response.text().then(text => Promise.reject(text)))
            .then(() => {
                troquelFormMessage.textContent = "Troquel guardado correctamente";
                troquelFormMessage.classList.add("success");
                inventarioSelect.value = modalInventario.value;
                tipoTroquelSelect.value = "TODOS";
                fetchData();
                setTimeout(cerrarModalTroquel, 700);
            })
            .catch(error => {
                troquelFormMessage.textContent = `No se pudo guardar el troquel: ${error}`;
            });
    }

    openTroquelModalButton.addEventListener("click", abrirModalTroquel);
    closeTroquelModalButton.addEventListener("click", cerrarModalTroquel);
    cancelTroquelModalButton.addEventListener("click", cerrarModalTroquel);
    modalInventario.addEventListener("change", actualizarNumeroSugerido);
    modalTipoTroquel.addEventListener("change", actualizarCamposPorTipoModal);
    modalTipoSobre.addEventListener("change", actualizarOrientacionModal);
    troquelForm.addEventListener("submit", guardarTroquel);
    troquelModalOverlay.addEventListener("click", event => {
        if (event.target === troquelModalOverlay) {
            cerrarModalTroquel();
        }
    });

    document.addEventListener("keydown", event => {
        if (event.key === "Escape" && !troquelModalOverlay.classList.contains("hidden")) {
            cerrarModalTroquel();
        }
    });

    function abrirModalPlaca() {
        placaForm.reset();
        placaSeleccionadaId.value = "";
        placaNumero.disabled = false;
        placaCantidad.value = "1";
        placaFormMessage.textContent = "";
        placaFormMessage.classList.remove("success");
        placaSuggestions.classList.add("hidden");
        placaSuggestions.innerHTML = "";
        cargarClientesPlaca();
        actualizarNumeroPlacaSugerido();
        placaModalOverlay.classList.remove("hidden");
        placaCliente.focus();
    }

    function cerrarModalPlaca() {
        placaModalOverlay.classList.add("hidden");
    }

    function actualizarNumeroPlacaSugerido() {
        fetch(`${API_BASE_URL}/api/placas/ultimo-numero`)
            .then(response => response.ok ? response.json() : Promise.reject(response.statusText))
            .then(ultimoNumero => {
                const sugerido = Number(ultimoNumero) + 1;
                ultimaPlacaText.textContent = ultimoNumero;
                numeroPlacaSugeridoText.textContent = sugerido;
                placaNumero.value = sugerido;
            })
            .catch(() => {
                ultimaPlacaText.textContent = "0";
                numeroPlacaSugeridoText.textContent = "1";
                placaNumero.value = "1";
            });
    }

    function guardarPlaca(event) {
        event.preventDefault();
        placaFormMessage.textContent = "";
        placaFormMessage.classList.remove("success");

        const payload = {
            numero: Number(placaNumero.value),
            cliente: placaCliente.value.trim(),
            descripcion: placaDescripcion.value.trim(),
            cantidad: Number(placaCantidad.value)
        };

        const placaId = placaSeleccionadaId.value;
        const url = placaId
            ? `${API_BASE_URL}/api/placas/${placaId}/sumar?cantidad=${payload.cantidad}`
            : `${API_BASE_URL}/api/placas`;

        fetch(url, {
            method: "POST",
            headers: authHeaders({ "Content-Type": "application/json" }),
            body: placaId ? null : JSON.stringify(payload)
        })
            .then(response => response.ok ? response.json() : response.text().then(text => Promise.reject(text)))
            .then(() => {
                placaFormMessage.textContent = "Placa guardada correctamente";
                placaFormMessage.classList.add("success");
                listaSelector.value = "PLACAS";
                toggleSectionVisibility();
                setTimeout(cerrarModalPlaca, 700);
            })
            .catch(error => {
                placaFormMessage.textContent = `No se pudo guardar la placa: ${error}`;
            });
    }

    function cargarClientesPlaca() {
        fetch(`${API_BASE_URL}/api/placas/buscar`)
            .then(response => response.json())
            .then(data => {
                const clientes = [...new Set(data.map(placa => placa.cliente).filter(Boolean))];
                placaClientesList.innerHTML = "";
                clientes.forEach(cliente => {
                    const option = document.createElement("option");
                    option.value = cliente;
                    placaClientesList.appendChild(option);
                });
            })
            .catch(error => console.error("Error al cargar clientes de placas:", error));
    }

    function limpiarSeleccionPlaca() {
        placaSeleccionadaId.value = "";
        placaNumero.disabled = false;
        placaFormMessage.textContent = "";
        placaFormMessage.classList.remove("success");
    }

    function buscarSugerenciasPlaca() {
        const cliente = placaCliente.value.trim();
        const descripcion = placaDescripcion.value.trim();
        placaSuggestions.classList.add("hidden");
        placaSuggestions.innerHTML = "";

        if (cliente.length < 3 && descripcion.length < 3) {
            return;
        }

        const params = new URLSearchParams();
        if (cliente) params.append("cliente", cliente);
        if (descripcion) params.append("descripcion", descripcion);

        fetch(`${API_BASE_URL}/api/placas/sugerencias?${params.toString()}`)
            .then(response => response.json())
            .then(data => {
                if (!data || data.length === 0) {
                    return;
                }

                placaSuggestions.innerHTML = "<p>Placas parecidas encontradas:</p>";
                data.forEach(placa => {
                    const button = document.createElement("button");
                    button.type = "button";
                    button.className = "suggestion-button";
                    button.textContent = `#${placa.numero} - ${placa.cliente} - ${placa.descripcion} (${placa.cantidad})`;
                    button.addEventListener("click", () => {
                        placaSeleccionadaId.value = placa.id;
                        placaNumero.value = placa.numero;
                        placaNumero.disabled = true;
                        placaCliente.value = placa.cliente;
                        placaDescripcion.value = placa.descripcion;
                        placaSuggestions.classList.add("hidden");
                        placaSuggestions.innerHTML = "";
                        placaFormMessage.textContent = "Se sumará la cantidad a esta placa existente";
                        placaFormMessage.classList.add("success");
                    });
                    placaSuggestions.appendChild(button);
                });
                placaSuggestions.classList.remove("hidden");
            })
            .catch(error => console.error("Error al buscar sugerencias de placas:", error));
    }

    openPlacaModalButton.addEventListener("click", abrirModalPlaca);
    closePlacaModalButton.addEventListener("click", cerrarModalPlaca);
    cancelPlacaModalButton.addEventListener("click", cerrarModalPlaca);
    placaForm.addEventListener("submit", guardarPlaca);
    placaCliente.addEventListener("input", limpiarSeleccionPlaca);
    placaDescripcion.addEventListener("input", limpiarSeleccionPlaca);
    placaCliente.addEventListener("blur", buscarSugerenciasPlaca);
    placaDescripcion.addEventListener("blur", buscarSugerenciasPlaca);
    placaModalOverlay.addEventListener("click", event => {
        if (event.target === placaModalOverlay) {
            cerrarModalPlaca();
        }
    });

    document.addEventListener("keydown", event => {
        if (event.key === "Escape" && !placaModalOverlay.classList.contains("hidden")) {
            cerrarModalPlaca();
        }
    });

    function abrirModalClise() {
        cliseForm.reset();
        cliseImpresion.value = "0";
        cliseRepujado.value = "0";
        cliseFormMessage.textContent = "";
        cliseFormMessage.classList.remove("success");
        cliseSuggestions.classList.add("hidden");
        cliseSuggestions.innerHTML = "";
        cargarClientesClise();
        cliseModalOverlay.classList.remove("hidden");
        cliseNombreCliente.focus();
    }

    function cerrarModalClise() {
        cliseModalOverlay.classList.add("hidden");
    }

    function guardarClise(event) {
        event.preventDefault();
        cliseFormMessage.textContent = "";
        cliseFormMessage.classList.remove("success");

        const impresion = Number(cliseImpresion.value);
        const repujado = Number(cliseRepujado.value);
        if (impresion === 0 && repujado === 0) {
            cliseFormMessage.textContent = "Ingresa al menos una cantidad de impresión o repujado";
            return;
        }

        const params = new URLSearchParams();
        params.append("nombreCliente", cliseNombreCliente.value.trim());
        params.append("impresion", impresion);
        params.append("repujado", repujado);

        fetch(`${API_BASE_URL}/api/clises/agregar-cantidades?${params.toString()}`, {
            method: "POST",
            headers: authHeaders()
        })
            .then(response => response.ok ? response.json() : response.text().then(text => Promise.reject(text)))
            .then(() => {
                cliseFormMessage.textContent = "Clisé guardado correctamente";
                cliseFormMessage.classList.add("success");
                listaSelector.value = "CLISES";
                toggleSectionVisibility();
                setTimeout(cerrarModalClise, 700);
            })
            .catch(error => {
                cliseFormMessage.textContent = `No se pudo guardar el clisé: ${error}`;
            });
    }

    function cargarClientesClise() {
        fetch(`${API_BASE_URL}/api/clises/buscar`)
            .then(response => response.json())
            .then(data => {
                cliseClientesList.innerHTML = "";
                data.forEach(cliente => {
                    const option = document.createElement("option");
                    option.value = cliente.nombreCliente;
                    cliseClientesList.appendChild(option);
                });
            })
            .catch(error => console.error("Error al cargar clientes de clisés:", error));
    }

    function buscarSugerenciasClise() {
        const nombre = cliseNombreCliente.value.trim();
        cliseSuggestions.classList.add("hidden");
        cliseSuggestions.innerHTML = "";

        if (nombre.length < 3) {
            return;
        }

        fetch(`${API_BASE_URL}/api/clises/sugerencias?nombre=${encodeURIComponent(nombre)}`)
            .then(response => response.json())
            .then(data => {
                if (!data || data.length === 0) {
                    return;
                }

                cliseSuggestions.innerHTML = "<p>Clientes parecidos encontrados:</p>";
                data.forEach(cliente => {
                    const button = document.createElement("button");
                    button.type = "button";
                    button.className = "suggestion-button";
                    button.textContent = cliente.nombreCliente;
                    button.addEventListener("click", () => {
                        cliseNombreCliente.value = cliente.nombreCliente;
                        cliseSuggestions.classList.add("hidden");
                        cliseSuggestions.innerHTML = "";
                    });
                    cliseSuggestions.appendChild(button);
                });
                cliseSuggestions.classList.remove("hidden");
            })
            .catch(error => console.error("Error al buscar sugerencias de clisés:", error));
    }

    function abrirLoginModal() {
        loginForm.reset();
        loginMessage.textContent = "";
        loginModalOverlay.classList.remove("hidden");
        loginUsername.focus();
    }

    function cerrarLoginModal() {
        loginModalOverlay.classList.add("hidden");
    }

    function iniciarSesion(event) {
        event.preventDefault();
        loginMessage.textContent = "";

        fetch(`${API_BASE_URL}/api/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                username: loginUsername.value.trim(),
                password: loginPassword.value
            })
        })
            .then(response => response.ok ? response.json() : response.text().then(text => Promise.reject(text)))
            .then(data => {
                authToken = data.token;
                authUsername = data.username;
                localStorage.setItem("authToken", authToken);
                localStorage.setItem("authUsername", authUsername);
                cerrarLoginModal();
                actualizarVistaAuth();
            })
            .catch(error => {
                loginMessage.textContent = `No se pudo iniciar sesión: ${error}`;
            });
    }

    function cerrarSesion() {
        fetch(`${API_BASE_URL}/api/auth/logout`, {
            method: "POST",
            headers: authHeaders()
        }).finally(() => {
            authToken = "";
            authUsername = "";
            localStorage.removeItem("authToken");
            localStorage.removeItem("authUsername");
            actualizarVistaAuth();
        });
    }

    openCliseModalButton.addEventListener("click", abrirModalClise);
    closeCliseModalButton.addEventListener("click", cerrarModalClise);
    cancelCliseModalButton.addEventListener("click", cerrarModalClise);
    cliseForm.addEventListener("submit", guardarClise);
    cliseNombreCliente.addEventListener("blur", buscarSugerenciasClise);
    cliseModalOverlay.addEventListener("click", event => {
        if (event.target === cliseModalOverlay) {
            cerrarModalClise();
        }
    });

    openLoginModalButton.addEventListener("click", abrirLoginModal);
    closeLoginModalButton.addEventListener("click", cerrarLoginModal);
    cancelLoginModalButton.addEventListener("click", cerrarLoginModal);
    loginForm.addEventListener("submit", iniciarSesion);
    logoutButton.addEventListener("click", cerrarSesion);
    loginModalOverlay.addEventListener("click", event => {
        if (event.target === loginModalOverlay) {
            cerrarLoginModal();
        }
    });

    document.addEventListener("keydown", event => {
        if (event.key === "Escape" && !cliseModalOverlay.classList.contains("hidden")) {
            cerrarModalClise();
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
        if (tipoCarpetaContainer) tipoCarpetaContainer.classList.add("hidden");
    });

    // Carga inicial de datos
    fetchData();
    toggleSectionVisibility();
    actualizarVistaAuth();


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
