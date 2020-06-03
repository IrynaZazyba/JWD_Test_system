/**
 * Created by ladyi on 01.03.2020.
 */
"use strict"

async function deleteTest(testId, obj) {
    console.log(obj);

    let response = await fetch("http://localhost:8080/test-system/ajax?command=delete_test&testId=" + testId, {
        method: 'DELETE',
    });


    if (response.status === 204) {
        obj.replaceWith("DELETED");
    } else {
        document.getElementById('invalidDeleteMessage').style.display = 'block';
    }
}


let count = 1;

function addAnswerTextArea(obj) {
    console.log(obj);
    obj.insertAdjacentHTML('beforebegin', generateTextArea());
}


function generateTextArea() {
    let html = "";
    if (count < 4) {
        count++;
        html = "<div class='input-group mb-3 jsInsertAnswer'>" +
            "<div class='input-group-prepend'>" +
            "<div class='input-group-text'>" +
            "<input type='checkbox'  name='check-" + count + "' aria-label='Checkbox for following text input'></div>" +
            "</div>" +
            "<textarea  class='form-control' name='answer-" + count + "' aria-label='Text input with checkbox'></textarea>" +
            "</div>";
    }
    return html;
}

async function saveQuestion(obj) {

    let formData = new FormData(obj);
    let testId = document.querySelector("#testId > input[type=hidden]").value;
    formData.append("testId", testId);
    console.log(obj);
    let response = await fetch("http://localhost:8080/test-system/ajax?command=create_question_answer", {
        method: 'POST',
        body: formData,
    });


    if (response.ok) {
        count = 1;
        console.log("ok");
        let addedQuestionDiv = document.getElementById('addedQuestions');
        addedQuestionDiv.style.display = 'block';
        addedQuestionDiv.insertAdjacentHTML('beforeend', generateQuestionView(formData));

        let nodeAnswers = document.querySelectorAll('.jsInsertAnswer');
        nodeAnswers.forEach(elem => elem.remove());

        document.querySelector("#quest").value = "";
        document.querySelector('textarea[name="answer-1"]').value = "";

    } else {

    }
}

function generateQuestionView(formData) {
    let html = "<div>" + formData.get('question') + "</div>";
    for (let [name, value] of formData) {
        console.log(name + " " + value);
        if (name.includes('answer-')) {
            html = html + "<div>- " + value + "</div>";
        }
    }
    return html+"<hr>";
}

async function saveTestInfo(obj) {

    let divButtonBack = document.getElementById('buttonBack');
    if (divButtonBack != null) {
        divButtonBack.remove();
    }

    let successMessageDiv = document.getElementById('successCreatedTest');
    if (successMessageDiv.style.display === 'block') {
        successMessageDiv.style.display = 'none';
    }

    let dangerMessageDiv = document.getElementById('dangerCreatedTest');
    if (dangerMessageDiv.style.display === 'block') {
        dangerMessageDiv.style.display = 'none';
    }

    let formData = new FormData(obj);

    let testId = document.querySelector("#testId > input[type=hidden]");
    if (testId != null) {
        console.log("testId" + testId);
        formData.append("testId", testId.value);
    }
    let response = await
        fetch("http://localhost:8080/test-system/ajax?command=create_test", {
            method: 'POST',
            body: formData,
        });


    if (response.ok) {
        let json = await response.json();

        if (json != null) {
            document.getElementById('testId').insertAdjacentHTML('afterbegin', "<input type='hidden' name='testId' value='" + json.testId + "'>");
        }
        successMessageDiv.style.display = 'block';

    } else {
        dangerMessageDiv.style.display = 'block';

    }
}