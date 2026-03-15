import React from 'react';
import logo from './logo.svg';
import './App.css';
import { Route, Routes } from 'react-router';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import LoggedUserPage from './layout/LoggedUserPage';
import NotFound from './pages/NotFound';
import NotLoggedUserPage from './layout/NotLoggedUserPage';
import ProtectedRoute from './components/ProtectedRoute';

function App() {

  return (
    <>
      <Routes>
          <Route element={<NotLoggedUserPage/>}>
            <Route index element={<Home/>}/>
          </Route>
          <Route path='/logged' element={
            <ProtectedRoute
              requiresLogin={true}
              content={<LoggedUserPage/>}
            />
          }/>
          <Route path='/login' element={<Login/>}/>
          <Route path='/register' element={<Register/>}/>
          <Route path='*' element={<NotFound/>}/>
      </Routes>
    </>
  );
}

export default App;
