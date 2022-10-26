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
					'192.168.50.230'
				],
				description: 'Адрес сервера куда пойдет по ssh'

			)
			
			string(
				name: 'dbServer',
				defaultValue: 'ubnt',
				description: 'Адрес сервера с pgsql'
			)
		}

		environment{
			NFS_PATH=""
		}

		stages {
			stage("Set vars") {
				steps {
					script {
					NFS_PATH="/backup"
					}
				}
			}
			stage("Ensure source folder exist") {
				steps {
					sh """
						ssh jenkins@${params.IpServer} 'bash -s << 'ENDSSH'
						echo =================Имя хоста===================
						docker exec pg5432 pg_dump \
						--username postgres \
						--verbose \
						--clean \
						--dbname elets \
						> ${NFS_PATH}/${env.BUILD_ID}.backup
						ls /backup
						
ENDSSH'
"""
				} 
			}
		}
	}
}