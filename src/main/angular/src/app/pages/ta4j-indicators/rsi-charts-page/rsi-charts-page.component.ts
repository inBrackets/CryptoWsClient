import { Component } from '@angular/core';
import {RsiIndicatorSingleTimeframeComponent} from '../../../components/charts/ta4j-indicators/rsi-indicator-single/rsi-indicator-single-timeframe.component';
import {
  RsiIndicatorAllTimeframesComponent
} from '../../../components/charts/ta4j-indicators/rsi-indicator-all-timeframes/rsi-indicator-all-timeframes.component';

@Component({
  selector: 'app-rsi-charts-page',
  imports: [
    RsiIndicatorSingleTimeframeComponent,
    RsiIndicatorAllTimeframesComponent
  ],
  templateUrl: './rsi-charts-page.component.html',
  standalone: true,
  styleUrl: './rsi-charts-page.component.css'
})
export class RsiChartsPageComponent {

}
