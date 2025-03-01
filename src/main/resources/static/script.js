// Función para cargar los troqueles desde la API
async function cargarTroqueles() {
    const inventario = document.getElementById('inventario').value;
    const tipoTroquel = document.getElementById('tipoTroquel').value;
    const orientacion = document.getElementById('orientacion') ? document.getElementById('orientacion').value : '';

    try {
        // Realizar la llamada a la API con los parámetros de inventario, tipo y orientación
        let url = `https://inventariodigital.onrender.com/api/troqueles/filtrar?inventario=${inventario}`;
        if (tipoTroquel) {
            url += `&tipo=${tipoTroquel}`;
        }
        if (orientacion) {
            url += `&orientacion=${orientacion}`;
        }

        const response = await fetch(url);
        const troqueles = await response.json();

        const tbody = document.getElementById('troqueles-table').getElementsByTagName('tbody')[0];
        tbody.innerHTML = ''; // Limpiar la tabla antes de agregar los nuevos datos

        // Iterar sobre los troqueles y agregar las filas a la tabla
        troqueles.forEach(troquel => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${troquel.numero}</td>
                <td>${troquel.tamanioCorteAncho} x ${troquel.tamanioCorteLargo}</td>
                <td>${troquel.ancho} x ${troquel.largo}${troquel.alto ? ' x ' + troquel.alto : ''}</td>
                <td>${troquel.tipo}</td>
                <td>${troquel.descripcion}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error al cargar los troqueles:', error);
    }
}

// Función para mostrar/ocultar el filtro de orientación según el tipo de troquel seleccionado
function mostrarFiltroOrientacion() {
    const tipoTroquel = document.getElementById('tipoTroquel').value;
    const orientacionDiv = document.getElementById('orientacionDiv');

    // Si el tipo de troquel es "Sobre", mostramos el filtro de orientación
    if (tipoTroquel === 'SOBRE') {
        orientacionDiv.style.display = 'block';
    } else {
        orientacionDiv.style.display = 'none';
    }

    // Recargar los troqueles cuando cambien los filtros
    cargarTroqueles();
}

// Cargar los troqueles por defecto (inventario grande) cuando la página se cargue
document.addEventListener('DOMContentLoaded', () => {
    cargarTroqueles();
});

// Agregar un listener para que se recargue la tabla cuando cambie el inventario, tipo de troquel o orientación
document.getElementById('inventario').addEventListener('change', () => {
    cargarTroqueles();
});
document.getElementById('tipoTroquel').addEventListener('change', mostrarFiltroOrientacion);
document.getElementById('orientacion').addEventListener('change', () => {
    cargarTroqueles();
});
