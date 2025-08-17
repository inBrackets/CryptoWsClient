import {Component, inject, OnInit} from '@angular/core';
import {OrderWebsocketClientService} from './services/order-websocket-client.service';
import {DatePipe, JsonPipe} from '@angular/common';
import {UserOrderMessage} from './model/dto';

@Component({
  selector: 'app-order',
  imports: [
    JsonPipe,
    DatePipe
  ],
  templateUrl: './order.component.html',
  standalone: true,
  styleUrl: './order.component.css'
})
export class OrderComponent implements OnInit {

  websocketSrv = inject(OrderWebsocketClientService);
  currentOrder: UserOrderMessage | undefined;
  lastUserOrders: UserOrderMessage[] = [];

  constructor() {
  }

  ngOnInit(): void {
    console.log('connecting...');
    this.websocketSrv.connect();

    this.websocketSrv.order$.subscribe(message => {
      this.currentOrder = message;

      this.lastUserOrders.unshift(message); // add new animal at the front
    });
  }

}
