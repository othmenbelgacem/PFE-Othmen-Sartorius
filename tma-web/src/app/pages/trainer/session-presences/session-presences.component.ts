import { DatePipe } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { TrainingSessionService } from "app/service/training-session/training-session.service";
import { UtilsService } from "app/service/utils.service";

@Component({
  selector: "session-presences",
  templateUrl: "./session-presences.component.html",
  styleUrls: ["./session-presences.component.scss"],
  providers: [DatePipe],
})
export class SessionPresencesComponent implements OnInit {
  sessions: any[] = [];
  sessionId: string;
  doneSessions: any[] = [];
  isDone: boolean = false;
  constructor(
    private service: TrainingSessionService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private datePipe: DatePipe
  ) {}

  ngOnInit(): void {
    this.sessionId = this.activatedRoute.snapshot.paramMap.get("uuid");
    console.log('Current sessionId:', this.sessionId);
    this.getSessionPresences();
    this.service.getDoneSessions().subscribe((sessions) => {
      this.doneSessions = sessions;

      this.isDone = this.doneSessions.some(session => {
      
        return session.uuid === this.sessionId; 
      });

      console.log('Is session DONE?', this.isDone);
    });
  
  }

  getSessionPresences() {
    this.service.getSessionPresences(this.sessionId).subscribe((data: any) => {
      this.sessions = data;
    });
  }
  isTrainer(): boolean {
    return UtilsService.isTrainer();
  }
  updateStatus(session) {}
  goToresencesheets(presence) {
    this.router.navigateByUrl(
      `/presence-sheet/${this.sessionId}/${presence.date}`
    );
  }
  addPresence() {
    const todayDate = this.datePipe.transform(new Date(), "yyyy-MM-dd");
    this.router.navigateByUrl(`/presence-sheet/${this.sessionId}/${todayDate}`);
  }
}
