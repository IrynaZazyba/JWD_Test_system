/**
 * Created by ladyi on 01.03.2020.
 */
"use strict"

async function deleteTest(testId, obj) {

    let response = await fetch("/test-system/ajax?command=delete_test&testId=" + testId, {
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
    let response = await fetch("/test-system/ajax?command=create_question_answer", {
        method: 'POST',
        body: formData,
    });


    if (response.ok) {

        count = 1;
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
        if (name.includes('answer-') && value !== "") {
            html = html + "<div>- " + value + "</div>";
        }
    }
    return html + "<hr>";
}

async function saveTestInfo(obj) {

    let divButtonBack = document.getElementById('buttonBack');


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
        formData.append("testId", testId.value);
    }
    let response = await
        fetch("/test-system/ajax?command=create_test", {
            method: 'POST',
            body: formData,
        });


    if (response.ok) {
        if (divButtonBack != null) {
            divButtonBack.remove();
        }

        document.getElementById("questionEditForm-tab").classList.remove('disabled');
        // document.getElementById("preview").setAttribute("onclick","document.location=")

        let json = await response.json();

        if (json != null) {
            document.getElementById('testId').insertAdjacentHTML('afterbegin', "<input type='hidden' name='testId' value='" + json.testId + "'>");
        }
        successMessageDiv.style.display = 'block';

    } else {
        dangerMessageDiv.style.display = 'block';

    }
}

let form;

function showModalWindowEdit(obj) {

    let modalBody = document.querySelector(".modal-body .questionFormEdit");
    modalBody.innerHTML = "";
    let formGroupQuestion = obj.closest("div[id^='modal']");
    form = formGroupQuestion.cloneNode(true);
    formGroupQuestion.insertAdjacentHTML('beforebegin', "<input type='hidden' id='placeToInsert'/>");
    formGroupQuestion.querySelectorAll("div[id^='modal'] button").forEach(b => b.style.display = 'none');
    $("#modal").modal('show');
    modalBody.insertAdjacentElement('afterbegin', formGroupQuestion);
    document.querySelectorAll(".modal-body div[id^='modal-'] :disabled");
    document.querySelectorAll(".modal-body div[id^='modal-'] :disabled").forEach(e => e.removeAttribute('disabled'));
    document.querySelectorAll(".modal-body input[type='text']").forEach(i => i.insertAdjacentHTML('afterend',
        "<button onclick='deleteAnswer(this); return false;' type='button' id='" + i.id + "' class='btn btn-link editAnswerButton'><i class='far fa-trash-alt'></i></button>"));

    let answerSize = document.querySelectorAll('.modal-body .answer').length;
    if (answerSize < 4) {
        //todo insertButtonAddAnswer
        document.querySelector(".modal-body div[id^='modal-']").insertAdjacentHTML('beforeend', "<button type='button' id='addAnswer' onclick='addAnswerInput()' class='btn btn-link btn-block'><i class='fas fa-plus'></i></button>")
    }
}

let countInsertedAnswer = 0;

function insertButtonAddAnswer() {
    document.querySelector(".modal-body div[id^='modal-']").insertAdjacentHTML('beforeend', "<button type='button' id='addAnswer' onclick='addAnswerInput()' class='btn btn-link btn-block'><i class='fas fa-plus'></i></button>")

}

function addAnswerInput() {
    let size = document.querySelectorAll('.modal-body .answer').length;
    if ((size) < 4) {
        document.querySelectorAll('.modal-body .answer')[size - 1].insertAdjacentHTML('afterend', generateInput());
    }
}


function generateInput() {
    countInsertedAnswer++;
    return "<div class='row m-t-7 answer'><div class='col-10 p-0'>" +
        "<div class='input-group mb-3'>" +
        "<div class='input-group-prepend'>" +
        "<div class='input-group-text'>" +
        "<input type='checkbox'  name='checkAdd-" + countInsertedAnswer + "' aria-label='Checkbox for following text input'></div>" +
        "</div>" +
        "<input  class='form-control' name='answerAdd-" + countInsertedAnswer + "' aria-label='Text input with checkbox'></input>" +
        "<button onclick='deleteAnswer(this); return false;' type='button' id='answer-add-" + countInsertedAnswer + "' class='btn btn-link editAnswerButton'><i class='far fa-trash-alt'></i></button></div></div></div>";
}

let answerToDelete = [];

function deleteAnswer(button) {

    let answerId = button.id;
    if (!answerId.includes('answer-add')) {
        answerToDelete.push(answerId);
    }

    if (document.getElementById("addAnswer") == null) {
        insertButtonAddAnswer();
    }
    button.closest(".answer").remove();


}

async function updateQuestion(button) {

    let form = document.querySelector(".modal-body form");
    let dataF = new FormData(form);
    dataF.append("deletedAnswers", answerToDelete);

    let response = await fetch("/test-system/ajax?command=update_question", {
        method: 'POST',
        body: dataF,
    });

    if (response.ok) {
        answerToDelete = [];
        location.reload();

    } else {


    }
    //отправляем данные
    //очищаем массив answerToDelete
    //перезагружаем страницу если все ок

}

function backEditedQuestion() {
    document.querySelectorAll(".modal-body input").forEach(n => n.setAttribute('disabled', 'disabled'));
    document.querySelector(".modal-body textarea").setAttribute('disabled', 'disabled');

    document.querySelectorAll(".modal-body div[id^='modal-'] .editAnswerButton").forEach(b => b.remove());
    document.querySelectorAll(".modal-body .edit-button button").forEach(eb => eb.style.display = 'block');

}

$('#modal').on('hidden.bs.modal', function (e) {
    countInsertedAnswer = 0;

    backEditedQuestion();

    let insertPlace = document.getElementById('placeToInsert').insertAdjacentElement('afterend', form);
    form = "";
    document.getElementById('placeToInsert').remove();
});


function showModalWindowAddQuestion() {

    let modalBody = document.querySelector(".modal-body .questionFormEdit");
    modalBody.innerHTML = "";
    $("#modalAddQuestion").modal('show');
}

async function addQuestion(button) {
    let questionAddForm = document.getElementById("addQuestionModalWindowForm");
    let formData = new FormData(questionAddForm);
    let testId = document.getElementById("testId").value;
    formData.append("testId", testId);
    let response = await fetch("/test-system/ajax?command=create_question_answer", {
        method: 'POST',
        body: formData,
    });

    if (response.ok) {
        location.reload();


    } else {

    }


}

function showPreviewPage() {
    let testId = document.querySelector("#testId > input[type=hidden]").value;
    document.location.href = "/test-system/test?command=edit_test&testId=" + testId;
}

async function completeTestCreating() {
    let testId = document.getElementById("testId").value;
    let response = await fetch("/test-system/ajax?command=complete_test&testId=" + testId, {
        method: 'GET',
    });

    if (response.ok) {
        answerToDelete = [];
        document.location.href = "/test-system/test?command=show_admin_panel";

    } else {

    }
}


async function deleteQuestion(button) {

    let questionId = button.value;
    let response = await fetch("/test-system/ajax?command=delete_question&questionId=" + questionId, {
        method: 'DELETE',
    });

    if (response.ok) {
        location.reload();

    } else {

    }
}
