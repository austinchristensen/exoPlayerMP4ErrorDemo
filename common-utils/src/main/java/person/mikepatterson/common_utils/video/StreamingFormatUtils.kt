package person.mikepatterson.common_utils.video

fun String.isValidVideoUrl(): Boolean {
    return isNotEmpty() && (isHlsUrl() || isDashUrl())
}

// a decent reason for moving "StreamingFormat" from models into here or a "definition" module
fun String.isHlsUrl(): Boolean {
    val domainAndPath = split("?")[0]
    return domainAndPath.endsWith("m3u") || domainAndPath.endsWith("m3u8")
}

fun String.isDashUrl(): Boolean {
    val domainAndPath = split("?")[0]
    return domainAndPath.endsWith("mpd")
}
