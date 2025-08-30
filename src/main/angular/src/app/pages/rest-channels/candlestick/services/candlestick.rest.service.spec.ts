import { TestBed } from '@angular/core/testing';

import { CandlestickRestService } from './candlestick.rest.service';

describe('CandlestickRestService', () => {
  let service: CandlestickRestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CandlestickRestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
