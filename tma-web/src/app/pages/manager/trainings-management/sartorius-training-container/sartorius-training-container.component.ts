import { Component, Input, OnInit, Output, EventEmitter } from "@angular/core";
import { TrainerDetailsModel } from "app/model/trainer-details.model";
import { UtilsService } from "app/service/utils.service";

@Component({
  selector: "st-training-component",
  templateUrl: "./sartorius-training-container.component.html",
  styleUrls: ["./sartorius-training-container.component.scss"],
})
export class ST_TrainingContainerComponent implements OnInit {
  public readonly ASSIGN_OPERATORS = "Assigner des opérateurs";
  public readonly CONSULT_SUB_TRAININGS = "Consulter les sous-formations";
  @Input() trainingId;
  @Input() title: string;
  @Input() description: string;
  @Input() actionName: string;
  @Input() duration: number;
  @Input() lifeDuration: number;
  @Input() trainersList: TrainerDetailsModel[];
  @Output() actionEmit: EventEmitter<any> = new EventEmitter<any>();

  public apiUrl: string = UtilsService.BASE_API_URL.slice(0, -1);

  ngOnInit(): void {}

  onAction() {
    this.actionEmit.emit({
      action: this.actionName,
      trainingId: this.trainingId,
    });
  }
}
