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
    const val OTP_URL = "otp"
    const val CHECKING_RECORD_URL = "record/checkin"
    const val HISTORY_STORY_URL = "history_story"
    const val VOCABULARY_URL = "vocabulary"
    const val CHEAT_URL = "points/cheat"
    const val MOOD_PUNCHER_URL = "mood/typewriter"
    const val GOOGLE_AUTH_URL = "authentication/oauth/google"
    const val LINE_AUTH_URL = "authentication/oauth/line"
    const val GOOGLE_OAUTH_REGISTER_URL = "registration/oauth/google"
    const val LINE_OAUTH_REGISTER_URL = "registration/oauth/line"
    const val GOOGLE_OAUTH_BIND_URL = "oauth/binding/google"
    const val LINE_OAUTH_BIND_URL = "oauth/binding/line"
    const val GOOGLE_OAUTH_REBIND_URL = "oauth/binding/replacement/google"
    const val LINE_OAUTH_REBIND_URL = "oauth/binding/replacement/line"
    const val OAUTH_CHECK_URL = "oauth/binding/inquiry"
    const val OAUTH_UNBIND_URL = "oauth/binding/cancelation/{oauth_type}"
    const val SPORT_VIDEO_UPLOAD_URL = "video/analysis/upload"
    const val REGISTRATION_URL = "device/registration"
    const val VIDEO_LIST_URL = "video/list"
    const val GAME_RECORD_URL = "game_record/web-based"
    const val STEPS_RECORD_URL = "pet_companion/pedometer"
    const val GET_USER_SETTING_URL = "user/config"
    const val POST_USER_SETTING_URL = "user/config/set"
    const val GET_SHOP_ITEMS_URL = "shop/items"
    const val GET_SHOP_PURCHASE_URL = "shop/purchase"
    const val GET_USER_INVENTORY_URL = "user/inventory"
    const val POST_USER_USE_ITEM_URL = "user/inventory/useitem"
}