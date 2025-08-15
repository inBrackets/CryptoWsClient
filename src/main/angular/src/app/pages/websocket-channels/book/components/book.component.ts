import {Component, inject} from '@angular/core';
import {BookService} from '../services/book.service';

@Component({
  selector: 'app-book',
  imports: [],
  templateUrl: './book.component.html',
  standalone: true,
  styleUrl: './book.component.css'
})
export class BookComponent {

  websocketSrv = inject(BookService);
  currentOrderbook: string = '';

  lastAnimals: string[] = [];

  ngOnInit(): void {
    console.log('connecting...');
    this.websocketSrv.connect();

    this.websocketSrv.orderbook$.subscribe(message => {
      this.currentOrderbook = message;
      //this.currentOrderbook = message.result.data[0]?.asks[0]?.[0];
    });
  }
}
