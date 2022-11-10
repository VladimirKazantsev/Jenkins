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
						def scannerHome = tool 'SonarQube-1'

						withSonarQubeEnv(installationName: 'sonarqubeElets') {
						
						sh "dotnet /var/lib/jenkins/.dotnet/tools/.store/dotnet-sonarscanner/5.8.0/dotnet-sonarscanner/5.8.0/tools/netcoreapp3.0/any/SonarScanner.MSBuild.dll begin  /k:\"testVladimir\" /d:sonar.login="sqp_5111c5b096cf74f8f17cccb9c0bd3d8de1ebf943""
						sh "dotnet build Helloworld.csproj"
						sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll end"

						// sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll begin /d:sonar.verbose=true /k:\"vladimir\""
						// sh "dotnet build Helloworld.csproj"
						// sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll end"
						}
						sh "pwd"
						sh "ls"
						sh "tree"
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
