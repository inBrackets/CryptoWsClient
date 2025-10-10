import {Injectable} from '@angular/core';
import {Client, IMessage, StompSubscription} from '@stomp/stompjs';
import {BehaviorSubject, Subject} from 'rxjs';
import SockJS from 'sockjs-client';
import {Orderbook} from '../model/dto';
import {environment} from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BookWebsocketService {

  private stompClient!: Client;
  private subscription?: StompSubscription;
  private connected$ = new BehaviorSubject<boolean>(false);

  readonly orderBookTopic = "/topic/book";
  readonly websocketEndpoint = `http://localhost:${environment.springBootPort}/ws`;

  /** Emits latest orderbook message parsed as JSON */
  readonly orderbook$ = new Subject<Orderbook>();

  constructor() {
  }

  /** 🔌 Connect to WebSocket (idempotent) */
  connect(): void {
    if (this.isConnected()) {
      console.log('[BookWebsocketService] Already connected.');
      return;
    }

    console.log('[BookWebsocketService] Initializing WebSocket connection...');

    this.stompClient = new Client({
      // Use SockJS as the transport
      webSocketFactory: () => new SockJS(this.websocketEndpoint),
      reconnectDelay: 5000,
      debug: () => {
      }, // disable internal STOMP logging

      onConnect: () => {
        console.log('[BookWebsocketService] Connected.');
        this.connected$.next(true);
        this.subscribeToOrderbook();
      },

      onStompError: (frame) => {
        console.error('[BookWebsocketService] Broker error:', frame.headers['message'], frame.body);
      },

      onWebSocketError: (error) => {
        console.error('[BookWebsocketService] WebSocket error:', error);
      },

      onDisconnect: () => {
        console.warn('[BookWebsocketService] Disconnected.');
        this.connected$.next(false);
      }
    });

    this.stompClient.activate();
  }

  /** 🔁 Subscribe to the orderbook topic */
  private subscribeToOrderbook(): void {
    if (!this.stompClient || !this.stompClient.connected) {
      console.warn('[BookWebsocketService] Cannot subscribe, client not connected yet.');
      return;
    }

    // prevent duplicate subscriptions
    this.subscription?.unsubscribe();

    this.subscription = this.stompClient.subscribe(this.orderBookTopic, (message: IMessage) => {
      this.onOrderBookMessageReceived(message);
    });

    console.log('[BookWebsocketService] Subscribed to', this.orderBookTopic);
  }

  /** 🔍 Check if connected and active */
  isConnected(): boolean {
    return !!this.stompClient && this.stompClient.connected;
  }

  /** 🧩 Optional helper — wait until connected */
  waitUntilConnected(): Promise<void> {
    return new Promise((resolve) => {
      if (this.isConnected()) return resolve();
      const sub = this.connected$.subscribe((connected) => {
        if (connected) {
          sub.unsubscribe();
          resolve();
        }
      });
    });
  }

  /** 🔌 Disconnect gracefully */
  disconnect(): void {
    if (this.stompClient && this.stompClient.active) {
      console.log('[BookWebsocketService] Disconnecting...');
      this.subscription?.unsubscribe();
      this.stompClient.deactivate().then(() => {
        this.connected$.next(false);
        console.log('[BookWebsocketService] Disconnected cleanly.');
      });
    } else {
      console.log('[BookWebsocketService] Already disconnected.');
    }
  }

  /** ♻️ Optionally reconnect manually */
  reconnect(): void {
    console.log('[BookWebsocketService] Reconnecting...');
    this.disconnect();
    setTimeout(() => this.connect(), 1000);
  }

  /** 📩 Handle incoming orderbook messages */
  private onOrderBookMessageReceived(message: IMessage): void {
    try {
      const orderbook: Orderbook = JSON.parse(message.body) as Orderbook;
      console.log("Message Received OrderBook::", orderbook);
      this.orderbook$.next(orderbook);
    } catch (error) {
      console.error('[BookWebsocketService] Failed to parse orderbook message', error, message.body);
    }
  }
}
