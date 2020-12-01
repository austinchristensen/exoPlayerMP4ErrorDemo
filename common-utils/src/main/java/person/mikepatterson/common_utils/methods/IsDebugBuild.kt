package person.mikepatterson.common_utils.methods

import person.mikepatterson.common_utils.BuildConfig

/**
 * This is ok as long as this module is packaged as debug for debug builds and release for release builds
 * Advantage is that I avoid requiring the god object, [Context]
 * https://medium.com/@elye.project/checking-debug-build-the-right-way-d12da1098120
 */
fun isDebugBuild(): Boolean {
    return BuildConfig.DEBUG
}
