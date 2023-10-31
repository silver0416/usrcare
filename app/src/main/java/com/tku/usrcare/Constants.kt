package com.tku.usrcare

object Constants {
    private const val HOST = "api.tkuusraicare.org"
    private const val VERSION = "v1"
    const val BASE_URL = "https://$HOST/$VERSION/"


    const val TEST_URL = "token"
    const val LOGIN_URL = "authentication"
    const val SCALE_LIST_URL = "mental_record"
    const val SCALE_URL = "mental_record/{id}"
    const val EMAIL_CHECk_URL = "registration/email/{email}"
    const val EMAIL_VERIFY_URL = "registration/email"
    const val USERNAME_CHECK_URL = "registration/username/{username}"
    const val REGISTER_URL = "registration"
    const val RETURN_SHEET_URL = "mental_record/{id}"
    const val SALT_URL = "salt/{username}"
    const val GET_EMAIL_ACCOUNT_LIST_URL = "forgot/email/{email}"
    const val MOOD_URL = "mood/{mood}"
    const val POINTS_URL = "points"
    const val POINTS_DEDUCTION_URL = "points/deduction"
    const val RESET_PASSWORD_URL = "password/reset"
}