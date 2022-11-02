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

						println "${it.testpath}"
						branchname = "${it.testpath}".replace("/", "-")
						println "${branchname}"

						sh "mkdir -p ${it.service}"

									dir("${it.service}") {

										git branch: "${it.branch}",
														url: "${it.repo}"
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
						datas.each {
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
}
