import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { TrainingSessionService } from "app/service/training-session/training-session.service";
import { UtilsService } from "app/service/utils.service";
import { ToastrService } from "ngx-toastr";
@Component({
  selector: "presence-sheet",
  templateUrl: "./presence-sheet.component.html",
  styleUrls: ["./presence-sheet.component.scss"],
})
export class PresenceSheetComponent implements OnInit {
  presences: any[] = [];
  sessionId: string;
  date: string;
  doneSessions: any[] = [];
  isDone: boolean = false;
  constructor(
    private service: TrainingSessionService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.sessionId = this.activatedRoute.snapshot.paramMap.get("uuid");
    this.date = this.activatedRoute.snapshot.paramMap.get("date");

    this.getPresences();
    this.service.getDoneSessions().subscribe((sessions) => {
      this.doneSessions = sessions;

      this.isDone = this.doneSessions.some(session => {
      
        return session.uuid === this.sessionId; 
      });

      console.log('Is session DONE?', this.isDone);
    });
  }

  getPresences() {
    this.service
      .getPresencesPerDate(this.sessionId, this.date)
      .subscribe((data: any) => {
        this.presences = data;
      });
  }
  isTrainer(): boolean {
    return UtilsService.isTrainer();
  }
  updateStatus(session) {}
  goToresencesheets(session) {}
  addPresence() {}
  onSave() {
    this.service.addPresence(this.sessionId, this.presences).subscribe({
      next: (response) => {
        this.toastr.success("La feuille de présence modifié avec succès");
        this.router.navigateByUrl(`/session-presences/${this.sessionId}`);
      },
      error: (error) => {
        if (error.status === 409) {
          this.toastr.error(error.error);
        } else {
          this.toastr.error("An error occurred while saving the attendance sheet");
        }
      }
    });
  }
  
  cancel() {
    this.router.navigateByUrl(`/session-presences/${this.sessionId}`);
  }
}
