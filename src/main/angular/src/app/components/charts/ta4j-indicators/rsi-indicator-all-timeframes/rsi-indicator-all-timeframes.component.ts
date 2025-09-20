import {Component, inject, Input, OnInit} from '@angular/core';
import {AllIndicatorsService} from '../all-indicators.service';
import {NgIf} from '@angular/common';
import {HighchartsChartComponent, providePartialHighcharts} from 'highcharts-angular';


@Component({
  selector: 'app-rsi-indicator-all-timeframes',
  imports: [
    HighchartsChartComponent,
    NgIf
  ],
  providers: [
    providePartialHighcharts({
      modules: () => [import('highcharts/esm/modules/stock')],
    })
  ],
  templateUrl: './rsi-indicator-all-timeframes.component.html',
  standalone: true,
  styleUrl: './rsi-indicator-all-timeframes.component.css'
})
export class RsiIndicatorAllTimeframesComponent implements OnInit {

  masterSrv = inject(AllIndicatorsService);
  data_1m: [number, number][] = [];
  data_5m: [number, number][] = [];
  data_15m: [number, number][] = [];
  data_30m: [number, number][] = [];
  data_1h: [number, number][] = [];
  data_2h: [number, number][] = [];
  data_4h: [number, number][] = [];
  data_12h: [number, number][] = [];
  chartOptions: Highcharts.Options = {}

  @Input() barCount: number = 14;

  ngOnInit(): void {
    this.masterSrv.getRsiSeries("12h", this.barCount).subscribe((response : { timestamp: number; rsi: number }[])=> {
      this.data_12h = response.map(
        ({timestamp, rsi}) => [timestamp, rsi]
      );
      this.updateChart();
    })
    this.masterSrv.getRsiSeries("4h", this.barCount).subscribe((response : { timestamp: number; rsi: number }[])=> {
      this.data_4h = response.map(
        ({timestamp, rsi}) => [timestamp, rsi]
      );
      this.updateChart();
    })
    this.masterSrv.getRsiSeries("2h", this.barCount).subscribe((response : { timestamp: number; rsi: number }[])=> {
      this.data_2h = response.map(
        ({timestamp, rsi}) => [timestamp, rsi]
      );
      this.updateChart();
    })
    this.masterSrv.getRsiSeries("1h", this.barCount).subscribe((response : { timestamp: number; rsi: number }[])=> {
      this.data_1h = response.map(
        ({timestamp, rsi}) => [timestamp, rsi]
      );
      this.updateChart();
    })
    this.masterSrv.getRsiSeries("30m", this.barCount).subscribe((response : { timestamp: number; rsi: number }[])=> {
      this.data_30m = response.map(
        ({timestamp, rsi}) => [timestamp, rsi]
      );
      this.updateChart();
    })
    this.masterSrv.getRsiSeries("15m", this.barCount).subscribe((response : { timestamp: number; rsi: number }[])=> {
      this.data_15m = response.map(
        ({timestamp, rsi}) => [timestamp, rsi]
      );
      this.updateChart();
    })
    this.masterSrv.getRsiSeries("5m", this.barCount).subscribe((response : { timestamp: number; rsi: number }[])=> {
      this.data_5m = response.map(
        ({timestamp, rsi}) => [timestamp, rsi]
      );
      this.updateChart();
    })
    this.masterSrv.getRsiSeries("1m", this.barCount).subscribe((response : { timestamp: number; rsi: number }[])=> {
      this.data_1m = response.map(
        ({timestamp, rsi}) => [timestamp, rsi]
      );
      this.updateChart();
    })
  }

  updateChart() {
    this.chartOptions = {
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
        selected: 4,
        inputEnabled: false
      },

      title: {
        text: `ta4j RSI - All timeframes (${this.barCount} bars)`,
      },

      legend: {
        itemStyle: {color: '#ffffff', fontSize: '14px'}
      },

      xAxis: {
        overscroll: '10px'
      },

      series: [
        {
          name: 'RSI 1m',
          data: this.data_1m,
          type: 'line',
          tooltip: { valueDecimals: 2 }
        },
        {
          name: 'RSI 5m',
          data: this.data_5m,
          type: 'line',
          tooltip: { valueDecimals: 2 }
        },
        {
          name: 'RSI 15m',
          data: this.data_15m,
          type: 'line',
          tooltip: { valueDecimals: 2 }
        },
        {
          name: 'RSI 30m',
          data: this.data_30m,
          type: 'line',
          tooltip: { valueDecimals: 2 }
        },
        {
          name: 'RSI 1h',
          data: this.data_1h,
          type: 'line',
          tooltip: { valueDecimals: 2 }
        },
        {
          name: 'RSI 2h',
          data: this.data_2h,
          type: 'line',
          tooltip: { valueDecimals: 2 }
        },
        {
          name: 'RSI 4h',
          data: this.data_4h,
          type: 'line',
          tooltip: { valueDecimals: 2 }
        },
        {
          name: 'RSI 1m',
          data: this.data_12h,
          type: 'line',
          tooltip: { valueDecimals: 2 }
        },
      ]
    }
  }
}
