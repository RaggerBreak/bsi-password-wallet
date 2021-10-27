import { TestBed } from '@angular/core/testing';

import { WalletPasswordService } from './wallet-password.service';

describe('WalletPasswordService', () => {
  let service: WalletPasswordService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WalletPasswordService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
