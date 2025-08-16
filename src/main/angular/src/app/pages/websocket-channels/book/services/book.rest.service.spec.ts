import { TestBed } from '@angular/core/testing';

import { BookRestService } from './book.rest.service';

describe('BookRestService', () => {
  let service: BookRestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookRestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
