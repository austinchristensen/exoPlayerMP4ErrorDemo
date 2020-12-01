package tests

// would like to use mockk but not familiar and I'm already slow at writing unit tests and setting up gradle
object TestingDependencies {

    const val junit = "junit:junit:${TestingVersions.junit}"
    const val koin = "org.koin:koin-test:${Versions.koin}"
    const val mockk = "io.mockk:mockk:${TestingVersions.mockk}"
}
