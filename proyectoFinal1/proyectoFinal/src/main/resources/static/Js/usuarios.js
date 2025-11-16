
          function mostrarModal() {
              document.getElementById("modalRegistro").classList.remove("hidden");
          }

          function ocultarModal() {
              document.getElementById("modalRegistro").classList.add("hidden");
          }

          function filtrarTabla() {
              const input = document.getElementById("searchInput").value.toLowerCase();
              const filtroRol = document.getElementById("filtroRol").value;
              const filas = document.querySelectorAll("#tablaUsuariosBody tr");

              filas.forEach((fila) => {
                  const texto = fila.innerText.toLowerCase();
                  const rol = fila.querySelector("td:nth-child(6)").innerText.trim();

                  if (texto.includes(input) && (filtroRol === "" || rol === filtroRol)) {
                      fila.style.display = "";
                  } else {
                      fila.style.display = "none";
                  }
              });
          }






















    // Variables globales para la tabla
    let paginaActual = 1;
    let registrosPorPagina = 10;
    let datosOriginales = [];
    let datosFiltrados = [];
    let ordenActual = { columna: null, direccion: null };

    // Inicializar tooltips y cargar datos
    document.addEventListener('DOMContentLoaded', function() {
        // Inicializar tooltips
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });

        // Cargar datos de la tabla
        cargarDatosTabla();
        actualizarTabla();
    });

    // Función para cargar los datos de la tabla
    function cargarDatosTabla() {
        const filas = document.querySelectorAll('#tablaUsuariosBody .usuario-row');
        datosOriginales = [];

        filas.forEach(fila => {
            const datos = {
                id: fila.children[0].textContent.trim(),
                username: fila.children[1].textContent.trim(),
                nombre: fila.children[2].textContent.trim(),
                email: fila.children[3].textContent.trim(),
                celular: fila.children[4].textContent.trim(),
                roles: Array.from(fila.querySelectorAll('.role-badge')).map(badge => badge.getAttribute('data-role')),
                elemento: fila
            };
            datosOriginales.push(datos);
        });

        datosFiltrados = [...datosOriginales];
        document.getElementById('totalUsuarios').textContent = datosOriginales.length;
    }

    // Función de filtrado
    function filtrarTabla() {
        const busqueda = document.getElementById('searchInput').value.toLowerCase();
        const filtroRol = document.getElementById('filtroRol').value;

        datosFiltrados = datosOriginales.filter(usuario => {
            // Filtro por texto de búsqueda
            const coincideTexto = busqueda === '' ||
                usuario.username.toLowerCase().includes(busqueda) ||
                usuario.nombre.toLowerCase().includes(busqueda) ||
                usuario.email.toLowerCase().includes(busqueda) ||
                usuario.celular.toLowerCase().includes(busqueda);

            // Filtro por rol
            const coincideRol = filtroRol === '' || usuario.roles.includes(filtroRol);

            return coincideTexto && coincideRol;
        });

        paginaActual = 1; // Resetear a la primera página
        actualizarTabla();
    }

    // Función para cambiar registros por página
    function cambiarRegistrosPorPagina() {
        registrosPorPagina = parseInt(document.getElementById('registrosPorPagina').value);
        paginaActual = 1;
        actualizarTabla();
    }

    // Función para ordenar la tabla
    function ordenarTabla(columna) {
        const columnas = ['id', 'username', 'nombre', 'email'];
        const nombreColumna = columnas[columna];

        if (ordenActual.columna === columna) {
            // Cambiar dirección si es la misma columna
            ordenActual.direccion = ordenActual.direccion === 'asc' ? 'desc' : 'asc';
        } else {
            // Nueva columna, ordenar ascendente
            ordenActual.columna = columna;
            ordenActual.direccion = 'asc';
        }

        datosFiltrados.sort((a, b) => {
            let valorA = a[nombreColumna];
            let valorB = b[nombreColumna];

            // Convertir a número si es ID
            if (nombreColumna === 'id') {
                valorA = parseInt(valorA);
                valorB = parseInt(valorB);
            }

            if (ordenActual.direccion === 'asc') {
                return valorA > valorB ? 1 : -1;
            } else {
                return valorA < valorB ? 1 : -1;
            }
        });

        actualizarIconosOrden();
        actualizarTabla();
    }

    // Función para actualizar iconos de ordenamiento
    function actualizarIconosOrden() {
        // Resetear todos los iconos
        document.querySelectorAll('.table-header-custom th').forEach(th => {
            th.classList.remove('sort-asc', 'sort-desc');
        });

        // Aplicar clase al encabezado actual
        if (ordenActual.columna !== null) {
            const th = document.querySelectorAll('.table-header-custom th')[ordenActual.columna];
            th.classList.add(ordenActual.direccion === 'asc' ? 'sort-asc' : 'sort-desc');
        }
    }

    // Función principal para actualizar la tabla
    function actualizarTabla() {
        const tbody = document.getElementById('tablaUsuariosBody');
        const noResultados = document.getElementById('noResultados');

        // Ocultar todas las filas primero
        datosOriginales.forEach(usuario => {
            usuario.elemento.style.display = 'none';
        });

        if (datosFiltrados.length === 0) {
            // Mostrar mensaje de no resultados
            noResultados.classList.remove('d-none');
            actualizarInfoPaginacion(0, 0, 0);
            document.getElementById('paginacion').innerHTML = '';
            return;
        } else {
            noResultados.classList.add('d-none');
        }

        // Calcular índices de paginación
        const inicio = (paginaActual - 1) * registrosPorPagina;
        const fin = Math.min(inicio + registrosPorPagina, datosFiltrados.length);
        const totalPaginas = Math.ceil(datosFiltrados.length / registrosPorPagina);

        // Mostrar solo las filas de la página actual
        for (let i = inicio; i < fin; i++) {
            datosFiltrados[i].elemento.style.display = '';
        }

        // Actualizar información de paginación
        actualizarInfoPaginacion(inicio + 1, fin, datosFiltrados.length);

        // Generar botones de paginación
        generarPaginacion(totalPaginas);
    }

    // Función para actualizar información de paginación
    function actualizarInfoPaginacion(desde, hasta, total) {
        document.getElementById('mostrandoDesde').textContent = desde;
        document.getElementById('mostrandoHasta').textContent = hasta;
        document.getElementById('totalFiltrados').textContent = total;

        // Mostrar información de filtrado si es necesario
        const totalOriginal = document.getElementById('totalOriginal');
        const totalCompleto = document.getElementById('totalCompleto');

        if (total < datosOriginales.length) {
            totalOriginal.style.display = 'inline';
            totalCompleto.textContent = datosOriginales.length;
        } else {
            totalOriginal.style.display = 'none';
        }
    }

    // Función para generar botones de paginación
    function generarPaginacion(totalPaginas) {
        const paginacion = document.getElementById('paginacion');
        paginacion.innerHTML = '';

        if (totalPaginas <= 1) return;

        // Botón anterior
        const anterior = document.createElement('li');
        anterior.className = `page-item ${paginaActual === 1 ? 'disabled' : ''}`;
        anterior.innerHTML = `<a class="page-link" href="#" onclick="cambiarPagina(${paginaActual - 1})" aria-label="Anterior">
            <i class="bi bi-chevron-left"></i>
        </a>`;
        paginacion.appendChild(anterior);

        // Números de página
        const maxBotones = 5;
        let inicio = Math.max(1, paginaActual - Math.floor(maxBotones / 2));
        let fin = Math.min(totalPaginas, inicio + maxBotones - 1);

        if (fin - inicio < maxBotones - 1) {
            inicio = Math.max(1, fin - maxBotones + 1);
        }

        for (let i = inicio; i <= fin; i++) {
            const boton = document.createElement('li');
            boton.className = `page-item ${i === paginaActual ? 'active' : ''}`;
            boton.innerHTML = `<a class="page-link" href="#" onclick="cambiarPagina(${i})">${i}</a>`;
            paginacion.appendChild(boton);
        }

        // Botón siguiente
        const siguiente = document.createElement('li');
        siguiente.className = `page-item ${paginaActual === totalPaginas ? 'disabled' : ''}`;
        siguiente.innerHTML = `<a class="page-link" href="#" onclick="cambiarPagina(${paginaActual + 1})" aria-label="Siguiente">
            <i class="bi bi-chevron-right"></i>
        </a>`;
        paginacion.appendChild(siguiente);
    }

    // Función para cambiar de página
    function cambiarPagina(nuevaPagina) {
        const totalPaginas = Math.ceil(datosFiltrados.length / registrosPorPagina);

        if (nuevaPagina >= 1 && nuevaPagina <= totalPaginas) {
            paginaActual = nuevaPagina;
            actualizarTabla();
        }

        // Prevenir el comportamiento por defecto del enlace
        return false;
    }