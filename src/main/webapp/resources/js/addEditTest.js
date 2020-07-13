/**
 * Created by ladyi on 01.03.2020.
 */
"use strict"

async function deleteTest(testId, obj) {


    let response = await fetch("/test-system/ajax?command=delete_test&testId=" + testId, {
        method: 'DELETE',
    });


    if (response.status === 204) {
        $("#confirm-delete").modal('hide');
        let tableRow = document.getElementById("btn-" + testId).closest("tr");
        tableRow.setAttribute("class", "deleted-row");
        tableRow.querySelectorAll("button").forEach(elem => elem.setAttribute("disabled", "disabled"));
        tableRow.querySelector("a").removeAttribute("href");
    } else {
        document.getElementById('invalidDeleteMessage').style.display = 'block';
    }
}


$('#confirm-delete').on('show.bs.modal', function (e) {
    $(this).find('.btn-ok').attr('onclick', $(e.relatedTarget).data('onclick'));
});


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


    let invalidClass = document.getElementsByClassName('is-invalid');
    console.log(invalidClass);
    if (invalidClass.length!==0) {
        Array.from(invalidClass).forEach(elem => elem.classList.remove('is-invalid'));
    }

    let addTestForm = document.getElementById("addTestForm");

    let key = addTestForm.testKey.value;
    let testTitle = addTestForm.testTitle.value;

    let result = /[a-zA-Z0-9]{4,7}/g.test(key);

    if (!result) {
        addTestForm.testKey.classList.add('is-invalid');
    }

    if (testTitle.length > 20) {
        addTestForm.testTitle.classList.add('is-invalid')
    }

    let divButtonBack = document.getElementById('buttonBack');

    if (result && testTitle.length <20) {

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
}

let form;
let testInfoForm;

function showModalWindowEditQuestion(obj) {

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
        insertButtonAddAnswer();
    }
}


function showModalWindowEditTestInfo(obj) {
    let modalBody = document.querySelector(".modal-body #testInfoFormEdit");
    modalBody.innerHTML = "";
    let formTestInfo = document.querySelector("div[id^='testInfo']");
    testInfoForm = formTestInfo.cloneNode(true);
    $("#modalTestInfo").modal('show');
    modalBody.insertAdjacentElement('afterbegin', formTestInfo);
    formTestInfo.querySelector("div[id^='modal'] button").style.display = 'none';
    document.querySelectorAll('#modalTestInfo .modal-body :disabled').forEach(e => e.removeAttribute('disabled'));


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


$('#modalTestInfo').on('hidden.bs.modal', function (e) {
    countInsertedAnswer = 0;

    document.querySelectorAll('#modalTestInfo .modal-body input').forEach(e => e.setAttribute('disabled', 'disabled'));
    document.querySelector('#modalTestInfo .modal-body select').setAttribute('disabled', 'disabled');
    document.querySelector("div[id^='modalTestInfo'] .modal-body button").style.display = 'block';

    let insertPlace = document.getElementById('editTest').insertAdjacentElement('afterbegin', testInfoForm);
    testInfoForm = "";
});


function showModalWindowAddQuestion() {
    $("#modalAddQuestion").modal('show');
    let modalBody = document.querySelector(".modal-body .questionFormEdit");
    modalBody.innerHTML = "";
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
    document.location.href = "/test-system/test?command=preview_test&testId=" + testId;
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

$('#deleteQuestion').on('show.bs.modal', function (e) {
    $(this).find('.btn-ok').attr('onclick', $(e.relatedTarget).data('onclick'));
    $(this).find('.btn-ok').attr('value', $(e.relatedTarget).data('value'));
});

async function deleteQuestion(button) {

    let questionId = button.value;
    let response = await fetch("/test-system/ajax?command=delete_question&questionId=" + questionId, {
        method: 'DELETE',
    });

    if (response.ok) {
        $("#deleteQuestion").modal('hide');

        location.reload();

    } else {

    }
}


async function updateTestInfo(button) {

    let testId = document.getElementById("testId").value;
    let testInfoFormEdit = document.getElementById("testInfoFormEdit");

    let response = await fetch("/test-system/ajax?command=update_test_info&testId=" + testId, {
        method: 'POST',
        body: new FormData(testInfoFormEdit),
    });

    if (response.ok) {
        location.reload();

    } else {

    }

}


async function addTestType() {

    let testTypeTitle = document.getElementById("testTypeTitle").value;
    let formData = new FormData();
    formData.append("testTypeTitle", testTypeTitle);
    let response = await fetch("/test-system/ajax?command=add_test_type", {
        method: 'POST',
        body: formData,
    });

    if (response.ok) {
        $("#addTestType").modal('hide');
        location.reload();

    } else {
        document.getElementById("testTypeTitle").classList.add('is-invalid');
    }

}
