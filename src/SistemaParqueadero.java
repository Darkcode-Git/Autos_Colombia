import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SistemaParqueadero {
    public static void main(String[] args) {
        Parqueadero autosColombia = new Parqueadero("Autos Colombia",  50);
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:  registrarEntradaVehiculo(autosColombia,  scanner); break;
                case 2:  registrarSalidaVehiculo(autosColombia,  scanner); break;
                case 3:  registrarUsuario(autosColombia,  scanner); break;
                case 4:  gestionarCeldas(autosColombia); break;
                case 5:  registrarNovedad(autosColombia,  scanner); break;
                case 6:  gestionarPagos(autosColombia,  scanner); break;
                case 7:  System.out.println("Gracias por usar el sistema de Autos Colombia."); break;
                default:  System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 7);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n=== SISTEMA PARQUEADERO AUTOS COLOMBIA ===");
        System.out.println("1. Registrar entrada de vehículo");
        System.out.println("2. Registrar salida de vehículo");
        System.out.println("3. Registrar usuario");
        System.out.println("4. Gestionar celdas");
        System.out.println("5. Registrar novedad");
        System.out.println("6. Gestionar pagos");
        System.out.println("7. Salir");
        System.out.print("Seleccione una opción:  ");
    }

    private static void registrarEntradaVehiculo(Parqueadero parqueadero,  Scanner scanner) {
        System.out.print("Ingrese la placa del vehículo:  ");
        String placa = scanner.nextLine();

        System.out.print("Ingrese el tipo de vehículo (CARRO/MOTO):  ");
        String tipo = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese el ID del usuario:  ");
        String idUsuario = scanner.nextLine();

        try {
            Vehiculo.TipoVehiculo tipoVehiculo = Vehiculo.TipoVehiculo.valueOf(tipo);
            Vehiculo vehiculo = new Vehiculo(placa,  tipoVehiculo);
            parqueadero.registrarEntrada(vehiculo,  idUsuario);
            System.out.println("Entrada registrada exitosamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de vehículo no válido. Debe ser CARRO o MOTO.");
        } catch (Exception e) {
            System.out.println("Error al registrar entrada:  " + e.getMessage());
        }
    }

    private static void registrarSalidaVehiculo(Parqueadero parqueadero,  Scanner scanner) {
        System.out.print("Ingrese la placa del vehículo:  ");
        String placa = scanner.nextLine();

        try {
            parqueadero.registrarSalida(placa);
            System.out.println("Salida registrada exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar salida:  " + e.getMessage());
        }
    }

    private static void registrarUsuario(Parqueadero parqueadero,  Scanner scanner) {
        System.out.print("Ingrese el ID del usuario:  ");
        String id = scanner.nextLine();

        System.out.print("Ingrese el nombre del usuario:  ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese el teléfono del usuario:  ");
        String telefono = scanner.nextLine();

        System.out.print("Ingrese el tipo de usuario (CLIENTE/ADMIN):  ");
        String tipo = scanner.nextLine().toUpperCase();

        try {
            Usuario.TipoUsuario tipoUsuario = Usuario.TipoUsuario.valueOf(tipo);
            Usuario usuario = new Usuario(id,  nombre,  telefono,  tipoUsuario);
            parqueadero.registrarUsuario(usuario);
            System.out.println("Usuario registrado exitosamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de usuario no válido. Debe ser CLIENTE o ADMIN.");
        } catch (Exception e) {
            System.out.println("Error al registrar usuario:  " + e.getMessage());
        }
    }

    private static void gestionarCeldas(Parqueadero parqueadero) {
        System.out.println("\n=== ESTADO DE CELDAS ===");
        System.out.println("Total de celdas:  " + parqueadero.getTotalCeldas());
        System.out.println("Celdas disponibles:  " + parqueadero.getCeldasDisponibles());
        System.out.println("Celdas ocupadas:  " + (parqueadero.getTotalCeldas() - parqueadero.getCeldasDisponibles()));
    }

    private static void registrarNovedad(Parqueadero parqueadero,  Scanner scanner) {
        System.out.print("Ingrese la placa del vehículo:  ");
        String placa = scanner.nextLine();

        System.out.print("Ingrese la descripción de la novedad:  ");
        String descripcion = scanner.nextLine();

        try {
            parqueadero.registrarNovedad(placa,  descripcion);
            System.out.println("Novedad registrada exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar novedad:  " + e.getMessage());
        }
    }

    private static void gestionarPagos(Parqueadero parqueadero,  Scanner scanner) {
        System.out.print("Ingrese la placa del vehículo para calcular pago:  ");
        String placa = scanner.nextLine();

        try {
            double valorPagar = parqueadero.calcularPago(placa);
            System.out.println("Valor a pagar:  $" + String.format("%.2f",  valorPagar));

            System.out.print("Ingrese el monto recibido:  ");
            double montoRecibido = scanner.nextDouble();
            scanner.nextLine();

            if (montoRecibido >= valorPagar) {
                double cambio = montoRecibido - valorPagar;
                parqueadero.registrarPago(placa,  valorPagar);
                System.out.println("Pago registrado exitosamente.");
                System.out.println("Cambio a entregar:  $" + String.format("%.2f",  cambio));
            } else {
                System.out.println("Monto insuficiente para realizar el pago.");
            }
        } catch (Exception e) {
            System.out.println("Error al gestionar pago:  " + e.getMessage());
        }
    }
}

class Parqueadero {
    private final String nombre;
    private final int totalCeldas;
    private final List<Celda> celdas;
    private final Map<String,  Vehiculo> vehiculosEnParqueadero;
    private final Map<String,  Usuario> usuarios;
    private final List<RegistroEntradaSalida> registros;
    private final List<Novedad> novedades;
    private final Map<String,  Pago> pagos;

    public Parqueadero(String nombre,  int totalCeldas) {
        this.nombre = nombre;
        this.totalCeldas = totalCeldas;
        this.celdas = new ArrayList<>(totalCeldas);
        this.vehiculosEnParqueadero = new HashMap<>();
        this.usuarios = new HashMap<>();
        this.registros = new ArrayList<>();
        this.novedades = new ArrayList<>();
        this.pagos = new HashMap<>();

        for (int i = 1; i <= totalCeldas; i++) {
            celdas.add(new Celda(i));
        }
    }

    public void registrarEntrada(Vehiculo vehiculo,  String idUsuario) throws Exception {
        if (vehiculosEnParqueadero.containsKey(vehiculo.getPlaca())) {
            throw new Exception("El vehículo ya se encuentra en el parqueadero.");
        }

        if (!usuarios.containsKey(idUsuario)) {
            throw new Exception("Usuario no registrado.");
        }

        Celda celdaDisponible = encontrarCeldaDisponible();
        if (celdaDisponible == null) {
            throw new Exception("No hay celdas disponibles.");
        }

        celdaDisponible.ocupar(vehiculo);
        vehiculosEnParqueadero.put(vehiculo.getPlaca(),  vehiculo);
        registros.add(new RegistroEntradaSalida(vehiculo,  usuarios.get(idUsuario),  celdaDisponible,  true));

        System.out.println("Vehículo asignado a la celda #" + celdaDisponible.getNumero());
    }

    public void registrarSalida(String placa) throws Exception {
        if (!vehiculosEnParqueadero.containsKey(placa)) {
            throw new Exception("El vehículo no se encuentra en el parqueadero.");
        }

        Celda celda = encontrarCeldaPorVehiculo(placa);
        if (celda != null) {
            celda.liberar();
        }

        Vehiculo vehiculo = vehiculosEnParqueadero.remove(placa);
        if (vehiculo != null) {
            registros.add(new RegistroEntradaSalida(vehiculo,  null,  celda,  false));
        }
    }

    public void registrarUsuario(Usuario usuario) throws Exception {
        if (usuarios.containsKey(usuario.getId())) {
            throw new Exception("Ya existe un usuario con ese ID.");
        }
        usuarios.put(usuario.getId(),  usuario);
    }

    public int getTotalCeldas() {
        return totalCeldas;
    }

    public int getCeldasDisponibles() {
        int disponibles = 0;
        for (Celda c :  celdas) {
            if (!c.isOcupada()) disponibles++;
        }
        return disponibles;
    }

    public void registrarNovedad(String placa,  String descripcion) throws Exception {
        Vehiculo vehiculo = vehiculosEnParqueadero.get(placa);
        if (vehiculo == null) {
            throw new Exception("El vehículo no se encuentra en el parqueadero.");
        }
        novedades.add(new Novedad(vehiculo,  descripcion,  LocalDateTime.now()));
    }

    public double calcularPago(String placa) throws Exception {
        Vehiculo vehiculo = vehiculosEnParqueadero.get(placa);
        if (vehiculo == null) {
            throw new Exception("El vehículo no se encuentra en el parqueadero.");
        }

        RegistroEntradaSalida registroEntrada = encontrarRegistroEntrada(placa);
        if (registroEntrada == null) {
            throw new Exception("No se encontró registro de entrada para el vehículo.");
        }

        long minutos = java.time.Duration.between(registroEntrada.getFechaHora(),  LocalDateTime.now()).toMinutes();
        double tarifaPorHora = vehiculo.getTipo() == Vehiculo.TipoVehiculo.CARRO ? 2000 :  1000;

        return Math.ceil(minutos / 60.0) * tarifaPorHora;
    }

    public void registrarPago(String placa,  double valor) throws Exception {
        if (!vehiculosEnParqueadero.containsKey(placa)) {
            throw new Exception("El vehículo no se encuentra en el parqueadero.");
        }
        pagos.put(placa,  new Pago(placa,  valor,  LocalDateTime.now()));
    }

    private Celda encontrarCeldaDisponible() {
        for (Celda c :  celdas) {
            if (!c.isOcupada()) return c;
        }
        return null;
    }

    private Celda encontrarCeldaPorVehiculo(String placa) {
        for (Celda c :  celdas) {
            if (c.getVehiculo() != null && c.getVehiculo().getPlaca().equals(placa)) {
                return c;
            }
        }
        return null;
    }

    private RegistroEntradaSalida encontrarRegistroEntrada(String placa) {
        for (int i = registros.size() - 1; i >= 0; i--) {
            RegistroEntradaSalida registro = registros.get(i);
            if (registro.getVehiculo().getPlaca().equals(placa) && registro.isEntrada()) {
                return registro;
            }
        }
        return null;
    }
}

class Celda {
    private final int numero;
    private boolean ocupada;
    private Vehiculo vehiculo;

    public Celda(int numero) {
        this.numero = numero;
        this.ocupada = false;
        this.vehiculo = null;
    }

    public void ocupar(Vehiculo vehiculo) {
        this.ocupada = true;
        this.vehiculo = vehiculo;
    }

    public void liberar() {
        this.ocupada = false;
        this.vehiculo = null;
    }

    public int getNumero() {
        return numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }
}

class Vehiculo {
    public enum TipoVehiculo {
        CARRO,  MOTO
    }

    private final String placa;
    private final TipoVehiculo tipo;

    public Vehiculo(String placa,  TipoVehiculo tipo) {
        this.placa = placa;
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) obj;
        return Objects.equals(placa, vehiculo.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placa);
    }
}

class Usuario {
    public enum TipoUsuario {
        CLIENTE,  ADMIN
    }

    private final String id;
    private final String nombre;
    private final String telefono;
    private final TipoUsuario tipo;

    public Usuario(String id,  String nombre,  String telefono,  TipoUsuario tipo) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class RegistroEntradaSalida {
    private final Vehiculo vehiculo;
    private final Usuario usuario;
    private final Celda celda;
    private final LocalDateTime fechaHora;
    private final boolean entrada;

    public RegistroEntradaSalida(Vehiculo vehiculo,  Usuario usuario,  Celda celda,  boolean entrada) {
        this.vehiculo = vehiculo;
        this.usuario = usuario;
        this.celda = celda;
        this.fechaHora = LocalDateTime.now();
        this.entrada = entrada;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Celda getCelda() {
        return celda;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public boolean isEntrada() {
        return entrada;
    }
}

class Novedad {
    private final Vehiculo vehiculo;
    private final String descripcion;
    private final LocalDateTime fechaHora;

    public Novedad(Vehiculo vehiculo,  String descripcion,  LocalDateTime fechaHora) {
        this.vehiculo = vehiculo;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}

class Pago {
    private final String placaVehiculo;
    private final double valor;
    private final LocalDateTime fechaHora;

    public Pago(String placaVehiculo,  double valor,  LocalDateTime fechaHora) {
        this.placaVehiculo = placaVehiculo;
        this.valor = valor;
        this.fechaHora = fechaHora;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}
