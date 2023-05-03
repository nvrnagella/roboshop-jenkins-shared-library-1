def call(){
    try{
        pipeline{
            agent{
                label 'ansible'
            }
            environment{
                THE_CREDENTIALS=credentials('venkat-aws-cred')
            }
            stages{
                stage('compile/built'){
                    steps{
                        script{
                            common.compile()
                        }

                    }
                }
                stage('unit test'){
                    steps{
                        script{
                            common.unittest()
                        }
                    }
                }
                stage('Quality control'){
                    steps{
                        script{
                           SONAR_USER='$(aws ssm get-parameters --region us-east-1 --name sonar.user --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                           SONAR_PASSWORD=sh (script: 'aws ssm get-parameters --region us-east-1 --name sonar.pass --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
                           wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_PASSWORD}", var: 'SECRET']]]){
                              sh "sonar-scanner -Dsonar.host.url=http://172.31.10.23:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASSWORD} -Dsonar.projectKey=${component}"
                              }
                        }

                    }
                }
                stage('code to centralized place'){
                    steps{
                        echo 'code to centralized place'
                    }
                }
            }
            post{
                always{
                    echo "this is post section"
                }
            }
        }
    }catch(Exception e){
        common.email("failed")
    }
}