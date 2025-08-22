import {Component, inject, OnInit} from '@angular/core';
import {OrderHistoryService} from './service/order-history.service';
import {ApiResponse, OrderHistoryItem, OrderHistoryResult} from './model/dto';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-order-history',
  imports: [
    NgClass
  ],
  templateUrl: './order-history.component.html',
  standalone: true,
  styleUrl: './order-history.component.css'
})
export class OrderHistoryComponent implements OnInit {

  orderHistorySrv = inject(OrderHistoryService);
  orderHistory: OrderHistoryItem[] = [];

  ngOnInit(): void {
    this.orderHistorySrv.getOrderHistory().subscribe((orderHistory: ApiResponse<OrderHistoryResult>) => {
      this.orderHistory = orderHistory.result.data
    })
  }
}
