/**
 * Created by ladyi on 01.03.2020.
 */
"use strict"


document.addEventListener("DOMContentLoaded", menuLinkHelper);


function menuLinkHelper() {
    console.log(window.location.search.toString());
    let urlParams = window.location.search.toString().split('&');
    urlParams.forEach(elem => {
        if (elem.includes('command')) {
            let command = elem.split('=')[1];
            let menuLinkActive = document.querySelector(".nav-item > a[href*='" + command + "']");
            if (menuLinkActive != null) {
                menuLinkActive.classList.add('active');
            }
        }
    })

}


async function changeUserInfo() {

    let formElem = document.getElementById("formElem");
    let response = await fetch("/test-system/ajax", {
        method: 'POST',
        body: new FormData(formElem),

    });

    if (response.ok) {
        let json = await response.json();
        document.getElementById('message').innerHTML = generateSuccessMessageDiv(json.message);
        document.getElementById('message').style.display = 'block';

    } else {
        let errorAnswer = await response.json();
        let message = errorAnswer.message;

        if (Array.isArray(message)) {
            message.forEach(mess => {
                document.getElementById('message').innerHTML = generateDangerMessageDiv(mess);
                document.getElementById('message').style.display = 'block';

            });
        } else {
            document.getElementById('message').innerHTML = generateDangerMessageDiv(message);
            document.getElementById('message').style.display = 'block';

        }
    }

    setTimeout(function request() {
        document.getElementById('message').style.display = 'none';
    }, 2500);

}


function generateSuccessMessageDiv(message) {
    return "<div class=\"alert alert-success\" id=\"edit_user_answer\" role=\"alert\">" + message + "</div>";
}

function generateDangerMessageDiv(message) {
    return "<div class=\"alert alert-danger\" id=\"edit_user_answer\" role=\"alert\">" + message + "</div>";
}


async function getQuestion() {

    await sendAnswer();

    let dataToGetQuestion = new FormData();
    let test_id = document.getElementById("testId").value;

    if (document.getElementById("key_value") != null) {
        let key = document.getElementById("key_value").value;
        dataToGetQuestion.append("testKey", key);
    }

    if (document.getElementById("conditions") != null) {
        dataToGetQuestion.append("test_started", "true");
    }
    dataToGetQuestion.append("command", "show_question");
    dataToGetQuestion.append("testId", test_id);


    let response = await fetch("/test-system/ajax", {
        method: 'POST',
        body: dataToGetQuestion,

    });


    if (response.ok) {


        let json = await response.json();

        if (json.timeIsOver != null) {
            hideKeyConditions();
            hideQuestion();
            document.getElementById("card-body").insertAdjacentHTML('afterbegin', generateHiddenAssignIdInput(json));
            document.getElementById('complete').insertAdjacentHTML('beforeend', generateButtonResult());
            document.getElementById("timeIsEnded").style.visibility = 'visible';
            document.getElementById("countdown").className = "hidden";

        } else if (json.invalidKey) {
            document.getElementById("invalid_key").style.visibility = 'visible';

        } else {

            hideKeyConditions();
            // скрыть неверный ключ сообщ
            if (document.getElementById("invalid_key") != null && document.getElementById("invalid_key").style.visibility === 'visible') {
                document.getElementById("invalid_key").remove();
            }

            if (json.question != null) {

                if (json.testDuration != null && document.getElementById("timer").style.visibility === 'hidden') {
                    document.getElementById('timer').style.visibility = 'visible';
                    startTimer(json.testDuration);
                }

                document.getElementById('quest').style.visibility = 'visible';

                if (document.getElementById("js_quest") != null) {
                    let del = document.getElementById("js_quest");
                    del.parentNode.removeChild(del);
                }

                document.getElementById("card-body").insertAdjacentHTML('afterbegin', generateHiddenAssignIdInput(json));
                document.getElementById('quest').insertAdjacentHTML('afterbegin', generateCheckBox(json));
            } else {
                document.getElementById("card-body").insertAdjacentHTML('afterbegin', generateHiddenAssignIdInput(json));
                document.getElementById('complete').style.visibility = 'visible';
                document.getElementById("exeTest").remove();
                document.getElementById("countdown").className = "hidden";
                document.getElementById('complete').insertAdjacentHTML('afterbegin', generateButtonResult());

            }

        }
    } else {

        document.location.href = '/test-system/errorPage.jsp';

    }

}


function hideQuestion() {
    document.getElementById('complete').style.visibility = 'visible';
    document.getElementById("exeTest").remove();
}

function generateHiddenAssignIdInput(json) {
    return "<input type=\"hidden\" id=\"assign_id\" name=\"assignId\" value=\"" + json.assignId + "\">";

}

function generateCheckBox(json) {

    let htmlCode = "<div id=\"js_quest\"><h4 id=\"text_question\" class=\"name-test text-ctr\">" + json.question.question + "</h4><hr>" +
        "<input type=\"hidden\" name=\"questionLogId\" value=\"" + json.questionLogId + "\">";


    for (let key of Object.keys(json.question.answers)) {
        let idAnsw = json.question.answers[key].id;
        htmlCode = htmlCode + "<div class=\"form-check check-box-style\">" +
            "<input class=\"form-check-input \" name=\"answerId\" type=\"checkbox\" value=\"" + idAnsw + "\" id=\"" + idAnsw + "\">" +
            "<label class=\"form-check-label\" for=\"" + idAnsw + "\">" +
            "<h5>" + json.question.answers[key].answer + "</h5>" +
            "</label></div>";
    }


    return htmlCode + "</div>";

}

function generateButtonResult() {
    let assignId = document.getElementById("assign_id").value;

    return "<div class=\"row justify-content-center p-t-95\">" +
        "<form action=\"test\" class=\"form-horizontal\" role=\"form\" method=\"GET\">" +
        "<input type=\"hidden\" name=\"command\" value=\"get_result\"/>" +
        "<input type=\"hidden\" name=\"assignId\" value=\"" + assignId + "\"/>" +
        "<button type=\"submit\" class=\"card-exe-btn btn btn-outline-primary\">Show result</button>" +
        "</form></div>"

}

async function sendAnswer() {

    let startTest = document.getElementById("exeTest");

    if (document.getElementById("js_quest") != null) {

        let response = await fetch("/test-system/ajax", {
            method: 'POST',
            body: new FormData(startTest),

        });

        if (response.ok) {
//todo

        } else {
            document.location.href = '/test-system/errorPage.jsp';
        }
    }
}


function getTimeRemaining(endtime) {
    let t = Date.parse(endtime) - Date.parse(new Date());
    let seconds = Math.floor((t / 1000) % 60);
    let minutes = Math.floor((t / 1000 / 60) % 60);

    return {
        'total': t,
        'minutes': minutes,
        'seconds': seconds
    };
}

function initializeClock(id, endtime) {
    let clock = document.getElementById(id);
    let minutesSpan = clock.querySelector('.minutes');
    let secondsSpan = clock.querySelector('.seconds');

    function updateClock() {
        var t = getTimeRemaining(endtime);

        if (t.total <= 0) {
            hideQuestion();
            document.getElementById('complete').insertAdjacentHTML('beforeend', generateButtonResult());
            document.getElementById("timeIsEnded").style.visibility = 'visible';
            document.getElementById("countdown").className = "hidden";
            clearInterval(timeinterval);
            return true;
        }
        minutesSpan.innerHTML = ("0" + t.minutes).slice(-2);
        secondsSpan.innerHTML = ("0" + t.seconds).slice(-2);
    }

    updateClock();
    var timeinterval = setInterval(updateClock, 1000);
}

function startTimer(deadlineTime) {

    let deadline = new Date(Date.parse(new Date()) + deadlineTime / 60 * 60000); // for endless timer
    initializeClock('countdown', deadline);

}

function hideKeyConditions() {

    if (document.getElementById('conditions') != null) {
        document.getElementById('conditions').remove();
        document.getElementById('exeButton').remove();
    }

    if (document.getElementById('key') != null) {
        document.getElementById('key').remove();
    }


}

async function getContinuedQuestion() {

    let dataToGetQuestion = new FormData();

    dataToGetQuestion.append("command", "get_empty_question_page");

    let response = await fetch("/test-system/test", {
        method: 'POST',
        body: dataToGetQuestion,

    });

    getQuestion();
}

async function changePassword(obj) {


    let formData = new FormData(obj);
    let response = await fetch("/test-system/ajax?command=change_password", {
        method: 'POST',
        body: formData,
    });


    if (response.ok) {
        let json = await response.json();
        document.getElementById('passMessage').innerHTML = generateSuccessMessageDiv(json.message);
        document.getElementById('passMessage').style.display = 'block';

    } else {
        let json = await response.json();
        document.getElementById('passMessage').innerHTML = generateDangerMessageDiv(json.message);
        document.getElementById('passMessage').style.display = 'block';

    }


    setTimeout(function request() {
        document.getElementById('passMessage').style.display = 'none';
    }, 2500);



}



