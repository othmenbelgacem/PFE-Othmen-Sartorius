import { SessionManagementComponent } from "./../../pages/session-management/session-management.component";
import { UsersManagementComponent } from "./../../pages/users-management/users-management.component";
import { Routes } from "@angular/router";

import { DashboardComponent } from "../../pages/dashboard/dashboard.component";
import { TeamsManagmentComponent } from "app/pages/teams-managment/teams-managment.component";
import { AddMembersComponent } from "app/pages/teams-managment/add-members/add-members.component";
import { NewPasswordComponent } from "app/pages/auth/new-password/new-password.component";
import { UserProfileComponent } from "app/pages/user-profile/user-profile.component";
import { TrainersManagementComponent } from "app/pages/trainers-management/trainers-management.component";
import { TrainingManagementComponent } from "app/pages/training-management/training-management.component";
import { SubTrainingManagementComponent } from "app/pages/training-management/sub-training-management/sub-training-management.component";
import { ManagerTrainingManagementComponent } from "app/pages/manager/trainings-management/manager-training-management.component";
import { SubFormationsListComponent } from "app/pages/manager/trainings-management/sub-formations-list/sub-formations-list.component";
import { TrainingsRequestsComponent } from "app/pages/admin-user/trainings-requests/trainings-requests.component";
import { ManagerTrainingRequestComponent } from "app/pages/manager/manager-training-request/manager-training-request.component";
import { SessionPresencesComponent } from "app/pages/trainer/session-presences/session-presences.component";
import { PresenceSheetComponent } from "app/pages/trainer/presence-sheet/presence-sheet.component";

export const AdminLayoutRoutes: Routes = [
  { path: "dashboard", component: DashboardComponent },
  { path: "users-management", component: UsersManagementComponent },
  { path: "trainers-management", component: TrainersManagementComponent },
  { path: "teams-management", component: TeamsManagmentComponent },
  { path: "team-members/:uuid", component: AddMembersComponent },
  { path: "training-management", component: TrainingManagementComponent },
  {
    path: "sub-training-management/:uuid",
    component: SubTrainingManagementComponent,
  },
  {
    path: "trainings",
    component: ManagerTrainingManagementComponent,
  },
  {
    path: "trainings/:uuid",
    component: SubFormationsListComponent,
  },
  {
    path: "trainings-requests",
    component: TrainingsRequestsComponent,
  },
  {
    path: "sessions",
    component: SessionManagementComponent,
  },
  {
    path: "session-presences/:uuid",
    component: SessionPresencesComponent,
  },
  {
    path: "presence-sheet/:uuid/:date",
    component: PresenceSheetComponent,
  },
  {
    path: "my-trainings-requests",
    component: ManagerTrainingRequestComponent,
  },
  {
    path: "reset-password",
    component: NewPasswordComponent,
  },
  {
    path: "user-profile",
    component: UserProfileComponent,
  },
];
