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
					sh """
						// ssh jenkins@192.168.50.230 'bash -s << 'ENDSSH'
						// echo =================Имя хоста===================
						// hostname
						// echo =============Рабочая директория==============
						// pwd
						// echo =============Образы докер==============
						// docker images
						//docker exec pg5432 pg_dump -U postgres elets > /backup/4.backup
						ls /backup
						
ENDSSH'
"""
				} 
			}
		}
	}
}