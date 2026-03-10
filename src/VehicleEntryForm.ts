import React, { useState } from "react";
import { Vehiculo, TipoVehiculo } from "../../types";
import api from "../../services/api";

interface VehicleEntryProps {
  onSuccess: () => void;
  onCancel: () => void;
}

const VehicleEntry: React.FC<VehicleEntryProps> = ({ onSuccess, onCancel }) => {
  const [placa, setPlaca] = useState("");
  const [tipo, setTipo] = useState<TipoVehiculo>(TipoVehiculo.CARRO);
  const [idUsuario, setIdUsuario] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const vehiculo: Vehiculo = { placa: placa.toUpperCase(), tipo };
      await api.registrarEntrada(vehiculo, idUsuario);
      setSuccess(true);
      setTimeout(onSuccess, 1500);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Error desconocido");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-container">
      <h2>📋 Registrar Entrada de Vehículo</h2>
      
      {success && <div className="success-message">✅ Entrada registrada exitosamente</div>}
      {error && <div className="error-message">❌ {error}</div>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="placa">Placa del vehículo:</label>
          <input
            id="placa"
            type="text"
            value={placa}
            onChange={(e) => setPlaca(e.target.value)}
            placeholder="Ej: ABC-123"
            required
            disabled={loading}
          />
        </div>

        <div className="form-group">
          <label htmlFor="tipo">Tipo de vehículo:</label>
          <select
            id="tipo"
            value={tipo}
            onChange={(e) => setTipo(e.target.value as TipoVehiculo)}
            disabled={loading}
          >
            <option value={TipoVehiculo.CARRO}>Carro</option>
            <option value={TipoVehiculo.MOTO}>Moto</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="usuario">ID del usuario:</label>
          <input
            id="usuario"
            type="text"
            value={idUsuario}
            onChange={(e) => setIdUsuario(e.target.value)}
            placeholder="Ej: 12345678"
            required
            disabled={loading}
          />
        </div>

        <div className="button-group">
          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? "Procesando..." : "Registrar Entrada"}
          </button>
          <button type="button" onClick={onCancel} className="btn-secondary">
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
};

export default VehicleEntry;
