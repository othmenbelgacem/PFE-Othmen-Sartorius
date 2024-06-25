export class TrainingSessionRequest {
  trainingTypeUuid: string;
  trainingSubTypeUuid: string;
  trainerUuid: string;
  startDate: string;
  endDate: string;
  operatorUuids: string[];
  place : string;
  startHour : string;
}
