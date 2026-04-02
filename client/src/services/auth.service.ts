import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  token: string = '';
  isLoggedIn: boolean = false;
  id: string = '';

  constructor() {}

  saveToken(token: string) {
    this.token = token;
    this.isLoggedIn = true;
    localStorage.setItem('token', token);
  }

  
getRole(): string | null {
  return localStorage.getItem('role');
}


  SetRole(role: any) {
    localStorage.setItem('role', role);
  }

  get getLoginStatus(): boolean {
    return this.isLoggedIn || localStorage.getItem('token') != null;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout() {
    this.isLoggedIn = false;
    this.token = '';
    localStorage.removeItem('token');
    localStorage.removeItem('role');
  }
}