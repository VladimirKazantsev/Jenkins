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
					// writeFile file : 'Dockerfile', text: dockerfilecontents
					sh "pwd"
					sh "ls"
					sh "tree"
					}
				} 
			}
			// stage("Create Docker image") {
			// 	steps {
			// 		echo "=====================Start build image============================="
			// 		script {
			// 			docker.build("anshelen/microservices-backend:${env.BUILD_ID}","-f Dockerfile .")
			// 		}
			// 	}
			// }
		}
	}
}
