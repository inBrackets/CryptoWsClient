import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {HighchartsChartComponent, providePartialHighcharts} from 'highcharts-angular';
import {InstrumentRestService} from './services/instrument.rest.service';

@Component({
  selector: 'app-instrument',
  imports: [HighchartsChartComponent],
  providers: [
    providePartialHighcharts({
      modules: () => [import('highcharts/esm/modules/stock')],
    }),
  ],
  templateUrl: './instrument.component.html',
  standalone: true,
  styleUrl: './instrument.component.css'
})
export class InstrumentComponent implements OnInit, OnDestroy {

  instrumentSrv = inject(InstrumentRestService);
  ngOnDestroy(): void {
  }

  ngOnInit(): void {
    this.instrumentSrv.getCcyPairsInstruments().subscribe((response) => {
      console.log(response);
    })
  }

}
