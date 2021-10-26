import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../_services/auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form: any = {
    username: null,
    email: null,
    password: null,
    passwordForm: null
  };
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  passwordForms: string[] = ['SHA512', 'HMAC'];

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    const { username, email, password, passwordForm } = this.form;

    this.authService.register(username, email, password, passwordForm).subscribe(data => {
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
        console.log(this.errorMessage);
      });
  }

}
