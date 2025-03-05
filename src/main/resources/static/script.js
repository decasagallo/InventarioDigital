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
    const tipoCarpetaContainer = document.getElementById("tipoCarpeta-container");
    const tipoCarpetaSelect = document.getElementById("tipoCarpeta");


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


    // Mostrar u ocultar filtros de carpetas
    function toggleFiltersCarpeta() {
        if (tipoTroquelSelect.value === "CARPETA") {
            tipoCarpetaContainer.classList.remove("hidden");
        } else {
            tipoCarpetaContainer.classList.add("hidden");
        }
    }

    // Controla la visibilidad del filtro de orientación
    function updateOrientacionVisibility() {
        const tipoSobre = tipoSobreSelect.value;
        if (tipoSobre === "RECTANGULAR" || tipoSobre === "MEDIO_SOBRE") {
            orientacionContainer.classList.remove("hidden");
        } else {
            orientacionContainer.classList.add("hidden");
        }
        toggleTipoTroquelColumn();
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
        troquelesSection.classList.toggle("hidden", selectedOption !== "TROQUELES");
        placasSection.classList.toggle("hidden", selectedOption !== "PLACAS");
        clisesSection.classList.toggle("hidden", selectedOption !== "CLISES");
    }
    // Obtener troqueles normales
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

        console.log("URL sobres:", url);

        fetch(url)
            .then(response => response.json())
            .then(data => mostrarResultados(data))
            .catch(error => console.error("Error en sobres:", error));
    }

    // Obtener Carpetas con sus filtros específicos
    function fetchCarpetas() {
        const inventario = inventarioSelect.value;
        const tipoCarpeta = tipoCarpetaSelect.value;
        const ancho = document.getElementById("ancho").value;
        const largo = document.getElementById("largo").value;

        let url = `${API_BASE_URL}/api/carpetas/filtrar?inventario=${inventario}`;
        if (tipoCarpeta) url += `&tipoCarpeta=${tipoCarpeta}`;
        if (ancho) url += `&ancho=${ancho}`;
        if (largo) url += `&largo=${largo}`;

        console.log("URL carpetas:", url);

        fetch(url)
            .then(response => response.json())
            .then(data => mostrarResultados(data))
            .catch(error => console.error("Error en Carpetas:", error));
    }




// Mostrar resultados en la tabla
    function mostrarResultados(data) {
        tableBody.innerHTML = "";
        if (!data || data.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">No se encontraron sobres con esas caracteristicas</td></tr>`;
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

    function fetchCarpetaData() {
        tipoTroquelSelect.value === "CARPETA" ? fetchCarpetas() : fetchTroqueles();
    }



    descripcionInput.addEventListener("input", function () {
        const filtro = descripcionInput.value.trim().toLowerCase();
        const filas = document.querySelectorAll("#troqueles-table tbody tr");
        let encontrado = false;

        filas.forEach(fila => {
            const descripcion = fila.cells[4].textContent.toLowerCase();
            if (descripcion.includes(filtro)) {
                fila.style.display = "";  // Mostrar la fila
                encontrado = true;
            } else {
                fila.style.display = "none";  // Ocultar la fila
            }
        });

        // Si no se encontraron resultados, mostrar el mensaje
        const mensajeNoResultados = document.getElementById("mensaje-no-resultados");
        if (!encontrado) {
            if (!mensajeNoResultados) {
                const mensaje = document.createElement("tr");
                mensaje.id = "mensaje-no-resultados";
                mensaje.innerHTML = `<td colspan="5" style="text-align:center; color:red;">No se encontraron coincidencias</td>`;
                document.querySelector("#troqueles-table tbody").appendChild(mensaje);
            }
        } else {
            // Si se encuentran resultados, eliminar el mensaje si existe
            if (mensajeNoResultados) {
                mensajeNoResultados.remove();
            }
        }
    });

    inventarioSelect.addEventListener("change", fetchData);
    inventarioSelect.addEventListener("change", fetchCarpetaData);
    tipoTroquelSelect.addEventListener("change", () => { toggleFilters(); fetchData(); });
    tipoTroquelSelect.addEventListener("change", () => { toggleFiltersCarpeta();fetchCarpetaData();  });
    document.getElementById("orientacion").addEventListener("change", fetchData);
    tipoSobreSelect.addEventListener("change", () => { updateOrientacionVisibility(); fetchData(); });
    document.getElementById("ancho").addEventListener("input", fetchData);
    document.getElementById("largo").addEventListener("input", fetchData);
    tipoSolapaSelect.addEventListener("change", fetchData);
    listaSelector.addEventListener("change", toggleSectionVisibility);
    tipoCarpetaSelect.addEventListener("change", () => { fetchCarpetaData(); });
    inventarioSelect.addEventListener("change", () => {
        // Reiniciar filtros
        tipoTroquelSelect.value = "TODOS"; // Restablecer el tipo de troquel
        tipoSobreSelect.value = ""; // Restablecer el tipo de sobre
        document.getElementById("orientacion").value = ""; // Restablecer la orientación
        tipoSolapaSelect.value = ""; // Restablecer el tipo de solapa
        document.getElementById("ancho").value = ""; // Limpiar el ancho
        document.getElementById("largo").value = ""; // Limpiar el largo
        tipoCarpetaSelect.value = ""; // Restablecer el tipo de carpeta

        toggleFilters(); // Actualizar la visibilidad de filtros
        toggleFiltersCarpeta(); // Actualizar visibilidad de carpetas
        fetchData(); // Volver a cargar los troqueles sin filtros
        fetchCarpetaData(); // Volver a cargar carpetas sin filtros
    });


    // Cargar datos al inicio
    fetchData();
    fetchCarpetaData();
    toggleSectionVisibility();
});