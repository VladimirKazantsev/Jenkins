#!/usr/bin/env groovy
def call(Map config) {
	pipeline {
		agent {
			label 'master'
		}
		options {
			timestamps()
		}
		stages {
			stage("Create Docker image") {
				steps {
					script {
						docker.build -f 
					}
				}
			}
		}
	}
}
