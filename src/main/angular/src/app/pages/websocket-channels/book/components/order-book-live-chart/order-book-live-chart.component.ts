import {Component, inject, OnInit} from '@angular/core';
import {HighchartsChartComponent} from 'highcharts-angular';
import {OrderbookData, OrderPoint} from '../../model/dto';
import {BookWebsocketService} from '../../services/book.websocket.service';

@Component({
  selector: 'app-order-book-live-chart',
  imports: [HighchartsChartComponent],
  templateUrl: './order-book-live-chart.component.html',
  standalone: true,
  styleUrl: './order-book-live-chart.component.css'
})
export class OrderBookLiveChartComponent implements OnInit {

  bidsData: OrderPoint[] = [];
  asksData: OrderPoint[] = [];

  websocketSrv = inject(BookWebsocketService);

  chartOptions: Highcharts.Options = {}

  ngOnInit(): void {
    console.log('connecting...');
    this.websocketSrv.connect();
    [this.bidsData, this.asksData] = this.generateBidAndAskData(10);
    this.updateChart(this.bidsData, this.asksData);

    this.websocketSrv.orderbook$.subscribe(message => {
      var currentOrderbook = message;
      var currentOrderbookData = message.result.data[0] as OrderbookData;
      //this.currentOrderbook = message.result.data[0]?.asks[0]?.[0];

    });
  }

  private getRandomNumber(min: number, max: number): number {
      return Math.round(Math.random() * (max - min)) + min;
    }

  private generateBidAndAskData(n: number): [OrderPoint[], OrderPoint[]] {
      const data: [OrderPoint[], OrderPoint[]] = [[], []];

      let bidPrice = this.getRandomNumber(29000, 30000);
      let askPrice = bidPrice + 0.5;

      for (let i = 0; i < n; i++) {
        bidPrice -= i * this.getRandomNumber(8, 10);
        askPrice += i * this.getRandomNumber(8, 10);

        data[0].push({
          x: i,
          y: (i + 1) * this.getRandomNumber(70000, 110000),
          price: bidPrice
        });

        data[1].push({
          x: i,
          y: (i + 1) * this.getRandomNumber(70000, 110000),
          price: askPrice
        });
      }

      return data;
    }


  updateChart(bidsData : OrderPoint[], asksData : OrderPoint[]) {
    this.chartOptions = {

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
        min: 0,
        max: 1200000,
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
        min: 0,
        max: 1200000,
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
          format: '{point.price:,.1f}'
        }],
        name: 'Asks',
        color: '#ce4548',
        data: asksData
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
          format: '{point.price:,.1f}'
        }],
        name: 'Bids',
        color: '#107db7',
        data: bidsData,
        yAxis: 1
      }]
    }
  }

}
