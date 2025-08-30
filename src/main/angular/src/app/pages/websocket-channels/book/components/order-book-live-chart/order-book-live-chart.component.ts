import {Component, inject, OnInit} from '@angular/core';
import {OrderbookData, OrderPoint} from '../../model/dto';
import {BookWebsocketService} from '../../services/book.websocket.service';
import {createOrderBookChartOptions} from './order-book-live-chart.config';
import {Chart, ChartModule} from 'angular-highcharts';

@Component({
  selector: 'app-order-book-live-chart',
  imports: [ChartModule],
  templateUrl: './order-book-live-chart.component.html',
  standalone: true,
  styleUrl: './order-book-live-chart.component.css'
})
export class OrderBookLiveChartComponent implements OnInit {

  instrumentName: string = "";

  bidsData: OrderPoint[] = [];
  asksData: OrderPoint[] = [];

  websocketSrv = inject(BookWebsocketService);

  chartOptions: Chart = createOrderBookChartOptions(
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

      // control animations
      const chartRef = this.chartOptions.ref;
      if (chartRef) {

        // animate bards
        chartRef.series[0].setData(this.asksData, true, {});
        chartRef.series[1].setData(this.bidsData, true, {});

        // prevent animating labels
        chartRef.yAxis[0].update({}, false); // false -> no redraw/animation
        (chartRef.userOptions as any)['instrumentName'] = this.instrumentName;
        chartRef.yAxis[1].update({
          labels: {
            formatter: function () {
              const instrument = (this.axis.chart.userOptions as any).instrumentName || '';
              if ((this as any).pos === 0) return `Price (${instrument})`;
              return '';
            }
          }
        }, false);

        chartRef.redraw(); // redraw once, instantly
      }
    });
  }
}
