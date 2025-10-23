'use strict';
document.addEventListener('DOMContentLoaded', function () {
  setTimeout(function () {
    var options11 = {
      series: [
        {
          data: [
            {
              x: '',
              y: [1, 6]
            },
            {
              x: '',
              y: [3, 7]
            },
            {
              x: '',
              y: [4, 8]
            },
            {
              x: '',
              y: [5, 9]
            },
            {
              x: '',
              y: [4, 8]
            },
            {
              x: '',
              y: [4, 7]
            },
            {
              x: '',
              y: [2, 5]
            }
          ]
        }
      ],
      chart: {
        type: 'rangeBar',
        height: 80,
        sparkline: { enabled: true },
        toolbar: {
          show: false
        }
      },
      colors: ['#E58A00'],
      plotOptions: {
        bar: {
          columnWidth: '30%',
          borderRadius: 5,
          horizontal: false
        }
      },
      yaxis: {
        tickAmount: 2,
        min: 0,
        max: 10
      },
      grid: {
        show: false,
        padding: {
          top: 0,
          right: 0,
          bottom: 0,
          left: 0
        }
      },
      xaxis: {
        labels: {
          show: false
        },
        axisBorder: {
          show: false
        },
        axisTicks: {
          show: false
        },
        axisBorder: {
          show: false
        }
      },
      dataLabels: {
        enabled: false
      }
    };
    var chart = new ApexCharts(document.querySelector('#visitors-graph'), options11);
    chart.render();
  }, 500);
});
