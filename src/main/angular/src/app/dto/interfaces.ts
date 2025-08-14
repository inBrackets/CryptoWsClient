export class OrderbookData {
  asks: [string, string, string][];
  bids: [string, string, string][];
  t: number;
  tt: number;
  u: number;
  cs: number;

  constructor() {
    this.asks = [];
    this.bids = [];
    this.t = 0;
    this.tt = 0;
    this.u = 0;
    this.cs = 0;
  }
}

export class OrderbookResult {
  instrument_name: string;
  subscription: string;
  channel: string;
  depth: number;
  data: OrderbookData[];

  constructor() {
    this.instrument_name = '';
    this.subscription = '';
    this.channel = '';
    this.depth = 0;
    this.data = [new OrderbookData()];
  }
}

export class Orderbook {
  id: number;
  method: string;
  code: number;
  result: OrderbookResult;

  constructor() {
    this.id = 0;
    this.method = '';
    this.code = 0;
    this.result = new OrderbookResult();
  }
}
