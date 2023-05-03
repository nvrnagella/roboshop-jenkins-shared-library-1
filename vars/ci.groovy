def call(){
    try{
        pipeline{
            agent{
                label 'ansible'
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
                            withCredentials([aws(credentialsId: 'venkat-aws-cred', acessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]){
                                SONAR_USER='$(aws ssm get-parameters --region us-east-1 --name sonar.user --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                                SONAR_PASSWORD='$(aws ssm get-parameters --region us-east-1 --name sonar.pass --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                                println "password=${SONAR_PASSWORD}"
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