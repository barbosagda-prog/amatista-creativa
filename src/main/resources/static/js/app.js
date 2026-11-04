// ---------- Utilidades ----------
const pesos = new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    maximumFractionDigits: 0,
});

// ---------- Filtros por categoría ----------
const chips = document.querySelectorAll(".chip");
const secciones = document.querySelectorAll(".category-section");
const catalogo = document.querySelector("#catalogo");

chips.forEach((chip) => {
    chip.addEventListener("click", () => {
        const cat = chip.dataset.cat;
        chips.forEach((c) => c.classList.toggle("active", c === chip));
        secciones.forEach((s) => {
            s.hidden = cat !== "all" && s.dataset.cat !== cat;
        });
        catalogo.scrollIntoView({ behavior: "smooth" });
    });
});

// ---------- Estado del carrito ----------
const CLAVE = "amatista-carrito";
let carrito = cargar();

function cargar() {
    try {
        return JSON.parse(localStorage.getItem(CLAVE)) || [];
    } catch {
        return [];
    }
}

function guardar() {
    try {
        localStorage.setItem(CLAVE, JSON.stringify(carrito));
    } catch {
        // si el navegador bloquea el almacenamiento, el carrito sigue en memoria
    }
}

function agregar(item) {
    const key = [item.id, item.color, item.talla].join("|");
    const existente = carrito.find((p) => p.key === key);
    if (existente) {
        existente.cantidad++;
    } else {
        carrito.push({ ...item, key, cantidad: 1 });
    }
    actualizar();
}

function cambiarCantidad(key, delta) {
    const fila = carrito.find((p) => p.key === key);
    if (!fila) return;
    fila.cantidad += delta;
    if (fila.cantidad <= 0) {
        carrito = carrito.filter((p) => p.key !== key);
    }
    actualizar();
}

function totalCarrito() {
    return carrito.reduce((suma, p) => suma + p.precio * p.cantidad, 0);
}

// ---------- Dibujar el carrito ----------
function crearBoton(texto, key, delta) {
    const b = document.createElement("button");
    b.type = "button";
    b.className = "qty-btn";
    b.textContent = texto;
    b.dataset.key = key;
    b.dataset.delta = delta;
    b.setAttribute("aria-label", delta > 0 ? "Aumentar cantidad" : "Disminuir cantidad");
    return b;
}

function renderLista() {
    const ul = document.querySelector("#lista-carrito");
    ul.replaceChildren();

    carrito.forEach((p) => {
        const li = document.createElement("li");
        li.className = "cart-item";

        const info = document.createElement("div");
        const nombre = document.createElement("strong");
        nombre.textContent = p.nombre;
        const detalle = document.createElement("small");
        detalle.textContent = [p.color, p.talla && "Talla " + p.talla]
            .filter(Boolean)
            .join(" · ");
        const subtotal = document.createElement("span");
        subtotal.textContent = pesos.format(p.precio * p.cantidad);
        info.append(nombre, detalle, subtotal);

        const control = document.createElement("div");
        control.className = "qty";
        const cantidad = document.createElement("span");
        cantidad.textContent = p.cantidad;
        control.append(crearBoton("−", p.key, -1), cantidad, crearBoton("+", p.key, 1));

        li.append(info, control);
        ul.appendChild(li);
    });
}

function actualizar() {
    guardar();
    const unidades = carrito.reduce((n, p) => n + p.cantidad, 0);
    document.querySelector(".cart-btn b").textContent =
        String(unidades).padStart(2, "0");
    document.querySelector("#carrito-vacio").hidden = carrito.length > 0;
    document.querySelector("#total").textContent = pesos.format(totalCarrito());
    renderLista();
}

function on(selector, evento, fn) {
    const el = document.querySelector(selector);
    if (!el) {
        console.warn("Falta el elemento", selector);
        return;
    }
    el.addEventListener(evento, fn);
}

// ---------- Abrir y cerrar el panel ----------
const panel = document.querySelector("#mini-carrito");
const fondo = document.querySelector("#overlay");

function mostrarPanel(visible) {
    panel.hidden = !visible;
    fondo.hidden = !visible;
}

on("#abrir-carrito", "click", () => mostrarPanel(true));
on("#cerrar-carrito", "click", () => mostrarPanel(false));
on("#overlay", "click", () => mostrarPanel(false));
document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") mostrarPanel(false);
});

on("#lista-carrito", "click", (e) => {
    const boton = e.target.closest(".qty-btn");
    if (!boton) return;
    cambiarCantidad(boton.dataset.key, Number(boton.dataset.delta));
});

on("#vaciar", "click", () => {
    carrito = [];
    actualizar();
});

// ---------- Resumen por WhatsApp ----------
on("#ir-pagar", "click", (e) => {
    if (carrito.length === 0) {
        e.preventDefault();
        return;
    }
    const lineas = carrito.map((p) => {
        const extras = [p.color, p.talla && "Talla " + p.talla].filter(Boolean).join(", ");
        return "- " + p.nombre + (extras ? " (" + extras + ")" : "") +
            " x" + p.cantidad + " = " + pesos.format(p.precio * p.cantidad);
    });
    const texto = "Hola, quiero hacer este pedido:\n" + lineas.join("\n") +
        "\nTotal: " + pesos.format(totalCarrito());
    e.currentTarget.href = "https://wa.me/" + document.body.dataset.wa +
        "?text=" + encodeURIComponent(texto);
});

// ---------- Opciones de cada tarjeta ----------
function leerOpciones(card) {
    const aviso = card.querySelector(".opt-msg");
    const color = card.querySelector('.swatch[aria-pressed="true"]');
    const talla = card.querySelector('.size-btn[aria-pressed="true"]');

    const faltan = [];
    if (card.querySelector(".swatch") && !color) faltan.push("el color");
    if (card.querySelector(".size-btn") && !talla) faltan.push("la talla");

    if (faltan.length > 0) {
        aviso.textContent = "Elige " + faltan.join(" y ") + " antes de continuar.";
        aviso.hidden = false;
        return null;
    }
    return {
        color: color ? color.dataset.color : "",
        talla: talla ? talla.textContent.trim() : "",
    };
}

catalogo.addEventListener("click", (e) => {
    const card = e.target.closest(".card");
    if (!card) return;
    const aviso = card.querySelector(".opt-msg");

    // Elegir color
    const color = e.target.closest(".swatch");
    if (color) {
        card.querySelectorAll(".swatch").forEach((b) => {
            b.setAttribute("aria-pressed", String(b === color));
        });
        card.querySelector(".color-name").textContent = color.dataset.color;
        aviso.hidden = true;
        return;
    }

    // Elegir talla
    const talla = e.target.closest(".size-btn");
    if (talla) {
        card.querySelectorAll(".size-btn").forEach((b) => {
            b.setAttribute("aria-pressed", String(b === talla));
        });
        aviso.hidden = true;
        return;
    }

    // Agregar al carrito
    const agregarBtn = e.target.closest(".add-btn");
    if (agregarBtn) {
        const opciones = leerOpciones(card);
        if (!opciones) return;
        agregar({
            id: card.dataset.id,
            nombre: card.dataset.nombre,
            precio: Number(card.dataset.precio),
            ...opciones,
        });
        agregarBtn.textContent = "Agregado ✓";
        setTimeout(() => { agregarBtn.textContent = "Agregar al carrito"; }, 1200);
        return;
    }

    // Pedir este producto por WhatsApp
    const pedir = e.target.closest(".order-btn");
    if (pedir) {
        const opciones = leerOpciones(card);
        if (!opciones) {
            e.preventDefault();
            return;
        }
        let texto = "Hola, quiero pedir: " + card.dataset.nombre;
        if (opciones.color) texto += " - Color " + opciones.color;
        if (opciones.talla) texto += " - Talla " + opciones.talla;
        pedir.href = "https://wa.me/" + document.body.dataset.wa +
            "?text=" + encodeURIComponent(texto);
    }
});

// ---------- Al cargar ----------
actualizar();