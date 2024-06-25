import { TrainerDto } from "./trainer-dto.model";
export class TrainingTypeRequest {
    trainingTypeUuid: string;
    label: string;
    details: string;
    hourDuration: string;
    lifeDuration: string;
    subTypeCount: number;
    trainerUuids: string[];
    integrationduration : string;
  }
  