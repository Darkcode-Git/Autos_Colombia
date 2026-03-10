
import React, { useEffect, useState } from "react";
import api from "../../services/api";

interface CellsStatusProps {
  totalCeldas: number;
  onBack: () => void;
}

const CellsStatus: React.FC<CellsStatusProps> = ({ totalCeldas, onBack }) => {
  const [disponibles, setDisponibles] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchCellsStatus();
  }, []);

  const fetchCellsStatus = async () => {
    try {
      const data = await api.obtenerEstadoCeldas();
      setDisponibles(data.disponibles);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Error desconocido");
    } finally {
      setLoading(false);
    }
  };

  const ocupadas = totalCeldas - disponibles;
  const porcentajeOcupacion = ((ocupadas / totalCeldas) * 100).toFixed(1);

  return (
    <div className="status-container">
      <h2>📊 Estado de Celdas</h2>

      {error && <div className="error-message">❌ {error}</div>}
      {loading ? (
        <p>Cargando...</p>
      ) : (
        <>
          <div className="stats-grid">
            <div className="stat-card">
              <h3>Total de Celdas</h3>
              <p className="stat-value">{totalCeldas}</p>
            </div>
            <div className="stat-card available">
              <h3>Celdas Disponibles</h3>
              <p className="stat-value">{disponibles}</p>
            </div>
            <div className="stat-card occupied">
              <h3>Celdas Ocupadas</h3>
              <p className="stat-value">{ocupadas}</p>
            </div>
            <div className="stat-card">
              <h3>Ocupación</h3>
              <p className="stat-value">{porcentajeOcupacion}%</p>
            </div>
          </div>

          <div className="progress-bar">
            <div 
              className="progress-fill" 
              style={{ width: `${porcentajeOcupacion}%` }}
            ></div>
          </div>
        </>
      )}

      <button onClick={onBack} className="btn-secondary">
        Volver al Menú
      </button>
    </div>
  );
};

export default CellsStatus;
