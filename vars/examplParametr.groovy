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
					'Никуда',
					'192.168.50.230',
					'192.168.50.12'
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
			stage("Сondition") {
				steps {
					script {
						if (params.IpServer == 192.168.50.230)
						echo "Переменная равна ${params.IpServer}"
					}
				}
			}
		}
	}
}