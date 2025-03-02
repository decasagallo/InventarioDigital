document.addEventListener('DOMContentLoaded', () => {
    cargarTroqueles();
    manejarFiltrosAdicionales();
});

document.getElementById('inventario')?.addEventListener('change', cargarTroqueles);
document.getElementById('tipoTroquel')?.addEventListener('change', () => {
    cargarTroqueles();
    manejarFiltrosAdicionales();
});

document.getElementById('medioSobre')?.addEventListener('change', cargarTroqueles);

async function cargarTroqueles() {
    const inventario = document.getElementById('inventario').value;
    const tipoTroquel = document.getElementById('tipoTroquel').value;
    const orientacion = document.getElementById('orientacion')?.value || '';
    const tipoSolapa = document.getElementById('tipoSolapa')?.value || '';
    const ancho = document.getElementById('ancho')?.value || '';
    const largo = document.getElementById('largo')?.value || '';
    const medioSobre = document.getElementById('medioSobre')?.value || '';

    let url = `https://inventariodigital.onrender.com/api/troqueles/filtrar?inventario=${inventario}`;
    if (tipoTroquel) url += `&tipo=${tipoTroquel}`;
    if (orientacion) url += `&orientacion=${orientacion}`;
    if (tipoSolapa) url += `&tipoSolapa=${tipoSolapa}`;
    if (ancho) url += `&ancho=${ancho}`;
    if (largo) url += `&largo=${largo}`;
    if (medioSobre) url += `&medioSobre=${medioSobre}`;

    try {
        const response = await fetch(url);
        const troqueles = await response.json();
        actualizarTabla(troqueles);
    } catch (error) {
        console.error('Error al cargar los troqueles:', error);
    }
}

function actualizarTabla(troqueles) {
    const tbody = document.getElementById('troqueles-table').querySelector('tbody');
    tbody.innerHTML = '';

    if (troqueles.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5">No se encontraron troqueles con los filtros seleccionados.</td></tr>';
        return;
    }

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
}

function manejarFiltrosAdicionales() {
    const tipoTroquel = document.getElementById('tipoTroquel').value;
    const mostrar = tipoTroquel === 'SOBRE';

    document.getElementById('orientacion-container').classList.toggle('hidden', !mostrar);
    document.getElementById('tipoSolapa-container').classList.toggle('hidden', !mostrar);
    document.getElementById('tamanios-container').classList.toggle('hidden', !mostrar);
    document.getElementById('medioSobre-container').classList.toggle('hidden', !mostrar);
}
