import React from "react";

interface MenuProps {
  onSelectOption: (option: number) => void;
}

const Menu: React.FC<MenuProps> = ({ onSelectOption }) => {
  const menuOptions = [
    "Registrar entrada de vehículo",
    "Registrar salida de vehículo",
    "Registrar usuario",
    "Gestionar celdas",
    "Registrar novedad",
    "Gestionar pagos",
    "Salir"
  ];

  return (
    <div className="menu-container">
      <h1>🅿️ SISTEMA PARQUEADERO AUTOS COLOMBIA</h1>
      <div className="menu-options">
        {menuOptions.map((option, index) => (
          <button
            key={index}
            className="menu-button"
            onClick={() => onSelectOption(index + 1)}
          >
            {index + 1}. {option}
          </button>
        ))}
      </div>
    </div>
  );
};

export default Menu;
