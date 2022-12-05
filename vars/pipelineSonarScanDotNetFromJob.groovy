#!/usr/bin/env groovy

/**
 * Библиотека CI/CD конвейера для типового dotnet сервиса продукта PlatformMD
 * @autor Ярослав Еремин
 * */

def call(Map config) {

  def serviceVersion

  pipeline {

    agent {

      label 'master'

    }

    triggers{

      cron('0 6 * * *')
    
    }

    options {

      ansiColor('xterm')

      buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))

      disableConcurrentBuilds()

      timestamps()

      timeout(time: 60, unit: 'MINUTES')

    }

    environment {

      serviceNameLowerCase = ""

      productNameLowerCase = ""

    }

    stages {

      stage("Sonar scan") {

        steps {

          script {

            // Заменяю на ims4 так, как сканирование платформенных проектов требует .net 6.0 и на текущий момент не реализовано в sonar
            def data = libraryResource "sonar/repos.yml"
            //def data = libraryResource "sonar/repos_ims4.yml"

            writeFile (file: 'repos.yml', text: data)

            def datas = readYaml file: 'repos.yml'

            datas.each {

              println "${it}"

              try {

                sh "mkdir -p ${it.service}"

                dir("${it.service}") {

                  git branch: "${it.branch}",
                          url: "${it.repo}"

                  def dataConfig = libraryResource "NuGet/NuGet-pmd-dev.Config"

                  if ("${it.product}" == "IMS4") {

                    dataConfig = libraryResource "NuGet/IMS4v1DEV.Nuget.Config"

                  }

                  writeFile (file: 'NuGet.Config', text: dataConfig)

									// if ("${it.product}" == "UTS") {
										
									// 	modifiedbranch = "${it.branch}".replace("/", "-")
										
									// 	withSonarQubeEnv('md-sonar.niaepnn.ru') {
										
									// 		sh """

									// 		dotnet /var/lib/jenkins/tools/hudson.plugins.sonar.MsBuildSQRunnerInstallation/5.8.0.52797_NET_Core_3.0/SonarScanner.MSBuild.dll begin \
									// 			/d:sonar.cs.opencover.reportsPaths=**/coverage.opencover.xml \
									// 			/n:${it.product}:${it.service}:${modifiedbranch} \
									// 			/k:${it.product}:${it.service}:${modifiedbranch}

									// 	"""
									// 	}
									// 	restoreDotnet(".", "NuGet.Config")
									// 	buildDotnet()
									// 	if ("${it.testpath}" == "" || "${it.testpath}" == "null" || "${it.testpath}" == null ) {

									// 		testDotnet()

									// 	} else {

									// 		testDotnet("${it.testpath}")
												
									// 	}

									// 	withSonarQubeEnv('md-sonar.niaepnn.ru') {

									// 		sh " dotnet /var/lib/jenkins/tools/hudson.plugins.sonar.MsBuildSQRunnerInstallation/5.8.0.52797_NET_Core_3.0/SonarScanner.MSBuild.dll end "

									// 	}
									// }else {

                  // withSonarQubeEnv('SonarQube ASE') {
                  withSonarQubeEnv('md-sonar.niaepnn.ru') {

                    sh """

                    dotnet /var/lib/jenkins/tools/hudson.plugins.sonar.MsBuildSQRunnerInstallation/5.8.0.52797_NET_Core_3.0/SonarScanner.MSBuild.dll begin \
                      /d:sonar.cs.opencover.reportsPaths=**/coverage.opencover.xml \
                      /n:${it.product}:${it.service}:${it.branch} \
                      /k:${it.product}:${it.service}:${it.branch}

                  """

                  }

                  restoreDotnet(".", "NuGet.Config")
// Ящитаю надо добавить в "it" переменные с путями до csproj сборки и тестов и посылать их в функции ниже
// Но пока без аргументов.
                  buildDotnet()

                  if ("${it.testpath}" == "" || "${it.testpath}" == "null" || "${it.testpath}" == null ) {

                    testDotnet()

                  } else {

                    testDotnet("${it.testpath}")
                  
                  }

                  // withSonarQubeEnv('SonarQube ASE') {
                  withSonarQubeEnv('md-sonar.niaepnn.ru') {

                    sh " dotnet /var/lib/jenkins/tools/hudson.plugins.sonar.MsBuildSQRunnerInstallation/5.8.0.52797_NET_Core_3.0/SonarScanner.MSBuild.dll end "

                  }
									// }
                  callPTAI(it.repo, it.branch, it.product+"."+it.service)

                }

              } catch(Exception ex) {

                println ("Что то пошло не так, перехожу к следующему сервису")

                def summary="ERROR ${it.product}.${it.service} - Sonar test failure"

                withCredentials([string(credentialsId: 'telegramBotSecret', variable: 'TOKEN'), string(credentialsId: "devops-jenkins-chatid", variable: 'chatID')]) {

                  sh (returnStdout: true, script: "curl -v -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage -d chat_id='${chatID}' -d parse_mode=markdown -d text='%E2%9D%8C ${summary}'")
                      
                }

              }

            }

            def summary="DONE - Sonar night scan completed"

            withCredentials([string(credentialsId: 'telegramBotSecret', variable: 'TOKEN'), string(credentialsId: "devops-jenkins-chatid", variable: 'chatID')]) {

                  sh (returnStdout: true, script: "curl -v -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage -d chat_id='${chatID}' -d parse_mode=markdown -d text='%E2%9B%B3 ${summary}'")
            }

          }

        }

      }

    }

    post {

      always {

        cleanWs()
        
      }

    }

  }

}
