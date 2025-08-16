// src/app/models/order-history.model.ts

/** Common “decimal carried as string” type from the API */
export type DecimalString = string;
/** Milliseconds since epoch */
export type UnixMillis = number;
/** Big integer carried as string */
export type BigIntString = string;

/** Optional string literal helpers if you want stronger typing later */
export type OrderSide = 'BUY' | 'SELL' | string;
export type OrderStatus = 'FILLED' | 'CANCELED' | string;
export type OrderType = 'LIMIT' | 'MARKET' | string;
export type TimeInForce = 'GOOD_TILL_CANCEL' | 'IMMEDIATE_OR_CANCEL' | 'FILL_OR_KILL' | string;
export type RefPriceType = 'NULL_VAL' | string;

/** Generic API response wrapper */
export interface ApiResponse<T> {
  id: number;
  method: string;     // e.g. "private/get-order-history"
  code: number;       // 0 = success (per sample)
  result: T;
}

/** Concrete response for get-order-history */
export type OrderHistoryResponse = ApiResponse<OrderHistoryResult>;

export interface OrderHistoryResult {
  data: OrderHistoryItem[];
}

/** One order entry from result.data[] */
export interface OrderHistoryItem {
  account_id: string;
  order_id: string;               // numeric but delivered as string
  client_oid: string;

  order_type: OrderType;          // e.g. "LIMIT"
  time_in_force: TimeInForce;     // e.g. "GOOD_TILL_CANCEL"
  side: OrderSide;                // "BUY" | "SELL"
  exec_inst: string[];            // left generic; API shows []

  quantity: DecimalString;        // e.g. "20"
  limit_price: DecimalString;     // e.g. "0.15085"
  order_value: DecimalString;     // e.g. "3.0170"

  maker_fee_rate: DecimalString;  // e.g. "0"
  taker_fee_rate: DecimalString;  // e.g. "0"
  avg_price: DecimalString;       // e.g. "0.15085"

  ref_price: DecimalString;       // often "0"
  ref_price_type: RefPriceType;   // e.g. "NULL_VAL"

  cumulative_quantity: DecimalString;
  cumulative_value: DecimalString;
  cumulative_fee: DecimalString;

  status: OrderStatus;            // "FILLED" | "CANCELED" | ...

  update_user_id: string;
  order_date: string;             // ISO date string "YYYY-MM-DD"
  instrument_name: string;        // e.g. "CRO_USD"
  fee_instrument_name: string;    // e.g. "CRO" | "USD"
  reason: number;                 // numeric code

  create_time: UnixMillis;        // e.g. 1755373040972
  create_time_ns: BigIntString;   // very large, delivered as string
  update_time: UnixMillis;
}
