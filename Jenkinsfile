pipeline {
    agent any

    parameters {
        string(name: 'BRANCH', defaultValue: '', description: 'Branch to build.')
        booleanParam(name: 'DEPLOY', defaultValue: true, description: 'Deploy to repository.')
        string(name: 'MAVEN_ARGS', defaultValue: '', description: 'Extra Maven arguments.')
    }

    stages {
        stage('Checkout Branch') {
            when {
                expression { params.BRANCH.trim() != "" }
            }
            steps {
                checkout scm: [
                    $class: 'GitSCM',
                    branches: [[name: '*/' + BRANCH]],
                    // userRemoteConfigs: scm.userRemoteConfigs
                ]
                // checkout scm is not returning the correct values (bug)
                // as an alternative we get the branch from the parameters (see ahead)
                // https://issues.jenkins-ci.org/browse/JENKINS-45489
                // https://issues.jenkins-ci.org/browse/JENKINS-53346
            }
        }
        stage('Build And Deploy') {
            steps {
                script {
                    currentBuild.keepLog = true
                    MVN_GOAL = 'verify'
                    if (params.DEPLOY) {
                        MVN_GOAL = 'deploy'
                    }
                    MVN_ARGS = ''
                    if (params.MAVEN_ARGS) {
                        MVN_ARGS = ' ' + params.MAVEN_ARGS
                    }
                    POM_VERSION = readMavenPom().getVersion()
                    GIT_COMMIT = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    // GIT_BRANCH = sh(returnStdout: true, script: 'git name-rev --name-only HEAD').trim()
                    // git name-rev can output the "wrong" value (multiple branches)
                    // using scm.branches[0].name instead of env.GIT_BRANCH to get a clean value (no origin/)
                    GIT_BRANCH = params.BRANCH.trim() != "" ? params.BRANCH.trim() : scm.branches[0].name
                    currentBuild.displayName = "#${BUILD_NUMBER} (${POM_VERSION}, ${GIT_BRANCH}, ${GIT_COMMIT})"
                }
                withMaven(
                    maven: 'mvn3',
                    mavenSettingsConfig: 'maven-settings'
                ) {
                    sh "mvn -B${MVN_ARGS} clean ${MVN_GOAL}"
                }
            }
        }
    }
}
