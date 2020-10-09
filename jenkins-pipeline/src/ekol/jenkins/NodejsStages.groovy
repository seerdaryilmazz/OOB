package ekol.jenkins

/**
 * Created by kilimci on 20/07/2017.
 */
class NodejsStages implements Serializable{
    def script

    NodejsStages(script){
        this.script = script
    }

    def run(){
        try{
            script.dir("/home/jenkins/workspace/pipeline/${script.env.JOB_NAME}") {
                script.stage('clean workspace') {
                    script.deleteDir()
                }
                script.dir("project") {
                    script.stage('checkout source') {
                        script.sh "git clone -b ${script.env.BRANCH_NAME} git@bitbucket.org:ekol1order/nodejs-services-config.git"
                        script.dir("app") {
                            script.checkout script.scm
                        }
                    }
                    script.stage('build project') {
                        script.dir("nodejs-services-config") {
                            script.sh 'npm install'
                        }
                        script.dir("app") {
                            script.sh 'npm install'
                            script.sh 'npm run test-start'
                            script.sh 'sleep 10s'
                            script.sh 'npm run test-run'
                            script.sh 'npm run test-stop'
                        }
                    }
                    script.stage('archive artfifacts') {
                        script.dir("nodejs-services-config") {
                            script.sh 'tar --exclude=\'./.git\' --exclude=\'.gitignore\' --exclude=\'./node_modules\' -zcf config.tar.gz *.js *.json'
                            script.sh 'mv config.tar.gz ../app/'
                        }
                        script.dir("app") {
                            script.sh 'tar --exclude=\'./.git\' --exclude=\'.gitignore\' --exclude=\'./node_modules\' --exclude=\'./test\' --exclude=\'test-reports.xml\' -zcf app.tar.gz *.js *.json'
                        }
                    }
                    script.stage('build docker image') {
                        def project = this.project()
                        script.dir("app") {
                            script.sh "docker build -t oneorder/${project}:${script.env.BRANCH_NAME} ."
                        }
                    }
                    script.stage('push docker image') {
                        def project = this.project()
                        def registry = '10.1.70.91'
                        script.dir("app") {
                            script.sh "docker tag oneorder/${project}:${script.env.BRANCH_NAME} ${registry}/oneorder/${project}:${script.env.BRANCH_NAME}"
                            script.sh "docker push ${registry}/oneorder/${project}:${script.env.BRANCH_NAME}"
                        }
                    }
                }
            }

        }catch(err){
            script.echo "Caught: ${err}"
            script.currentBuild.result = "FAILURE"
        }

        def slack = new Slack(script)
        slack.notifySlack()
    }

    def project(){
        def tokens = "${script.env.JOB_NAME}".tokenize('/')
        return tokens[1]
    }

}
