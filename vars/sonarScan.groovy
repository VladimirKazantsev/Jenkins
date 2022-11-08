#!/usr/bin/env groovy
def call() {
	pipeline {
		agent any
		
		options {
			timestamps()
		}
		
		stages {
			
			stage("Scan Sonar") {
				
				steps {
					echo "===============Scan Sonar======================="
					
					script {
					def data = libraryResource "sonar/repos.yml"
					writeFile (file: 'repos.yml', text: data)
					def datas = readYaml file: 'repos.yml'
					def branchname =""
					datas.each {

						println "${it.branch}"
						branchname = "${it.testpath}".replace("/", "-")
						println "${branchname}"

						sh "mkdir -p ${it.service}"

									dir("${it.service}") {
										git	branch: "1.9.6.2/develop",
												url: "git@github.com:VladimirKazantsev/web1.git"
										// git branch: "develop" //"${it.branch}",
										// 				url: "git@github.com:VladimirKazantsev/web1.git" //"${it.repo}"
									}
					
					}
					// writeFile file : 'Dockerfile', text: dockerfilecontents
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
			stage("Sonar scan") {
				steps {
					withSonarQubeEnv(installationName: 'sonarqubeElets') { 
          sh './mvnw clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
					sh "dotnet ${MSBUILD_SQ_SCANNER_HOME}/SonarScanner.MSBuild.dll begin /k:\"Testing-Local\""
					}
				}
      }
		}
	}
}
