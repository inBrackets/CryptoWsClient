import { TestBed } from '@angular/core/testing';

import { AllIndicatorsService } from './all-indicators.service';

describe('AllIndicatorsService', () => {
  let service: AllIndicatorsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AllIndicatorsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
