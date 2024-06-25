import {Component, OnInit, TemplateRef, ViewChild} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal,} from "@ng-bootstrap/ng-bootstrap";
import {ManagerTraining} from "app/model/training/training.model";
import {Operator} from "app/model/user/operator.model";
import {ManagerTrainingService} from "app/service/manager/manager-training.service";
import {UtilsService} from "app/service/utils.service";
import {ToastrService} from "ngx-toastr";
import {Observable} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: "manager-training-management",
  templateUrl: "./manager-training-management.component.html",
  styleUrls: ["./manager-training-management.component.scss"],
})
export class ManagerTrainingManagementComponent implements OnInit {
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
  apiUrl = UtilsService.BASE_API_URL + "api";
  searchText: string;
  message: string | null = null;

  constructor(
    private trainingService: ManagerTrainingService,
    private modal: NgbModal,
    private toastr: ToastrService,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.loadTrainings();
  }
  search(event) {
    this.loadTrainings();
  }
  loadTrainings() {
    this.trainingService
      .getAllManagerTrainings(this.searchText)
      .subscribe((res) => {
        this.trainings = res;
        this.trainingModalOpened = null;
      });
  }

  loadSubTrainings(trainingId: string) {
    this.trainingService
      .getAllManagerSubTrainings(trainingId)
      .subscribe((res) => {
        this.trainings = res;
        this.trainingModalOpened = null;
      });
  }

  backToTrainings() {
    this.parentTraining = null;
    this.managingSubTrainings = false;
    this.loadTrainings();
  }

  actionEmitted(event) {
    if (event.action == this.CONSULT_SUB_TRAININGS) {
      this.parentTraining = this.trainings.find(
        (tr) => tr.uuid == event.trainingId
      );
      this.router.navigateByUrl(`trainings/${this.parentTraining.uuid}`);
    } else if (event.action == this.ASSIGN_OPERATORS) {
      this.trainingService
        .getAssignedOperatorsToTraining(event.trainingId)
        .subscribe((res) => (this.operators = res));

      this.trainingModalOpened = this.trainings.find(
        (tr) => tr.uuid == event.trainingId
      );
      this.modal.open(this.operatorsModal, this.modalOptions);
    }
  }

  onCheckOperator(operator: Operator) {
    if (!operator.alreadyRequestedForTheTraining) {
      const trainingLabel = this.trainingModalOpened?.label;
      let assignments$: Observable<any>;
      let isSubTraining: boolean;
      if (this.parentTraining && this.managingSubTrainings) {
        assignments$ = this.trainingService.assignOperatorToSubTraining({
          subTrainingId: this.trainingModalOpened?.uuid,
          operatorId: operator.userUuid,
        });

        assignments$ = this.trainingService.assignOperatorToSubTraining({
          subTrainingId: this.trainingModalOpened?.uuid,
          operatorId: operator.userUuid,
        });
        isSubTraining = true;
      } else {
        assignments$ = this.trainingService.assignOperatorToTraining({
          trainingId: this.trainingModalOpened?.uuid,
          operatorId: operator.userUuid,
        });
        isSubTraining = false;
      }

      assignments$.subscribe(
        (res) => {
          this.message = 'Formation assignée avec succès';
          this.operators = this.operators?.map((op) => {
            if (op.userUuid == operator.userUuid) {
              return {
                ...op,
                alreadyRequestedForTheTraining: true,
              };
            } else return op;
          });

          this.showOperatorToast(
            "success",
            operator.userFirstName + " " + operator.userLastName,
            trainingLabel,
            isSubTraining
          );
        },
        (error: HttpErrorResponse) => {
          if (error.status === 409) {
            this.message = "L'opérateur a déjà fait cette formation et la durée de vie de la formation est encore valide";
          } else {
            this.message = "Erreur lors de l'assignation de la formation";
          }
          this.showOperatorToast(
            "error",
            operator.userFirstName + " " + operator.userLastName,
            trainingLabel,
            isSubTraining
          );
        }
      );
    }
  }

  showOperatorToast(
    type: string,
    operatorName: string,
    trainingLabel: string,
    isSubTraining: boolean
  ) {
    const sousPrefix = isSubTraining ? "sous-" : "";
    switch (type) {
      case "success": {
        this.toastr.success(
          `L'opérateur ${operatorName} a été ajouté avec succès à la ${sousPrefix}formation ${trainingLabel}`
        );
        break;
      }
      case "error": {
        this.toastr.error(
          `Erreur lors de l'ajout de l'opérateur ${operatorName} à la ${sousPrefix}formation ${trainingLabel}`
        );
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
}
