
import { 
  Vehiculo, 
  Usuario, 
  Celda, 
  RegistroEntradaSalida, 
  Novedad, 
  Pago,
  ParqueaderoState 
} from "../types";

const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api";

class ParqueaderoAPI {
  // Entrada de vehículos
  async registrarEntrada(vehiculo: Vehiculo, idUsuario: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/entrada`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ vehiculo, idUsuario })
    });
    
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.mensaje || "Error al registrar entrada");
    }
  }

  // Salida de vehículos
  async registrarSalida(placa: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/salida`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ placa })
    });
    
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.mensaje || "Error al registrar salida");
    }
  }

  // Registro de usuarios
  async registrarUsuario(usuario: Usuario): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/usuarios`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(usuario)
    });
    
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.mensaje || "Error al registrar usuario");
    }
  }

  // Obtener estado de celdas
  async obtenerEstadoCeldas(): Promise<{ total: number; disponibles: number }> {
    const response = await fetch(`${API_BASE_URL}/celdas`);
    if (!response.ok) throw new Error("Error al obtener estado de celdas");
    return response.json();
  }

  // Registrar novedad
  async registrarNovedad(placa: string, descripcion: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/novedades`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ placa, descripcion })
    });
    
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.mensaje || "Error al registrar novedad");
    }
  }

  // Calcular pago
  async calcularPago(placa: string): Promise<number> {
    const response = await fetch(`${API_BASE_URL}/pago/calcular/${placa}`);
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.mensaje || "Error al calcular pago");
    }
    const data = await response.json();
    return data.valor;
  }

  // Registrar pago
  async registrarPago(placa: string, valor: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/pagos`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ placa, valor })
    });
    
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.mensaje || "Error al registrar pago");
    }
  }
}

export default new ParqueaderoAPI();
