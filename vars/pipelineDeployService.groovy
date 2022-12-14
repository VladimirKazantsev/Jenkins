#!/usr/bin/env groovy
def call() {
	pipeline {
		agent any
		
		options {
		
			timestamps()
		
		}
		parameters {

			choice(name: 'IpServer',
				choices: [
					'192.168.50.231',
					'Никуда'
				],
				description: 'Адрес сервера куда пойдет по ssh'

			)
			
			string(
				name: 'dbServer',
				defaultValue: 'ubnt',
				description: 'Адрес сервера с pgsql'
			)
		}

		stages {
			stage("Deploy conteiner") {
				steps {
					sh """
						ssh jenkins@${params.IpServer} 'bash -s << 'ENDSSH'
						echo =================Развертывание контейнера===================
						docker run -d \
						--name moskow-${BUILD_NUMBER} \
						--restart always \
						-p 80:80 \
						nginx
						
						
ENDSSH'
"""
				} 
			}
		}
	}
}