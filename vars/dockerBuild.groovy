#!/usr/bin/env groovy
def call() {
	pipeline {
		agent any
		// options {
		// 	timestamps()
		// }
		stages {
			stage("Load Dockerfile") {
				steps {
					script {
					def dockerfilecontents = libraryResource "docker/Dockerfile"
					writeFile file : 'Dockerfile', text: dockerfilecontents
					sh "pwd"
					sh "ls"
					}
				} 
			}
			stage("Create Docker image") {
				steps {
					script {
						def dockerImage = docker.build("anshelen/microservices-backend","-f Dockerfile .")
					}
				}
			}
		}
	}
}
