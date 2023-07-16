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
    const val AUTHANTICATION_URL = "authentication"
}