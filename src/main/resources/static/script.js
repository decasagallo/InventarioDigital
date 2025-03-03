const API_BASE_URL = "https://inventariodigital.onrender.com";
// Usa 127.0.0.1 en lugar de localhost

document.addEventListener("DOMContentLoaded", function () {
    const tipoTroquelSelect = document.getElementById("tipoTroquel");
    const inventarioSelect = document.getElementById("inventario");
    const tipoSobreSelect = document.getElementById("tipoSobre");
    const tipoSobreContainer = document.getElementById("tipoSobre-container");
    const orientacionContainer = document.getElementById("orientacion-container");
    const tipoSolapaSelect = document.getElementById("tipoSolapa");
    const tipoSolapaContainer = document.getElementById("tipoSolapa-container");
    const tamaniosContainer = document.getElementById("tamanios-container");
    const tableBody = document.querySelector("#troqueles-table tbody");

    // Función para activar/desactivar los filtros exclusivos de "Sobre"
    function toggleSobreFilters() {
        if (tipoTroquelSelect.value === "SOBRE") {
            tipoSobreContainer.classList.remove("hidden");
            tipoSolapaContainer.classList.remove("hidden");
            tamaniosContainer.classList.remove("hidden");
            updateOrientacionVisibility(); // Actualiza visibilidad de orientación cuando el tipo es sobre
        } else {
            tipoSobreContainer.classList.add("hidden");
            orientacionContainer.classList.add("hidden");
            tipoSolapaContainer.classList.add("hidden");
            tamaniosContainer.classList.add("hidden");
        }
    }

    // Función para actualizar la visibilidad del filtro de orientación según el tipo de sobre
    function updateOrientacionVisibility() {
        const tipoSobre = tipoSobreSelect.value;

        // Solo muestra el filtro de orientación si es "rectangular" o "medio sobre"
        if (tipoSobre === "RECTANGULAR" || tipoSobre === "MEDIO_SOBRE") {
            orientacionContainer.classList.remove("hidden");
        } else {
            orientacionContainer.classList.add("hidden");
        }
    }

    // Función para obtener los troqueles por inventario y tipo
    function fetchTroqueles() {
        const tipo = tipoTroquelSelect.value;
        const inventario = inventarioSelect.value;
        const tipoSobre = tipoSobreSelect.value;
        const orientacion = document.getElementById("orientacion").value;
        const tipoSolapa = tipoSolapaSelect.value;
        const ancho = document.getElementById("ancho").value;
        const largo = document.getElementById("largo").value;

        let url;

        // Si el tipo seleccionado es "SOBRE" o si no se selecciona un tipo específico, aplicar los filtros para sobres
        if (tipo === "SOBRE") {
            url = `${API_BASE_URL}/api/sobres/filtrar?inventario=${inventario}&tipoSobre=${tipoSobre}&orientacion=${orientacion}&tipoSolapa=${tipoSolapa}&ancho=${ancho}&largo=${largo}`;
        } else if (tipo && tipo !== "TODOS") {
            // Endpoint para otros tipos de troqueles (cuando no es "todos")
            url = `${API_BASE_URL}/api/troqueles/filtrar/inventario-y-tipo?inventario=${inventario}&tipo=${tipo}`;
        } else {
            // Endpoint para obtener todos los troqueles (cuando el tipo es "todos")
            url = `${API_BASE_URL}/api/troqueles/filtrar/inventario?inventario=${inventario}`;
        }

        console.log("URL generada:", url); // Para verificar la URL generada

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                tableBody.innerHTML = "";

                // Verifica si no se encontraron resultados
                if (data.length === 0) {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <td colspan="5" style="text-align: center; color: red;">No se encontraron troqueles con esas especificaciones</td>
                    `;
                    tableBody.appendChild(row);
                } else {
                    data.forEach(troquel => {
                        const row = document.createElement("tr");
                        row.innerHTML = `
                            <td>${troquel.numero}</td>
                            <td>${troquel.tamanioCorteAncho} x ${troquel.tamanioCorteLargo}</td>
                            <td>${troquel.ancho} x ${troquel.largo}${troquel.alto ? ' x ' + troquel.alto : ''}</td>
                            <td>${troquel.tipo}</td>
                            <td>${troquel.descripcion}</td>
                        `;
                        tableBody.appendChild(row);
                    });
                }
            })
            .catch(error => console.error("Error al obtener los datos:", error));
    }

    // Función para obtener sobres sin aplicar el filtro
    function fetchSobres() {
        const inventario = inventarioSelect.value;
        const tipoSobre = tipoSobreSelect.value;
        const orientacion = document.getElementById("orientacion").value;
        const tipoSolapa = tipoSolapaSelect.value;
        const ancho = document.getElementById("ancho").value;
        const largo = document.getElementById("largo").value;

        // Si tipoSobre es "TODOS", llamamos a los sobres sin filtro
        let url = `${API_BASE_URL}/api/sobres/filtrar?inventario=${inventario}&tipoSobre=${tipoSobre}&orientacion=${orientacion}&tipoSolapa=${tipoSolapa}&ancho=${ancho}&largo=${largo}`;

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                // Llenar la tabla de sobres
                tableBody.innerHTML = "";
                if (data.length === 0) {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <td colspan="5" style="text-align: center; color: red;">No se encontraron sobres con esas especificaciones</td>
                    `;
                    tableBody.appendChild(row);
                } else {
                    data.forEach(sobre => {
                        const row = document.createElement("tr");
                        row.innerHTML = `
                            <td>${sobre.numero}</td>
                            <td>${sobre.tamanioCorteAncho} x ${sobre.tamanioCorteLargo}</td>
                            <td>${sobre.ancho} x ${sobre.largo}${sobre.alto ? ' x ' + sobre.alto : ''}</td>
                            <td>${sobre.tipo}</td>
                            <td>${sobre.descripcion}</td>
                        `;
                        tableBody.appendChild(row);
                    });
                }
            })
            .catch(error => console.error("Error al obtener los sobres:", error));
    }

    // Escuchar el cambio de los filtros y obtener los resultados
    inventarioSelect.addEventListener("change", function() {
        tipoTroquelSelect.value = "TODOS"; // Cambiar tipo a "TODOS" al seleccionar un nuevo inventario
        tipoTroquelSelect.selectedIndex = 0; // Restablecer la selección del tipo de troquel al valor predeterminado
        tipoSobreSelect.value = "TODOS"; // Resetear tipo de sobre a "TODOS"
        tipoSobreSelect.selectedIndex = 0; // Restablecer la selección de tipo de sobre al valor predeterminado
        tipoSolapaSelect.value = "TODOS"; // Resetear tipo de solapa a "TODOS"
        tipoSolapaSelect.selectedIndex = 0; // Restablecer la selección de tipo de solapa al valor predeterminado
        toggleSobreFilters(); // Verificar si se deben mostrar los filtros de "Sobre"
        updateOrientacionVisibility(); // Actualizar visibilidad de orientación
        fetchTroqueles(); // Recargar troqueles al cambiar el inventario
    });

    tipoTroquelSelect.addEventListener("change", function() {
        toggleSobreFilters(); // Verificar si se deben mostrar los filtros de "Sobre"
        fetchTroqueles(); // Recargar troqueles al cambiar el tipo de troquel
    });

    tipoSobreSelect.addEventListener("change", function() {
        if (tipoSobreSelect.value === "TODOS") {
            fetchTroqueles(); // Obtener todos los troqueles
            fetchSobres(); // Obtener sobres
        } else {
            fetchTroqueles(); // Recargar troqueles al cambiar el tipo de sobre
        }
        updateOrientacionVisibility(); // Actualizar visibilidad de orientación al cambiar el tipo de sobre
    });

    tipoSolapaSelect.addEventListener("change", function() {
        fetchTroqueles(); // Recargar troqueles al cambiar el tipo de solapa
    });

    document.getElementById("orientacion").addEventListener("change", fetchTroqueles);
    document.getElementById("ancho").addEventListener("input", fetchTroqueles);
    document.getElementById("largo").addEventListener("input", fetchTroqueles);

    // Llamada inicial para cargar los datos al cargar la página
    fetchTroqueles();
});
