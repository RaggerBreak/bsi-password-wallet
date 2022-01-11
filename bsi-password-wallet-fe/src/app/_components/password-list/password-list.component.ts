import {Component, Inject, Input, OnInit} from '@angular/core';
import {WalletPassword} from "../../_common/wallet-password";
import {WalletPasswordService} from "../../_services/wallet-password.service";
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-password-list',
  templateUrl: './password-list.component.html',
  styleUrls: ['./password-list.component.css']
})
export class PasswordListComponent implements OnInit {

  formNewPassword: any = {
    name: null,
    login: null,
    description: null,
    password: null
  };

  walletPasswords: WalletPassword[] = [];

  constructor(private walletPasswordService: WalletPasswordService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.listWalletPasswords();
    this.formNewPassword.name = null;
    this.formNewPassword.login = null;
    this.formNewPassword.description = null;
    this.formNewPassword.password = null;
  }

  listWalletPasswords(): void {
    this.walletPasswordService.getWalletPasswords().subscribe(data => {
      this.walletPasswords = data;
    });
  }

  onClickShow(passwordId: number): void {

    const modalRef = this.modalService.open(NgbdModalContent);

    this.walletPasswordService.getRawPassword(passwordId).subscribe(data => {
      modalRef.componentInstance.password = data.password;
      modalRef.componentInstance.passwordId = passwordId;
    });
  }

  onClickDelete(passwordId: number): void {
    this.walletPasswordService.deletePassword(passwordId).subscribe((result) => {
      this.ngOnInit();
    });
  }

  onClickNewPassword(): void {
    const { name, login, description, password } = this.formNewPassword;
    this.walletPasswordService.addPassword(name, login, description, password).subscribe(data => {
      this.ngOnInit();
    })
  }

  onClickDeleteUserFromSharedPassword(passwordId: number, userId: number): void {
    this.walletPasswordService.deleteUserFromSharedPassword(passwordId, userId).subscribe((result) => {
      this.ngOnInit();
    });
  }
}

@Component({
  selector: 'ngbd-modal-content',
  template: `
    <div class="modal-header">
      <h4 class="modal-title">Password</h4>
      <button type="button" class="close" aria-label="Close" (click)="activeModal.dismiss('Cross click')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="modal-body">
      <p>{{password}}</p>
      <hr>
      <h6>Share password</h6>
      <div class="input-group mb-3">
        <span class="input-group-text" id="basic-addon1">@</span>
        <input type="email" class="form-control" placeholder="e-mail@example.com" aria-label="userEmail"
               aria-describedby="basic-addon1" required minlength="1"
               [(ngModel)]="formSharePassword.userEmail" #userEmail="ngModel"/>
        <button type="button" class="btn btn-outline-dark " (click)="onClickShare()">
          <i class="fas fa-share-alt"></i>
        </button>
      </div>
      <div class="alert alert-danger" role="alert" *ngIf="isError">
        {{errorMessage}}
      </div>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-outline-dark" (click)="activeModal.close('Close click')">Close</button>
    </div>
  `
})
export class NgbdModalContent implements OnInit{
  @Input() password: string;
  @Input() passwordId: number;
  formSharePassword: any = {
    userEmail: null
  };

  errorMessage = '';
  isError: boolean = false;

  constructor(public activeModal: NgbActiveModal, private walletPasswordService: WalletPasswordService) {}

  ngOnInit(): void {
        this.isError = false;
    }

  onClickShare() {
    this.walletPasswordService.sharePassword(this.passwordId, this.formSharePassword.userEmail).subscribe(data => {
      this.activeModal.close('Close click');
      location.reload();
    }, err => {
      this.errorMessage = err.error.message;
      this.isError = true;
    });
  }
}
