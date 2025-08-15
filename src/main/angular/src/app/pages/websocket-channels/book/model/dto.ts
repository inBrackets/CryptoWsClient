export interface Orderbook {
  id: number;
  method: string;
  code: number;
  result: {
    instrument_name: string;
    subscription: string;
    channel: string;
    depth: number;
    data: OrderbookData[];
  };
}

export interface OrderbookData {
  asks: [number, number, number][]; // [price, size, order_count]
  bids: [number, number, number][]; // [price, size, order_count]
  t: number;
  tt: number;
  u: number;
  cs: number;
}
