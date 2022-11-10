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
						sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll begin /k:\"vladimir\""
						sh "dotnet build Helloworld.csproj"
						sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll end"
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
