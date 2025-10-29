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
    this.instrumentSrv.getCcyPairsInstruments().subscribe((response: Instrument[]) => {
      console.log('Instruments:', response);

      const networkData: [string, string][] = response.map(inst => [inst.base_ccy, inst.quote_ccy]);

      console.log('Network data:', networkData);

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
