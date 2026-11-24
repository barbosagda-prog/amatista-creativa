const CLAVE = "amatista-carrito";
const pesos = new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    maximumFractionDigits: 0,
});

function leerCarrito() {
    try {
        return JSON.parse(localStorage.getItem(CLAVE)) || [];
    } catch {
        return [];
    }
}

const carrito = leerCarrito();
const form = document.querySelector("#form-pedido");
const aviso = document.querySelector("#form-msg");
const boton = document.querySelector("#btn-enviar");

// ---------- Resumen (solo para mostrar) ----------
function dibujarResumen() {
    const ul = document.querySelector("#resumen-lista");
    ul.replaceChildren();

    carrito.forEach((p) => {
        const li = document.createElement("li");
        const detalle = [p.color, p.talla && "Talla " + p.talla].filter(Boolean).join(" · ");
        li.textContent = p.nombre + (detalle ? " (" + detalle + ")" : "") +
            " x" + p.cantidad + " — " + pesos.format(p.precio * p.cantidad);
        ul.appendChild(li);
    });

    const total = carrito.reduce((s, p) => s + p.precio * p.cantidad, 0);
    document.querySelector("#resumen-total").textContent = pesos.format(total);

    const vacio = carrito.length === 0;
    document.querySelector("#resumen-vacio").hidden = !vacio;
    boton.disabled = vacio;
}

dibujarResumen();

// ---------- Confirmación ----------
function mostrarConfirmacion(pedido) {
    document.querySelector("#checkout-contenido").hidden = true;
    document.querySelector("#confirmacion").hidden = false;

    const total = pesos.format(pedido.total);
    document.querySelector("#conf-codigo").textContent = pedido.code;
    document.querySelector("#conf-total").textContent = total;

    const texto = "Hola, hice el pedido " + pedido.code + " por " + total +
        ". Quedo atento para confirmar el envío y el pago.";
    document.querySelector("#conf-wa").href =
        "https://wa.me/" + document.body.dataset.wa + "?text=" + encodeURIComponent(texto);
}

// ---------- Enviar el pedido ----------
form.addEventListener("submit", async (e) => {
    e.preventDefault();
    aviso.hidden = true;
    if (carrito.length === 0) return;

    const datos = Object.fromEntries(new FormData(form));
    const payload = {
        ...datos,
        items: carrito.map((p) => ({
            productId: p.id,
            color: p.color || null,
            size: p.talla || null,
            quantity: p.cantidad,
        })),
    };

    const token = document.querySelector('meta[name="_csrf"]').content;
    const cabecera = document.querySelector('meta[name="_csrf_header"]').content;

    boton.disabled = true;
    try {
        const resp = await fetch("/api/orders", {
            method: "POST",
            headers: { "Content-Type": "application/json", [cabecera]: token },
            body: JSON.stringify(payload),
        });
        if (!resp.ok) throw new Error("estado " + resp.status);

        const pedido = await resp.json();
        try { localStorage.removeItem(CLAVE); } catch { /* sin almacenamiento */ }
        mostrarConfirmacion(pedido);
    } catch (err) {
        aviso.textContent = "No pudimos registrar el pedido. Revisa los datos e inténtalo de nuevo.";
        aviso.hidden = false;
        boton.disabled = false;
    }
});