import { TestBed } from '@angular/core/testing';

import { BookWebsocketService } from './book.websocket.service';

describe('BookService', () => {
  let service: BookWebsocketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookWebsocketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
