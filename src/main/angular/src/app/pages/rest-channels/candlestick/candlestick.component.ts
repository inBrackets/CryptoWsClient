import {Component, inject, OnInit} from '@angular/core';
import {OrderHistoryService} from '../order-history/service/order-history.service';
import {ApiResponse, OrderHistoryItem, OrderHistoryResult} from '../order-history/model/dto';
import {CandlestickRestService} from './services/candlestick.rest.service';
import {Candlestick} from './model/dto';
import {JsonPipe} from '@angular/common';

@Component({
  selector: 'app-candlestick',
  imports: [
    JsonPipe
  ],
  templateUrl: './candlestick.component.html',
  standalone: true,
  styleUrl: './candlestick.component.css'
})
export class CandlestickComponent implements OnInit {

  candlestickSrv = inject(CandlestickRestService);
  candlesticks: Candlestick[] = [];

  ngOnInit(): void {
    this.candlestickSrv.getCandlesticks().subscribe((orderHistory: Candlestick[]) => {
      this.candlesticks = orderHistory
    })
  }
}
