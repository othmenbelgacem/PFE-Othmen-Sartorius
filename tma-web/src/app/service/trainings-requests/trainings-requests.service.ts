import { Injectable } from "@angular/core";
import { UtilsService } from "../utils.service";
import { PaginateTrainingRequestsRequest } from "app/model/training-requests/paginate-training-requests-req.model";
import { Observable } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";
import { AdminTrainingRequestsPage } from "app/model/training-requests/admin-training-requests-page.model";
import { Operator } from "app/model/user/operator.model";

@Injectable({
  providedIn: "root",
})
export class TrainingRequestService {
  private TRAINING_REQUESTS_API =
    UtilsService.BASE_API_URL + "api/training-request";

  constructor(private http: HttpClient) {}

  getPaginateTrainingRequests(
    request: PaginateTrainingRequestsRequest
  ): Observable<AdminTrainingRequestsPage> {
    return this.http.get<AdminTrainingRequestsPage>(
      `${this.TRAINING_REQUESTS_API}?page=${request.page}&offset=${request.offset}&trainingId=${request.trainingId}&subTrainingId=${request.subTrainingId}`
    );
  }
  getTrainingRequestOperators(
    trainingId: string,
    subTrainingId: string
  ): Observable<Operator[]> {
    let params = new HttpParams();

    if (trainingId !== undefined && trainingId !== null) {
      params = params.set("trainingId", trainingId);
    }

    if (subTrainingId !== undefined && subTrainingId !== null) {
      params = params.set("subTrainingId", subTrainingId);
    }
    return this.http.get<Operator[]>(
      `${this.TRAINING_REQUESTS_API}/requested-operators`,
      { params }
    );
  }
  getRequestedCount(): Observable<number> {
    return this.http.get<number>(`${this.TRAINING_REQUESTS_API}/count-requested`);
  }
}
