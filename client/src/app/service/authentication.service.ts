import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';

export class User {
  constructor(public status: string) {}
}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  constructor(private httpClient: HttpClient) {}
// Provide username and password for authentication, and once authentication is successful,
// store JWT token in session
  authenticate(email, password) {
    return this.httpClient
      .post<any>('http://localhost:8080/auth', {email , password })
      .pipe(
        map(userData => {
          sessionStorage.setItem('username', email);
          let tokenStr = userData.token;
          sessionStorage.setItem('token', tokenStr);
          sessionStorage.setItem('userId', userData.userId);
          sessionStorage.setItem('userRole', userData.role);
          return userData;
        })
      );
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem('username');
    console.log(!(user === null));
    return !(user === null);
  }

  logOut() {
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('userId');
    sessionStorage.removeItem('userRole');
  }

  isUserModerator() {
    return sessionStorage.getItem('userRole').toString() === 'MODERATOR';
  }
}
