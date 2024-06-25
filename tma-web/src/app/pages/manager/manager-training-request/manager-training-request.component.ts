import { TrainerService } from "./../../../service/trainer/trainer.service";
import { Component, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { TrainerDto } from "app/model/trainer-dto.model";
import { AdminTrainingRequestsPage } from "app/model/training-requests/admin-training-requests-page.model";
import { PaginateTrainingRequestsRequest } from "app/model/training-requests/paginate-training-requests-req.model";
import { TrainingSessionRequest } from "app/model/training-session-request.model";
import { TrainingTypeRequest } from "app/model/training-type-request.model";
import { TrainingSessionService } from "app/service/training-session/training-session.service";
import { TrainingTypeService } from "app/service/training-type/training-type.service";
import { TrainingRequestService } from "app/service/trainings-requests/trainings-requests.service";
import { ToastrService } from "ngx-toastr";
import {ManagerTrainingService} from "app/service/manager/manager-training.service";
import {Operator} from "app/model/user/operator.model";
@Component({
  selector: "manager-training-request",
  templateUrl: "./manager-training-request.component.html",
  styleUrls: ["./manager-training-request.component.scss"],
})
export class ManagerTrainingRequestComponent implements OnInit {
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
  public operators: Operator[];

  trainingSessionRequest: TrainingSessionRequest = new TrainingSessionRequest();
  trainers: TrainerDto[] = [];
  trainings: any[] = [];
  subTrainings: any[] = [];
  @ViewChild("modalContent", { static: true }) modalContent: TemplateRef<any> | undefined;
  minStartDate = new Date();

  constructor(
    private trainingRequestService: TrainingRequestService,
    private trainingTypeService: TrainingTypeService,
    private trainerService: TrainerService,
    private trainingSessionService: TrainingSessionService,
    private modal: NgbModal,
    private toastr: ToastrService,
    private trainingService: ManagerTrainingService
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
    this.trainingRequestService.getPaginateTrainingRequests(request).subscribe(
      (res: AdminTrainingRequestsPage) => {
        this.currentPageContent = res;
        console.log('Loaded page content:', this.currentPageContent);
      }
    );
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
    this.trainers = this.trainings.filter(
      (t) => t.uuid === this.trainingSessionRequest.trainingTypeUuid
    )[0].trainers;
    this.trainingTypeService
      .getAllSubTrainings(this.trainingSessionRequest.trainingTypeUuid)
      .subscribe((response) => {
        this.subTrainings = response;
      });
  }

  onChangeSubFormation(event) {
    console.log(event);
    this.trainers = this.subTrainings.filter(
      (t) => t.uuid === this.trainingSessionRequest.trainingSubTypeUuid
    )[0].trainers;
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

  onCancelOperator(trainingTypeUuid: string, operatorUuid: string) {
    console.log('Cancelling operator training with trainingTypeUuid:', trainingTypeUuid, 'and operatorUuid:', operatorUuid);

    if (!trainingTypeUuid || !operatorUuid) {
      this.toastr.error('Invalid training or operator ID');
      return;
    }

    this.trainingService.cancelOperatorTraining({ trainingId: trainingTypeUuid, operatorId: operatorUuid }).subscribe(
      (res) => {
        this.toastr.success('Formation annulée avec succès');
        this.loadPage(this.getDefaultPageRequest()); // Refresh the page to update the list
      },
      (error) => {
        this.toastr.error('Erreur lors de l\'annulation de la formation');
      }
    );
  }

}