import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {HighchartsChartComponent, providePartialHighcharts} from 'highcharts-angular';
import {InstrumentRestService} from './services/instrument.rest.service';
import {NgIf} from '@angular/common';
import {Instrument} from './model/dto';

@Component({
  selector: 'app-instrument',
  imports: [HighchartsChartComponent, NgIf],
  providers: [
    providePartialHighcharts({
      modules: () => [
        import('highcharts/esm/modules/networkgraph')
      ],
    }),
  ],
  templateUrl: './instrument.component.html',
  standalone: true,
  styleUrl: './instrument.component.css'
})
export class InstrumentComponent implements OnInit, OnDestroy {

  chartOptions: Highcharts.Options = {}

  instrumentSrv = inject(InstrumentRestService);

  ngOnDestroy(): void {}

  ngOnInit(): void {
    const filterSymbols = ['ADA', 'ATOM'];

    this.instrumentSrv.getCcyPairsInstruments().subscribe((response: Instrument[]) => {
      console.log('Instruments:', response);

      // Filter only CCY_PAIRs where base or quote currency matches one of the symbols
      const filteredPairs = response
        .filter(inst => filterSymbols.includes(inst.base_ccy) || filterSymbols.includes(inst.quote_ccy));

      // Map to network data format
      const networkData: [string, string][] = filteredPairs.map(inst => [inst.base_ccy, inst.quote_ccy]);

      console.log('Filtered network data:', networkData);

      this.updateChart(networkData);
    });
  }

  updateChart(data: [string, string][]) {
    this.chartOptions = {
      chart: {
        type: 'networkgraph',
        height: '100%'
      },
      title: {
        text: 'Currency Network Graph',
        align: 'left'
      },
      subtitle: {
        text: 'Base vs Quote currency connections',
        align: 'left'
      },
      plotOptions: {
        series: {
          turboThreshold: 0
        },
        networkgraph: {
          keys: ['from', 'to'],
          layoutAlgorithm: {
            enableSimulation: true,
            friction: -0.9
          }
        }
      },
      series: [{
        type: 'networkgraph',
        id: 'ccy-network',
        accessibility: {
          enabled: false
        },
        dataLabels: {
          enabled: true,
          linkFormat: '',
          style: {
            fontSize: '0.8em',
            fontWeight: 'normal'
          }
        },
        data
      }]
    };
  }
}
