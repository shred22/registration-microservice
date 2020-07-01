pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'pwd'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh 'ls -l'
            }
        }
        stage('Ansible Tweaks') {
            steps {
                echo 'Deploying....'
            }
        }
        stage('Deploy') {
                    steps {
                        echo 'Deploying....'
                        dir("${env.WORKSPACE}/ansible"){
                            sh "ls -l"
                            sh "rm -rfv *"
                            sh "ls -l"
                            sh "cp -r /home/dell/shreyas/Programming/Ansible/pipeline-data ."

                            sh """ansible-playbook -i ./pipeline-data/inventory.txt ./pipeline-data/templating-playbook.yaml"""
                        }

                    }
                }
    }
}