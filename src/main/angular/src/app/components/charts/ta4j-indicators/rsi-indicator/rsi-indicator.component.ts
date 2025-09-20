import {Component, inject, OnInit} from '@angular/core';
import {NgIf} from '@angular/common';
import {HighchartsChartComponent, providePartialHighcharts} from 'highcharts-angular';
import {AllIndicatorsService} from '../all-indicators.service';

@Component({
  selector: 'app-rsi-indicator',
  imports: [
    HighchartsChartComponent,
    NgIf
  ],
  providers: [
    providePartialHighcharts({
      modules: () => [import('highcharts/esm/modules/stock')],
    })
  ],
  templateUrl: './rsi-indicator.component.html',
  standalone: true,
  styleUrl: './rsi-indicator.component.css'
})
export class RsiIndicatorComponent implements OnInit {

  masterSrv = inject(AllIndicatorsService);
  data: [number, number][] = [];
  chartOptions: Highcharts.Options = {}

  ngOnInit(): void {
    this.masterSrv.getRsiSeries("5m", 14).subscribe((response : { timestamp: number; rsi: number }[])=> {
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
        text: 'ta4j RSI'
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
