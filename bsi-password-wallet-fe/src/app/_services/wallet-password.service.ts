import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {WalletPassword} from "../_common/wallet-password";

const API_URL = 'http://localhost:8080/api/wallet/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class WalletPasswordService {

  constructor(private httpClient: HttpClient) { }

  getWalletPasswords(): Observable<WalletPassword[]> {
    return this.httpClient.get<WalletPassword[]>(API_URL);
  }

  getSharedPasswords(): Observable<WalletPassword[]> {
    return this.httpClient.get<WalletPassword[]>(API_URL + 'password/share');
  }

  getRawPassword(passwordId: number): Observable<PasswordResponse> {
    return this.httpClient.get<PasswordResponse>(API_URL + `password/${passwordId}`)
  }

  deletePassword(passwordId: number) {
    return this.httpClient.delete(API_URL + `password/${passwordId}`);
  }

  addPassword(name: string, login: string, description: string, password: string): Observable<any> {
    return this.httpClient.post(API_URL, {
      name,
      login,
      description,
      password
    }, httpOptions);
  }

  deleteUserFromSharedPassword(passwordId: number, userId: number) {
    return this.httpClient.delete(API_URL + `password/share/${passwordId}/${userId}`);
  }

  sharePassword(passwordId: number, userEmail: string): Observable<any> {
    return this.httpClient.post(API_URL + `password/share/${passwordId}/${userEmail}`, httpOptions);
  }

  updatePassword(passwordId: number, name: string, login: string, description: string, oldPassword: string, newPassword: string): Observable<any>  {
    return this.httpClient.put(API_URL + `password/${passwordId}`, {
      name,
      login,
      description,
      oldPassword,
      newPassword
    }, httpOptions);
  }
}

interface PasswordResponse {
  password: string
}

