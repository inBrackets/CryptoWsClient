import {Component, inject} from '@angular/core';
import {BookService} from '../services/book.service';
import {Orderbook, OrderbookData} from '../model/dto';
import {JsonPipe} from '@angular/common';

@Component({
  selector: 'app-book',
  imports: [
    JsonPipe
  ],
  templateUrl: './book.component.html',
  standalone: true,
  styleUrl: './book.component.css'
})
export class BookComponent {

  websocketSrv = inject(BookService);
  currentOrderbook: Orderbook | undefined;
  currentOrderbookData: OrderbookData | undefined;

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
