
import React, { useState } from "react";
import { Usuario, TipoUsuario } from "../../types";
import api from "../../services/api";

interface UserRegistrationProps {
  onSuccess: () => void;
  onCancel: () => void;
}

const UserRegistration: React.FC<UserRegistrationProps> = ({ onSuccess, onCancel }) => {
  const [formData, setFormData] = useState<Usuario>({
    id: "",
    nombre: "",
    telefono: "",
    tipo: TipoUsuario.CLIENTE
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      await api.registrarUsuario(formData);
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
      <h2>👤 Registrar Usuario</h2>
      
      {success && <div className="success-message">✅ Usuario registrado exitosamente</div>}
      {error && <div className="error-message">❌ {error}</div>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="id">ID del usuario:</label>
          <input
            id="id"
            name="id"
            type="text"
            value={formData.id}
            onChange={handleChange}
            placeholder="Cédula o ID"
            required
            disabled={loading}
          />
        </div>

        <div className="form-group">
          <label htmlFor="nombre">Nombre:</label>
          <input
            id="nombre"
            name="nombre"
            type="text"
            value={formData.nombre}
            onChange={handleChange}
            placeholder="Nombre completo"
            required
            disabled={loading}
          />
        </div>

        <div className="form-group">
          <label htmlFor="telefono">Teléfono:</label>
          <input
            id="telefono"
            name="telefono"
            type="tel"
            value={formData.telefono}
            onChange={handleChange}
            placeholder="3001234567"
            required
            disabled={loading}
          />
        </div>

        <div className="form-group">
          <label htmlFor="tipo">Tipo de usuario:</label>
          <select
            id="tipo"
            name="tipo"
            value={formData.tipo}
            onChange={handleChange}
            disabled={loading}
          >
            <option value={TipoUsuario.CLIENTE}>Cliente</option>
            <option value={TipoUsuario.ADMIN}>Administrador</option>
          </select>
        </div>

        <div className="button-group">
          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? "Procesando..." : "Registrar Usuario"}
          </button>
          <button type="button" onClick={onCancel} className="btn-secondary">
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
};

export default UserRegistration;
