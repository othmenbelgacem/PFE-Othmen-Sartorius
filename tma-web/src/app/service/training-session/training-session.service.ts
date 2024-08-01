import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { UtilsService } from "../utils.service";
import { Observable } from "rxjs";
import { TrainingSessionRequest } from "app/model/training-session-request.model";

@Injectable({
  providedIn: "root",
})
export class TrainingSessionService {
  TRAINING_SESSION_API = UtilsService.BASE_API_URL + "api/training-session";

  constructor(private http: HttpClient) {}

  saveTrainingSession(request: TrainingSessionRequest): Observable<any> {
    return this.http.post<any>(`${this.TRAINING_SESSION_API}`, request);
  }

  getSessions(page: number, offset: number, status: string = ''): Observable<any> {
    let params = new HttpParams();

    if (page !== undefined && page !== null) {
        params = params.set("page", page.toString());
    }

    if (offset !== undefined && offset !== null) {
        params = params.set("offset", offset.toString());
    }

    if (status) {
        params = params.set("status", status);
    }

    return this.http.get<any>(`${this.TRAINING_SESSION_API}`, { params });
}

  

  getSessionPresences(sessionId: string): Observable<any> {
    return this.http.get<any>(
      `${this.TRAINING_SESSION_API}/presences/${sessionId}`
    );
  }
  getPresencesPerDate(sessionId: string, date: string) {
    return this.http.get<any>(
      `${this.TRAINING_SESSION_API}/presences-by-date/${sessionId}?date=${date}`
    );
  }
  addPresence(sessionId: string, request: any): Observable<any> {
    return this.http.post<any>(
      `${this.TRAINING_SESSION_API}/add-presences/${sessionId}`,
      request
    );
  }
  updateStatus(sessionId: string, status: string): Observable<any> {
    return this.http.put<any>(
      `${this.TRAINING_SESSION_API}/update-status/${sessionId}/${status}`,
      null
    );
  }
  uploadDocuments(sessionId: string, formData: FormData): Observable<any> {
    return this.http.post<any>(`${this.TRAINING_SESSION_API}/${sessionId}/upload-documents`, formData);
  }

  getDocuments(sessionId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.TRAINING_SESSION_API}/${sessionId}/documents`);
  }
  
  downloadDocument(sessionId: string, fileName: string): Observable<Blob> {
    console.log('Request URL:', `${this.TRAINING_SESSION_API}/${sessionId}/documents/download`); // Debug log
    console.log('fileName:', fileName); // Debug log
    return this.http.get(`${this.TRAINING_SESSION_API}/${sessionId}/documents/download`, {
        params: { fileName: fileName },
        responseType: 'blob'
    });
}

}
