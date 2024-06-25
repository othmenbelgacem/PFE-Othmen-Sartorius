import { RoleCode } from "../enumeration/role-code";
import { Picture } from "./user/picture.model";
export class TrainerDetailsModel {
  userUuid: string;
  userEmail: string;
  userFirstName: string;
  userLastName: string;
  userPhoneNumber: string;
  userRole: RoleCode;
  userProfilePicture: any;
  about?: string;
  picture?: Picture;
  identifier : string;
}
