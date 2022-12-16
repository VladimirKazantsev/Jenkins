#!/usr/bin/env groovy
def call() {
	pipeline {
		agent {

      label 'linux'

		}
		options {
			timestamps()
		}
		stages {
			stage("Load Dockerfile") {
				steps {
					echo "===============Load Dockerfile on Jenkins======================="
					script {
					def dockerfilecontents = libraryResource "docker/Dockerfile"
					writeFile file : 'Dockerfile', text: dockerfilecontents
					sh "pwd"
					sh "ls"
					sh "tree"
					}
				} 
			}
			stage("Create/Push Docker image") {
				steps {
					echo "=====================Start build image============================="
					script {
						docker.withRegistry('http://192.168.50.17:8123','mynexusdockerhub'){
						def customImage = docker.build("192.168.50.17:8123/microservices-backend:${env.BUILD_ID}","-f Dockerfile .")
						println (customImage.id)
						customImage.push()
					 }
					}
				}
			}
			stage("Deploy") {
				steps {
					echo "=====================Deploy conteiner on server============================="
				}
			}
		}
	}
}
