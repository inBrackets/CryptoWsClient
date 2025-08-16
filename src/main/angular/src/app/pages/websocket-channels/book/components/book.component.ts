import {Component, inject, OnInit} from '@angular/core';
import {BookWebsocketService} from '../services/book.websocket.service';
import {Orderbook, OrderbookData} from '../model/dto';
import {JsonPipe} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {BookRestService} from '../services/book.rest.service';
import {HttpClient} from '@angular/common/http';
import {OrderBookLiveChartComponent} from './order-book-live-chart/order-book-live-chart.component';

@Component({
  selector: 'app-book',
  imports: [
    JsonPipe,
    FormsModule,
    OrderBookLiveChartComponent
  ],
  templateUrl: './book.component.html',
  standalone: true,
  styleUrl: './book.component.css'
})
export class BookComponent implements OnInit{

  websocketSrv = inject(BookWebsocketService);
  restSrv = inject(BookRestService);
  currentOrderbook: Orderbook | undefined;
  currentOrderbookData: OrderbookData | undefined;

  channel = '';

  constructor(private bookRestService: BookRestService, private http: HttpClient) {}

  connect() {
    this.bookRestService.connectToChannel(this.channel)
      .subscribe(res => console.log(res));
  }



  ngOnInit(): void {
    console.log('connecting...');
    this.websocketSrv.connect();

    this.websocketSrv.orderbook$.subscribe(message => {
      this.currentOrderbook = message;
      this.currentOrderbookData = message.result.data[0] as OrderbookData;
      //this.currentOrderbook = message.result.data[0]?.asks[0]?.[0];
    });
  }
}
