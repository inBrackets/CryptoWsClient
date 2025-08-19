import { Injectable } from '@angular/core';
import SockJS from 'sockjs-client';
import { Client, IMessage } from '@stomp/stompjs';
import { Subject } from 'rxjs';
import {Orderbook} from '../dto/interfaces';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  private stompClient!: Client; // Modern STOMP client

  readonly timeStampTopic = "/topic/user.balance";
  readonly foodTopic = "/topic/random.food";
  readonly animalTopic = "/topic/random.animal";
  readonly orderBookTopic = "/topic/book";
  readonly websocketEndpoint = `http://localhost:${environment.springBootPort}/ws`;

  // Subject to push timestamp updates
  timestamp$ = new Subject<string>();
  animal$ = new Subject<string>();
  food$ = new Subject<string>();
  orderbook$ = new Subject<string>();

  constructor() { }

  connect() {
    console.log("Initialize Websocket Connection");

    this.stompClient = new Client({
      // Use SockJS as the transport
      webSocketFactory: () => new SockJS(this.websocketEndpoint),

      // Optional: disable verbose internal logging
      debug: () => {},
      // debug: (msg: string) => {
      //   console.log("%cSTOMP", "color: green;", msg.replace(/\u0000/g, "\\u0000"));
      // },

      // Called on successful connection
      onConnect: () => {
        this.stompClient.subscribe(this.timeStampTopic, (message: IMessage) => {
          this.onTimeStampMessageReceived(message);
        });
        this.stompClient.subscribe(this.animalTopic, (message: IMessage) => {
          this.onAnimalMessageReceived(message);
        });
        this.stompClient.subscribe(this.foodTopic, (message: IMessage) => {
          this.onFoodMessageReceived(message);
        });
        this.stompClient.subscribe(this.orderBookTopic, (message: IMessage) => {
          this.onOrderBookMessageReceived(message);
        });
      },

      // Called on error or disconnect
      onStompError: (frame) => {
        console.error("Broker error:", frame.headers['message'], frame.body);
      },
      onWebSocketError: (error) => {
        console.error("WebSocket error:", error);
      },

      // Reconnect automatically after 5 seconds
      reconnectDelay: 5000
    });

    this.stompClient.activate();
  }

  disconnect() {
    if (this.stompClient && this.stompClient.active) {
      this.stompClient.deactivate();
      console.log("Disconnected");
    }
  }

  private onTimeStampMessageReceived(message: IMessage) {
    const timestamp = message.body;
    console.log("Message Received Timestamp::", timestamp);
    this.timestamp$.next(timestamp);
  }

  private onAnimalMessageReceived(message: IMessage) {
    const animal = message.body;
    console.log("Message Received Timestamp::", animal);
    this.animal$.next(animal);
  }

  private onFoodMessageReceived(message: IMessage) {
    const food = message.body;
    console.log("Message Received Timestamp::", food);
    this.food$.next(food);
  }

  private onOrderBookMessageReceived(message: IMessage) {
    try {
      const orderbook = message.body;
      console.log("Message Received Timestamp::", orderbook);
      // @ts-ignore
      this.orderbook$.next(orderbook);
    } catch (error) {
      console.error("Failed to parse orderbook message", error, message.body);
    }
  }
}
