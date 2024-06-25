import { Injectable } from "@angular/core";
import { HttpClient, HttpParams, HttpResponse } from "@angular/common/http";
import { catchError, map, Observable, throwError } from "rxjs";
import { UtilsService } from "../utils.service";
import { TrainingTypeRequest } from "app/model/training-type-request.model";
import { TrainingSubTypeRequest } from "app/model/training-sub-type-request.model";
import { TrainingSubTypeDetails } from "app/model/training-sub-type-details.model";

@Injectable({
  providedIn: "root",
})
export class TrainingTypeService {
  TRAINING_TYPE_API = UtilsService.BASE_API_URL + "api/training-type";

  constructor(private http: HttpClient) {}

  saveTrainingType(trainingType: any): Observable<any> {
    return this.http.post<any>(
      `${this.TRAINING_TYPE_API}/add-or-update`,
      trainingType
    );
  }

  getAllTrainingTypes(
    page: number,
    offset: number,
    searchText: string
  ): Observable<TrainingTypeRequest[]> {
    let params = new HttpParams();

    if (page !== undefined && page !== null) {
      params = params.set("page", page.toString());
    }

    if (offset !== undefined && offset !== null) {
      params = params.set("offset", offset.toString());
    }
    if (searchText !== undefined && searchText !== null && searchText !== "") {
      params = params.set("text", searchText.toString());
    }
    return this.http.get<TrainingTypeRequest[]>(
      `${this.TRAINING_TYPE_API}/get-paged-training-types`,
      { params }
    );
  }

  getTrainingTypeLabel(trainingTypeUuid: string): Observable<string> {
    let params = new HttpParams().set("trainingTypeUuid", trainingTypeUuid);

    return this.http.get(`${this.TRAINING_TYPE_API}/label`, {
      params,
      responseType: "text",
    });
  }

  getByUUId(trainingTypeUuid: string): Observable<any> {
    return this.http.get(`${this.TRAINING_TYPE_API}/${trainingTypeUuid}`);
  }

  saveSubTraining(
    trainingSubTypeRequest: TrainingSubTypeRequest
  ): Observable<void> {
    return this.http.post<void>(
      `${this.TRAINING_TYPE_API}/add-sub-training`,
      trainingSubTypeRequest
    );
  }

  updateSubTraining(
    trainingSubTypeRequest: TrainingSubTypeRequest
  ): Observable<void> {
    return this.http.post<void>(
      `${this.TRAINING_TYPE_API}/update-sub-training`,
      trainingSubTypeRequest
    );
  }

  getSubTrainingTypesByTrainingType(
    trainingTypeUuid: string,
    text: string
  ): Observable<TrainingSubTypeDetails[]> {
    let params = new HttpParams().set("trainingTypeUuid", trainingTypeUuid);
    if (text != null && text !== "") {
      params = params.set("text", text);
    }

    return this.http.get<TrainingSubTypeDetails[]>(
      `${this.TRAINING_TYPE_API}/get-sub-training-by-training-type-uuid`,
      { params }
    );
  }

  deleteTrainingSubType(trainingSubTypeUuid: string): Observable<void> {
    let params = new HttpParams().set(
      "trainingSubTypeUuid",
      trainingSubTypeUuid
    );

    return this.http.delete<void>(
      `${this.TRAINING_TYPE_API}/delete-sub-training`,
      { params }
    );
  }

  deleteTrainingType(trainingTypeUuid: string): Observable<void> {
    let params = new HttpParams().set("trainingTypeUuid", trainingTypeUuid);

    return this.http.delete<void>(`${this.TRAINING_TYPE_API}`, { params });
  }

  getAllTrainings(): Observable<any[]> {
    return this.http.get<any[]>(`${this.TRAINING_TYPE_API}/all`);
  }

  getAllSubTrainings(trainingId: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.TRAINING_TYPE_API}/${trainingId}/all-sub-trainings`
    );
  }
}
