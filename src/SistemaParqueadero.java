import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

enum CategoriaVehiculo {
    AUTOMOVIL(12000, 800, 12000, 60000),   
    MOTOCICLETA(7000, 500, 7000, 35000),   
    CAMIONETA(15000, 1000, 15000, 75000),   
    BICICLETA(4000, 300, 4000, 20000);
    
    private final int tarifaHora;
    private final int tarifaDia;
    private final int tarifaNoche;
    private final int tarifaSemana;
    
    CategoriaVehiculo(int tarifaHora, int tarifaDia, int tarifaNoche, int tarifaSemana) {
        this.tarifaHora = tarifaHora;
        this.tarifaDia = tarifaDia;
        this.tarifaNoche = tarifaNoche;
        this.tarifaSemana = tarifaSemana;
    }
    
    public int getTarifaHora() { return tarifaHora; }
    public int getTarifaDia() { return tarifaDia; }
    public int getTarifaNoche() { return tarifaNoche; }
    public int getTarifaSemana() { return tarifaSemana; }
}

enum TipoPeriodo {
    HORA, DIA, NOCHE, SEMANA, MENSUALIDAD
}

enum MetodoPago {
    EFECTIVO, TARJETA, TRANSFERENCIA, NEQUI, DAVIPLATA
}

interface ICalcMora {
    double calcularMora(Pago pago, LocalDateTime fechaActual);
}

interface IGestorPagos {
    void crearPago(Pago pago);
    Pago obtenerPago(String id);
    List<Pago> obtenerTodosLosPagos();
    List<Pago> obtenerPagosPorUsuario(Usuario usuario);
    List<Pago> obtenerPagosPorCategoria(CategoriaVehiculo categoria);
    List<Pago> obtenerPagosPorTipo(TipoPeriodo tipo);
    List<Pago> obtenerPagosPorMetodo(MetodoPago metodo);
    List<Pago> obtenerPagosPorRangoFechas(LocalDateTime inicio, LocalDateTime fin);
    void actualizarPago(Pago pago);
    void eliminarPago(String id);
    double calcularTotalIngresos();
    double calcularTotalMora();
}

class Usuario {
    private final String id;
    private final String nombre;
    private final String identificacion;
    private final String correo;
    private final String telefono;
    private boolean esMensualidad;
    private CategoriaVehiculo categoriaPreferida;
    
    public Usuario(String nombre, String identificacion, String correo, String telefono,  
                   boolean esMensualidad, CategoriaVehiculo categoriaPreferida) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.correo = correo;
        this.telefono = telefono;
        this.esMensualidad = esMensualidad;
        this.categoriaPreferida = categoriaPreferida;
    }
    
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getIdentificacion() { return identificacion; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public boolean esMensualidad() { return esMensualidad; }
    public CategoriaVehiculo getCategoriaPreferida() { return categoriaPreferida; }
    
    public void setEsMensualidad(boolean esMensualidad) { this.esMensualidad = esMensualidad; }
    public void setCategoriaPreferida(CategoriaVehiculo categoriaPreferida) { this.categoriaPreferida = categoriaPreferida; }
    
    @Override
    public String toString() {
        return "Usuario{id='" + id + "', nombre='" + nombre + "', identificacion='" + identificacion + 
               "', correo='" + correo + "', telefono='" + telefono + "', esMensualidad=" + esMensualidad + 
               ", categoria=" + categoriaPreferida + "}";
    }
}

class Pago {
    private final String id;
    private final RegistroEntradaSalida registro;
    private double valor;
    private double mora;
    private LocalDateTime fechaPago;
    private final MetodoPago metodoPago;
    private final TipoPeriodo tipoPeriodo;
    private boolean pagado;
    private LocalDateTime fechaVencimiento;
    
    public Pago(RegistroEntradaSalida registro, double valor, MetodoPago metodoPago, TipoPeriodo tipoPeriodo) {
        this.id = UUID.randomUUID().toString();
        this.registro = registro;
        this.valor = valor;
        this.mora = 0.0;
        this.fechaPago = LocalDateTime.now();
        this.metodoPago = metodoPago;
        this.tipoPeriodo = tipoPeriodo;
        this.pagado = true;
        this.fechaVencimiento = LocalDateTime.now().plusDays(30);
    }
    
    public Pago(double valor, MetodoPago metodoPago, TipoPeriodo tipoPeriodo, LocalDateTime fechaVencimiento) {
        this.id = UUID.randomUUID().toString();
        this.registro = null;
        this.valor = valor;
        this.mora = 0.0;
        this.fechaPago = null;
        this.metodoPago = metodoPago;
        this.tipoPeriodo = tipoPeriodo;
        this.pagado = false;
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public String getId() { return id; }
    public RegistroEntradaSalida getRegistro() { return registro; }
    public double getValor() { return valor; }
    public double getMora() { return mora; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public TipoPeriodo getTipoPeriodo() { return tipoPeriodo; }
    public boolean isPagado() { return pagado; }
    public LocalDateTime getFechaVencimiento() { return fechaVencimiento; }
    
    public void setMora(double mora) { this.mora = mora; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }
    
    public double getTotal() {
        return valor + mora;
    }
    
    @Override
    public String toString() {
        String vehiculoInfo = "N/A";
        if (registro != null && registro.getVehiculo() != null) {
            vehiculoInfo = registro.getVehiculo().getPlaca();
        } else if (registro != null) {
            vehiculoInfo = "Sin vehículo";
        }
        
        return "Pago{id='" + id + "', vehiculo=" + vehiculoInfo + 
               ", valor=$" + String.format("%.2f", valor) + 
               ", mora=$" + String.format("%.2f", mora) + 
               ", total=$" + String.format("%.2f", getTotal()) +
               ", tipo=" + tipoPeriodo + 
               ", fechaVencimiento=" + fechaVencimiento +
               ", pagado=" + (pagado ? "Sí" : "No") +
               ", metodoPago=" + metodoPago + "}";
    }
}

class Vehiculo {
    private final String placa;
    private final String marca;
    private final String modelo;
    private final CategoriaVehiculo categoria;
    private Usuario propietario;
    
    public Vehiculo(String placa, String marca, String modelo, CategoriaVehiculo categoria) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.propietario = null;
    }
    
    public Vehiculo(String placa, String marca, String modelo, CategoriaVehiculo categoria, Usuario propietario) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.propietario = propietario;
    }
    
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public CategoriaVehiculo getCategoria() { return categoria; }
    public Usuario getPropietario() { return propietario; }
    
    public void setPropietario(Usuario propietario) { this.propietario = propietario; }
    
    @Override
    public String toString() {
        return "Vehiculo{placa='" + placa + "', marca='" + marca + "', modelo='" + modelo + 
               "', categoria=" + categoria + 
               ", propietario=" + (propietario != null ? propietario.getNombre() : "Ninguno") + "}";
    }
}

class Celda {
    private final int numero;
    private boolean ocupada;
    private Vehiculo vehiculo;
    private final boolean esMensualidad;
    
    public Celda(int numero) {
        this.numero = numero;
        this.ocupada = false;
        this.vehiculo = null;
        this.esMensualidad = false;
    }
    
    public Celda(int numero, boolean esMensualidad) {
        this.numero = numero;
        this.ocupada = false;
        this.vehiculo = null;
        this.esMensualidad = esMensualidad;
    }
    
    public boolean isOcupada() { return ocupada; }
    public void asignarVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
        this.ocupada = true;
    }
    
    public void liberarCelda() {
        this.vehiculo = null;
        this.ocupada = false;
    }
    
    public int getNumero() { return numero; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public boolean esCeldaMensualidad() { return esMensualidad; }
    
    @Override
    public String toString() {
        return "Celda{numero=" + numero + ", ocupada=" + ocupada + 
               ", tipo=" + (esMensualidad ? "Mensualidad" : "Ocasional") + 
               ", vehiculo=" + (vehiculo != null ? vehiculo.getPlaca() : "Ninguno") + "}";
    }
}

class RegistroEntradaSalida {
    private final String id;
    private final Vehiculo vehiculo;
    private final LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;
    private final Celda celda;
    private final boolean esMensualidad;
    
    public RegistroEntradaSalida(Vehiculo vehiculo, Celda celda) {
        this.id = UUID.randomUUID().toString();
        this.vehiculo = vehiculo;
        this.celda = celda;
        this.fechaEntrada = LocalDateTime.now();
        this.fechaSalida = null;
        this.esMensualidad = celda.esCeldaMensualidad() || 
                            (vehiculo.getPropietario() != null && vehiculo.getPropietario().esMensualidad());
    }
    
    public void registrarSalida() {
        this.fechaSalida = LocalDateTime.now();
        celda.liberarCelda();
    }
    
    public String getId() { return id; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public LocalDateTime getFechaEntrada() { return fechaEntrada; }
    public LocalDateTime getFechaSalida() { return fechaSalida; }
    public Celda getCelda() { return celda; }
    public boolean esRegistroMensualidad() { return esMensualidad; }
    
    public long getDuracionMinutos() {
        return Duration.between(fechaEntrada, fechaSalida != null ? fechaSalida : LocalDateTime.now()).toMinutes();
    }
    
    @Override
    public String toString() {
        return "RegistroEntradaSalida{id='" + id + "', vehiculo=" + vehiculo.getPlaca() + 
               ", fechaEntrada=" + fechaEntrada + 
               ", fechaSalida=" + (fechaSalida != null ? fechaSalida : "Aún no ha salido") + 
               ", celda=" + celda.getNumero() + 
               ", tipo=" + (esMensualidad ? "Mensualidad" : "Ocasional") + "}";
    }
}

class CalcMora implements ICalcMora {
    private static final double PORCENTAJE_MORA_DIARIO = 0.05;
    
    @Override
    public double calcularMora(Pago pago, LocalDateTime fechaActual) {
        if (pago.isPagado() || pago.getFechaVencimiento() == null || 
            fechaActual.isBefore(pago.getFechaVencimiento())) {
            return 0.0;
        }
        
        long diasRetraso = Duration.between(pago.getFechaVencimiento(), fechaActual).toDays();
        return pago.getValor() * PORCENTAJE_MORA_DIARIO * diasRetraso;
    }
}

class GestorPagos implements IGestorPagos {
    private final List<Pago> pagos = new ArrayList<>();
    private final ICalcMora calcMora = new CalcMora();
    
    @Override
    public void crearPago(Pago pago) {
        pagos.add(pago);
        System.out.println("✅ Pago creado exitosamente: " + pago.getId());
    }
    
    @Override
    public Pago obtenerPago(String id) {
        for (Pago pago : pagos) {
            if (pago.getId().equals(id)) {
                return pago;
            }
        }
        return null;
    }
    
    @Override
    public List<Pago> obtenerTodosLosPagos() {
        return new ArrayList<>(pagos);
    }
    
    @Override
    public List<Pago> obtenerPagosPorUsuario(Usuario usuario) {
        return pagos.stream()
            .filter(p -> p.getRegistro() != null && 
                         p.getRegistro().getVehiculo() != null &&
                         p.getRegistro().getVehiculo().getPropietario() != null &&
                         p.getRegistro().getVehiculo().getPropietario().getId().equals(usuario.getId()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Pago> obtenerPagosPorCategoria(CategoriaVehiculo categoria) {
        return pagos.stream()
            .filter(p -> (p.getRegistro() != null && 
                          p.getRegistro().getVehiculo() != null &&
                          p.getRegistro().getVehiculo().getCategoria() == categoria) ||
                         (p.getRegistro() == null && categoria == CategoriaVehiculo.AUTOMOVIL))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Pago> obtenerPagosPorTipo(TipoPeriodo tipo) {
        return pagos.stream()
            .filter(p -> p.getTipoPeriodo() == tipo)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Pago> obtenerPagosPorMetodo(MetodoPago metodo) {
        return pagos.stream()
            .filter(p -> p.getMetodoPago() == metodo)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Pago> obtenerPagosPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return pagos.stream()
            .filter(p -> p.getFechaPago() != null && 
                         !p.getFechaPago().isBefore(inicio) && 
                         !p.getFechaPago().isAfter(fin))
            .collect(Collectors.toList());
    }
    
    @Override
    public void actualizarPago(Pago pago) {
        System.out.println("✅ Pago actualizado: " + pago.getId());
    }
    
    @Override
    public void eliminarPago(String id) {
        pagos.removeIf(p -> p.getId().equals(id));
        System.out.println("✅ Pago eliminado: " + id);
    }
    
    @Override
    public double calcularTotalIngresos() {
        return pagos.stream()
            .mapToDouble(Pago::getTotal)
            .sum();
    }
    
    @Override
    public double calcularTotalMora() {
        LocalDateTime ahora = LocalDateTime.now();
        return pagos.stream()
            .mapToDouble(p -> calcMora.calcularMora(p, ahora))
            .sum();
    }
    
    public void actualizarTodasLasMoras() {
        LocalDateTime ahora = LocalDateTime.now();
        pagos.stream()
            .filter(p -> !p.isPagado())
            .forEach(p -> p.setMora(calcMora.calcularMora(p, ahora)));
        System.out.println("✅ Moras actualizadas para todos los pagos pendientes");
    }
    
    public List<Pago> obtenerPagosPendientes() {
        return pagos.stream()
            .filter(p -> !p.isPagado())
            .collect(Collectors.toList());
    }
    
    public List<Pago> obtenerPagosVencidos() {
        LocalDateTime ahora = LocalDateTime.now();
        return pagos.stream()
            .filter(p -> !p.isPagado() && p.getFechaVencimiento() != null && p.getFechaVencimiento().isBefore(ahora))
            .collect(Collectors.toList());
    }
}

public class Parqueadero {
    private final List<Celda> celdas;
    private final List<RegistroEntradaSalida> registros = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();
    private final GestorPagos gestorPagos = new GestorPagos();
    private final Map<String, RegistroEntradaSalida> vehiculosEnParqueadero = new HashMap<>();
    
    public Parqueadero(int totalCeldas, int celdasMensualidad) {
        celdas = new ArrayList<>(totalCeldas);
        
        for (int i = 1; i <= (totalCeldas - celdasMensualidad); i++) {
            celdas.add(new Celda(i, false));
        }
        
        for (int i = totalCeldas - celdasMensualidad + 1; i <= totalCeldas; i++) {
            celdas.add(new Celda(i, true));
        }
    }
    
    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        System.out.println("✅ Usuario registrado: " + usuario.getNombre());
    }
    
    public Usuario buscarUsuarioPorId(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return null;
    }
    
    public Usuario buscarUsuarioPorIdentificacion(String identificacion) {
        for (Usuario usuario : usuarios) {
            if (usuario.getIdentificacion().equals(identificacion)) {
                return usuario;
            }
        }
        return null;
    }
    
    public boolean registrarEntrada(Vehiculo vehiculo) {
        if (vehiculosEnParqueadero.containsKey(vehiculo.getPlaca())) {
            System.out.println("❌ El vehículo ya se encuentra en el parqueadero.");
            return false;
        }
        
        boolean necesitaMensualidad = vehiculo.getPropietario() != null && 
                                     vehiculo.getPropietario().esMensualidad();
        
        for (Celda celda : celdas) {
            if (!celda.isOcupada() && celda.esCeldaMensualidad() == necesitaMensualidad) {
                celda.asignarVehiculo(vehiculo);
                RegistroEntradaSalida registro = new RegistroEntradaSalida(vehiculo, celda);
                registros.add(registro);
                vehiculosEnParqueadero.put(vehiculo.getPlaca(), registro);
                System.out.println("✅ Vehículo registrado en entrada. Celda asignada: " + celda.getNumero());
                return true;
            }
        }
        
        System.out.println("❌ No hay celdas disponibles del tipo requerido.");
        return false;
    }
    
    public boolean registrarSalida(String placa) {
        RegistroEntradaSalida registro = vehiculosEnParqueadero.remove(placa);
        if (registro == null) {
            System.out.println("❌ Vehículo no encontrado o ya ha salido.");
            return false;
        }
        
        registro.registrarSalida();
        
        if (!registro.esRegistroMensualidad()) {
            double valor = calcularPagoPorTiempo(registro);
            Pago pago = new Pago(registro, valor, MetodoPago.EFECTIVO, determinarTipoPeriodo(registro));
            gestorPagos.crearPago(pago);
            System.out.println("💰 Pago generado: $" + String.format("%.2f", valor) + 
                             " por " + registro.getDuracionMinutos() + " minutos");
        }
        
        System.out.println("✅ Vehículo salió del parqueadero.");
        return true;
    }
    
    private TipoPeriodo determinarTipoPeriodo(RegistroEntradaSalida registro) {
        long minutos = registro.getDuracionMinutos();
        if (minutos <= 60) return TipoPeriodo.HORA;
        if (minutos <= 1440) return TipoPeriodo.DIA;
        if (minutos <= 10080) return TipoPeriodo.SEMANA;
        return TipoPeriodo.MENSUALIDAD;
    }
    
    private double calcularPagoPorTiempo(RegistroEntradaSalida registro) {
        CategoriaVehiculo categoria = registro.getVehiculo().getCategoria();
        long minutos = registro.getDuracionMinutos();
        
        if (minutos <= 60) return categoria.getTarifaHora();
        if (minutos <= 1440) return categoria.getTarifaDia();
        if (minutos <= 10080) return categoria.getTarifaSemana();
        return categoria.getTarifaSemana() * 4;
    }
    
    public void registrarPagoMensualidad(Usuario usuario, MetodoPago metodoPago) {
        if (!usuario.esMensualidad()) {
            System.out.println("❌ El usuario no tiene contrato de mensualidad.");
            return;
        }
        
        CategoriaVehiculo categoria = usuario.getCategoriaPreferida();
        double tarifaMensual = categoria.getTarifaSemana() * 4;
        LocalDateTime fechaVencimiento = LocalDateTime.now().plusMonths(1);
        Pago pago = new Pago(tarifaMensual, metodoPago, TipoPeriodo.MENSUALIDAD, fechaVencimiento);
        gestorPagos.crearPago(pago);
        System.out.println("💰 Pago mensual registrado: $" + String.format("%.2f", tarifaMensual));
    }
    
    public void mostrarEstado() {
        System.out.println("\n=== ESTADO DEL PARQUEADERO ===");
        celdas.forEach(System.out::println);
    }
    
    public void mostrarRegistros() {
        System.out.println("\n=== REGISTROS DE ENTRADA/SALIDA ===");
        registros.forEach(System.out::println);
    }
    
    public void mostrarUsuarios() {
        System.out.println("\n=== USUARIOS REGISTRADOS ===");
        usuarios.forEach(System.out::println);
    }
    
    public void mostrarPagos() {
        System.out.println("\n=== REGISTRO DE PAGOS ===");
        gestorPagos.obtenerTodosLosPagos().forEach(System.out::println);
    }
    
    public void mostrarIngresosTotales() {
        System.out.println("\n=== INGRESOS TOTALES ===");
        System.out.println("Total recaudado: $" + String.format("%.2f", gestorPagos.calcularTotalIngresos()));
    }
    
    public void mostrarTarifas() {
        System.out.println("\n=== TARIFAS DEL PARQUEADERO ===");
        System.out.println("CATEGORÍA\t\tHORA\t\tDÍA\t\tNOCHE\t\tSEMANA");
        for (CategoriaVehiculo categoria : CategoriaVehiculo.values()) {
            System.out.printf("%-15s\t$%,-8d\t$%,-6d\t$%,-8d\t$%,-8d%n",  
                categoria.name(),  
                categoria.getTarifaHora(),  
                categoria.getTarifaDia(),  
                categoria.getTarifaNoche(),  
                categoria.getTarifaSemana());
        }
        System.out.println("\nMENSUALIDAD: 4 semanas de tarifa semanal");
    }
    
    public void mostrarPagosFiltrados() {
        System.out.println("\n=== FILTROS DE PAGOS ===");
        System.out.println("1. Por usuario");
        System.out.println("2. Por categoría");
        System.out.println("3. Por tipo de periodo");
        System.out.println("4. Por método de pago");
        System.out.println("5. Por rango de fechas");
        System.out.println("6. Pagos pendientes");
        System.out.println("7. Pagos vencidos");
        
        System.out.println("\n--- Pagos pendientes ---");
        gestorPagos.obtenerPagosPendientes().forEach(System.out::println);
        
        System.out.println("\n--- Pagos vencidos ---");
        gestorPagos.obtenerPagosVencidos().forEach(System.out::println);
    }
    
    public static void main(String[] args) {
        Parqueadero parqueadero = new Parqueadero(10, 3);
        parqueadero.mostrarTarifas();
        
        Usuario usuario1 = new Usuario("Carlos Pérez", "123456789", "carlos@email.com", "3001234567", true, CategoriaVehiculo.AUTOMOVIL);
        Usuario usuario2 = new Usuario("Ana Gómez", "987654321", "ana@email.com", "3017654321", false, CategoriaVehiculo.MOTOCICLETA);
        parqueadero.registrarUsuario(usuario1);
        parqueadero.registrarUsuario(usuario2);
        
        Vehiculo v1 = new Vehiculo("ABC123", "Toyota", "Corolla", CategoriaVehiculo.AUTOMOVIL, usuario1);
        Vehiculo v2 = new Vehiculo("XYZ789", "Mazda", "3", CategoriaVehiculo.MOTOCICLETA, usuario2);
        Vehiculo v3 = new Vehiculo("DEF456", "Renault", "Logan", CategoriaVehiculo.AUTOMOVIL);
        
        parqueadero.registrarEntrada(v1);
        parqueadero.registrarEntrada(v2);
        parqueadero.registrarEntrada(v3);
        parqueadero.mostrarEstado();
        parqueadero.registrarSalida("XYZ789");
        parqueadero.registrarPagoMensualidad(usuario1, MetodoPago.TRANSFERENCIA);
        parqueadero.gestorPagos.actualizarTodasLasMoras();
        parqueadero.mostrarRegistros();
        parqueadero.mostrarUsuarios();
        parqueadero.mostrarPagos();
        parqueadero.mostrarPagosFiltrados();
        parqueadero.mostrarIngresosTotales();
    }
}
