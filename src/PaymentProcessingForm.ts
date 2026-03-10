import React, { useState } from "react";
import api from "../../services/api";

interface PaymentProps {
  onSuccess: () => void;
  onCancel: () => void;
}

const Payment: React.FC<PaymentProps> = ({ onSuccess, onCancel }) => {
  const [placa, setPlaca] = useState("");
  const [monto, setMonto] = useState("");
  const [precioPago, setPrecioPago] = useState<number | null>(null);
  const [cambio, setCambio] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const [step, setStep] = useState<"calculate" | "payment">("calculate");

  const handleCalculate = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const valor = await api.calcularPago(placa.toUpperCase());
      setPrecioPago(valor);
      setStep("payment");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Error al calcular pago");
    } finally {
      setLoading(false);
    }
  };

  const handlePayment = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    if (precioPago === null) {
      setError("Error en el cálculo del pago");
      setLoading(false);
      return;
    }

    const montoRecibido = parseFloat(monto);

    if (montoRecibido < precioPago) {
      setError("Monto insuficiente para realizar el pago");
      setLoading(false);
      return;
    }

    try {
      await api.registrarPago(placa.toUpperCase(), precioPago);
      const cambioCalculado = montoRecibido - precioPago;
      setCambio(cambioCalculado);
      setSuccess(true);
      setTimeout(onSuccess, 2000);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Error al registrar pago");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-container">
      <h2>💳 Gestionar Pagos</h2>

      {success && (
        <div className="success-message">
          ✅ Pago registrado exitosamente
          <p>Cambio a entregar: ${cambio?.toFixed(2)}</p>
        </div>
      )}
      {error && <div className="error-message">❌ {error}</div>}

      {step === "calculate" ? (
        <form onSubmit={handleCalculate}>
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
              {loading ? "Calculando..." : "Calcular Pago"}
            </button>
            <button type="button" onClick={onCancel} className="btn-secondary">
              Cancelar
            </button>
          </div>
        </form>
      ) : (
        <form onSubmit={handlePayment}>
          <div className="payment-info">
            <p><strong>Placa:</strong> {placa}</p>
            <p><strong>Valor a pagar:</strong> ${precioPago?.toFixed(2)}</p>
          </div>

          <div className="form-group">
            <label htmlFor="monto">Monto recibido:</label>
            <input
              id="monto"
              type="number"
              step="0.01"
              value={monto}
              onChange={(e) => setMonto(e.target.value)}
              placeholder="0.00"
              required
              disabled={loading}
            />
          </div>

          <div className="button-group">
            <button type="submit" disabled={loading} className="btn-primary">
              {loading ? "Procesando..." : "Confirmar Pago"}
            </button>
            <button 
              type="button" 
              onClick={() => {
                setStep("calculate");
                setMonto("");
                setPrecioPago(null);
              }} 
              className="btn-secondary"
            >
              Volver
            </button>
          </div>
        </form>
      )}
    </div>
  );
};

export default Payment;
