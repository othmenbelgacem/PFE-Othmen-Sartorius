import { Component, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ToastrService } from "ngx-toastr";
import Swal from "sweetalert2";

import { TrainingTypeService } from "app/service/training-type/training-type.service";
import { TrainerService } from "app/service/trainer/trainer.service";
import { TrainingSubTypeDetails } from "app/model/training-sub-type-details.model";
import { TrainerDto } from "app/model/trainer-dto.model";

@Component({
  selector: "sub-training-management",
  templateUrl: "./sub-training-management.component.html",
  styleUrls: ["./sub-training-management.component.scss"],
})
export class SubTrainingManagementComponent implements OnInit {
  @ViewChild("modalContent", { static: true }) modalContent:
    | TemplateRef<any>
    | undefined;
  trainingTypeUuid: string = "";
  trainingTypeLabel: string = "";
  modalTitle = "";
  trainingSubTypeRequest: any;
  trainingSubTypeList: TrainingSubTypeDetails[] = [];
  trainerList: TrainerDto[] = [];
  selectedTrainers: TrainerDto[] = [];
  trainerUuids: string[] = [];
  searchText: string;
  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modal: NgbModal,
    private trainingTypeService: TrainingTypeService,
    private trainerService: TrainerService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.trainingTypeUuid = this.activatedRoute.snapshot.paramMap.get("uuid");
    this.getTrainingTypeLabel();
    this.getTrainingSubTypesByTrainingType();
  }

  getTrainingTypeLabel() {
    this.trainingTypeService
      .getTrainingTypeLabel(this.trainingTypeUuid)
      .subscribe((label) => {
        this.trainingTypeLabel = label;
      });
  }
  search(event) {
    this.getTrainingSubTypesByTrainingType();
  }
  getTrainingSubTypesByTrainingType() {
    this.trainingTypeService
      .getSubTrainingTypesByTrainingType(this.trainingTypeUuid, this.searchText)
      .subscribe((subTrainingTypes) => {
        this.trainingSubTypeList = subTrainingTypes;
      });
  }

  onDeleteTrainingSubType(trainingSubTypeUuid) {
    Swal.fire({
      text: "Voulez-vous supprimer cette sous-formation ?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Confirmer",
      cancelButtonText: "Annuler",
    }).then((result) => {
      if (result.value) {
        this.trainingTypeService
          .deleteTrainingSubType(trainingSubTypeUuid)
          .subscribe((res) => {
            this.getTrainingSubTypesByTrainingType();
            this.showSuccess("Sous-formation supprimée avec succès");
          });
      }
    });
  }

  onEdit(trainingSubType) {
    this.retrieveAllTrainers();
    this.modalTitle = "Modifier sous-formation";
    this.trainingSubTypeRequest = trainingSubType;
    this.loadTrainersByTrainingSubType();
    this.modal.open(this.modalContent);
  }

  retrieveAllTrainers() {
    this.trainerService
      .retrieveAllTrainers()
      .subscribe((trainers: TrainerDto[]) => {
        this.trainerList = trainers;
      });
  }

  loadTrainersByTrainingSubType() {
    this.trainerService
      .getTrainersByTrainingSubTypeUuid(this.trainingSubTypeRequest.uuid)
      .subscribe((data: TrainerDto[]) => {
        this.selectedTrainers = data;
      });
  }

  onUpdateSubTraining() {
    if (
      !this.isEmpty(this.trainingSubTypeRequest.label) &&
      !this.isEmpty(this.trainingSubTypeRequest.hourDuration) &&
      !this.isEmpty(this.trainingSubTypeRequest.lifeDuration)
    ) {
      this.trainingSubTypeRequest.trainerUuids = this.selectedTrainers.map(
        (trainer) => trainer.userUuid
      );
      this.trainingTypeService
        .updateSubTraining(this.trainingSubTypeRequest)
        .subscribe((res) => {
          this.modal.dismissAll();
          this.getTrainingSubTypesByTrainingType;
          this.getTrainingSubTypesByTrainingType();
          this.showSuccess("Sous-formation ajoutée avec succès");
        });
    }
  }

  cancel() {
    this.router.navigateByUrl("/training-management");
  }

  showSuccess(msg) {
    this.toastr.success(msg);
  }

  isEmpty(value) {
    return value == null || value.length === 0;
  }
}
