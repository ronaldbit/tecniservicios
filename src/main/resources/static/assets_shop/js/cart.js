(function () {
    const CART_KEY = "cartItems";
    const SHIPPING_PERCENT = 0.05; // 5% del subtotal (puedes cambiarlo)

    function loadCart() {
        try {
            const raw = localStorage.getItem(CART_KEY);
            if (!raw) return [];
            const parsed = JSON.parse(raw);
            return Array.isArray(parsed) ? parsed : [];
        } catch (e) {
            console.error("Error leyendo carrito:", e);
            return [];
        }
    }

    function saveCart(items) {
        localStorage.setItem(CART_KEY, JSON.stringify(items));
    }

    function getCartCount(items) {
        return items.reduce((acc, item) => acc + (item.cantidad || 0), 0);
    }

    function getCartSubtotal(items) {
        return items.reduce((acc, item) => acc + (item.precio || 0) * (item.cantidad || 0), 0);
    }

    function formatMoney(num) {
        return (num || 0).toFixed(2);
    }

    function addToCartFromButton(btn) {
        const id = btn.getAttribute("data-id");
        const nombre = btn.getAttribute("data-nombre") || "Producto";
        const precio = parseFloat(btn.getAttribute("data-precio") || "0");
        const imagen = btn.getAttribute("data-imagen") || "images/products/product-4-70x70.jpg";
        const url = btn.getAttribute("data-url") || ("/producto/" + id); // opcional

        if (!id) return;

        let items = loadCart();
        const existing = items.find(i => i.id === id);

        if (existing) {
            existing.cantidad += 1;
        } else {
            items.push({
                id,
                nombre,
                precio,
                imagen,
                url,
                cantidad: 1
            });
        }

        saveCart(items);
        renderCartHeader();
        renderCartPage();
    }

    function removeFromCart(id) {
        let items = loadCart();
        items = items.filter(i => i.id !== id);
        saveCart(items);
        renderCartHeader();
        renderCartPage();
    }

    function updateQty(id, nuevaCantidad) {
        let items = loadCart();
        const item = items.find(i => i.id === id);
        if (!item) return;

        const qty = parseInt(nuevaCantidad, 10);
        if (isNaN(qty) || qty <= 0) {
            removeFromCart(id);
            return;
        }
        item.cantidad = qty;
        saveCart(items);
        renderCartHeader();
        renderCartPage();
    }

    // HEADER: contador + mini lista + totales mini
    function renderCartHeader() {
        const items = loadCart();
        const countSpan = document.getElementById("cart-count");
        const headerTotalSpan = document.getElementById("cart-header-total");
        const miniEmpty = document.getElementById("cart-mini-empty");
        const miniList = document.getElementById("cart-mini-list");
        const miniSubtotal = document.getElementById("cart-mini-subtotal");
        const miniTotal = document.getElementById("cart-mini-total");

        const subtotal = getCartSubtotal(items);
        const shipping = subtotal * SHIPPING_PERCENT;
        const total = subtotal + shipping;

        if (countSpan) {
            countSpan.textContent = getCartCount(items);
        }
        if (headerTotalSpan) {
            headerTotalSpan.textContent = "S/. " + formatMoney(total);
        }

        if (!miniList || !miniSubtotal || !miniTotal || !miniEmpty) return;

        if (items.length === 0) {
            miniEmpty.style.display = "block";
            miniList.innerHTML = "";
            miniSubtotal.textContent = "S/. 0.00";
            miniTotal.textContent = "S/. 0.00";
            return;
        }

        miniEmpty.style.display = "none";
        miniList.innerHTML = "";

        items.slice(0, 5).forEach(item => {
            const li = document.createElement("li");
            li.className = "dropcart__item";

            li.innerHTML = `
                <div class="dropcart__item-image image image--type--product">
                    <a class="image__body" href="${item.url}">
                        <img class="image__tag" src="${item.imagen}" alt="${item.nombre}">
                    </a>
                </div>
                <div class="dropcart__item-info">
                    <div class="dropcart__item-name">
                        <a href="${item.url}">${item.nombre}</a>
                    </div>
                    <div class="dropcart__item-meta">
                        <div class="dropcart__item-quantity">${item.cantidad}</div>
                        <div class="dropcart__item-price">S/. ${formatMoney(item.precio)}</div>
                    </div>
                </div>
                <button type="button"
                        class="dropcart__item-remove"
                        data-id="${item.id}">
                    <svg width="10" height="10">
                        <path d="M8.8,8.8L8.8,8.8c-0.4,0.4-1,0.4-1.4,0L5,6.4L2.6,8.8c-0.4,0.4-1,0.4-1.4,0l0,0c-0.4-0.4-0.4-1,0-1.4L3.6,5L1.2,2.6 c-0.4-0.4-0.4-1,0-1.4l0,0c0.4-0.4,1-0.4,1.4,0L5,3.6l2.4-2.4c0.4-0.4,1-0.4,1.4,0l0,0c0.4,0.4,0.4,1,0,1.4L6.4,5l2.4,2.4 C9.2,7.8,9.2,8.4,8.8,8.8z" />
                    </svg>
                </button>
            `;

            miniList.appendChild(li);

            // Divider después de cada producto (puedes quitar el if si quieres también después del último)
        const divider = document.createElement("li");
        divider.className = "dropcart__divider";
        divider.setAttribute("role", "presentation");
        miniList.appendChild(divider);

        });

        miniSubtotal.textContent = "S/. " + formatMoney(subtotal);
        miniTotal.textContent = "S/. " + formatMoney(total);
    }

    // PÁGINA /carrito
    function renderCartPage() {
        const items = loadCart();
        const tbody = document.getElementById("cart-table-body");
        const pageSubtotal = document.getElementById("cart-page-subtotal");
        const pageShipping = document.getElementById("cart-page-shipping");
        const pageTotal = document.getElementById("cart-page-total");

        if (!tbody || !pageSubtotal || !pageShipping || !pageTotal) {
            return; // no estamos en /carrito
        }

        tbody.innerHTML = "";

        if (items.length === 0) {
            // sin filas → todo 0
            pageSubtotal.textContent = "S/. 0.00";
            pageShipping.textContent = "0.00";
            pageTotal.textContent = "S/. 0.00";
            return;
        }

        const subtotal = getCartSubtotal(items);
        const shipping = subtotal * SHIPPING_PERCENT;
        const total = subtotal + shipping;

        items.forEach(item => {
            const tr = document.createElement("tr");
            tr.className = "cart-table__row";

            tr.innerHTML = `
                <td class="cart-table__column cart-table__column--image">
                    <div class="image image--type--product">
                        <a href="${item.url}" class="image__body">
                            <img class="image__tag" src="${item.imagen}" alt="${item.nombre}">
                        </a>
                    </div>
                </td>
                <td class="cart-table__column cart-table__column--product">
                    <a href="${item.url}" class="cart-table__product-name">${item.nombre}</a>
                    <ul class="cart-table__options">
                        <!-- aquí podrías poner atributos extras si los tienes -->
                    </ul>
                </td>
                <td class="cart-table__column cart-table__column--price" data-title="Price">
                    S/. ${formatMoney(item.precio)}
                </td>
                <td class="cart-table__column cart-table__column--quantity" data-title="Quantity">
                    <div class="cart-table__quantity input-number">
                        <input class="form-control input-number__input js-cart-qty"
                               type="number"
                               min="1"
                               value="${item.cantidad}"
                               data-id="${item.id}">
                        <div class="input-number__add" data-id="${item.id}"></div>
                        <div class="input-number__sub" data-id="${item.id}"></div>
                    </div>
                </td>
                <td class="cart-table__column cart-table__column--total" data-title="Total">
                    S/. ${formatMoney(item.precio * item.cantidad)}
                </td>
                <td class="cart-table__column cart-table__column--remove">
                    <button type="button"
                            class="cart-table__remove btn btn-sm btn-icon btn-muted js-cart-remove"
                            data-id="${item.id}">
                        <svg width="12" height="12">
                            <path d="M10.8,10.8L10.8,10.8c-0.4,0.4-1,0.4-1.4,0L6,7.4l-3.4,3.4c-0.4,0.4-1,0.4-1.4,0l0,0c-0.4-0.4-0.4-1,0-1.4L4.6,6L1.2,2.6
                                c-0.4-0.4-0.4-1,0-1.4l0,0c0.4-0.4,1-0.4,1.4,0L6,4.6l3.4-3.4c0.4-0.4,1-0.4,1.4,0l0,0c0.4,0.4,0.4,1,0,1.4L7.4,6l3.4,3.4
                                C11.2,9.8,11.2,10.4,10.8,10.8z" />
                        </svg>
                    </button>
                </td>
            `;

            tbody.appendChild(tr);
        });

        pageSubtotal.textContent = "S/. " + formatMoney(subtotal);
        pageShipping.textContent = formatMoney(shipping);
        pageTotal.textContent = "S/. " + formatMoney(total);
    }

    // EVENTOS
    document.addEventListener("DOMContentLoaded", function () {
        renderCartHeader();
        renderCartPage();

        // Agregar al carrito
        document.body.addEventListener("click", function (e) {
            const btn = e.target.closest(".js-add-to-cart");
            if (btn) {
                e.preventDefault();
                addToCartFromButton(btn);
            }

            // eliminar desde mini carrito
            const miniRemove = e.target.closest(".dropcart__item-remove");
            if (miniRemove) {
                e.preventDefault();
                const id = miniRemove.getAttribute("data-id");
                if (id) removeFromCart(id);
            }

            // eliminar desde /carrito
            const removeBtn = e.target.closest(".js-cart-remove");
            if (removeBtn) {
                e.preventDefault();
                const id = removeBtn.getAttribute("data-id");
                if (id) removeFromCart(id);
            }

            // botones + y - en cantidad
            const addBtn = e.target.closest(".input-number__add");
            if (addBtn) {
                const id = addBtn.getAttribute("data-id");
                if (!id) return;
                const input = document.querySelector(`.js-cart-qty[data-id="${id}"]`);
                if (!input) return;
                input.value = parseInt(input.value || "1", 10) + 1;
                updateQty(id, input.value);
            }

            const subBtn = e.target.closest(".input-number__sub");
            if (subBtn) {
                const id = subBtn.getAttribute("data-id");
                if (!id) return;
                const input = document.querySelector(`.js-cart-qty[data-id="${id}"]`);
                if (!input) return;
                input.value = Math.max(1, parseInt(input.value || "1", 10) - 1);
                updateQty(id, input.value);
            }
        });

        // cambio manual de cantidad
        document.body.addEventListener("change", function (e) {
            if (e.target.classList.contains("js-cart-qty")) {
                const id = e.target.getAttribute("data-id");
                const value = e.target.value;
                if (id) updateQty(id, value);
            }
        });
    });
})();
