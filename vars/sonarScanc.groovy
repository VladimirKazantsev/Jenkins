#!/usr/bin/env groovy
def call() {
	pipeline {
		agent any
		
		options {
			timestamps()
		}
		
		stages {
			
			stage("SonarQube scan") {
				
				steps {
					echo "===============Scan Sonar======================="
					
					script {
						git url: "git@github.com:VladimirKazantsev/web1.git"
						def scannerHome = tool 'SonarQube-1'

						withSonarQubeEnv(installationName: 'sonarqubeElets') {
						sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll begin /k:\"vladimir\""
						sh "dotnet build"
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
									sh 'ls truba'
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
