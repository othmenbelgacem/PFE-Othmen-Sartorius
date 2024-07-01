import { Component, Input, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { TrainingSessionService } from "app/service/training-session/training-session.service";
import { UtilsService } from "app/service/utils.service";
import { ToastrService } from "ngx-toastr";
import Swal from "sweetalert2";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { RoleCode } from "app/enumeration/role-code";

@Component({
  selector: "session-management",
  templateUrl: "./session-management.component.html",
  styleUrls: ["./session-management.component.scss"],
})
export class SessionManagementComponent implements OnInit {
  @Input() showOperator: boolean = false;
  currentPageContent: any = null;
  public currentPageNumber: number = 0;
  public readonly PAGE_SIZE: number = 100000;
  page: number = 1;
  pageSize: number = 10;
  collectionSize: number = 0;
  documents: string[] = [];
  currentSessionId: string;
  selectedFiles: FileList;

  @ViewChild("memberModal", { static: true }) memberModal: TemplateRef<any>;
  @ViewChild("uploadModal", { static: true }) uploadModal: TemplateRef<any>;
  @ViewChild("downloadModal", { static: true }) downloadModal: TemplateRef<any>;

  selectedSessionMembers: any[] = [];

  constructor(
    private service: TrainingSessionService,
    private router: Router,
    private toastr: ToastrService,
    private modal: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadPage();
  }

  onPageChange(event) {
    this.page = event;
    this.loadPage();
  }

  loadPage() {
    this.service.getSessions(this.currentPageNumber, this.PAGE_SIZE).subscribe((data: any) => {
      this.currentPageContent = data;
    });
  }

  isTrainer(): boolean {
    return UtilsService.isTrainer();
  }
  isAdministrator(): boolean {
    return UtilsService.isAdministrator();
  }
  isManager(): boolean {
    return UtilsService.isManager();
  }
  isCollaborator(): boolean {
    return UtilsService.isCollaborator();
  }

  startSession(session) {
    this.service.updateStatus(session.sessionId, "IN_PROGRESS").subscribe(() => {
      this.toastr.success("La session est marqué en cours!");
      this.loadPage();
    });
  }

  closeSession(session) {
    this.service.updateStatus(session.sessionId, "DONE").subscribe(() => {
      this.toastr.success("La session est marqué comme cloturé!");
      this.loadPage();
    });
  }

  goToresencesheets(session) {
    this.router.navigateByUrl(`/session-presences/${session.sessionId}`);
  }

  markAsRejected(session) {
    Swal.fire({
      text: "Voulez vous rejeté cette session ?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Confirmer",
      cancelButtonText: "Annuler",
    }).then((result) => {
      if (result.isConfirmed) {
        this.service.updateStatus(session.sessionId, "REJECTED").subscribe(
          () => {
            this.toastr.success("The session has been marked as rejected!");
            this.loadPage();
          },
          (error) => {
            this.toastr.error("Failed to mark the session as rejected.");
          }
        );
      }
    });
  }

  openMemberModal(session: any): void {
    if (session.operators && session.operators.length > 0) {
      this.selectedSessionMembers = session.operators;
    } else {
      this.selectedSessionMembers = [];
    }
    this.modal.open(this.memberModal);
  }

  openUploadModal(session: any): void {
    this.currentSessionId = session.sessionId;
    this.modal.open(this.uploadModal);
  }

  openDownloadModal(session: any): void {
    this.currentSessionId = session.sessionId;
    this.loadDocuments(session.sessionId);
    this.modal.open(this.downloadModal);
  }

  onFileSelected(event): void {
    this.selectedFiles = event.target.files;
  }

  uploadDocuments(): void {
    if (this.selectedFiles && this.selectedFiles.length > 0) {
      const formData = new FormData();
      for (let i = 0; i < this.selectedFiles.length; i++) {
        formData.append('files', this.selectedFiles[i]);
      }
      this.service.uploadDocuments(this.currentSessionId, formData).subscribe(() => {
        this.toastr.success("Documents téléchargés avec succès !");
        this.modal.dismissAll();
      }, () => {
        this.toastr.error("Échec du téléchargement des documents.");
      });
    }
  }

  loadDocuments(sessionId: string): void {
    this.service.getDocuments(sessionId).subscribe((data: string[]) => {
      this.documents = data;
    }, error => {
      this.toastr.error("Échec du chargement des documents.");
    });
  }
  
  downloadDocument(mediaLabel: string, originalName: string): void {
    console.log('Downloading document with mediaLabel:', mediaLabel, 'and originalName:', originalName); // Debug log
    this.service.downloadDocument(this.currentSessionId, mediaLabel).subscribe((response: Blob) => {
        const url = window.URL.createObjectURL(response);
        const a = document.createElement('a');
        a.href = url;
        a.download = originalName;
        a.click();
        window.URL.revokeObjectURL(url);
    }, error => {
        console.error('Download error:', error); // Log errors
    });
}

}
