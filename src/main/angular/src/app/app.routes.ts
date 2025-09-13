// routes.ts
import { LayoutComponent } from './layout/layout/layout.component';
import { AppComponent } from './app.component';
import { Routes } from '@angular/router';
import {MiscComponent} from './pages/misc/misc.component';
import {BookComponent} from './pages/websocket-channels/book/components/book.component';
import {OrderHistoryComponent} from './pages/rest-channels/order-history/order-history.component';
import {OrderComponent} from './pages/websocket-channels/order/order.component';
import {StockSingleLineSeriesComponent} from './highcharts/stock-single-line-series/stock-single-line-series.component';
import {
  StockIntradayCandlestickComponent
} from './highcharts/stock-intraday-candlestick/stock-intraday-candlestick.component';
import {CandlestickComponent} from './pages/rest-channels/candlestick/candlestick.component';
import {RsiIndicatorComponent} from './pages/ta4j-indicators/rsi-indicator/rsi-indicator.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', component: MiscComponent },
      {
        path: 'ws/book', component: BookComponent,
      },
      { path: 'rest/candlesticks', component: CandlestickComponent },
      {
        path: 'stock-single-line-series', component: StockSingleLineSeriesComponent,
      },
      {
        path: 'stock-intraday-candlestick-series', component: StockIntradayCandlestickComponent,
      },
      { path: 'ws/order', component: OrderComponent },
      {
        path: 'order-history', component: OrderHistoryComponent,
      },
      {
        path: 'indicators/rsi', component: RsiIndicatorComponent
      }
    ]
  }
];
