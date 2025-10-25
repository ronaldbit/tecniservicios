# MVC Simple (sin DAO/Service) + JDBC + Rutas Dinámicas

- **Modelo directo** con `JdbcTemplate` (sin capa DAO/Service).
- **Controladores MVC** en `controllers/tienda/**` y `controllers/admin/**`.
- **Rutas dinámicas**: `/admin/...` → `templates/admin/...`; demás → `templates/tienda/...`.
- **MySQL** configurado en `application.properties` (no YAML).
- **APIs** para usar `fetch` desde las vistas.

## Ejecutar
1. Ajusta credenciales en `src/main/resources/application.properties`.
2. Asegura que exista la tabla `productos` con columnas mínimas:
   - `id_producto` (PK autoincrement), `codigo` (varchar), `nombre` (varchar), `precio` (decimal), `publicar_online` (tinyint/bool).
3. Corre:
```bash
mvn spring-boot:run
```

## URLs
- Cliente: `http://localhost:8080/`
- Admin: `http://localhost:8080/admin` (admin/admin123)
- API pública: `GET /api/tienda/products[?q=]`, `GET /api/tienda/products/{id}`
- API admin: `POST/PUT/DELETE /api/admin/products`

## Notas
- 
-
-
-
-
-
-

## Rutas
```bash
mvc-simple-jdbc/
├─ pom.xml
├─ README.md
├─ src/
│  └─ main/
│     ├─ java/
│     │  └─ com/example/simplemvc/
│     │     ├─ Application.java
│     │     ├─ config/
│     │     │  └─ SecurityConfig.java
│     │     ├─ controllers/
│     │     │  ├─ DynamicRouterController.java           # Resuelve rutas dinámicas → templates
│     │     │  ├─ tienda/
│     │     │  │  ├─ ProductosController.java            # Vista /productos
│     │     │  │  └─ api/
│     │     │  │     └─ ProductoPublicApi.java           # API pública /api/tienda/**
│     │     │  └─ admin/
│     │     │     ├─ AdminProductosController.java       # Vista /admin/productos
│     │     │     └─ api/
│     │     │        └─ ProductoAdminApi.java            # API privada /api/admin/**
│     │     └─ model/
│     │        ├─ Producto.java
│     │        └─ ProductoModel.java                     # Consultas JDBC directas
│     ├─ resources/
│     │  ├─ application.properties                       # Tu config (no YAML)
│     │  ├─ static/
│     │  │  └─ assets/
│     │  │     └─ style.css
│     │  └─ templates/
│     │     ├─ tienda/
│     │     │  ├─ home.html
│     │     │  ├─ productos.html
│     │     │  └─ producto.html
│     │     ├─ admin/
│     │     │  ├─ index.html
│     │     │  ├─ productos.html
│     │     │  └─ login.html
│     │     └─ errors/
│     │        └─ 404.html
└─ ...

```

