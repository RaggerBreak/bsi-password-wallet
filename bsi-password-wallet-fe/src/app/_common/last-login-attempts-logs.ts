import {LoginAttempt} from "./login-attempt";

export class LastLoginAttemptsLogs {
  lastFailedLoginAttempt: LoginAttempt = null;
  lastSuccessfulLoginAttempt: LoginAttempt = null;
}
