import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { UtilsService } from "../utils.service";
import { Injectable } from "@angular/core";
import { ManagerTraining } from "app/model/training/training.model";
import { Observable, map } from "rxjs";
import { Operator } from "app/model/user/operator.model";

@Injectable({
  providedIn: "root",
})
export class ManagerTrainingService {
  private MANAGER_TRAINING_API =
    UtilsService.BASE_API_URL + "api/training-type";
  private readonly reqHeader = new HttpHeaders({
    "Content-Type": "application/json",
    "No-Auth": "True",
  });

  constructor(private http: HttpClient) {}

  getAllManagerTrainings(searchText: string): Observable<ManagerTraining[]> {
    let params = new HttpParams();
    if (searchText !== undefined && searchText !== null && searchText !== "") {
      params = params.set("text", searchText.toString());
    }
    return this.http.get<ManagerTraining[]>(
      `${this.MANAGER_TRAINING_API}/all`,
      { params }
    );
  }

  getAllManagerSubTrainings(trainingId: string): Observable<ManagerTraining[]> {
    return this.http.get<ManagerTraining[]>(
      `${this.MANAGER_TRAINING_API}/${trainingId}/all-sub-trainings`
    );
  }

  getAssignedOperatorsToTraining(trainingId: string): Observable<Operator[]> {
    return this.http.get<Operator[]>(
      `${this.MANAGER_TRAINING_API}/assigned-operators-to-training/${trainingId}`
    );
  }

  getAssignedOperatorsToSubTraining(
    subTrainingId: string
  ): Observable<Operator[]> {
    return this.http.get<Operator[]>(
      `${this.MANAGER_TRAINING_API}/assigned-operators-to-sub-training/${subTrainingId}`
    );
  }

  assignOperatorToSubTraining(request: {
    subTrainingId: string;
    operatorId: string;
  }): Observable<any> {
    return this.http.post<any>(
      `${this.MANAGER_TRAINING_API}/assign-sub-training-type/${request.subTrainingId}/${request.operatorId}`,
      null
    );
  }

  assignOperatorToTraining(request: {
    trainingId: string;
    operatorId: string;
  }): Observable<any> {
    return this.http.post<any>(
      `${this.MANAGER_TRAINING_API}/assign-training-type/${request.trainingId}/${request.operatorId}`,
      null
    );
  }
  cancelOperatorTraining(request: { trainingId: string, operatorId: string }): Observable<any> {
    const url = `${this.MANAGER_TRAINING_API}/cancel-training-type/${request.trainingId}/${request.operatorId}`;
    console.log('DELETE URL:', url);
    return this.http.delete<any>(url);
  }
}
