import {Component, inject, OnInit} from '@angular/core';
import {ApiResponse} from '../order-history/model/dto';
import {CandlestickRestService} from './services/candlestick.rest.service';
import {Candlestick, CandlestickResult} from './model/dto';
import {JsonPipe} from '@angular/common';
import {ChartModule, StockChart} from 'angular-highcharts';

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
  stock: StockChart = new StockChart();
  stockData: [number, number, number, number, number][] = [];

  ngOnInit(): void {
    const instrumentName: string = "CRO_USD"
    this.candlestickSrv.getCandlesticks(instrumentName).subscribe((orderHistory: ApiResponse<CandlestickResult>) => {
      this.candlesticks = orderHistory.result.data;
      this.stockData = this.candlesticks.map(
        ({o, h, l, c, v, t}) => [t, o, h, l, c]
      );

      this.stock = new StockChart({

        title: {
          text: `Price of ${instrumentName}`
        },

        rangeSelector: {
          buttons: [{
            type: 'hour',
            count: 1,
            text: '1h'
          }, {
            type: 'day',
            count: 1,
            text: '1D'
          }, {
            type: 'all',
            count: 1,
            text: 'All'
          }],
          selected: 1,
          inputEnabled: false
        },

        series: [{
          name: `${instrumentName}`,
          type: 'candlestick',
          data: this.stockData,
          tooltip: {
            valueDecimals: 2
          }
        }]
      });
    })
  }
}
