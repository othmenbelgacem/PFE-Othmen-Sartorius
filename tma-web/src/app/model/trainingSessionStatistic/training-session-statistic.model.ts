export class TrainingSessionStatistic {
  totalSession: number;
  totalPlannedSession: number;
  totalInProgressSession: number;
  totalDoneSession: number;
  rejectedSession: number; 
  sessionStaticByTrainerOrOperator: TrainingSessionStatisticByTrainerOrOperator[];
}

export class TrainingSessionStatisticByTrainerOrOperator {
  totalSession: number;
  totalInProgressSession: number;
  totalDoneSession: number;
  fullName: string;
}

export class TrainingRequestCount {
  trainingName: string;
  requestCount: number;
}
