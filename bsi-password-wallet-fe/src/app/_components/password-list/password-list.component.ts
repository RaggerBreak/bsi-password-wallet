import { Component, OnInit } from '@angular/core';
import {WalletPassword} from "../../_common/wallet-password";
import {WalletPasswordService} from "../../_services/wallet-password.service";

@Component({
  selector: 'app-password-list',
  templateUrl: './password-list.component.html',
  styleUrls: ['./password-list.component.css']
})
export class PasswordListComponent implements OnInit {

  walletPasswords: WalletPassword[] = [];

  constructor(private walletPasswordService: WalletPasswordService) { }

  ngOnInit(): void {
    this.listWalletPasswords();
  }

  listWalletPasswords(): void {
    this.walletPasswordService.getWalletPasswords().subscribe(data => {
      this.walletPasswords = data;
      console.log(data);
    });
  }

}
