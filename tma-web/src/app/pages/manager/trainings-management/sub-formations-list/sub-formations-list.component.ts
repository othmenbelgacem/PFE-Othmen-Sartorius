import {Component, OnInit, TemplateRef, ViewChild} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ManagerTraining} from "app/model/training/training.model";
import {Operator} from "app/model/user/operator.model";
import {ManagerTrainingService} from "app/service/manager/manager-training.service";
import {TrainingTypeService} from "app/service/training-type/training-type.service";
import {UtilsService} from "app/service/utils.service";
import {ToastrService} from "ngx-toastr";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: "sub-formations-list",
  templateUrl: "./sub-formations-list.component.html",
  styleUrls: ["./sub-formations-list.component.scss"],
})
export class SubFormationsListComponent implements OnInit {
  @ViewChild("operatorsModal", { static: true }) operatorsModal:
    | TemplateRef<any>
    | undefined;

  public trainings: ManagerTraining[];
  public readonly ASSIGN_OPERATORS = "Assigner des opérateurs";
  public readonly CONSULT_SUB_TRAININGS = "Consulter les sous-formations";
  public managingSubTrainings: boolean = false; // initially false when managing trainings, Switch to -> true when managing sub-trainings.
  public parentTraining: ManagerTraining;
  public modalTitle: string = `Assigner des opérateurs`;
  private readonly modalOptions: {
    backdrop: "static";
    size: "sm";
    centered: true;
  };
  public operators: Operator[];
  private trainingModalOpened: ManagerTraining | null;
  trainingTypeUuid: string = "";
  apiUrl = UtilsService.BASE_API_URL + "api";

  constructor(
    private service: ManagerTrainingService,
    private trainingService: TrainingTypeService,
    private modal: NgbModal,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.trainingTypeUuid = this.activatedRoute.snapshot.paramMap.get("uuid");
    this.trainingService.getByUUId(this.trainingTypeUuid).subscribe((res) => {
      this.parentTraining = res;
    });
    this.loadSubTrainings();
  }

  loadSubTrainings() {
    this.service
      .getAllManagerSubTrainings(this.trainingTypeUuid)
      .subscribe((res) => {
        this.trainings = res;
        this.trainingModalOpened = null;
      });
  }

  backToTrainings() {
    this.router.navigateByUrl("trainings");
  }

  onCheckOperator(operator: Operator) {
    if (!operator.alreadyRequestedForTheTraining) {
      const trainingLabel = this.trainingModalOpened?.label;
      this.service.assignOperatorToSubTraining({
        subTrainingId: this.trainingModalOpened?.uuid,
        operatorId: operator.userUuid,
      }).subscribe(
        () => {
          this.showOperatorToast(
            "success",
            operator.userFirstName + " " + operator.userLastName,
            trainingLabel
          );
          this.operators = this.operators?.map((op) => {
            if (op.userUuid == operator.userUuid) {
              return {
                ...op,
                alreadyRequestedForTheTraining: true,
              };
            } else return op;
          });

        },
        (error: HttpErrorResponse) => {
          if (error.status === 409) {
            this.showOperatorToast(
              "error",
              operator.userFirstName + " " + operator.userLastName,
              trainingLabel,
              "L'opérateur a déjà fait cette formation et la durée de vie de la formation est encore valide"
            );
          } else {
            this.showOperatorToast(
              "error",
              operator.userFirstName + " " + operator.userLastName,
              trainingLabel,
              "Erreur lors de l'assignation de la formation"
            );
          }
        }
      );

    }
  }

  showOperatorToast(type: string, operatorName: string, trainingLabel: string, message?: string) {
    const finalMessage = message || `L'opérateur ${operatorName} a été ajouté avec succès à la sous-formation ${trainingLabel}`;
    switch (type) {
      case "success": {
        this.toastr.success(finalMessage);
        break;
      }
      case "error": {
        this.toastr.error(finalMessage);
        break;
      }
      default:
        break;
    }
  }

  closeModal() {
    this.trainingModalOpened = null;
    this.operators = null;
    this.modal.dismissAll();
  }

  actionEmitted(event) {
    this.service
      .getAssignedOperatorsToSubTraining(event.trainingId)
      .subscribe((res) => (this.operators = res));

    this.trainingModalOpened = this.trainings.find(
      (tr) => tr.uuid == event.trainingId
    );
    this.modal.open(this.operatorsModal, this.modalOptions);
  }
}
