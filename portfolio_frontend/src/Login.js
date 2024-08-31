import React, { useState } from 'react';
import ApiService from './ApiService';
import './App.css';


const Login = ({ onLoginSuccess, onCancel }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = () => {
    ApiService.login(username, password)
      .then(data => {
        onLoginSuccess(data);
      })
      .catch(error => {
        console.error('Error logging in', error);
        alert('Anmeldung fehlgeschlagen!');
      });
  };

  return (
    <div className="login-form">
      <h2>Login</h2>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <div className="login-buttons">
        <button onClick={handleLogin}>Login</button>
        <button className="cancel-button" onClick={onCancel}>Cancel</button>
      </div>
    </div>
  );
};

export default Login;
