import { Component, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ToastrService } from "ngx-toastr";

import { TrainingTypeRequest } from "app/model/training-type-request.model";
import { TrainingSubTypeRequest } from "app/model/training-sub-type-request.model";
import { TrainingTypeService } from "app/service/training-type/training-type.service";
import { TrainerService } from "app/service/trainer/trainer.service";
import { TrainingSubTypeDetails } from "app/model/training-sub-type-details.model";
import { TrainerDto } from "app/model/trainer-dto.model";
import Swal from "sweetalert2";

@Component({
  selector: "training-management",
  templateUrl: "./training-management.component.html",
  styleUrls: ["./training-management.component.scss"],
})
export class TrainingManagementComponent implements OnInit {
  @ViewChild("modalContent", { static: true }) modalContent:
    | TemplateRef<any>
    | undefined;
  @ViewChild("subTypeModalContent", { static: true }) subTypeModalContent:
    | TemplateRef<any>
    | undefined;

  addOrUpdateMode: number = -1;
  modalTitle = "";
  trainingTypeLabel = "";
  trainingTypeUuid = "";
  trainingTypeRequest: any;
  trainingSubTypeRequest: any;

  page: number = 1;
  pageSize: number = 5;
  collectionSize: number = 0;
  trainingTypeList: TrainingTypeRequest[] = [];
  trainingSubTypeList: TrainingSubTypeDetails[] = [];
  trainerList: TrainerDto[] = [];
  selectedTrainers: TrainerDto[] = [];
  trainerUuids: string[] = [];
  searchText: string;
  constructor(
    private router: Router,
    private modal: NgbModal,
    private trainingTypeService: TrainingTypeService,
    private toastr: ToastrService,
    private trainerService: TrainerService
  ) {}

  ngOnInit(): void {
    this.getAllTrainingTypes();
    
   
  }

  onAdd() {
    this.retrieveAllTrainers();
    this.modalTitle = "Ajouter une nouvelle formation";
    this.addOrUpdateMode = 0;
    this.trainingTypeRequest = new TrainingTypeRequest();
    this.selectedTrainers = [];
    this.modal.open(this.modalContent);
  }
  search(event) {
    this.page = 1;
    this.getAllTrainingTypes();
  }
  onAddTrainingSubType(trainingType) {
    this.retrieveAllTrainers();
    this.modalTitle = "Ajouter un sous formation";
    this.trainingTypeRequest = trainingType;
    this.trainingSubTypeRequest = new TrainingSubTypeRequest();
    this.selectedTrainers = [];
    this.modal.open(this.subTypeModalContent);
  }

  onViewTrainingSubType(trainingType) {
    this.router.navigateByUrl(
      `/sub-training-management/${trainingType.trainingTypeUuid}`
    );
  }

  onSaveSubTraining() {
    if (
      !this.isEmpty(this.trainingSubTypeRequest.label) &&
      !this.isEmpty(this.trainingSubTypeRequest.hourDuration) &&
      !this.isEmpty(this.trainingSubTypeRequest.lifeDuration)
    ) {
      this.trainingSubTypeRequest.trainingTypeUuid =
        this.trainingTypeRequest.trainingTypeUuid;
      this.trainingSubTypeRequest.trainerUuids = this.selectedTrainers.map(
        (trainer) => trainer.userUuid
      );
      this.trainingTypeService
        .saveSubTraining(this.trainingSubTypeRequest)
        .subscribe((res) => {
          this.modal.dismissAll();
          this.getAllTrainingTypes();
          this.showSuccess("Sous-formation ajoutée avec succès");
        });
    }
  }

  onSaveTrainingType() {
    if (!this.isEmpty(this.trainingTypeRequest.label)) {
      this.trainingTypeRequest.trainerUuids = this.selectedTrainers.map(
        (trainer) => trainer.userUuid
      );
      this.trainingTypeService
        .saveTrainingType(this.trainingTypeRequest)
        .subscribe((res) => {
          this.modal.dismissAll();
          this.getAllTrainingTypes();
          if (this.addOrUpdateMode === 0) {
            this.showSuccess("Formation ajoutée avec succès");
          } else if (this.addOrUpdateMode === 1) {
            this.showSuccess("Formation modifiée avec succès");
          }
        });
    }
  }

  getAllTrainingTypes() {
    this.retrieveAllTrainers();
    this.trainingTypeService
      .getAllTrainingTypes(this.page - 1, this.pageSize, this.searchText)
      .subscribe((res: any) => {
        this.trainingTypeList = res.items;
        this.collectionSize = res.count;
      });
  }

  getTrainingSubTypesByTrainingType() {
    this.trainingTypeService
      .getSubTrainingTypesByTrainingType(this.trainingTypeUuid, this.searchText)
      .subscribe((subTrainingTypes) => {
        this.trainingSubTypeList = subTrainingTypes;
      });
  }

  onPageChange(event) {
    this.page = event;
    this.getAllTrainingTypes();
  }

  onEdit(trainingType) {
    this.retrieveAllTrainers();
    this.modalTitle = "Modifier Formation";
    this.addOrUpdateMode = 1;
    this.trainingTypeRequest = trainingType;
    this.loadTrainersByTrainingType();
    this.modal.open(this.modalContent);
  }

  onDeleteTrainingType(trainingTypeUuid: string) {
    Swal.fire({
      text: "Voulez-vous supprimer cette formation ?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Confirmer",
      cancelButtonText: "Annuler",
    }).then((result) => {
      if (result.value) {
        this.trainingTypeService
          .deleteTrainingType(trainingTypeUuid)
          .subscribe(() => {
            this.getAllTrainingTypes();
            this.showSuccess("Formation supprimé avec succès");
          });
      }
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
            this.getAllTrainingTypes();
            this.showSuccess("Sous-formation supprimée avec succès");
          });
      }
    });
  }

  retrieveAllTrainers(): void {
    this.trainerService
      .retrieveAllTrainers()
      .subscribe((trainers: TrainerDto[]) => {
        this.trainerList = trainers;
        console.log('Trainers fetched:', this.trainerList); // Log inside the subscribe block
      });
  }
  

  loadTrainersByTrainingType() {
    this.trainerService
      .getTrainersByTrainingTypeUuid(this.trainingTypeRequest.trainingTypeUuid)
      .subscribe((data: TrainerDto[]) => {
        this.selectedTrainers = data;
      });
  }

  showSuccess(msg) {
    this.toastr.success(msg);
  }

  isEmpty(value) {
    return value == null || value.length === 0;
  }
}
