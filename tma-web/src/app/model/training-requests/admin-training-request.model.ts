import { SartoriusBaseEntity } from "./base-entity.model";

export class AdminTrainingRequestResponse {
    uuid: string;
    operator: SartoriusBaseEntity;
    teamLeader: SartoriusBaseEntity;
    trainingType: SartoriusBaseEntity;
    trainingSubType: SartoriusBaseEntity;
    requestDate: string;
}