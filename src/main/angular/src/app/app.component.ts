import {Component, inject} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {WebsocketService} from './services/websocket.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  standalone: true,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'angular';
  websocketSrv = inject(WebsocketService);
  currentTimestamp: string = '';
  currentAnimal: string = '';
  currentFood: string = '';

  ngOnInit(): void {
    console.log('connecting...');
    this.websocketSrv.connect();

    this.websocketSrv.timestamp$.subscribe(timestamp => {
      this.currentTimestamp = timestamp;
    });

    this.websocketSrv.animal$.subscribe(message => {
      this.currentAnimal = message;
    });

    this.websocketSrv.food$.subscribe(message => {
      this.currentFood = message;
    });
  }
}
