pipeline {
    agent any
    options { buildDiscarder(logRotator(numToKeepStr: '6')) }
    parameters {
            string(name: 'ansible_branch', defaultValue: 'master', description: 'Which Ansible branch to use for reading template vars ?')
    }
    /* tools {
            maven 'apache-maven-3.0.1'
    } */
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