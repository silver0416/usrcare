package com.tku.usrcare

object Constants {
    private const val HOST = "api.tkuusraicare.org"
    private const val VERSION = "v1"
    const val BASE_URL = "https://$HOST/$VERSION/"

    /**
     * 測試連線
     */
    const val TEST_URL = "test/token"
    const val LOGIN_URL = "registration"
    const val AUTHENTICATION_URL = "authentication"
    const val SCALE_LIST_URL = "mental_record"
    const val SCALE_URL = "mental_record/{id}"
    const val EMAIL_CHECk_URL = "registration/email/{email}"
    const val EMAIL_VERIFY_URL = "registration/email"
    const val USERNAME_CHECK_URL = "registration/username/{username}"
    const val REGISTER_URL = "registration"
}