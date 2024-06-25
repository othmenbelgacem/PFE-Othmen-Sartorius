package com.sartorius.tma.utils;

public class Constants {

  public static final String MAIL_SUBJECT_USER_WELCOME = "Bienvenue à Free Academy";

  public final static String CODE = "UUID";

  public final static String FRONT_BASE_URL = "frontBaseUrl";
  public final static String NOT_FOUND = "NOT_FOUND";
  public final static String BACKEND_BASE_URL = "backendBaseUrl";

  // Default platform timezone
  public static final String DEFAULT_TIMEZONE = "Africa/Tunis";

  public static final String MAIL_SUBJECT_USER_RESET = "Réinitialiser le mot de passe";

  //public static final String MAIL_SUBJECT_EXPERT_NEW_ANNOUNCEMENT = "Une nouvelle annonce dans votre domaine d´expertise";
  public static final String MAIL_SUBJECT_EXPERT_NEW_ANNOUNCEMENT = "Une nouvelle annonce publiée";
  public static final String MAIL_SUBJECT_USER_PUBLISHED_ANNOUNCEMENT = "Annonce publiée";

  public static final String MAIL_SUBJECT_EXPERT_OFFER_ACCEPTED = "Offre acceptée par l´annonceur";
  public static final String MAIL_SUBJECT_USER_NEW_OFFER = "Offre récue pour votre annonce";
  public static final String MAIL_SUBJECT_EXPERT_OFFER_DECLINED = "Offre réfusée par l´annonceur";

  public static final String MAIL_SUBJECT_EXPERT_OFFER_DECLINED_AFTER_ACCEPTED = "Offre réfusée par l´annonceur";
  public static final String MAIL_SUBJECT_EXPERT_VALIDATION_ACCEPTED = "Votre compte est vérifié et publié";
  public static final String MAIL_SUBJECT_EXPERT_VALIDATION_DECLINE = "Validation refusée";

  public static final String MAIL_SUBJECT_ADMIN_CREATE_PROFESSIONAL_ACCOUNT = "Un nouveau compte professionnel vient d´etre crée";
  public static final String MAIL_SUBJECT_ADMIN_VALIDATE_ACCOUNT_REQUEST = "Une nouvelle demande de validation de compte";
  public static final String CREATION = "creation";
  public static final String VALIDATION = "validation";
  public static final String MAIL_SUBJECT_USER_ANNOUNCEMENT_REFUSED = "Annonce réfusée par l´administrateur";
  public static final String MAIL_SUBJECT_NOTIF_ADMIN_NEW_CONTACT_MESSAGE="Nouveau message réçu";

  public static final String MAIL_SUBJECT_USER_OFFER_ACCEPTED = "Paiement ouvert pour votre annonce";
  public static final String MAIL_SUBJECT_USER_OFFER_NOT_ACCEPTED = "Offre refusée par l'enseignant";

  public static final String MAIL_SUBJECT_EXPERT_PAYMENT_VALID = "Commencez à préparer votre prestation!";
  public static final String MAIL_SUBJECT_USER_PAYMENT_VALID = "Paiement validé";
  public static final String MAIL_SUBJECT_USER_START_MEET = "Réunion commencée";
  public static final String MAIL_SUBJECT_EXPERT_PRESTATION_VALID = "Prestation validée";
  public static final String MAIL_SUBJECT_EXPERT_PRESTATION_NOT_VALID = "Prestation refusée";

  public static final String MAIL_SUBJECT_EXPERT_PAYMENT_FEE_VALID = "Paiement validé";
  public static final String MAIL_SUBJECT_EXPERT_PAYMENT_FEE_NOT_VALID = "Paiement non validé";
  
  public static final String MAIL_SUBJECT_EXPERT_ASSIGNMENT_TO_COURSE = "Vous êtes assignés à une nouvelle prestation";
  public static final String MAIL_SUBJECT_USER_ASSIGNMENT_TO_COURSE = "Un enseignant est disponible pour vous";


	public static final String OK = "OK";
	public static final String CONFLICT = "CONFLICT";
	public static final String PROMOTIONAL_CODE_NO_VALID = "PROMOTIONAL_CODE_NO_VALID";
  public static final String ALREADYDONE = "L'opérateur a déjà fait cette formation et la durée de vie de la formation est encore valide";
// Max hours of a study package
	public static final int STUDY_PACKAGE_MAX_HOURS = 18;

  private Constants() {
  }
}
