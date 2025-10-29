export interface Instrument {
  symbol: string;
  inst_type: string;
  display_name: string;
  base_ccy: string;
  quote_ccy: string;
  quote_decimals: number;
  quantity_decimals: number;
  price_tick_size: string;
  qty_tick_size: string;
  max_leverage: string;
  tradable: boolean;
  expiry_timestamp_ms: number;
  beta_product: boolean;
  underlying_symbol?: string | null;
  contract_size?: string | null;
  margin_buy_enabled: boolean;
  margin_sell_enabled: boolean;
}
