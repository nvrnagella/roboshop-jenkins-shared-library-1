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
                                SONAR_USER='$(aws ssm get-parameters --region us-east-1 --name sonar.user --with-decryption --query Parameters[0].Value)'
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