// Validación personalizada del formulario de agregar usuario


document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('#modalAgregarUsuario .needs-validation');
    const password = form.querySelector('#usuarioPassword');
    const passwordConfirm = form.querySelector('#usuarioPasswordConfirm');
    const confirmPasswordFeedback = form.querySelector('#confirmPasswordFeedback');


    const validateField = (field) => {

        if (field === passwordConfirm) {
            if (password.value === passwordConfirm.value && passwordConfirm.value.length >= 6) {
                passwordConfirm.classList.remove('is-invalid');
                passwordConfirm.classList.add('is-valid');
                confirmPasswordFeedback.textContent = '';
            } else {
                passwordConfirm.classList.remove('is-valid');
                passwordConfirm.classList.add('is-invalid');
                if (passwordConfirm.value.length < 6) {
                    confirmPasswordFeedback.textContent = 'La contraseña debe tener al menos 6 caracteres.';
                } else {
                    confirmPasswordFeedback.textContent = 'Las contraseñas no coinciden.';
                }
            }
            return;
        }

        if (!field.checkValidity()) {
            field.classList.add('is-invalid');
            field.classList.remove('is-valid');
        } else {
            field.classList.add('is-valid');
            field.classList.remove('is-invalid');
        }
    };


    form.querySelectorAll('input:not([type="date"]), select, textarea').forEach(field => {
        field.addEventListener('input', () => {

            if (field === password) {
                validateField(passwordConfirm);
            }
            validateField(field);
        });
    });


    form.querySelectorAll('input, select, textarea').forEach(field => {
        field.addEventListener('blur', () => {
            validateField(field);

            if (field === password) {
                validateField(passwordConfirm);
            }
        });
    });
    form.addEventListener('submit', event => {
        validateField(passwordConfirm);

        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();


            form.querySelectorAll('input:invalid, select:invalid, textarea:invalid').forEach(invalidField => {
                validateField(invalidField);
            });

        } else {

            console.log('Formulario válido, enviando datos...');
            event.preventDefault();
            ;
        }

        form.classList.add('was-validated');
    }, false);

    const modalElement = document.getElementById('modalAgregarUsuario');
    modalElement.addEventListener('hidden.bs.modal', function () {
        form.classList.remove('was-validated');
        form.querySelectorAll('.is-valid, .is-invalid').forEach(field => {
            field.classList.remove('is-valid', 'is-invalid');
        });
        form.reset();
    });

});


let rolesDisponibles = [];

document.addEventListener("DOMContentLoaded", async () => {
    const API_USUARIOS = "/api/admin/usuarios";
    const API_ROLES = "/api/admin/usuarios/roles";
    const tbody = document.querySelector("#pc-dt-simple tbody");

    try {
        // 1️⃣ Obtener roles disponibles
        const resRoles = await fetch(API_ROLES);
        if (!resRoles.ok) throw new Error("Error al obtener roles");
        const rolesDisponibles = await resRoles.json();
        console.log("Roles disponibles:", rolesDisponibles);

        // 2️⃣ Obtener usuarios
        const resUsuarios = await fetch(API_USUARIOS);
        if (!resUsuarios.ok) throw new Error("Error al obtener usuarios");
        const usuarios = await resUsuarios.json();

        tbody.innerHTML = "";

        // 3️⃣ Crear filas dinámicas
        usuarios.forEach((usuario) => {
            const fila = document.createElement("tr");

            const estado = usuario.activo
                ? '<td class="text-success"><i class="fas fa-circle f-10 m-r-10"></i> Activo</td>'
                : '<td class="text-danger"><i class="fas fa-circle f-10 m-r-10"></i> Inactivo</td>';

            const fechaInicio = usuario.createdAt
                ? new Date(usuario.createdAt).toLocaleDateString("es-PE", {
                    year: "numeric",
                    month: "2-digit",
                    day: "2-digit",
                })
                : "-";

            const rol = rolesDisponibles.find((r) => r.id_rol === usuario.idRol);
            const rolTexto = rol
                ? `<span class="badge text-bg-success">${rol.nombre}</span>`
                : `<span class="badge text-bg-secondary">Sin rol</span>`;

            fila.innerHTML = `
        <td>
          <div class="d-flex align-items-center">
            <img src="/assets/images/user/avatar-1.jpg" alt="Imagen usuario" class="img-radius wid-40" />
            <div class="ms-3">
              <h6 class="mb-0">${usuario.nombreUsuario || "Sin nombre"}</h6>
            </div>
          </div>
        </td>
        <td>${rolTexto}</td>
        <td>${fechaInicio}</td>
        ${estado}
        <td>
          <a href="#" class="avtar avtar-xs btn-link-secondary btn-ver-detalle" 
             data-id="${usuario.idUsuario}" title="Ver detalles" 
             data-bs-toggle="modal" data-bs-target="#modalDetalleUsuario">
            <i class="ti ti-eye f-20"></i>
          </a>
          <a href="#" class="avtar avtar-xs btn-link-secondary" title="Editar">
            <i class="ti ti-edit f-20"></i>
          </a>
          <a href="#" class="avtar avtar-xs btn-link-secondary" title="Eliminar">
            <i class="ti ti-trash f-20"></i>
          </a>
        </td>
      `;

            tbody.appendChild(fila);
        });

        // 4️⃣ Escuchar clics en los botones de detalles
        tbody.addEventListener("click", async (e) => {
            const btn = e.target.closest(".btn-ver-detalle");
            if (!btn) return;

            const id = btn.dataset.id;
            try {
                const res = await fetch(`${API_USUARIOS}/${id}`);
                if (!res.ok) throw new Error("No se pudo obtener el usuario");

                const usuario = await res.json();

                // Actualizar modal con la información
                document.getElementById("detalleUsuarioNombre").textContent =
                    usuario.nombreUsuario || "—";
                document.getElementById("detalleUsuarioRol").textContent =
                    usuario.rolNombre || "—";
                document.getElementById("detalleUsuarioTelefono").textContent =
                    usuario.telefono || "—";
                document.getElementById("detalleUsuarioFecha").textContent = usuario.createdAt
                    ? new Date(usuario.createdAt).toLocaleString("es-PE")
                    : "—";
                document.getElementById("detalleUsuarioEstado").innerHTML = usuario.activo
                    ? '<span class="text-success">Activo</span>'
                    : '<span class="text-danger">Inactivo</span>';
                document.getElementById("detalleUsuarioImg").src =
                    usuario.foto || "/assets/images/user/avatar-1.jpg";
            } catch (error) {
                console.error("Error al cargar detalles del usuario:", error);
                alert("No se pudieron cargar los detalles del usuario.");
            }
        });
    } catch (error) {
        console.error("Error:", error);
        tbody.innerHTML = `<tr><td colspan="5" class="text-center text-danger">No se pudieron cargar los usuarios</td></tr>`;
    }
});



document.addEventListener("DOMContentLoaded", async () => {
    const selectRol = document.getElementById("usuarioRol");
    try {
        const response = await fetch("/api/admin/usuarios/roles");
        if (!response.ok) throw new Error("Error al obtener roles");
        const roles = await response.json();
        selectRol.innerHTML = `<option value="" selected disabled>Seleccione un rol...</option>`;
        roles.forEach(rol => {
            const option = document.createElement("option");
            option.value = rol.id_rol;
            option.textContent = rol.nombre;
            selectRol.appendChild(option);
        });
    } catch (error) {
        console.error("Error al cargar roles:", error);
        selectRol.innerHTML = `<option value="" disabled>Error al cargar roles</option>`;
    }
});

