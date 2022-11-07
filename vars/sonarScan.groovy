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
										git	branch: "testirovchik",
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
		}
	}
}
