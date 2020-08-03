( function ( $ ) {
    "use strict";
    
    //Sales chart
    var ctx = document.getElementById( "sales-chart" );

    ctx.height = 140;
    Chart.defaults.global.defaultFontColor = '#f8f8f8';

    

    var myChart = new Chart( ctx, {
        type: 'line',
        data: {
            labels: [ "2020", "2021", "2022", "2023", "2024", "2025", "2026" ],
            type: 'line',
            defaultFontFamily: 'Montserrat',
            bodyFontColor: '#ffffff',
            datasets: [ {
                label: "Registered Students",
                data: [ 0, 30, 10, 120, 50, 63, 10 ],
                backgroundColor: 'transparent',
                borderColor: 'yellow',
                bodyFontColor: '#ffffff',
                borderWidth: 3,
                pointStyle: 'circle',
                pointRadius: 5,
                pointBorderColor: 'transparent',
                pointBackgroundColor: 'yellow',
                    }, {
                label: "Examined Students",
                data: [ 0, 50, 40, 80, 40, 79, 120 ],
                backgroundColor: 'transparent',
                borderColor: 'green',
                borderWidth: 3,
                pointStyle: 'circle',
                pointRadius: 5,
                pointBorderColor: 'transparent',
                pointBackgroundColor: 'rgba(40,167,69,0.75)',
                    } ]
        },
        options: {
            responsive: true,

            tooltips: {
                mode: 'index',
                titleFontSize: 12,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#c5c5c5',
                titleFontFamily: 'Arial',
                bodyFontFamily: 'Arial',
                cornerRadius: 3,
                intersect: false,
            },
            legend: {
                display: false,
                labels: {
                    usePointStyle: true,
                    fontFamily: '#f8f8f8',
                    fontColor: '#fff'
                },
            },
            scales: {
                xAxes: [ {
                    display: true,
                    gridLines: {
                        display: false,
                        drawBorder: true,
                    },
                    scaleLabel: {
                        display: false,
                        labelString: 'Year',
                    }
                        } ],
                yAxes: [ {
                    display: true,
                    gridLines: {
                        display: false,
                        drawBorder: true
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'No of Students'
                    }
                        } ]
            },
            title: {
                display: false,
                text: 'Normal Legend'
            }
        }
    } );
} )( jQuery );