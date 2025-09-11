import {Component, inject, OnInit} from '@angular/core';
import {ApiResponse} from '../order-history/model/dto';
import {CandlestickRestService} from './services/candlestick.rest.service';
import {Candlestick, CandlestickResult, CandlestickWithInstrumentName} from './model/dto';
import {JsonPipe} from '@angular/common';
import {ChartModule, StockChart} from 'angular-highcharts';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-candlestick',
  imports: [
    JsonPipe,
    ChartModule,
    FormsModule,
  ],
  templateUrl: './candlestick.component.html',
  standalone: true,
  styleUrl: './candlestick.component.css'
})
export class CandlestickComponent implements OnInit {

  instrument: string = "";
  timeFrame: string = "";

  candlestickSrv = inject(CandlestickRestService);
  candlesticks: CandlestickWithInstrumentName[] = [];
  stock: StockChart = new StockChart();
  stockData: [number, number, number, number, number][] = [];

  ngOnInit(): void {
    // set defaults
    this.instrument = "CRO_USD"
    this.timeFrame = "M15"
    this.loadCandlesticks();
  }

  // 🔑 reusable loader
  loadCandlesticks() {
    this.candlestickSrv.getCandlesticks(this.instrument, this.timeFrame)
      .subscribe((response: CandlestickWithInstrumentName[]) => {
        this.candlesticks = response;

        // map to Highcharts format: [timestamp, open, high, low, close]
        this.stockData = this.candlesticks.map(
          ({instrumentName, o, h, l, c, v, t}) => [t, o, h, l, c]
        );

        // rebuild chart with new data
        this.stock = new StockChart({
          title: {
            text: `Price of ${this.instrument}`
          },
          rangeSelector: {
            buttons: [
              {type: 'minute', count: 5, text: '5m'},
              { type: 'hour', count: 1, text: '1h' },
              { type: 'hour', count: 2, text: '2h' },
              { type: 'day', count: 1, text: '1D' },
              { type: 'month', count: 1, text: '1M' },
              { type: 'all', count: 1, text: 'All' }
            ],
            selected: 1,
            inputEnabled: false
          },
          series: [{
            name: this.instrument,
            type: 'candlestick',
            data: this.stockData,
            tooltip: { valueDecimals: 5 }
          }]
        });
      });
  }

  // called when you click the button
  changeInstrumentAndTimeFrame() {
    this.loadCandlesticks();
  }
}
