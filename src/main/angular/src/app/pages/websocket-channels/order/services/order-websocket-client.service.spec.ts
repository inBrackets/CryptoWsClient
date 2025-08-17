import { TestBed } from '@angular/core/testing';

import { OrderWebsocketClientService } from './order-websocket-client.service';

describe('OrderWebsocketClientService', () => {
  let service: OrderWebsocketClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrderWebsocketClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
