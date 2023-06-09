def call(){
//    if(!env.SONAR_EXTRA_OPTS){
//        env.SONAR_EXTRA_OPTS = " "
//    }
    if(!env.extraFiles){
        env.extraFiles = ""
    }
    if(!env.TAG_NAME){
        env.PUSH_CODE = "false"
    }else{
        env.PUSH_CODE = "true"
    }
    try{
        node{
            stage('clean workspace'){
                cleanWs()
                git branch: 'main', url: "https://github.com/nvrnagella/${component}-1"
            }
            stage('compile/built'){
                common.compile()
                }
            stage('unit test'){
                common.unittest()
            }
            stage('Quality control'){
//                withCredentials([aws(credentialsId: 'venkat-aws-cred', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
//                    SONAR_USER = sh (script: 'aws ssm get-parameters --region us-east-1 --name sonar.user --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
//                    SONAR_PASSWORD = sh (script: 'aws ssm get-parameters --region us-east-1 --name sonar.pass --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
//                    wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_PASSWORD}", var: 'SECRET']]]){
//                        sh "sonar-scanner -Dsonar.host.url=http://172.31.10.23:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASSWORD} -Dsonar.projectKey=${component} -Dsonar.qualitygate.wait=true ${SONAR_EXTRA_OPTS}"
//                    }
//                }
                println "pushing code to sonarqube for analysis"

            }
            if(env.PUSH_CODE == "true"){
                stage('code to centralized place'){
                    common.pushArtifact()
                }
            }

        }
    }catch(Exception e){
        common.email("failed")
    }
}