import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";


const API_URL = 'http://localhost:8080/api/board/';

const API_URL_USER = 'http://localhost:8080/api/user/';

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

}
