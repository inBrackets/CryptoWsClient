import {Component, inject, OnInit} from '@angular/core';
import {HighchartsChartComponent} from 'highcharts-angular';
import * as Highcharts from 'highcharts';
import {OrderbookData, OrderPoint} from '../../model/dto';
import {BookWebsocketService} from '../../services/book.websocket.service';
import {createOrderBookChartOptions} from './order-book-live-chart.config';

@Component({
  selector: 'app-order-book-live-chart',
  imports: [HighchartsChartComponent],
  templateUrl: './order-book-live-chart.component.html',
  standalone: true,
  styleUrl: './order-book-live-chart.component.css'
})
export class OrderBookLiveChartComponent implements OnInit {

  updateFlag = false;
  instrumentName: string = "";

  bidsData: OrderPoint[] = [];
  asksData: OrderPoint[] = [];

  websocketSrv = inject(BookWebsocketService);

  chartOptions: Highcharts.Options = createOrderBookChartOptions(
    this.instrumentName, this.asksData, this.bidsData
  );

  ngOnInit(): void {
    console.log('connecting...');
    this.websocketSrv.connect();

    this.websocketSrv.orderbook$.subscribe(message => {
      const currentOrderbookData = message.result.data[0] as OrderbookData;
      this.instrumentName = message.result.instrument_name;

      // process asks
      let cumulativeAsk = 0;
      this.asksData = currentOrderbookData.asks.map((a, i) => {
        cumulativeAsk += Number(a[1]);
        return {x: i, y: cumulativeAsk, price: Number(a[0])};
      });

      // process bids
      let cumulativeBid = 0;
      this.bidsData = currentOrderbookData.bids
        .slice().reverse()
        .map((b, i) => {
          cumulativeBid += Number(b[1]);
          return {x: i, y: cumulativeBid, price: Number(b[0])};
        }).reverse();

      // rebuild options with updated data
      this.chartOptions = createOrderBookChartOptions(
        this.instrumentName, this.asksData, this.bidsData
      );

      this.updateFlag = true;
    });
  }
}
