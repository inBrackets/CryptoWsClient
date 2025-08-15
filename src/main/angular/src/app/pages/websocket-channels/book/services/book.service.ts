import { Injectable } from '@angular/core';
import {Client, IMessage} from '@stomp/stompjs';
import {Subject} from 'rxjs';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private stompClient!: Client; // Modern STOMP client

  readonly orderBookTopic = "/subscribe/user.orderbook";
  readonly websocketEndpoint = "http://localhost:8080/gs-guide-websocket";

  orderbook$ = new Subject<string>();

  constructor() { }

  connect() {
    console.log("Initialize Websocket Connection");

    this.stompClient = new Client({
      // Use SockJS as the transport
      webSocketFactory: () => new SockJS(this.websocketEndpoint),

      // Optional: disable verbose internal logging
      debug: () => {},

      // Called on successful connection
      onConnect: () => {
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
      this.stompClient.deactivate().then(r => console.log("Disconnected"));
    }
  }

  private onOrderBookMessageReceived(message: IMessage) {
    const orderbook = message.body;
    console.log("Message Received Timestamp::", orderbook);
    this.orderbook$.next(orderbook);
  }
}
