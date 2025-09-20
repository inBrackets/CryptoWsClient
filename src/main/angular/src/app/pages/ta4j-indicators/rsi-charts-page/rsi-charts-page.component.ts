import { Component } from '@angular/core';
import {RsiIndicatorSingleTimeframeComponent} from '../../../components/charts/ta4j-indicators/rsi-indicator-single-timeframe/rsi-indicator-single-timeframe.component';
import {
  RsiIndicatorAllTimeframesComponent
} from '../../../components/charts/ta4j-indicators/rsi-indicator-all-timeframes/rsi-indicator-all-timeframes.component';
import {
  ClosePriceAllTimeframesComponent
} from '../../../components/charts/misc/close-price-all-timeframes/close-price-all-timeframes.component';
import {
  OpenPriceAllTimeframesComponent
} from '../../../components/charts/misc/open-price-all-timeframes/open-price-all-timeframes.component';

@Component({
  selector: 'app-rsi-charts-page',
  imports: [
    RsiIndicatorSingleTimeframeComponent,
    RsiIndicatorAllTimeframesComponent,
    ClosePriceAllTimeframesComponent,
    OpenPriceAllTimeframesComponent
  ],
  templateUrl: './rsi-charts-page.component.html',
  standalone: true,
  styleUrl: './rsi-charts-page.component.css'
})
export class RsiChartsPageComponent {

}
