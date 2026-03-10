import React, { useState } from "react";
import api from "../../services/api";

interface VehicleExitProps {
  onSuccess: () => void;
  onCancel: () => void;
}

const VehicleExit: React.FC<VehicleExitProps> = ({ onSuccess, onCancel }) => {
  const [placa, setPlaca] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      await api.registrarSalida(placa.toUpperCase());
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
      <h2>🚗 Registrar Salida de Vehículo</h2>
      
      {success && <div className="success-message">✅ Salida registrada exitosamente</div>}
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

        <div className="button-group">
          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? "Procesando..." : "Registrar Salida"}
          </button>
          <button type="button" onClick={onCancel} className="btn-secondary">
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
};

export default VehicleExit;
