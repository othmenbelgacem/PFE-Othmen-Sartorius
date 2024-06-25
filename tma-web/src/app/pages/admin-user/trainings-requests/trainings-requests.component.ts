import { TrainerService } from "./../../../service/trainer/trainer.service";
import { Component, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { TrainerDto } from "app/model/trainer-dto.model";
import { AdminTrainingRequestsPage } from "app/model/training-requests/admin-training-requests-page.model";
import { PaginateTrainingRequestsRequest } from "app/model/training-requests/paginate-training-requests-req.model";
import { TrainingSessionRequest } from "app/model/training-session-request.model";
import { TrainingTypeRequest } from "app/model/training-type-request.model";
import { Operator } from "app/model/user/operator.model";
import { TrainingSessionService } from "app/service/training-session/training-session.service";
import { TrainingTypeService } from "app/service/training-type/training-type.service";
import { TrainingRequestService } from "app/service/trainings-requests/trainings-requests.service";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "trainings-requests",
  templateUrl: "./trainings-requests.component.html",
  styleUrls: ["./trainings-requests.component.scss"],
})
export class TrainingsRequestsComponent implements OnInit {
  public readonly TRAINING_FILTER_ELEMENT_ID = "trainings-filter";
  public readonly SUB_TRAINING_FILTER_ELEMENT_ID = "sub-trainings-filter";
  public readonly DEFAULT_FILTER_VALUE = "default-none";
  public readonly PAGE_SIZE: number = 10;

  public currentPageContent: AdminTrainingRequestsPage;
  public currentPageNumber: number = 1;

  public selectedTrainingId: string = this.DEFAULT_FILTER_VALUE;
  public selectedSubTrainingId: string = this.DEFAULT_FILTER_VALUE;

  public trainingsFilterOptions: TrainingTypeRequest[] = [];
  public subTrainingsFilterOptions: any[] = [];

  trainingSessionRequest: TrainingSessionRequest = new TrainingSessionRequest();
  trainers: TrainerDto[] = [];
  trainings: any[] = [];
  subTrainings: any[] = [];
  requestedOperators: Operator[] = [];
  @ViewChild("modalContent", { static: true }) modalContent:
    | TemplateRef<any>
    | undefined;
  minStartDate = new Date();
  constructor(
    private trainingRequestService: TrainingRequestService,
    private trainingTypeService: TrainingTypeService,
    private trainerService: TrainerService,
    private trainingSessionService: TrainingSessionService,
    private modal: NgbModal,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.loadPage(
      new PaginateTrainingRequestsRequest(
        this.currentPageNumber - 1,
        this.PAGE_SIZE
      )
    );
    this.loadTrainingsFilterOptions();
    this.loadAllTrainingTypes();
    this.loadTrainers();
  }
  loadTrainers() {
    this.trainerService.retrieveAllTrainers().subscribe((response) => {
      this.trainers = response;
    });
  }
  showPlanningSessionModal() {
    this.trainingSessionRequest = new TrainingSessionRequest();
    this.modal.open(this.modalContent);
  }

  onPageChange(event) {
    this.currentPageNumber = event;
    this.loadPage(this.getDefaultPageRequest());
  }

  loadPage(request: PaginateTrainingRequestsRequest) {
    this.trainingRequestService
      .getPaginateTrainingRequests(request)
      .subscribe((res: AdminTrainingRequestsPage) => {
        this.currentPageContent = res;
      });
  }

  loadAllTrainingTypes() {
    this.trainingTypeService.getAllTrainings().subscribe((response) => {
      this.trainings = response;
    });
  }

  loadTrainingsFilterOptions() {
    this.trainingTypeService
      .getAllTrainingTypes(0, 1000, null)
      .subscribe((res: any) => {
        this.trainingsFilterOptions = res.items;
      });
  }

  loadSubTrainingsFilterOptions(trainingId: string) {
    this.trainingTypeService
      .getSubTrainingTypesByTrainingType(trainingId, null)
      .subscribe((res: any) => {
        this.subTrainingsFilterOptions = res;
      });
  }

  onChangeOptions(event) {
    this.currentPageNumber = 1;
    if (event?.target?.id == this.TRAINING_FILTER_ELEMENT_ID) {
      if (event?.target?.value == this.DEFAULT_FILTER_VALUE) {
        this.selectedTrainingId = this.DEFAULT_FILTER_VALUE;
        this.selectedSubTrainingId = this.DEFAULT_FILTER_VALUE;
        this.subTrainingsFilterOptions = [];
        this.loadPage(this.getDefaultPageRequest());
      } else {
        this.selectedTrainingId = event.target.value;
        this.selectedSubTrainingId = this.DEFAULT_FILTER_VALUE;
        this.loadPage(this.getTrainingPageRequest(this.selectedTrainingId));
        this.loadSubTrainingsFilterOptions(this.selectedTrainingId);
      }
    } else if (event?.target?.id == this.SUB_TRAINING_FILTER_ELEMENT_ID) {
      if (event?.target?.value == this.DEFAULT_FILTER_VALUE) {
        this.selectedSubTrainingId = this.DEFAULT_FILTER_VALUE;
        this.loadPage(this.getTrainingPageRequest(this.selectedTrainingId));
      } else {
        this.selectedSubTrainingId = event.target.value;
        this.loadPage(
          this.getSubTrainingPageRequest(this.selectedSubTrainingId)
        );
      }
    }
  }

  getDefaultPageRequest(): PaginateTrainingRequestsRequest {
    return new PaginateTrainingRequestsRequest(
      this.currentPageNumber - 1,
      this.PAGE_SIZE
    );
  }

  getTrainingPageRequest(trainingId: string) {
    return new PaginateTrainingRequestsRequest(
      this.currentPageNumber - 1,
      this.PAGE_SIZE,
      trainingId
    );
  }

  getSubTrainingPageRequest(subTrainingId: string) {
    return new PaginateTrainingRequestsRequest(
      this.currentPageNumber - 1,
      this.PAGE_SIZE,
      "",
      subTrainingId
    );
  }

  onChangeFormation(event) {
    this.requestedOperators = [];
    this.trainers = this.trainings.filter(
      (t) => t.uuid === this.trainingSessionRequest.trainingTypeUuid
    )[0].trainers;
    this.trainingTypeService
      .getAllSubTrainings(this.trainingSessionRequest.trainingTypeUuid)
      .subscribe((response) => {
        this.subTrainings = response;
        if (this.subTrainings.length == 0) {
          this.trainingRequestService
            .getTrainingRequestOperators(
              this.trainingSessionRequest.trainingTypeUuid,
              null
            )
            .subscribe((data) => {
              this.requestedOperators = data;
            });
        }
      });
  }

  onChangeSubFormation(event) {
    this.requestedOperators = [];
    console.log(event);
    this.trainers = this.subTrainings.filter(
      (t) => t.uuid === this.trainingSessionRequest.trainingSubTypeUuid
    )[0].trainers;
    this.trainingRequestService
      .getTrainingRequestOperators(
        this.trainingSessionRequest.trainingTypeUuid,
        this.trainingSessionRequest.trainingSubTypeUuid
      )
      .subscribe((data) => {
        this.requestedOperators = data;
      });
  }

  saveSession() {
    this.trainingSessionService
      .saveTrainingSession(this.trainingSessionRequest)
      .subscribe((resposne) => {
        this.showSuccess("La session a été planifiée avec succés");
        this.modal.dismissAll();
      });
  }
  showSuccess(msg) {
    this.toastr.success(msg);
  }
}
