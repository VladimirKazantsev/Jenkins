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
						sh "ls"
						sh "pwd"
						println "${scannerHome}"
						sh "whoami"
						sh "which ls"
						//sh "which dotnet"
						withSonarQubeEnv(installationName: 'sonarqubeElets') {
						
						// sh "dotnet /var/lib/jenkins/.dotnet/tools/.store/dotnet-sonarscanner/5.8.0/dotnet-sonarscanner/5.8.0/tools/netcoreapp3.0/any/SonarScanner.MSBuild.dll begin  /k:\"testVladimir\" /d:sonar.verbose=true /d:sonar.login="sqp_5111c5b096cf74f8f17cccb9c0bd3d8de1ebf943""
						//sh "dotnet /var/lib/jenkins/.dotnet/tools/.store/dotnet-sonarscanner/5.8.0/dotnet-sonarscanner/5.8.0/tools/netcoreapp3.0/any/SonarScanner.MSBuild.dll begin /k:\"testVladimir\" /d:sonar.host.url=\"http://192.168.50.20:9000\" /d:sonar.login="sqp_5111c5b096cf74f8f17cccb9c0bd3d8de1ebf943""
						//sh "dotnet build"
						//sh "dotnet /var/lib/jenkins/.dotnet/tools/.store/dotnet-sonarscanner/5.8.0/dotnet-sonarscanner/5.8.0/tools/netcoreapp3.0/any/SonarScanner.MSBuild.dll end /d:sonar.login="sqp_5111c5b096cf74f8f17cccb9c0bd3d8de1ebf943""
						// sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll end"

						// sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll begin /d:sonar.verbose=true /k:\"testVladimir\" /d:sonar.verbose=true /d:sonar.login="sqp_5111c5b096cf74f8f17cccb9c0bd3d8de1ebf943""
						// sh "dotnet build Helloworld.csproj"
						// sh "dotnet test Helloworld.csproj"
						// sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll end"
						sh "which dotnet"
						sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll begin /k:\"testVladimir\" /d:sonar.verbose=true /d:sonar.login="sqp_5111c5b096cf74f8f17cccb9c0bd3d8de1ebf943""
						sh "dotnet build Helloworld.csproj"
						sh "dotnet ${scannerHome}/SonarScanner.MSBuild.dll end /d:sonar.login="sqp_5111c5b096cf74f8f17cccb9c0bd3d8de1ebf943""
						sh "which dotnet"
						}
						sh "pwd"
						sh "which dotnet"
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
