import { Component, OnInit } from '@angular/core';
import {TokenStorageService} from "../../_services/token-storage.service";
import {UserService} from "../../_services/user.service";

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

  constructor(private token: TokenStorageService, private userService: UserService) { }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.formChangePassword.oldPassword = null;
    this.formChangePassword.newPassword = null;
    this.formChangePassword.passwordForm = null;
  }

  onClickChangePassword() {
    const { oldPassword, newPassword, passwordForm } = this.formChangePassword;
    this.userService.changePassword(oldPassword, newPassword, passwordForm).subscribe(data => {
      this.ngOnInit();
    });
  }
}
