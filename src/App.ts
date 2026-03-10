import React, { useState } from "react";
import Menu from "../components/Menu/Menu";
import VehicleEntry from "../components/VehicleEntry/VehicleEntry";
import VehicleExit from "../components/VehicleExit/VehicleExit";
import UserRegistration from "../components/UserRegistration/UserRegistration";
import CellsStatus from "../components/CellsStatus/CellsStatus";
import Payment from "../components/Payment/Payment";
import "../styles/App.css";

const TOTAL_CELDAS = 50;

type ViewType = "menu" | "entry" | "exit" | "user" | "cells" | "payment" | "novedad";

const App: React.FC = () => {
  const [currentView, setCurrentView] = useState<ViewType>("menu");

  const handleMenuSelect = (option: number) => {
    switch (option) {
      case 1:
        setCurrentView("entry");
        break;
      case 2:
        setCurrentView("exit");
        break;
      case 3:
        setCurrentView("user");
        break;
      case 4:
        setCurrentView("cells");
        break;
      case 5:
        setCurrentView("novedad");
        break;
      case 6:
        setCurrentView("payment");
        break;
      case 7:
        handleExit();
        break;
      default:
        break;
    }
  };

  const handleExit = () => {
    alert("Gracias por usar el sistema de Autos Colombia");
    setCurrentView("menu");
  };

  const goBack = () => {
    setCurrentView("menu");
  };

  return (
    <div className="app-container">
      {currentView === "menu" && <Menu onSelectOption={handleMenuSelect} />}
      {currentView === "entry" && <VehicleEntry onSuccess={goBack} onCancel={goBack} />}
      {currentView === "exit" && <VehicleExit onSuccess={goBack} onCancel={goBack} />}
      {currentView === "user" && <UserRegistration onSuccess={goBack} onCancel={goBack} />}
      {currentView === "cells" && <CellsStatus totalCeldas={TOTAL_CELDAS} onBack={goBack} />}
      {currentView === "payment" && <Payment onSuccess={goBack} onCancel={goBack} />}
    </div>
  );
};

export default App;
