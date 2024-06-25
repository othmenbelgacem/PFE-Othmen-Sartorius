import { HttpErrorResponse } from "@angular/common/http";
import { Component, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { MediaContext } from "app/enumeration/media-context";
import { RoleCode } from "app/enumeration/role-code";
import { TrainerDetailsModel } from "app/model/trainer-details.model";
import { UserDetailsModel } from "app/model/user-details.model";
import { FileService } from "app/service/file/file.service";
import { TrainerService } from "app/service/trainer/trainer.service";
import { UserService } from "app/service/user/user.service";
import { UtilsService } from "app/service/utils.service";
import { ToastrService } from "ngx-toastr";
import Swal from "sweetalert2";

@Component({
  selector: "trainers-management",
  templateUrl: "./trainers-management.component.html",
  styleUrls: ["./trainers-management.component.scss"],
})
export class TrainersManagementComponent implements OnInit {
  @ViewChild("modalContent", { static: true }) modalContent:
    | TemplateRef<any>
    | undefined;

  accountTypes = Object.keys(RoleCode).map((key) => ({
    key,
    name: Object(RoleCode)[key],
  }));
  urlUserPhoto: any = null;
  userPhoto: any;
  userRequest: TrainerDetailsModel = new TrainerDetailsModel();
  userList: any[] = [];
  addOrUpdateMode: number = -1;
  modalTitle = "";

  page: number = 1;
  pageSize: number = 5;
  collectionSize: number = 0;
  userType: RoleCode = null;
  constructor(
    private modal: NgbModal,
    private trainerService: TrainerService,
    private toastr: ToastrService,
    private fileService: FileService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getAllUser();
  }

  onAdd() {
    this.modalTitle = "Ajouter un nouveau formateur";
    this.addOrUpdateMode = 0;
    this.userRequest = new UserDetailsModel();
    this.modal.open(this.modalContent);
  }
  getAllUser() {
    this.trainerService
      .getAllUsers(null, this.pageSize, this.page - 1)
      .subscribe((res: any) => {
        this.userList = res.items;
        this.collectionSize = res.count;
        this.userList.forEach((user) => {
          if (
            user.userProfilePicture != null &&
            user.userProfilePicture.mediaUrl
          ) {
            user.userProfilePicture.mediaUrl =
              UtilsService.BASE_API_URL +
              "api" +
              user.userProfilePicture.mediaUrl;
          }
        });
      });
  }
  onSelectFile(event: any) {
    if (event.target.files && event.target.files[0]) {
      window.URL = window.URL || window.webkitURL;
      const img = new Image();
      img.src = window.URL.createObjectURL(event.target.files[0]);
      const reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]); // read file as data url
      reader.onload = (e) => {
        window.URL.revokeObjectURL(img.src);
        if (e.target) {
          this.userPhoto = event.target.files[0];
          this.urlUserPhoto = e.target.result;
        }
      };
    } else {
      this.urlUserPhoto = null;
    }
  }

  onSaveUser() {
    if (
      !this.isEmpty(this.userRequest.userEmail) &&
      !this.isEmpty(this.userRequest.userFirstName) &&
      !this.isEmpty(this.userRequest.userLastName)
    ) {
      const formdata = new FormData();
      formdata.append("identifier",this.userRequest.identifier);
      formdata.append("userEmail", this.userRequest.userEmail);
      formdata.append("userPhoneNumber", this.userRequest.userPhoneNumber);
      formdata.append("userFirstName", this.userRequest.userFirstName);
      formdata.append("userLastName", this.userRequest.userLastName);
      formdata.append("about", this.userRequest.about);
      if (this.userPhoto) {
        formdata.append("profilePicture", this.userPhoto);
      }
      if (this.addOrUpdateMode == 0) {
        this.trainerService.saveUser(formdata).subscribe((res) => {
          this.modal.dismissAll();
          this.getAllUser();
          this.showSuccess("Employé ajouté avec succès");
        });
      } else if (this.addOrUpdateMode == 1) {
        formdata.append("userUuid", this.userRequest.userUuid);
        this.trainerService.updateUser(formdata).subscribe((res) => {
          if (
            this.userRequest.userProfilePicture != null &&
            this.urlUserPhoto === null
          ) {
            this.removePicture(
              this.userRequest.userUuid,
              MediaContext[MediaContext.PICTURE_PROFIL]
            );
          }
          this.getAllUser();
          this.modal.dismissAll();
          this.showSuccess("Employé modifié avec succès");
        });
      }
    }
  }

  onEdit(user) {
    this.urlUserPhoto = null;
    this.modalTitle = "Modifier un formateur";
    this.addOrUpdateMode = 1;
    this.userRequest = user;
    if (user.userProfilePicture != null) {
      this.urlUserPhoto =
        //UtilsService.BASE_API_URL +
        //"api" +
        this.userRequest.userProfilePicture.mediaUrl;
    }
    this.modal.open(this.modalContent);
  }

  deletePhoto() {
    this.urlUserPhoto = null;
  }

  showSuccess(msg) {
    this.toastr.success(msg);
  }

  onDelete(user) {
    Swal.fire({
      text: "Voulez vous supprimer cet employé ?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Confirmer",
      cancelButtonText: "Annuler",
    }).then((result) => {
      if (result.value) {
        this.trainerService.deleteUser(user.userUuid).subscribe(
          (response) => {
            this.getAllUser();
            this.showSuccess("Formateur supprimé avec succès");
          },
          (error: HttpErrorResponse) => {
            this.showError(
              "La suppression de ce formateur est non aboutie. Il est fort probable qu'il est liée à une formation ou une session de formation."
            );
          }
        );
      }
    });
  }
  showError(msg) {
    this.toastr.error(msg);
  }
  onPageChange(event) {
    this.page = event;
    this.getAllUser();
  }

  isEmpty(value) {
    return value == null || value.length === 0;
  }

  removePicture(uuid: string, context: any) {
    this.fileService.deleteProfilePhoto(uuid, context).subscribe(
      (response) => {
        this.getAllUser();
      },
      (error) => {}
    );
  }
}