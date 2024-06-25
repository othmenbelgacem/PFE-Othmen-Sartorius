import { Picture } from "./picture.model";

export class Operator {
    public userUuid: string;
    public userFirstName: string;
    public userLastName: string;
    public userProfilePicture: Picture;
    public alreadyRequestedForTheTraining: boolean;

    constructor(
        userUuid: string,
        userFirstName: string,
        userLastName: string,
        // userProfilePicture: Picture,
        alreadyRequestedForTheTraining: boolean,
    ) {
        this.userUuid = userUuid;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        // this.userProfilePicture = userProfilePicture,
        this.alreadyRequestedForTheTraining = alreadyRequestedForTheTraining;
    }
}