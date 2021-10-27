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

  walletPasswords: WalletPassword[] = [];

  constructor(private walletPasswordService: WalletPasswordService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.listWalletPasswords();
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
    });
  }

  onClickDelete(passwordId: number): void {
    this.walletPasswordService.deletePassword(passwordId).subscribe((result) => {
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
      <p>{{password}}!</p>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-outline-dark" (click)="activeModal.close('Close click')">Close</button>
    </div>
  `
})
export class NgbdModalContent {
  @Input() password;

  constructor(public activeModal: NgbActiveModal) {}
}
