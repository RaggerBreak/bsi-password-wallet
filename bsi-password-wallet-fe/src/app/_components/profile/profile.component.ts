import { Component, OnInit } from '@angular/core';
import {TokenStorageService} from "../../_services/token-storage.service";
import {UserService} from "../../_services/user.service";
import {LastLoginAttemptsLogs} from "../../_common/last-login-attempts-logs";
import {IpAddressLock} from "../../_common/ip-address-lock";
import {PasswordAccessMode} from "../../_common/password-access-mode";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  formChangePassword: any = {
    oldPassword: null,
    newPassword: null,
    passwordForm: null
  };
  passwordForms: string[] = ['SHA512', 'HMAC'];
  currentUser: any;
  lastLoginAttemptsLogs: LastLoginAttemptsLogs;
  permanentlyLockedIps: IpAddressLock[];

  passwordAccessMode: PasswordAccessMode;

  constructor(private token: TokenStorageService, private userService: UserService) { }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.formChangePassword.oldPassword = null;
    this.formChangePassword.newPassword = null;
    this.formChangePassword.passwordForm = null;

    this.userService.getAccessMode().subscribe(data => {
      this.passwordAccessMode = data;
    });

    this.userService.getLastLoginAttemptsLogs().subscribe(data => {
      this.lastLoginAttemptsLogs = data;
    });

    this.userService.getPermanentlyLockedIpAddresses().subscribe(data => {
      this.permanentlyLockedIps = data;
    })
  }

  onClickChangePassword() {
    const { oldPassword, newPassword, passwordForm } = this.formChangePassword;
    this.userService.changePassword(oldPassword, newPassword, passwordForm).subscribe(data => {
      this.ngOnInit();
    });
  }

  onClickDeletePermanentlyLockedIp(addressLockId: number): void {
    this.userService.deletePermanentlyLockedIp(addressLockId).subscribe((result) => {
      this.ngOnInit();
    });
  }

  onClickChangePasswordAccessMode(passwordAccessMode: PasswordAccessMode) {
    this.userService.changePasswordAccessMode(passwordAccessMode).subscribe((result) => {
      this.ngOnInit();
    });
  }
}
