package ekol.jenkins

/**
 * Created by kilimci on 09/03/2017.
 */

class Stages implements Serializable {
    def script
    def config
    Stages(script, config){
        this.script = script
        this.config = config
    }

    private callAction(action){
        if(action != null){
            action.call(script)
        }
    }

    def runBackend(actions){
        def functions = new StageFunctions(script, config)
        actions['checkout'] = {functions.checkout()}
        actions['build'] = {functions.buildBackend()}
        actions['archive'] = {functions.archive()}
        actions['analysis'] = {functions.analysis()}
        actions['buildImage'] = {functions.buildImage()}
        actions['pushImage'] = {functions.pushImage()}
        run(actions)
    }

    def runFrontend(actions){
        def functions = new StageFunctions(script, config)
        actions['checkout'] = {functions.checkout()}
        actions['build'] = {functions.buildFrontend()}
        actions['archive'] = {functions.archive()}
        actions['buildImage'] = {functions.buildImage()}
        actions['pushImage'] = {functions.pushImage()}
        run(actions)
    }

    private run(actions){
        try{
            script.dir("/home/jenkins/workspace/pipeline/${script.env.JOB_NAME}") {
                script.stage('clean workspace') {
                    clean()
                }
                script.dir("project") {
                    checkoutStage(actions)
                    buildStage(actions)
                    archiveStage(actions)
                    analysisStage(actions)
                    buildImageStage(actions)
                    pushImageStage(actions)
                }
            }

        }catch(err){
            script.echo "Caught: ${err}"
            script.currentBuild.result = "FAILURE"
        }

        def slack = new Slack(script)
        slack.notifySlack()
    }

    private clean(){
        script.deleteDir()
    }
    private checkoutStage(actions){
        script.stage('checkout source') {
            callAction(actions['preCheckout'])
            callAction(actions['checkout'])
            callAction(actions['postCheckout'])
        }
    }
    private buildStage(actions){
        script.stage('build project') {
            callAction(actions['preBuild'])
            callAction(actions['build'])
            callAction(actions['postBuild'])
        }
    }
    private archiveStage(actions){
        script.stage('archive artfifacts') {
            callAction(actions['preArchive'])
            callAction(actions['archive'])
            callAction(actions['postArchive'])
        }
    }
    private analysisStage(actions){
        def branch = script.env.BRANCH_NAME
        if(branch == 'dev'){
            callAction(actions['preAnalysis'])
            script.stage('code analysis') {
                callAction(actions['analysis'])
            }
            callAction(actions['postAnalysis'])

        }
    }
    private buildImageStage(actions){
        script.stage('build docker image') {
            callAction(actions['preBuildImage'])
            callAction(actions['buildImage'])
            callAction(actions['postBuildImage'])
        }
    }
    private pushImageStage(actions){
        script.stage('push docker image') {
            callAction(actions['prePushImage'])
            callAction(actions['pushImage'])
            callAction(actions['postPushImage'])
        }
    }
}