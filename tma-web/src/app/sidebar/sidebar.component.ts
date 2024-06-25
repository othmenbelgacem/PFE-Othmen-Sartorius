import { Component, OnInit } from "@angular/core";
import { UtilsService } from "app/service/utils.service";
import { TrainingRequestService } from "app/service/trainings-requests/trainings-requests.service";

export interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
  hidden: boolean;
  badge?: number;
}

export const ROUTES: RouteInfo[] = [
  {
    path: "/dashboard",
    title: "Dashboard",
    icon: "fa fa-home",
    class: "",
    hidden: false,
  },
  {
    path: "/users-management",
    title: "Fiches employés",
    icon: "fa fa-address-card",
    class: "",
    hidden: !UtilsService.isAdministrator(),
  },
  {
    path: "/teams-management",
    title: "Equipes",
    icon: "fa fa-group",
    class: "",
    hidden: !UtilsService.isAdministrator(),
  },
  {
    path: "/trainers-management",
    title: "Formateurs",
    icon: "fa fa-address-book-o",
    class: "",
    hidden: !UtilsService.isAdministrator(),
  },
  {
    path: "/training-management",
    title: "Formations",
    icon: "fa fa-graduation-cap",
    class: "",
    hidden: !UtilsService.isAdministrator(),
  },
   {
    path: "/trainings-requests",
    title: "Demandes des formations",
    icon: "fa fa-files-o",
    class: "",
    hidden: !UtilsService.isAdministrator(),
    badge: 0,
  },
  {
    path: "/trainings",
    title: "Formations",
    icon: "fa fa-graduation-cap",
    class: "",
    hidden: !UtilsService.isManager(),
  },
  {
    path: "/my-trainings-requests",
    title: "Mes Demandes des formations",
    icon: "fa fa-files-o",
    class: "",
    hidden: UtilsService.isAdministrator()|| UtilsService.isTrainer() || UtilsService.isCollaborator()
    
  },
  {
    path: "/sessions",
    title: "Sessions des formations",
    icon: "fa fa-graduation-cap",
    class: "",
    hidden: false,
  },
 
];

@Component({
  moduleId: module.id,
  selector: "sidebar-cmp",
  templateUrl: "sidebar.component.html",
})
export class SidebarComponent implements OnInit {
  public menuItems: any[];

  constructor(private trainingRequestService: TrainingRequestService) {}

  ngOnInit() {
    this.menuItems = ROUTES.filter((menuItem) => menuItem.hidden === false);
    this.loadRequestCount();
  }

  loadRequestCount() {
    this.trainingRequestService.getRequestedCount().subscribe(count => {
      const requestRoute = ROUTES.find(item => item.path === '/trainings-requests');
      if (requestRoute) {
        requestRoute.badge = count;
      }
    });
  }
}
