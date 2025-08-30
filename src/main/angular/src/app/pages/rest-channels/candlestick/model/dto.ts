import {OrderHistoryItem} from '../../order-history/model/dto';

/** Generic API response wrapper */
export interface ApiResponse<T> {
  id: number;
  method: string;     // e.g. "private/get-order-history"
  code: number;       // 0 = success (per sample)
  result: T;
}

export interface CandlestickResult {
  instrument_name: String;
  interval: String;
  data: Candlestick[];
}

export interface Candlestick {
  o: number;
  h: number;
  l: number;
  c: number;
  v: number;
  t: number;
}
