import {Component, inject, OnInit} from '@angular/core';
import {OrderWebsocketClientService} from './services/order-websocket-client.service';
import {DatePipe, JsonPipe, NgClass} from '@angular/common';
import {UserOrderMessage} from './model/dto';
import {OrderHistoryItem} from '../../rest-channels/order-history/model/dto';

@Component({
  selector: 'app-order',
  imports: [
    JsonPipe,
    DatePipe,
    NgClass
  ],
  templateUrl: './order.component.html',
  standalone: true,
  styleUrl: './order.component.css'
})
export class OrderComponent implements OnInit {

  hoveredOrderId: string | null = null;
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

  removeOrder(index: number) {
    this.lastUserOrders.splice(index, 1);
  }

  onRowEnter(orderId: string) {
    this.hoveredOrderId = orderId;
  }

  onRowLeave() {
    this.hoveredOrderId = null;
  }

  getRowClass(order: UserOrderMessage): string {
    if (order.result.data[0].side === 'BUY' && order.result.data[0].status === 'FILLED') return 'table-danger';
    if (order.result.data[0].side === 'SELL' && order.result.data[0].status === 'FILLED') return 'table-success';
    return '';
  }
}
