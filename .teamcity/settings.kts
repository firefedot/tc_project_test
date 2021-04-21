import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.2"

project {

    buildType(BuildTest)
}

object BuildTest : BuildType({
    name = "build_test"

    steps {
        script {
            scriptContent = """
                echo 'start test'
                pass='%system.teamcity.auth.password%'
                echo ${'$'}pass
                echo "%teamcity.build.id% %teamcity.build.triggeredBy.username%:%system.teamcity.auth.password% %system.teamcity.auth.password%"
                curl --user %teamcity.build.triggeredBy.username%:%system.teamcity.auth.userId% -X POST --data "a:%teamcity.build.triggeredBy.username%" http://192.168.88.253:8111/app/rest/builds/id:%teamcity.build.id%/tags/ --header "Content-Type: text/plain"
            """.trimIndent()
        }
    }
})
