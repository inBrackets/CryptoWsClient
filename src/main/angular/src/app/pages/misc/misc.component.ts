import {Component, inject} from '@angular/core';
import {WebsocketService} from '../../services/websocket.service';
import {RouterOutlet} from '@angular/router';
import {LayoutComponent} from '../../layout/layout/layout.component';
import {OrderbookResult} from '../../dto/interfaces';

@Component({
  selector: 'app-misc',
  imports: [
    // RouterOutlet, LayoutComponent
  ],
  templateUrl: './misc.component.html',
  standalone: true,
  styleUrl: './misc.component.css'
})
export class MiscComponent {
  title = 'angular';
  websocketSrv = inject(WebsocketService);
  currentTimestamp: string = '';
  currentAnimal: string = '';
  currentFood: string = '';
  currentOrderbook: string = '';

  lastAnimals: string[] = [];

  ngOnInit(): void {
    console.log('connecting...');
    this.websocketSrv.connect();

    this.websocketSrv.timestamp$.subscribe(timestamp => {
      this.currentTimestamp = timestamp;
    });

    this.websocketSrv.animal$.subscribe(message => {
      this.currentAnimal = message;

      // Update lastAnimals array
      this.lastAnimals.unshift(message); // add new animal at the front
      if (this.lastAnimals.length > 10) {
        this.lastAnimals.pop(); // remove oldest from the back
      }
    });

    this.websocketSrv.food$.subscribe(message => {
      this.currentFood = message;
    });

    this.websocketSrv.orderbook$.subscribe(message => {
      this.currentOrderbook = message;
      //this.currentOrderbook = message.result.data[0]?.asks[0]?.[0];
    });
  }
}
