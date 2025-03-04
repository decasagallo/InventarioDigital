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

    // Mostrar u ocultar filtros de sobres según el tipo seleccionado
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

    // Controla la visibilidad del filtro de orientación
    function updateOrientacionVisibility() {
        const tipoSobre = tipoSobreSelect.value;
        if (tipoSobre === "RECTANGULAR" || tipoSobre === "MEDIO_SOBRE") {
            orientacionContainer.classList.remove("hidden");
        } else {
            orientacionContainer.classList.add("hidden");
        }
    }

    // Mostrar u ocultar la columna "Tipo de Troquel"
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

    // Cambiar la visibilidad de las secciones según la selección del listado
    function toggleSectionVisibility() {
        const selectedOption = listaSelector.value;
        if (selectedOption === "TROQUELES") {
            troquelesSection.classList.remove("hidden");
            placasSection.classList.add("hidden");
            clisesSection.classList.add("hidden");
        } else if (selectedOption === "PLACAS") {
            troquelesSection.classList.add("hidden");
            placasSection.classList.remove("hidden");
            clisesSection.classList.add("hidden");
        } else if (selectedOption === "CLISES") {
            troquelesSection.classList.add("hidden");
            placasSection.classList.add("hidden");
            clisesSection.classList.remove("hidden");
        }
    }

    // Obtener troqueles normales
    function fetchTroqueles() {
        const inventario = inventarioSelect.value;
        const tipo = tipoTroquelSelect.value;

        let url = `${API_BASE_URL}/api/troqueles/filtrar/inventario?inventario=${inventario}`;

        if (tipo !== "TODOS") {
            url = `${API_BASE_URL}/api/troqueles/filtrar/inventario-y-tipo?inventario=${inventario}&tipo=${tipo}`;
        }

        console.log("URL troqueles:", url); // Verifica la URL

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error en la solicitud: ${response.statusText}`);
                }
                return response.json();
            })
            .then(data => mostrarResultados(data))
            .catch(error => {
                console.error("Error al obtener troqueles:", error);
                tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">Error en la solicitud: ${error.message}</td></tr>`;
            });
    }

    // Obtener sobres con sus filtros específicos
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

        console.log("URL sobres:", url); // Verifica la URL

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error en la solicitud: ${response.statusText}`);
                }
                return response.json();
            })
            .then(data => mostrarResultados(data))
            .catch(error => {
                console.error("Error en sobres:", error);
                tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">Error en la solicitud: ${error.message}</td></tr>`;
            });
    }

    // Mostrar resultados en la tabla
    function mostrarResultados(data) {
        console.log(data); // Verifica el contenido de la respuesta

        tableBody.innerHTML = "";
        if (!data || data.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">No se encontraron resultados</td></tr>`;
        } else {
            // Solo procesar si data es un arreglo
            if (Array.isArray(data)) {
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
            } else {
                console.error("La respuesta no es un arreglo:", data);
                tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">Error en la respuesta del servidor</td></tr>`;
            }
        }
        toggleTipoTroquelColumn();
    }

    // Evento principal para determinar qué función llamar
    function fetchData() {
        if (tipoTroquelSelect.value === "SOBRE") {
            fetchSobres();
        } else {
            fetchTroqueles();
        }
    }

    // Event Listeners
    inventarioSelect.addEventListener("change", fetchData);
    tipoTroquelSelect.addEventListener("change", function () {
        toggleFilters();
        fetchData();
    });
    tipoSobreSelect.addEventListener("change", function () {
        updateOrientacionVisibility();
        fetchData();
    });
    tipoSolapaSelect.addEventListener("change", fetchData);
    document.getElementById("orientacion").addEventListener("change", fetchData);
    document.getElementById("ancho").addEventListener("input", fetchData);
    document.getElementById("largo").addEventListener("input", fetchData);
    listaSelector.addEventListener("change", toggleSectionVisibility);

    // Cargar datos al inicio
    fetchData();
    toggleSectionVisibility();
});
