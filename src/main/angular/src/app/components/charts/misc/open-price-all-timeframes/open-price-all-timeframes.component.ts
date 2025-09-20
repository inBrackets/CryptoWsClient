import {Component, inject, OnInit} from '@angular/core';
import {AllIndicatorsService} from '../../ta4j-indicators/all-indicators.service';
import {CandlestickWithInstrumentName} from '../../../../pages/rest-channels/candlestick/model/dto';
import {NgIf} from '@angular/common';
import {HighchartsChartComponent, providePartialHighcharts} from 'highcharts-angular';


@Component({
  selector: 'app-open-price-all-timeframes',
  imports: [
    HighchartsChartComponent,
    NgIf
  ],
  providers: [
    providePartialHighcharts({
      modules: () => [import('highcharts/esm/modules/stock')],
    })
  ],
  templateUrl: './open-price-all-timeframes.component.html',
  standalone: true,
  styleUrl: './open-price-all-timeframes.component.css'
})
export class OpenPriceAllTimeframesComponent  implements OnInit {

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


  ngOnInit(): void {
    this.masterSrv.getCandleSticksSeries("12h").subscribe((response : CandlestickWithInstrumentName[])=> {
      this.data_12h = response.map(
        ({instrumentName, o, h, l, c, v, t}) => [t, o]
      );
      this.updateChart();
    })
    this.masterSrv.getCandleSticksSeries("4h").subscribe((response : CandlestickWithInstrumentName[])=> {
      this.data_4h = response.map(
        ({instrumentName, o, h, l, c, v, t}) => [t, o]
      );
      this.updateChart();
    })
    this.masterSrv.getCandleSticksSeries("2h").subscribe((response : CandlestickWithInstrumentName[])=> {
      this.data_2h = response.map(
        ({instrumentName, o, h, l, c, v, t}) => [t, o]
      );
      this.updateChart();
    })
    this.masterSrv.getCandleSticksSeries("1h").subscribe((response : CandlestickWithInstrumentName[])=> {
      this.data_1h = response.map(
        ({instrumentName, o, h, l, c, v, t}) => [t, o]
      );
      this.updateChart();
    })
    this.masterSrv.getCandleSticksSeries("30m").subscribe((response : CandlestickWithInstrumentName[])=> {
      this.data_30m = response.map(
        ({instrumentName, o, h, l, c, v, t}) => [t, o]
      );
      this.updateChart();
    })
    this.masterSrv.getCandleSticksSeries("15m").subscribe((response : CandlestickWithInstrumentName[])=> {
      this.data_15m = response.map(
        ({instrumentName, o, h, l, c, v, t}) => [t, o]
      );
      this.updateChart();
    })
    this.masterSrv.getCandleSticksSeries("5m").subscribe((response : CandlestickWithInstrumentName[])=> {
      this.data_5m = response.map(
        ({instrumentName, o, h, l, c, v, t}) => [t, o]
      );
      this.updateChart();
    })
    this.masterSrv.getCandleSticksSeries("1m").subscribe((response : CandlestickWithInstrumentName[])=> {
      this.data_1m = response.map(
        ({instrumentName, o, h, l, c, v, t}) => [t, o]
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
        text: `Open prices - All timeframes`,
      },

      legend: {
        enabled: true,
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle'
      },

      xAxis: {
        overscroll: '10px'
      },

      series: [
        {
          name: 'Open 1m',
          data: this.data_1m,
          type: 'line',
          tooltip: { valueDecimals: 5 }
        },
        {
          name: 'Open 5m',
          data: this.data_5m,
          type: 'line',
          tooltip: { valueDecimals: 5 }
        },
        {
          name: 'Open 15m',
          data: this.data_15m,
          type: 'line',
          tooltip: { valueDecimals: 5 }
        },
        {
          name: 'Open 30m',
          data: this.data_30m,
          type: 'line',
          tooltip: { valueDecimals: 5 }
        },
        {
          name: 'Open 1h',
          data: this.data_1h,
          type: 'line',
          tooltip: { valueDecimals: 5 }
        },
        {
          name: 'Open 2h',
          data: this.data_2h,
          type: 'line',
          tooltip: { valueDecimals: 5 }
        },
        {
          name: 'Open 4h',
          data: this.data_4h,
          type: 'line',
          tooltip: { valueDecimals: 5 }
        },
        {
          name: 'Open 12h',
          data: this.data_12h,
          type: 'line',
          tooltip: { valueDecimals: 5 }
        },
      ]
    }
  }
}
