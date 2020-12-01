package person.mikepatterson.networking.api.config

import person.mikepatterson.common_utils.time.MidstUnit
import person.mikepatterson.common_utils.time.Seconds

data class NetworkConfiguration(val defaultTimeout: MidstUnit = Seconds(10))
