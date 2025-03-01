// Función para cargar los troqueles desde la API
async function cargarTroqueles() {
    const inventario = document.getElementById('inventario').value;

    try {
        // Realizar la llamada a la API, asegurando que el valor de inventario esté correcto
        const response = await fetch(`http://localhost:8080/api/troqueles/filtrar?inventario=${inventario}`);
        const troqueles = await response.json();

        const tbody = document.getElementById('troqueles-table').getElementsByTagName('tbody')[0];
        tbody.innerHTML = ''; // Limpiar la tabla antes de agregar los nuevos datos

        // Iterar sobre los troqueles y agregar las filas a la tabla
        troqueles.forEach(troquel => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${troquel.numero}</td>
                <td>${troquel.inventario}</td>
                <td>${troquel.ancho}</td>
                <td>${troquel.alto || 'null'}</td>
                <td class="${!troquel.largo ? 'null' : ''}">${troquel.largo || 'null'}</td>
                <td>${troquel.tipo}</td>
                <td>${troquel.descripcion}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error al cargar los troqueles:', error);
    }
}

// Cargar los troqueles por defecto (inventario grande) cuando la página se cargue
document.addEventListener('DOMContentLoaded', () => {
    cargarTroqueles();
});

// Agregar un listener para que se recargue la tabla cuando cambie el inventario
document.getElementById('inventario').addEventListener('change', () => {
    cargarTroqueles();
});
