#!/usr/bin/env groovy
package ekol.jenkins
import groovy.json.JsonOutput

/**
 * Created by kilimci on 09/03/2017.
 */
class Slack implements Serializable {
    def script
    Slack ( script ) {
        this.script = script
    }

    def notifySlack() {
        def status = pickStatus(script.currentBuild.result)
        def color = pickColor(script.currentBuild.result)
        def slackURL = 'https://hooks.slack.com/services/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'
        def console = "${script.env.JOB_URL}/${script.env.BUILD_NUMBER}/console"
        def attachments = []
        def jobInfo = [
                fallback  : "${script.env.JOB_NAME} - #${script.env.BUILD_NUMBER} build is ${status}",
                title     : "${script.env.JOB_NAME} - #${script.env.BUILD_NUMBER} build is completed",
                title_link: "${script.env.JOB_URL}",
                text      : "Build status is ${status} - <${console}|Console>",
                color     : color,
                footer    : "${script.currentBuild.duration} sec."
        ]
        attachments.push(jobInfo)

        def payload = JsonOutput.toJson([
                channel    : '#builds',
                username   : 'jenkins',
                icon_url   : 'https://a.slack-edge.com/7bf4/img/services/jenkins-ci_48.png',
                attachments: attachments
        ])

        script.sh "echo payload: ${payload}"
        script.sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURL}"
    }

    def pickStatus(result) {
        def status = 'successful'
        if (result == 'UNSTABLE') {
            status = 'unstable'
        } else if (result == 'FAILURE') {
            status = 'failed'
        }
        return status
    }

    def pickColor(status) {
        def color = "good"
        if (status == 'UNSTABLE') {
            color = "warning"
        } else if (status == 'FAILURE') {
            color = "danger"
        }
        return color
    }
}