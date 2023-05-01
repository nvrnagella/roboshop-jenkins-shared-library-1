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
        sh 'npm test'
    }
}
def email(email_note){
    println "sending failed status to autorized persons"
}