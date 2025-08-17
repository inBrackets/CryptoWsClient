export interface UserOrderMessage {
  id: number;
  method: string;
  code: number;
  result: UserOrderResult;
}

export interface UserOrderResult {
  subscription: string;
  channel: string;
  instrument_name: string;
  data: UserOrder[];
}

export interface UserOrder {
  account_id: string;
  client_oid: string;
  instrument_name: string;
  fee_instrument_name: string;
  order_type: string;
  time_in_force: string;
  side: string;
  exec_inst: string[];
  quantity: string;
  limit_price: string;
  ref_price: string;
  order_value: string;
  maker_fee_rate: string;
  taker_fee_rate: string;
  create_time: number;
  create_time_ns: string;
  order_date: string;
  order_id: string;
  avg_price: string;
  cumulative_quantity: string;
  cumulative_value: string;
  cumulative_fee: string;
  status: string;
  update_user_id: string;
  update_time: number;
  transaction_time_ns: number;
}
