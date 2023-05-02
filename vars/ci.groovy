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
                stage('code analysis'){
                    steps{
                        script{

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