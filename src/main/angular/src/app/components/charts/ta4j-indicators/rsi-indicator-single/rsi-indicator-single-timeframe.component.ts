import {Component, inject, Input, OnInit} from '@angular/core';
import {NgIf} from '@angular/common';
import {HighchartsChartComponent, providePartialHighcharts} from 'highcharts-angular';
import {AllIndicatorsService} from '../all-indicators.service';

@Component({
  selector: 'app-rsi-indicator-single-timeframe',
  imports: [
    HighchartsChartComponent,
    NgIf
  ],
  providers: [
    providePartialHighcharts({
      modules: () => [import('highcharts/esm/modules/stock')],
    })
  ],
  templateUrl: './rsi-indicator-single-timeframe.component.html',
  standalone: true,
  styleUrl: './rsi-indicator-single-timeframe.component.css'
})
export class RsiIndicatorSingleTimeframeComponent implements OnInit {

  masterSrv = inject(AllIndicatorsService);
  data: [number, number][] = [];
  chartOptions: Highcharts.Options = {}

  @Input() timeframe: string = "15m";
  @Input() barCount: number = 14;

  ngOnInit(): void {
    this.masterSrv.getRsiSeries(this.timeframe, this.barCount).subscribe((response : { timestamp: number; rsi: number }[])=> {
      this.data = response.map(
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
        text: `ta4j RSI - ${this.timeframe}(${this.barCount} bars)`,
      },

      xAxis: {
        overscroll: '10px'
      },

      series: [{
        name: 'RSI',
        data: this.data,
        type: 'line',
        tooltip: {
          valueDecimals: 2
        },
        lastPrice: {
          enabled: true,
          color: 'transparent',
          label: {
            enabled: true,
            backgroundColor: '#ffffff',
            borderColor: '#2caffe',
            borderWidth: 1,
            style: {
              color: '#000000'
            }
          }
        }
      }]
    }
  }
}
