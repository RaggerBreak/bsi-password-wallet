import {SharedUser} from "./shared-user";

export class WalletPassword {
  id: number;
  name: string;
  login: string;
  description: string;
  password: string;
  sharedUsers: SharedUser[];
}
