import {Injectable} from '@angular/core';
import {Client, IMessage} from '@stomp/stompjs';
import {Subject} from 'rxjs';
import {Orderbook} from '../../book/model/dto';
import SockJS from 'sockjs-client';
import {UserOrderMessage} from '../model/dto';

@Injectable({
  providedIn: 'root'
})
export class OrderWebsocketClientService {

  private stompClient!: Client; // Modern STOMP client

  readonly orderTopic = "/topic/user.order";
  readonly websocketEndpoint = "http://localhost:8080/ws";

  order$ = new Subject<UserOrderMessage>();

  constructor() {
  }

  connect(){
    console.log("Initialize Websocket Connection");

    this.stompClient = new Client({
      // Use SockJS as the transport
      webSocketFactory: () => new SockJS(this.websocketEndpoint),

      // Optional: disable verbose internal logging
      debug: () => {
      },

      // Called on successful connection
      onConnect: () => {
        this.stompClient.subscribe(this.orderTopic, (message: IMessage) => {
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
    try {
      const order: UserOrderMessage = JSON.parse(message.body) as UserOrderMessage;
      console.log("Message Received Order::", order);
      this.order$.next(order);
    } catch (error) {
      console.error("Failed to parse order message", error, message.body);
    }
  }
}
