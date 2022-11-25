#!/usr/bin/env groovy
def call() {
	pipeline {
		agent {
			label 'Glavnyi'
		} 
		
		options {
			timestamps()
		}
		
		stages {
			
			stage("SonarQube scan") {
				
				steps {
					echo "===============Scan Sonar======================="
					
					script {
						git branch: "main",
								url: "git@github.com:VladimirKazantsev/c-app.git"
						def scannerHome = tool 'SonarScannerMSBuildElets1'
						sh "ls"
						sh "pwd"
						println "${scannerHome}"
						withSonarQubeEnv(installationName: 'sonarqubeElets') {
						sh "which dotnet"
						sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll begin /k:\"testVladimir\" /d:sonar.verbose=true /d:sonar.login=\"sqp_5111c5b096cf74f8f17cccb9c0bd3d8de1ebf943\""
						sh "dotnet build Helloworld.csproj"
						sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll end /d:sonar.login=\"sqp_5111c5b096cf74f8f17cccb9c0bd3d8de1ebf943\""
						}
					}
				} 
			}
			
			stage("Example") {
				steps {
					script {
						
							try {
									sh 'ls'
							}
							catch (all) {
									echo 'Neverno zadana direktory'
							}
						
					}
				}
      }
		}
	}
}
