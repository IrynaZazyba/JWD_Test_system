/**
 * Created by ladyi on 01.03.2020.
 */
"use strict"


// $('li.nav-item a').on('click', function (e) {
//     // e.preventDefault();
//     $('#myTab a.active').removeClass('active');
//     $(this).tab('show');
//     console.log("fghj");
// })


formElem.onsubmit = async (e) => {
    e.preventDefault();

    let response = await fetch("http://localhost:8080/test-system/ajax", {
        method: 'POST',
        body: new FormData(formElem),

    });

    if (response.ok) {
        let json = await response.json();
        console.log(json);
        document.getElementById('message').innerHTML = generateMessageDiv(json);
    } else {
        alert("ERROR: " + response.status);
    }


    setTimeout(function request() {
        document.getElementById('edit_user_answer').remove();
    }, 2500);

}


function generateMessageDiv(json) {
    let htmlCode = "";
    console.log(json.status);
    if (json.status === 'ok') {
        htmlCode = "<div class=\"alert alert-success\" id=\"edit_user_answer\" role=\"alert\">" + json.message + "</div>";
    } else {
        console.log(json);
        for (let mess of Object.keys(json)) {
            if (mess !== "status") {
                htmlCode = htmlCode + "<div class=\"alert alert-danger\" id=\"edit_user_" + mess + "\" role=\"alert\">" + json[mess] + "</div>";
            }
        }
    }
    return htmlCode;
}


async function getQuestion() {


    await sendAnswer();


    let dataToGetQuestion = new FormData();
    let test_id = document.getElementById("testId").value;

    if (document.getElementById("key_value") != null) {
        let key = document.getElementById("key_value").value;
        dataToGetQuestion.append("key", key);
    }

    if (document.getElementById("conditions") != null) {
        dataToGetQuestion.append("test_started", "true");
    }
    dataToGetQuestion.append("command", "show_question");
    dataToGetQuestion.append("testId", test_id);


    let response = await fetch("http://localhost:8080/test-system/ajax", {
        method: 'POST',
        body: dataToGetQuestion,

    });


    if (response.ok) {


        let json = await response.json();

        if (json.time_is_over != null) {
            hideKeyConditions();
            console.log("time_is_over" + json.time_is_over);
            hideQuestion();
            document.getElementById("card-body").insertAdjacentHTML('afterbegin', generateHiddenAssignIdInput(json));
            document.getElementById('complete').insertAdjacentHTML('beforeend', generateButtonResult());
            document.getElementById("timeIsEnded").style.visibility = 'visible';
            document.getElementById("countdown").className = "hidden";

        } else if (json.invalid_key) {
            document.getElementById("invalid_key").style.visibility = 'visible';

        } else {

            hideKeyConditions();
            // скрыть неверный ключ сообщ
            if (document.getElementById("invalid_key") != null && document.getElementById("invalid_key").style.visibility === 'visible') {
                document.getElementById("invalid_key").remove();
            }

            if (json.question != null) {

                if (json.duration != null && document.getElementById("timer").style.visibility === 'hidden') {
                    document.getElementById('timer').style.visibility = 'visible';
                    startTimer(json.duration);
                }

                document.getElementById('quest').style.visibility = 'visible';

                if (document.getElementById("js_quest") != null) {
                    let del = document.getElementById("js_quest");
                    del.parentNode.removeChild(del);
                }

                document.getElementById("card-body").insertAdjacentHTML('afterbegin', generateHiddenAssignIdInput(json));
                document.getElementById('quest').insertAdjacentHTML('afterbegin', generateCheckBox(json));
            } else {

                document.getElementById('complete').style.visibility = 'visible';
                document.getElementById("exeTest").remove();
                document.getElementById("countdown").className = "hidden";
                document.getElementById('complete').insertAdjacentHTML('afterbegin', generateButtonResult());

            }

        }
    } else {

        document.location.href = 'http://localhost:8080/test-system/errorPage.jsp';

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
        "<input type=\"hidden\" name=\"question_log_id\" value=\"" + json.question_log_id + "\">";

    console.log(Object.keys(json.question.answers[0]));

    for (let key of Object.keys(json.question.answers)) {
        let idAnsw = json.question.answers[key].id;
        htmlCode = htmlCode + "<div class=\"form-check check-box-style\">" +
            "<input class=\"form-check-input \" name=\"answer\" type=\"checkbox\" value=\"" + idAnsw + "\" id=\"" + idAnsw + "\">" +
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
    console.log("send answer");

    let startTest = document.getElementById("exeTest");

    if (document.getElementById("js_quest") != null) {

        let response = await fetch("http://localhost:8080/test-system/ajax", {
            method: 'POST',
            body: new FormData(startTest),

        });

        if (response.ok) {
            console.log("ok");
//todo

        } else {
            console.log("error location");
            document.location.href = 'http://localhost:8080/test-system/errorPage.jsp';
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

    let response = await fetch("http://localhost:8080/test-system/test", {
        method: 'POST',
        body: dataToGetQuestion,

    });

    getQuestion();

}





