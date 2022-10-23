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
					//def dockerfilecontents = libraryResource "docker/Dockerfile"
					def dockerfilecontents = libraryResource "scripts/linux/hello-world.sh"
					writeFile file : 'Dockerfile', text: dockerfilecontents
					sh "pwd"
					sh "ls"
				} 
			}
			// stage("Create Docker image") {
			// 	steps {
			// 		script {
			// 			//docker.build .
			// 		}
			// 	}
			// }
		}
	}
}
