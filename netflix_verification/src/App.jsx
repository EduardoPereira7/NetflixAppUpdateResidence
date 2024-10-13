import { useState, useEffect } from "react";
import netflixLogo from "./assets/netflix.png";
import viteLogo from "/vite.svg";
import "./App.css";

function App() {
  const [update, setUpdate] = useState(0);
  const [state, setState] = useState("Aguardando atualização");
  const [emails, setEmails] = useState([]);

  useEffect(() => {
    if (update > 0) {
      fetch("http://localhost:3000/latest-emails")
        .then((response) => response.json())
        .then((data) => {
          setEmails(data.emails);
          setState("Emails recebidos!");
        })
        .catch((error) => {
          console.error("Erro ao buscar emails:", error);
          setState("Erro ao buscar emails");
        });
    }
  }, [update]);

  return (
    <>
      <div>
        <a href="https://netflix.com" target="_blank">
          <img src={netflixLogo} className="logo" alt="Netflix logo" />
        </a>
      </div>
      <h1>Atualizar residência</h1>
      <p className="read-the-docs">
        1º - Clica em atualizar para receber o último email com o link
      </p>
      <p>Estado atual: {state} </p>
      <button onClick={() => setUpdate((update) => update + 1)}>
        Atualizar
      </button>
      <p className="read-the-docs">
        2º - Clica no link recebido nos emails abaixo para atualizar a tua
        residência:
      </p>
      <ul>
        {emails.map((email, index) => (
          <li key={index}>{email}</li>
        ))}
      </ul>
    </>
  );
}

export default App;
