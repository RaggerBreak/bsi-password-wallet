import {Component, Input, OnInit} from '@angular/core';
import {WalletPassword} from "../../../_common/wallet-password";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {WalletPasswordService} from "../../../_services/wallet-password.service";

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.css']
})
export class UpdatePasswordComponent implements OnInit {

  errorMessage = '';
  isError: boolean = false;

  @Input() walletPassword: WalletPassword;
  formUpdatePassword: any = {
    name: null,
    login: null,
    description: null,
    oldPassword: null,
    newPassword: null
  };

  constructor(public activeModal: NgbActiveModal, private walletPasswordService: WalletPasswordService) { }

  ngOnInit(): void {
    this.isError = false;
    this.formUpdatePassword.name = this.walletPassword.name;
    this.formUpdatePassword.login = this.walletPassword.login;
    this.formUpdatePassword.description = this.walletPassword.description;
  }

  onClickUpdate(): void {
    if (this.formUpdatePassword.oldPassword == null || this.formUpdatePassword.oldPassword == '') {

      this.errorMessage = "Confirm old password";
      this.isError = true;

    } else {

      const {name, login, description, oldPassword, newPassword} = this.formUpdatePassword;
      this.walletPasswordService.updatePassword(this.walletPassword.id, name, login, description, oldPassword, newPassword).subscribe(data => {
        this.activeModal.close('Close click');
        location.reload();
      }, err => {
        this.errorMessage = err.error.message;
        this.isError = true;
      });

    }
  }
}
