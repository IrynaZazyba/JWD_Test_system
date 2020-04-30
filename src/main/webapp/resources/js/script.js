/**
 * Created by ladyi on 01.03.2020.
 */
"use strict"


$('li.nav-item a').on('click', function (e) {
    // e.preventDefault();
    $('#myTab a.active').removeClass('active');
    $(this).tab('show');
    console.log("fghj");
})


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
    let test_id = document.getElementById("test_id").value;

    if (document.getElementById("key_value") != null) {
        let key = document.getElementById("key_value").value;
        dataToGetQuestion.append("key", key);
    }

    dataToGetQuestion.append("command", "show_question");
    dataToGetQuestion.append("test_id", test_id);

    let response = await fetch("http://localhost:8080/test-system/ajax", {
        method: 'POST',
        body: dataToGetQuestion,

    });


    if (response.ok) {
        if (document.getElementById('key') != null) {
            document.getElementById('key').remove();
            document.getElementById('conditions').remove();
        }

        let json = await response.json();

        if (json.question != null) {


            if (json.time_start!=null) {
                console.log("timer null");
                document.getElementById('timer').style.visibility = 'visible';
                startTimer(json.time_start);
            }


            document.getElementById('quest').style.visibility = 'visible';
            if (document.getElementById("js_quest") != null) {
                let del = document.getElementById("js_quest");
                del.parentNode.removeChild(del);
            }


            document.getElementById('quest').insertAdjacentHTML('afterbegin', generateCheckBox(json));
        } else {

            document.getElementById('complete').style.visibility = 'visible';
            document.getElementById("exeTest").remove();
            document.getElementById('complete').insertAdjacentHTML('afterbegin', generateButtonResult(json));

        }

    } else {

        document.location.href = 'http://localhost:8080/test-system/errorPage.jsp';

    }

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

function generateButtonResult(json) {
    return "<div class=\"row justify-content-center p-t-95\">" +
        "<form action=\"ajax\" class=\"form-horizontal\" role=\"form\" method=\"POST\">" +
        "<input type=\"hidden\" name=\"command\" value=\"get_result\"/>" +
        "<input type=\"hidden\" name=\"assign_id\" value=\"" + json.assign_id + "\"/>" +
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
        let t = getTimeRemaining(endtime);

        minutesSpan.innerHTML = ('0' + t.minutes).slice(-2);
        secondsSpan.innerHTML = ('0' + t.seconds).slice(-2);

        if (t.total <= 0) {
            clearInterval(timeinterval);
        }
    }

    updateClock();
    let timeinterval = setInterval(updateClock, 1000);
}

function startTimer(deadlineTime) {

    let deadline = new Date(Date.parse(new Date()) + deadlineTime * 60000); // for endless timer
    initializeClock('countdown', deadline);

}


