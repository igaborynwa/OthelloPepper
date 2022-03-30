package hu.netlife.othellopepper.grpc

object MessageTypes {
    const val ERROR = "ERROR"
    const val FLAP_STATE_CHANGE = "FLAP_STATE_CHANGE"
    const val SAY = "SAY"
    const val ANIMATION = "ANIMATION"
    const val AUDIO_PLAY = "AUDIO_PLAY"
    const val IMAGE_PLAY = "IMAGE_PLAY"
    const val VIDEO_PLAY = "VIDEO_PLAY"
    const val WEBSITE_PLAY = "WEBSITE_PLAY"
    const val HUMAN_AROUND = "HUMAN_AROUND"
    const val TOUCH_SENSOR = "TOUCH_SENSOR"
    const val MASK_DETECTION = "MASK_DETECTION"
    const val MAP_READ_STATUS = "MAP_READ_STATUS"
    const val LOCALIZATION_STATUS = "LOCALIZATION_STATUS"
    const val MAPPING_STATUS = "MAPPING_STATUS"
    const val GO_TO = "GO_TO"
    const val GRPC_CALLS_ALLOWED = "GRPC_CALLS_ALLOWED"
    const val ENFORCE_TABLET_REACHABILITY = "ENFORCE_TABLET_REACHABILITY"
}