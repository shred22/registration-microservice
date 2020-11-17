pipeline {
  agent {
        docker {
            image 'maven:3-alpine'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    options { buildDiscarder(logRotator(numToKeepStr: '5')) }
    /* docker {
          registryUrl 'https://registry.hub.docker.com'
          registryCredentialsId 'GitHub-Credentials' // the id of username/password credentials I have in Jenkins
    } */
    parameters {
            string(name: 'ansible_branch', defaultValue: 'master', description: 'Which Ansible branch to use for reading template vars ?')
            string(name: 'push_to_dockerhub', defaultValue: 'N', description: 'Push this build\'s image to Docker hub Y or N ?')
    }
    /* tools {
            maven 'apache-maven-3.0.1'
    } */
    stages {

        stage('Build With Unit Tests') {
                    steps {
                     configFileProvider([configFile(fileId: "maven-settings-file", variable: 'MAVEN_SETTINGS')]) {
                            sh 'mvn -version'
                            sh 'mvn clean test -Punit-tests -s $MAVEN_SETTINGS'
                     }
                        
                    }
                }

        /* stage('Prepare Environment') {
                    steps {
                         dir("${env.WORKSPACE}/deploy") {
                               echo "Displaying user info"
                               sh "rm -rfv *"
                               sh "cp -r ../target/registration-service-bundle-zip.zip ."
                               sh "unzip -o registration-service-bundle-zip.zip"
                               sh "cp lib/registration-service.jar ."
                               sh "ls -l"
                         }
                    }
        } */
        /* stage('Modify Config With Ansible') {
                     steps {
                            echo 'Deploying....'
                            dir("${env.WORKSPACE}/ansible") {
                              sh "ls -l"
                              sh "rm -rfv *"
                              sh "ls -l"
                              //sh "cp -r /home/dell/shreyas/Programming/Ansible/pipeline-data ."
                              //sh """ansible-playbook -i ./pipeline-data/inventory.txt ./pipeline-data/templating-playbook.yaml"""
                            }
                     }

         } */
        stage('Integration Tests') {
               steps {
                    //sleep(time:5,unit:"SECONDS")
                    configFileProvider([configFile(fileId: "maven-settings-file", variable: 'MAVEN_SETTINGS')]) {
                        sh "mvn install -Pintegration-tests,docker-containers,build-tag-image -s $MAVEN_SETTINGS"
                    }
                      
               }
        }
        stage('Push Docker Image') {
                       steps {
                         script {
                                if("${params.push_to_dockerhub}" == 'Y') {
                                 withCredentials([usernamePassword(
                                        credentialsId: 'DockerHub',
                                        usernameVariable: 'DOCKER_USERNAME',
                                        passwordVariable: 'DOCKER_PASSWORD',
                                    )]) {
                                        sh 'docker login --username="${DOCKER_USERNAME}" --password="${DOCKER_PASSWORD}"'
                                    }
                                   configFileProvider([configFile(fileId: "maven-settings-file", variable: 'MAVEN_SETTINGS')]) {
                                          sh "mvn install -DskipTests -Ppush-docker-image -s $MAVEN_SETTINGS"

                                   }
                             }

                         }
                       }
                }
                stage('Upload TestReport') {
                    steps {
                    echo 'Publishing Serenity TestReport'
                         publishHTML(target: [
                                                                                  reportName : 'Serenity',
                                                                                  reportDir:   'target/site/serenity',
                                                                                  reportFiles: 'index.html',
                                                                                  keepAll:     true,
                                                                                  alwaysLinkToLastBuild: true,
                                                                                  allowMissing: false
                                                                              ])
                    }
                }

        stage('Sonar Scan') {
               steps {
                   configFileProvider([configFile(fileId: "maven-settings-file", variable: 'MAVEN_SETTINGS')]) {
                             sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=0675e85b9fa3ae24dd38fb9aa50715d7e7f23d5b -s $MAVEN_SETTINGS'
                   }
               }
        }
    }
}
