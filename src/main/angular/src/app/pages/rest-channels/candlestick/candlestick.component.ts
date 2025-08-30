import {Component, inject, OnInit} from '@angular/core';
import {ApiResponse} from '../order-history/model/dto';
import {CandlestickRestService} from './services/candlestick.rest.service';
import {Candlestick, CandlestickResult} from './model/dto';
import {JsonPipe} from '@angular/common';
import {Chart, ChartModule} from 'angular-highcharts';

@Component({
  selector: 'app-candlestick',
  imports: [
    JsonPipe,
    ChartModule,
  ],
  templateUrl: './candlestick.component.html',
  standalone: true,
  styleUrl: './candlestick.component.css'
})
export class CandlestickComponent implements OnInit {

  candlestickSrv = inject(CandlestickRestService);
  candlesticks: Candlestick[] = [];

  ngOnInit(): void {
    this.candlestickSrv.getCandlesticks().subscribe((orderHistory: ApiResponse<CandlestickResult>) => {
      this.candlesticks = orderHistory.result.data;
    })
  }
}
