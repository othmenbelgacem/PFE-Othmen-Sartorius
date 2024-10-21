import { Component, OnInit } from '@angular/core';
import Chart from 'chart.js'; 
import { TrainingSessionStatisticService } from "../../service/training-session-statistic/trainings-session-static.service";
import { UtilsService } from "../../service/utils.service";
import { TrainingRequestCount, TrainingSessionStatisticByTrainerOrOperator, TrainingSessionStatistic } from "../../model/trainingSessionStatistic/training-session-statistic.model";

@Component({
  selector: 'dashboard-cmp',
  moduleId: module.id,
  templateUrl: 'dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  showOperator = true;
  isManager: boolean = UtilsService.isManager();
  isAdministrator: boolean = UtilsService.isAdministrator();
  isTrainer: boolean = UtilsService.isTrainer();
  isOperator: boolean = UtilsService.isCollaborator();
  totalSession: number = 0;
  totalPlannedSession: number = 0;
  totalInProgressSession: number = 0;
  totalDoneSession: number = 0;
  rejectedSession: number = 0; 
  rejectedSessionPercentage: number = 0; 
  sessionStaticByTrainerOrOperator: TrainingSessionStatisticByTrainerOrOperator[] = [];
  public chartColor;
  public chart: any;
  public year: number = new Date().getFullYear();
  public month: number = new Date().getMonth() + 1;

  constructor(private trainingSessionStatisticService: TrainingSessionStatisticService) { }

  ngOnInit() {
    this.loadChartData(this.year, this.month);
    if (this.isAdministrator) {
      this.trainingSessionStatisticService.getTop10TrainingRequests().subscribe((data: TrainingRequestCount[]) => {
        this.createTopTrainingChart(data);
      });
    }
    this.trainingSessionStatisticService.getTrainingSessionStatistic().subscribe((value: TrainingSessionStatistic) => {
      this.totalSession = value.totalSession;
      this.totalPlannedSession = value.totalPlannedSession;
      this.totalInProgressSession = value.totalInProgressSession;
      this.totalDoneSession = value.totalDoneSession;
      this.rejectedSession = value.rejectedSession; 
      this.sessionStaticByTrainerOrOperator = value.sessionStaticByTrainerOrOperator;

      this.calculateRejectedSessionPercentage();
      this.createRejectedSessionChart();

      if (this.isAdministrator) {
        this.createLineChart();
      }
    });

    this.chartColor = "#FFFFFF";
  }

  calculateRejectedSessionPercentage() {
    if (this.totalSession > 0) {
      this.rejectedSessionPercentage = parseFloat(((this.rejectedSession / this.totalSession) * 100).toFixed(2));
    } else {
      this.rejectedSessionPercentage = 0;
    }
  }

  createRejectedSessionChart(): void {
    const ctx = document.getElementById('rejectedSessionsChart') as HTMLCanvasElement;
    new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['Sessions rejetées', 'Autres sessions'],
        datasets: [{
          data: [this.rejectedSession, this.totalSession - this.rejectedSession],
          backgroundColor: ['#FF6384', '#36A2EB'],
          hoverBackgroundColor: ['#FF6384', '#36A2EB']
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            display: true,
            position: 'top'
          },
          title: {
            display: true,
            text: 'Taux de rejet des sessions'
          }
        }
      }
    });
  }

  loadChartData(year: number, month: number) {
    this.trainingSessionStatisticService.getSessionStatsByMonth(year, month).subscribe(data => {
      this.createSessionProgressChart(data.done, data.inProgress);
    });
  }

  onPeriodChange(year: number, month: number): void {
    this.trainingSessionStatisticService.getSessionStatsByMonth(year, month).subscribe(stats => {
      const doneSessions = stats.done;
      const inProgressSessions = stats.inProgress;
      this.createSessionProgressChart(doneSessions, inProgressSessions);
    });
  }

  createSessionProgressChart(doneSessions: number, inProgressSessions: number): void {
    const ctx = document.getElementById('sessionChart') as HTMLCanvasElement;
    if (this.chart) {
      this.chart.destroy(); 
    }
    this.chart = new Chart(ctx, {
      type: 'doughnut', // Changed to doughnut chart
      data: {
        labels: ['Sessions terminées', 'Sessions en cours'],
        datasets: [{
          label: 'Sessions',
          data: [doneSessions, inProgressSessions],
          backgroundColor: ['#4CAF50', '#FFC107'],
          borderColor: ['#FFFFFF'],
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'top', // Position of the legend
          },
          tooltip: {
            callbacks: {
              label: function(tooltipItem) {
                const label = tooltipItem.label || '';
                return `${label}: ${tooltipItem.raw}`; // Show label and value in tooltips
              }
            }
          }
        }
      }
    });
  }
  

  createLineChart(): void {
    const speedCanvas = document.getElementById("speedChart") as HTMLCanvasElement;
    const dataFirst = {
      data: this.sessionStaticByTrainerOrOperator.map(item => item.totalInProgressSession),
      fill: false,
      borderColor: '#fbc658',
      backgroundColor: 'transparent',
      pointBorderColor: '#fbc658',
      pointRadius: 4,
      pointHoverRadius: 4,
      pointBorderWidth: 8,
    };

    const dataSecond = {
      data: this.sessionStaticByTrainerOrOperator.map(item => item.totalDoneSession),
      fill: false,
      borderColor: '#51CACF',
      backgroundColor: 'transparent',
      pointBorderColor: '#51CACF',
      pointRadius: 4,
      pointHoverRadius: 4,
      pointBorderWidth: 8,
    };

    const speedData = {
      labels: this.sessionStaticByTrainerOrOperator.map(item => item.fullName),
      datasets: [dataFirst, dataSecond]
    };

    const chartOptions = {
      legend: {
        display: false,
        position: 'top'
      }
    };

    new Chart(speedCanvas, {
      type: 'line',
      data: speedData,
      options: chartOptions
    });
  }

  createTopTrainingChart(data: TrainingRequestCount[]): void {
    const trainingNames = data.map(item => item.trainingName);
    const requestCounts = data.map(item => item.requestCount);

    const ctx = document.getElementById('trainingRequestChart') as HTMLCanvasElement;
    new Chart(ctx, {
      type: 'bar',
      data: {
        labels: trainingNames,
        datasets: [{
          label: 'Nombre total des demandes',
          data: requestCounts,
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          x: {
            beginAtZero: true,
            min: 0
          },
          y: {
            beginAtZero: true,
            min: 0
          }
        },
        responsive: true,
        plugins: {
          legend: {
            display: true,
            position: 'top'
          },
          title: {
            display: true,
            text: 'Top 10 des formations les plus demandées'
          }
        },
        indexAxis: 'x',
        barThickness: 20,
        maxBarThickness: 30,
        categoryPercentage: 0.5,
        barPercentage: 0.5
      }
    });
  }
}
