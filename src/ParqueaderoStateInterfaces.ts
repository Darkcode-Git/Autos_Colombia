
export enum TipoVehiculo {
  CARRO = "CARRO",
  MOTO = "MOTO"
}

export enum TipoUsuario {
  CLIENTE = "CLIENTE",
  ADMIN = "ADMIN"
}

export interface Vehiculo {
  placa: string;
  tipo: TipoVehiculo;
}

export interface Usuario {
  id: string;
  nombre: string;
  telefono: string;
  tipo: TipoUsuario;
}

export interface Celda {
  numero: number;
  ocupada: boolean;
  placaVehiculo?: string;
}

export interface RegistroEntradaSalida {
  placaVehiculo: string;
  idUsuario?: string;
  numeroCelda: number;
  fechaHora: string;
  esEntrada: boolean;
}

export interface Novedad {
  placaVehiculo: string;
  descripcion: string;
  fechaHora: string;
}

export interface Pago {
  placaVehiculo: string;
  valor: number;
  fechaHora: string;
}

export interface ParqueaderoState {
  nombre: string;
  totalCeldas: number;
  celdasDisponibles: number;
  vehiculosEnParqueadero: Map<string, Vehiculo>;
  usuarios: Map<string, Usuario>;
  celdas: Celda[];
  registros: RegistroEntradaSalida[];
  novedades: Novedad[];
  pagos: Pago[];
}
