import { RoleCode } from "./../../enumeration/role-code";
import { UserDetailsModel } from "./../../model/user-details.model";
import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
  HttpResponse,
} from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, map, Observable, tap, throwError } from "rxjs";
import { UtilsService } from "../utils.service";
import { TrainerDetailsModel } from "app/model/trainer-details.model";
import { TrainerDto } from "app/model/trainer-dto.model";

@Injectable({
  providedIn: "root",
})
export class TrainerService {
  TRAINER_API = UtilsService.BASE_API_URL + "api/trainer";

  constructor(private http: HttpClient) {}

  getListUsers(): Observable<TrainerDetailsModel[]> {
    return this.http.get<TrainerDetailsModel[]>(`${this.TRAINER_API}/list`);
  }

  getAllUsers(
    userType: RoleCode,
    offset: number,
    page: number
  ): Observable<TrainerDetailsModel[]> {
    let params = new HttpParams();
    if (offset !== undefined && offset !== -1) {
      params = params.set("offset", "" + offset);
    }
    if (page !== undefined && page !== -1) {
      params = params.set("page", "" + page);
    }
    if (userType !== undefined && userType !== null) {
      params = params.set("userType", "" + userType);
    }
    return this.http.get<TrainerDetailsModel[]>(`${this.TRAINER_API}`, {
      params: params,
    });
  }

  saveUser(userRequest: any): Observable<any> {
    return this.http.post<any>(`${this.TRAINER_API}/add-new-user`, userRequest);
    /* .pipe(
        map((res) => res.data),
        catchError((error: HttpErrorResponse) => {
          return throwError(error);
        })
      );*/
  }

  updateUser(payload: any): Observable<any> {
    return this.http.patch<any>(`${this.TRAINER_API}/update-user`, payload);
  }

  deleteUser(uuid: any): Observable<any> {
    return this.http.delete<any>(`${this.TRAINER_API}?user-uuid=${uuid}`);
  }

  getUserInfo(): Observable<UserDetailsModel> {
    return this.http.get<UserDetailsModel>(`${this.TRAINER_API}/user-profile`);
  }

  retrieveAllTrainers(): Observable<TrainerDto[]> {
    return this.http.get<TrainerDto[]>(`${this.TRAINER_API}/all`).pipe(
      tap(trainers => console.log('Trainers fetched: ', trainers))
    );
  }

  getTrainersByTrainingTypeUuid(
    trainingTypeUuid: string
  ): Observable<TrainerDto[]> {
    let params = new HttpParams().set("trainingTypeUuid", trainingTypeUuid);

    return this.http.get<TrainerDto[]>(`${this.TRAINER_API}/by-training-type`, {
      params,
    });
  }

  getTrainersByTrainingSubTypeUuid(
    trainingSubTypeUuid: string
  ): Observable<TrainerDto[]> {
    let params = new HttpParams().set(
      "trainingSubTypeUuid",
      trainingSubTypeUuid
    );

    return this.http.get<TrainerDto[]>(
      `${this.TRAINER_API}/by-training-sub-type`,
      { params }
    );
  }
  isMatriculeUnique(params: HttpParams): Observable<boolean> {
    return this.http.get<boolean>(`${this.TRAINER_API}/is-matricule-unique`, { params });
}
}