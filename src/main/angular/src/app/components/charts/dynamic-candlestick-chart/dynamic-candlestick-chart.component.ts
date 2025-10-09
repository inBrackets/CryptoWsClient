import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts/highstock';
import { HighchartsChartComponent, providePartialHighcharts } from 'highcharts-angular';
import { NgIf } from '@angular/common';
import { Subscription, interval, switchMap } from 'rxjs';
import {CandlestickRestService} from '../../../pages/rest-channels/candlestick/services/candlestick.rest.service';
import {CandlestickWithInstrumentName} from '../../../pages/rest-channels/candlestick/model/dto';
import {BookWebsocketService} from '../../../pages/websocket-channels/book/services/book.websocket.service';
import {OrderbookData} from '../../../pages/websocket-channels/book/model/dto';

@Component({
  selector: 'app-dynamic-candlestick-chart',
  imports: [HighchartsChartComponent, NgIf],
  providers: [
    providePartialHighcharts({
      modules: () => [import('highcharts/esm/modules/stock')],
    }),
  ],
  templateUrl: './dynamic-candlestick-chart.component.html',
  standalone: true,
  styleUrl: './dynamic-candlestick-chart.component.css'
})
export class DynamicCandlestickChartComponent implements OnInit, OnDestroy {
  candlestickSrv = inject(CandlestickRestService);
  websocketSrv = inject(BookWebsocketService);

  data: [number, number, number, number, number][] = [];
  chartOptions: Highcharts.Options = {};
  Highcharts: typeof Highcharts = Highcharts;
  chart?: Highcharts.Chart;
  bids: Highcharts.PointOptionsObject[] = [];
  asks: Highcharts.PointOptionsObject[] = [];
  private subscription?: Subscription;
  private refreshSubscription?: Subscription;

  ngOnInit(): void {
    // Initial load
    this.candlestickSrv.getCandlesticks("CRO_USD", "1m").subscribe((response) => {
      this.data = response.map(
        ({instrumentName, o, h, l, c, v, t}) => [t, o, h, l, c]
      );
      this.initChart();
      setTimeout(() => this.startLiveLastCandleUpdates(), 300);
      this.startAutoRefreshCandles();
    });
  }

  onChartInstance(chart: Highcharts.Chart) {
    this.chart = chart;
  }

  startLiveLastCandleUpdates() {

    this.websocketSrv.connect();

    this.websocketSrv.orderbook$.subscribe(message => {
      const data = message.result.data[0];

      this.bids = data.bids.map(([price, size, count]) => ({
        x: price,
        y: size,
        price: +price,
      })) as Highcharts.PointOptionsObject[];

      this.asks = data.asks.map(([price, size, count]) => ({
        x: price,
        y: size,
        price: +price,
      })) as Highcharts.PointOptionsObject[];
        console.log('Bids:', this.bids);
        console.log('Asks:', this.asks);
        this.updateLastCandleWithMidPrice();
      });
  }

  updateLastCandleWithMidPrice() {
    if (!this.chart) return;

    const series = this.chart.get('aapl-series') as Highcharts.Series;
    if (!series || !('data' in series) || !series.data.length) return;

    const lastPoint = series.data.at(-1)!;
    const lastMid = this.calculateMidPrice(this.bids, this.asks);
    if (lastMid == null) return;

    const opts = lastPoint.options;
    const open = opts.open ?? 0;
    const high = opts.high ?? 0;
    const low = opts.low ?? 0;
    const close = lastMid;

    lastPoint.update(
      [lastPoint.x, open, Math.max(high, close), Math.min(low, close), close],
      true,
      { duration: 150, easing: 'easeOutQuad' }
    );
  }

  private calculateMidPrice(
    bids: Highcharts.PointOptionsObject[],
    asks: Highcharts.PointOptionsObject[]
  ): number {
    const extractPrice = (p: any) => +((p.price) ?? 0);

    const bestBid = bids.length ? Math.max(...bids.map(extractPrice)) : 0;
    const bestAsk = asks.length ? Math.min(...asks.map(extractPrice)) : 0;

    if (bestBid != null && bestAsk != null) return (bestBid + bestAsk) / 2;
    return bestBid ?? bestAsk;
  }

  initChart() {
    this.chartOptions = {
      chart: { width: null },
      rangeSelector: { selected: 1 },
      title: { text: 'AAPL Stock Price' },
      series: [{
        type: 'candlestick',
        id: 'aapl-series',
        name: 'Dynamic data in Stock',
        data: this.data,
        color: '#FF7F7F',
        upColor: '#90EE90',
        lastPrice: { enabled: true, label: { enabled: true } },
      }],
      credits: { enabled: false },
    };
  }

  /** 🔁 Refresh all candles every 30 seconds */
  startAutoRefreshCandles() {
    this.refreshSubscription = interval(30_000)
      .pipe(switchMap(() => this.candlestickSrv.getCandlesticks("CRO_USD", "1m")))
      .subscribe((response) => {
        this.data = response.map(
          ({instrumentName, o, h, l, c, v, t}) => [t, o, h, l, c]
        );
        const series = this.chart?.get('aapl-series') as Highcharts.Series;
        if (series) {
          series.setData(this.data, true);
        }
      });
  }

  stopUpdates() {
    this.subscription?.unsubscribe();
    this.refreshSubscription?.unsubscribe();
  }

  ngOnDestroy(): void {
    this.stopUpdates();
  }
}
