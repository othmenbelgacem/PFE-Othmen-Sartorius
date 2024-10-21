import { Injectable } from '@angular/core';
import { UtilsService } from '../utils.service';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {
  TrainingRequestCount,
  TrainingSessionStatistic
} from '../../model/trainingSessionStatistic/training-session-statistic.model';

@Injectable({
  providedIn: 'root',
})
export class TrainingSessionStatisticService {
  private TRAINING_REQUESTS_API = UtilsService.BASE_API_URL + 'api/training-session-static';

  constructor(private http: HttpClient) { }

  getTrainingSessionStatistic(): Observable<TrainingSessionStatistic> {
    return this.http.get<TrainingSessionStatistic>(this.TRAINING_REQUESTS_API);
  }

  getTop10TrainingRequests(): Observable<TrainingRequestCount[]> {
    return this.http.get<TrainingRequestCount[]>(`${this.TRAINING_REQUESTS_API}/top10`);
  }
  getSessionStatsByMonth(year: number, month: number): Observable<any> {
    return this.http.get<any>(`${this.TRAINING_REQUESTS_API}/sessions-by-month?year=${year}&month=${month}`);
  }
}
