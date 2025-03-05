const API_BASE_URL = window.location.hostname === "127.0.0.1" || window.location.hostname === "localhost"
    ? "http://127.0.0.1:8080" // Modo local
    : "https://inventariodigital.onrender.com"; // Modo producción en Render

document.addEventListener("DOMContentLoaded", function () {
    // Elementos del DOM
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

    function toggleFilters() {
        if (tipoTroquelSelect.value === "SOBRE") {
            tipoSobreContainer.classList.remove("hidden");
            tipoSolapaContainer.classList.remove("hidden");
            tamaniosContainer.classList.remove("hidden");
            updateOrientacionVisibility();
        } else {
            tipoSobreContainer.classList.add("hidden");
            orientacionContainer.classList.add("hidden");
            tipoSolapaContainer.classList.add("hidden");
            tamaniosContainer.classList.add("hidden");
        }
        toggleTipoTroquelColumn();
    }

    function updateOrientacionVisibility() {
        const tipoSobre = tipoSobreSelect.value;
        if (tipoSobre === "RECTANGULAR" || tipoSobre === "MEDIO_SOBRE") {
            orientacionContainer.classList.remove("hidden");
        } else {
            orientacionContainer.classList.add("hidden");
        }
    }

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

    function toggleSectionVisibility() {
        const selectedOption = listaSelector.value;
        troquelesSection.classList.toggle("hidden", selectedOption !== "TROQUELES");
        placasSection.classList.toggle("hidden", selectedOption !== "PLACAS");
        clisesSection.classList.toggle("hidden", selectedOption !== "CLISES");
    }

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

    function fetchSobres() {
        const params = new URLSearchParams({
            inventario: inventarioSelect.value,
            tipoSobre: tipoSobreSelect.value,
            orientacion: document.getElementById("orientacion").value,
            tipoSolapa: tipoSolapaSelect.value,
            ancho: document.getElementById("ancho").value,
            largo: document.getElementById("largo").value
        });

        fetch(`${API_BASE_URL}/api/sobres/filtrar?${params}`)
            .then(response => response.ok ? response.json() : Promise.reject(response.statusText))
            .then(data => mostrarResultados(data))
            .catch(error => mostrarError(error));
    }

    function mostrarResultados(data) {
        tableBody.innerHTML = "";
        if (!data || data.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">No se encontraron resultados</td></tr>`;
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

    function mostrarError(error) {
        tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">Error en la solicitud: ${error}</td></tr>`;
    }

    function fetchData() {
        tipoTroquelSelect.value === "SOBRE" ? fetchSobres() : fetchTroqueles();
    }

    descripcionInput.addEventListener("input", function () {
        const filtro = descripcionInput.value.trim().toLowerCase();
        document.querySelectorAll("#troqueles-table tbody tr").forEach(fila => {
            const descripcion = fila.cells[4].textContent.toLowerCase();
            fila.style.display = descripcion.includes(filtro) ? "" : "none";
        });
    });

    inventarioSelect.addEventListener("change", fetchData);
    tipoTroquelSelect.addEventListener("change", () => { toggleFilters(); fetchData(); });
    tipoSobreSelect.addEventListener("change", () => { updateOrientacionVisibility(); fetchData(); });
    tipoSolapaSelect.addEventListener("change", fetchData);
    listaSelector.addEventListener("change", toggleSectionVisibility);
    fetchData();
    toggleSectionVisibility();
});
