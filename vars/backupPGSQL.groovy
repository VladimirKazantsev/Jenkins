#!/usr/bin/env groovy
def call() {
	pipeline {
		agent any
		options {
			timestamps()
		}
		parameters {
			
			string(
				name: 'dbServer',
				defaultValue: 'ubnt',
				description: 'Адрес сервера с pgsql'
			)
		}

		stages {
			stage("Ensure source folder exist") {
				steps {
					script {
					sh '''!#/bin/bash 
						ssh jenkins@192.168.50.230 << ENDSSH
						
						hostname
						
						ENDSSH
					'''
					}
				} 
			}
		}
	}
}