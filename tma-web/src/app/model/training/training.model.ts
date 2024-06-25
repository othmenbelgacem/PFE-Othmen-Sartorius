import { TrainerDetailsModel } from "../trainer-details.model";

export class ManagerTraining {
    uuid: string;
    label: string;
    details: string;
    hourDuration: number;
    lifeDuration: number;
    subTypeCount: number;
    trainers: TrainerDetailsModel[];
}