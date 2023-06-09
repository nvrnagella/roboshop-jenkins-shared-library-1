def compile(){
    if (app_lang == "nodejs"){
        sh 'npm install'
    }
    if (app_lang == "maven"){
        sh 'mvn compile'
    }
}
def unittest(){
    if (app_lang == "nodejs"){
        sh 'npm test || true'
    }
    if (app_lang == "maven"){
        sh 'mvn test'
    }
    if (app_lang == "python"){
        sh 'python3 -m unittest'
    }
}
def email(email_note){
    //mail bcc: '', body: "failure job name- ${JOB_BASE_NAME} \n failed job url - ${JOB_URL} ", cc: '', from: 'nvrnagella90@gmail.com', replyTo: '', subject: 'job failured', to: 'nvrnagella@gmail.com'
    println "sending failed job status to authorized persons"
}
def pushArtifact(){
    sh "echo ${TAG_NAME} > VERSION"
    if(app_lang == "nodejs"){
        sh "zip -r ${component}-${TAG_NAME}.zip node_modules server.js VERSION ${extraFiles}"
    }
    if(app_lang == "nginx" || app_lang == "python"){
        sh "zip -r ${component}-${TAG_NAME}.zip VERSION ${extraFiles} * -x Jenkinsfile"
    }
    if(app_lang == "maven"){
        sh "mvn package && cp target/${component}-1.0.jar ${component}.jar"
        sh "zip -r ${component}-${TAG_NAME}.zip VERSION ${component}.jar ${extraFiles} -x Jenkinsfile"
    }
    withCredentials([aws(credentialsId: 'venkat-aws-cred', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
        NEXUS_USER = sh (script: 'aws ssm get-parameters --region us-east-1 --name nexus.user --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
        NEXUS_PASSWORD = sh (script: 'aws ssm get-parameters --region us-east-1 --name nexus.pass --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
        wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${NEXUS_PASSWORD}", var: 'SECRET']]]){
            sh "curl -v -u ${NEXUS_USER}:${NEXUS_PASSWORD} --upload-file ${component}-${TAG_NAME}.zip http://172.31.10.126:8081/repository/${component}/${component}-${TAG_NAME}.zip"
        }
    }
}