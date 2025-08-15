import { Component, inject, OnInit } from '@angular/core';
import { HighchartsChartComponent } from 'highcharts-angular';
import * as Highcharts from 'highcharts';
import { OrderbookData, OrderPoint } from '../../model/dto';
import { BookWebsocketService } from '../../services/book.websocket.service';

@Component({
  selector: 'app-order-book-live-chart',
  imports: [HighchartsChartComponent],
  templateUrl: './order-book-live-chart.component.html',
  standalone: true,
  styleUrl: './order-book-live-chart.component.css'
})
export class OrderBookLiveChartComponent implements OnInit {

  Highcharts: typeof Highcharts = Highcharts;
  updateFlag = false;

  bidsData: OrderPoint[] = [];
  asksData: OrderPoint[] = [];

  maxBidVolume:number = 0;
  maxAskVolume:number = 0;

  websocketSrv = inject(BookWebsocketService);


  chartOptions: Highcharts.Options = {

      chart: {
        animation: {
          duration: 200
        },
        backgroundColor: '#23232f',
        marginTop: 70,
      },

      title: {
        text: 'Order book live chart',
        style: {
          color: '#ffffff'
        }
      },

    tooltip: {
      headerFormat: 'Price: <b>${point.point.price:,.5f}</b></br>',
      pointFormat: '{series.name}: <b>{point.y:,.0f}</b>',
      shape: 'rect'
    },

      xAxis: [{
        reversed: true,
        visible: false,
        title: {
          text: 'Market depth / price'
        },
        accessibility: {
          description: 'Bid orders'
        }
      }, {
        opposite: true,
        visible: false,
        title: {
          text: 'Market depth / price'
        },
        accessibility: {
          description: 'Ask orders'
        }
      }],

      yAxis: [{
        offset: 0,
        visible: true,
        opposite: true,
        gridLineWidth: 0,
        tickAmount: 1,
        left: '50%',
        width: '50%',
        title: {
          text: 'Amount of ask orders',
          style: {
            visibility: 'hidden'
          }
        },
        // min: 0,
        // max: 100000,
        labels: {
          enabled: true,
          format: '{#if isLast}Asks{/if}',
          style: {
            color: '#ffffff',
            fontSize: '16px',
            fontWeight: '700'
          },
          y: 10
        }
      }, {
        offset: 0,
        visible: true,
        opposite: true,
        gridLineWidth: 0,
        tickAmount: 2,
        left: '0%',
        width: '50%',
        reversed: true,
        title: {
          text: 'Amount of bid orders',
          style: {
            visibility: 'hidden'
          }
        },
        // min: 0,
        // max: 100000,
        labels: {
          enabled: true,
          format: `
                {#if (eq pos 0)}Price ($){/if}
                {#if isLast}Bids{/if}
            `,
          style: {
            color: '#ffffff',
            fontSize: '16px',
            fontWeight: '700'
          },
          y: 10
        }
      }],

      plotOptions: {
        series: {
          animation: false,
          dataLabels: {
            enabled: true,
            color: '#ffffff'
          },
          borderWidth: 0,
          crisp: false
        }
      },

      series: [{
        type: 'bar',
        dataLabels: [{
          align: 'right',
          alignTo: 'plotEdges',
          style: {
            fontSize: '14px',
            textOutline: 'none'
          },
          format: '{point.y:,.0f}'
        }, {
          align: 'left',
          inside: true,
          style: {
            fontSize: '13px',
            textOutline: 'none'
          },
          format: '{point.price:,.5f}'
        }],
        name: 'Asks',
        color: '#C1666B',
        data: this.asksData
      }, {
        type: 'bar',
        dataLabels: [{
          align: 'left',
          alignTo: 'plotEdges',
          style: {
            fontSize: '14px',
            textOutline: 'none'
          },
          format: '{point.y:,.0f}'
        }, {
          align: 'right',
          inside: true,
          style: {
            fontSize: '13px',
            textOutline: 'none'
          },
          format: '{point.price:,.5f}'
        }],
        name: 'Bids',
        color: '#48A9A6',
        data: this.bidsData,
        yAxis: 1
      }]
    };

  ngOnInit(): void {
    console.log('connecting...');
    this.websocketSrv.connect();

    this.websocketSrv.orderbook$.subscribe(message => {
      const currentOrderbookData = message.result.data[0] as OrderbookData;

      // --- Cumulative Asks ---
      let cumulativeAsk = 0;
      this.asksData = currentOrderbookData.asks.map((a, i) => {
        cumulativeAsk += Number(a[1]); // sum volume
        return {
          x: i,
          y: cumulativeAsk,       // cumulative volume
          price: Number(a[0])
        };
      });

      // --- Cumulative Bids ---
      let cumulativeBid = 0;
      // Usually bids are sorted from highest to lowest price, so sum in reverse
      this.bidsData = currentOrderbookData.bids
        .slice() // copy array
        .reverse()
        .map((b, i) => {
          cumulativeBid += Number(b[1]);
          return {
            x: i,
            y: cumulativeBid,
            price: Number(b[0])
          };
        })
        .reverse(); // restore original order

      // Update series data directly
      (this.chartOptions.series![0] as Highcharts.SeriesBarOptions).data = this.asksData;
      (this.chartOptions.series![1] as Highcharts.SeriesBarOptions).data = this.bidsData;

      // Trigger chart refresh
      this.updateFlag = true;
    });
  }

}
