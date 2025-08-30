import {OrderPoint} from '../../model/dto';
import {Chart} from 'angular-highcharts';

// factory function that returns chart options
export function createOrderBookChartOptions(
  instrumentName: string,
  asksData: OrderPoint[],
  bidsData: OrderPoint[]
): Chart {
  return new Chart({
    // custom property (not in Highcharts types)
    // we cast as any so we can store instrumentName
    ...({instrumentName} as any),

    chart: {
      animation: {duration: 200},
      backgroundColor: '#23232f',
      marginTop: 70,
    },

    legend: {
      itemStyle: {color: '#ffffff', fontSize: '14px'}
    },

    plotOptions: {
      series: {
        animation: false,
        dataLabels: {enabled: true, color: '#ffffff'},
        borderWidth: 0,
        crisp: false
      }
    },

    series: [{
      type: 'bar',
      pointWidth: 25, // thicker bars (Bids)
      dataLabels: [{
        align: 'right', alignTo: 'plotEdges',
        style: {fontSize: '14px', textOutline: 'none'},
        format: '{point.y:,.0f}'
      }, {
        align: 'left', inside: true,
        style: {fontSize: '13px', textOutline: 'none'},
        format: '{point.price:,.5f}'
      }],
      name: 'Asks',
      color: '#C1666B',
      data: asksData
    }, {
      type: 'bar',
      pointWidth: 25, // thicker bars (Bids)
      dataLabels: [{
        align: 'left', alignTo: 'plotEdges',
        style: {fontSize: '14px', textOutline: 'none'},
        format: '{point.y:,.0f}'
      }, {
        align: 'right', inside: true,
        style: {fontSize: '13px', textOutline: 'none'},
        format: '{point.price:,.5f}'
      }],
      name: 'Bids',
      color: '#48A9A6',
      data: bidsData,
      yAxis: 1
    }],

    title: {
      text: 'Order book live chart',
      style: {color: '#ffffff'}
    },

    tooltip: {
      headerFormat: 'Price: <b>${point.point.price:,.5f}</b></br>',
      pointFormat: '{series.name}: <b>{point.y:,.0f}</b>',
      shape: 'rect'
    },

    xAxis: [{
      reversed: true,
      visible: false,
      title: {text: 'Market depth / price'},
      accessibility: {description: 'Bid orders'}
    }, {
      opposite: true,
      visible: false,
      title: {text: 'Market depth / price'},
      accessibility: {description: 'Ask orders'}
    }],

    yAxis: [{
      offset: 0,
      visible: true,
      opposite: true,
      gridLineWidth: 0,
      tickAmount: 1,
      left: '50%',
      width: '50%',
      title: {text: 'Amount of ask orders', style: {visibility: ''}},
      // min: 0,
      // max: 100000,
      labels: {
        enabled: true,
        formatter: function () {
          if ((this as any).isLast) return 'Asks';
          return '';
        },
        style: {color: '#ffffff', fontSize: '16px', fontWeight: '700'},
        y: 10
      }
    }, {
      offset: 0,
      visible: true,
      opposite: true,
      gridLineWidth: 0,
      tickAmount: 2,
      left: '0%',
      width: '50%',
      reversed: true,
      title: {text: 'Amount of bid orders', style: {visibility: ''}},
      // min: 0,
      // max: 100000,
      labels: {
        enabled: true,
        formatter: function () {
          // const instrument = instrumentName;
          // if ((this as any).pos === 0) return `Price (${instrument})`;
          if (this.isLast) return 'Bids';
          return '';
        },
        style: {color: '#ffffff', fontSize: '16px', fontWeight: '700'},
        y: 10
      }
    }]
  });
}
