import {Component, inject, OnInit} from '@angular/core';
import {ApiResponse} from '../order-history/model/dto';
import {CandlestickRestService} from './services/candlestick.rest.service';
import {Candlestick, CandlestickResult} from './model/dto';
import {JsonPipe} from '@angular/common';
import {HighchartsChartComponent} from 'highcharts-angular';
import * as Highcharts from 'highcharts/highstock';
import {createCandlestickAndVolumeChartOptions} from './candlestick-and-volume-chart.config';

@Component({
  selector: 'app-candlestick',
  imports: [
    JsonPipe,
    HighchartsChartComponent,
  ],
  templateUrl: './candlestick.component.html',
  standalone: true,
  styleUrl: './candlestick.component.css'
})
export class CandlestickComponent implements OnInit {

  Highcharts: typeof Highcharts = Highcharts;
  updateFlag = false;
  candlestickSrv = inject(CandlestickRestService);
  candlesticks: Candlestick[] = [];
  chartOptions: Highcharts.Options = {}

  ngOnInit(): void {
    this.candlestickSrv.getCandlesticks().subscribe((orderHistory: ApiResponse<CandlestickResult>) => {
      this.candlesticks = orderHistory.result.data;
      this.chartOptions = createCandlestickAndVolumeChartOptions(this.candlesticks);
      this.updateFlag = true;
    })
  }
}
