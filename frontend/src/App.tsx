import React from 'react';
import logo from './logo.svg';
import './App.css';
import { Route, Routes } from 'react-router';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import LoggedUserPage from './layout/LoggedUserPage';

function App() {

  return (
    <>
      <Routes>
        <Route element={<LoggedUserPage/>}>
          <Route index element={<Home/>}/>
          <Route path='/login' element={<Login/>}/>
          <Route path='/register' element={<Register/>}/>
        </Route>
      </Routes>
    </>
  );
}

export default App;
