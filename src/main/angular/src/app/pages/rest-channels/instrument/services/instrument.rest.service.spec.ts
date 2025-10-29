import { TestBed } from '@angular/core/testing';

import { InstrumentRestService } from './instrument.rest.service';

describe('InstrumentRestService', () => {
  let service: InstrumentRestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InstrumentRestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
