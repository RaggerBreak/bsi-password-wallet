import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {LastLoginAttemptsLogs} from "../_common/last-login-attempts-logs";
import {IpAddressLock} from "../_common/ip-address-lock";


const API_URL = 'http://localhost:8080/api/board/';
const API_URL_USER = 'http://localhost:8080/api/user/';
const API_URL_LOGIN_ATTEMPT = 'http://localhost:8080/api/loginAttempt';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  getPublicContent(): Observable<any> {
    return this.httpClient.get(API_URL + 'all', {responseType: 'text'});
  }

  getUserBoard(): Observable<any> {
    return this.httpClient.get(API_URL + 'user', {responseType: 'text'})
  }

  getAdminBoard(): Observable<any> {
    return this.httpClient.get(API_URL + 'admin', { responseType: 'text' });
  }

  changePassword(oldPassword: string, newPassword: string, passwordForm: string): Observable<any> {
    return this.httpClient.post(API_URL_USER + 'password/change', {
      oldPassword,
      newPassword,
      passwordForm
    }, httpOptions);
  }

  getLastLoginAttemptsLogs(): Observable<LastLoginAttemptsLogs> {
    return this.httpClient.get<LastLoginAttemptsLogs>(API_URL_LOGIN_ATTEMPT);
  }

  getPermanentlyLockedIpAddresses(): Observable<IpAddressLock[]> {
    return this.httpClient.get<IpAddressLock[]>(API_URL_LOGIN_ATTEMPT + `/permanentlyLockedIp`);
  }

  deletePermanentlyLockedIp(addressLockId: number) {
    return this.httpClient.delete(API_URL_LOGIN_ATTEMPT + `/permanentlyLockedIp/${addressLockId}`);
  }
}
