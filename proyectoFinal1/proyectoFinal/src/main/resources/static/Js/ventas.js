// ventas.js

document.addEventListener("DOMContentLoaded", function () {
    const agregarForm = document.getElementById("formAgregarItem");
    const registrarForm = document.getElementById("formRegistrarVenta");

    if (agregarForm) {
        agregarForm.addEventListener("submit", function (e) {
            const cantidadInput = agregarForm.querySelector("input[name='cantidad']");
            if (!cantidadInput.value || parseInt(cantidadInput.value) <= 0) {
                alert("⚠️ Debes ingresar una cantidad válida.");
                e.preventDefault();
            }
        });
    }

    if (registrarForm) {
        registrarForm.addEventListener("submit", function () {
            alert("✅ Venta registrada correctamente.");
        });
    }

    // Confirmar cancelación
    const cancelarBtn = document.querySelector("a[href='/ventas/cancelar']");
    if (cancelarBtn) {
        cancelarBtn.addEventListener("click", function (e) {
            if (!confirm("¿Estás seguro que deseas cancelar la venta?")) {
                e.preventDefault();
            }
        });
    }
});
