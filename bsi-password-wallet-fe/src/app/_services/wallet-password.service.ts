import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {WalletPassword} from "../_common/wallet-password";

const API_URL = 'http://localhost:8080/api/wallet/';

@Injectable({
  providedIn: 'root'
})
export class WalletPasswordService {

  constructor(private httpClient: HttpClient) { }

  getWalletPasswords(): Observable<WalletPassword[]> {
    return this.httpClient.get<WalletPassword[]>(API_URL);
  }
}
