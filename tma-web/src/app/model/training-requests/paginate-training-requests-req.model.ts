export class PaginateTrainingRequestsRequest {
    page: number;
    offset: number;
    trainingId: string;
    subTrainingId: string;

    constructor(page: number, offset: number, trainingId?: string, subTrainingId?: string) {
        this.page = page;
        this.offset = offset;
        this.trainingId = trainingId || '';
        this.subTrainingId = subTrainingId || '';
    }
}