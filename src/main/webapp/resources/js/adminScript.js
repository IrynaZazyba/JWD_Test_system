/**
 * Created by ladyi on 01.03.2020.
 */
"use strict"

/* Add calendar to assign test */
// document.getElementById("date").daterangepicker({
//     singleDatePicker: true,
//     locale: {
//         format: 'DD.MM.YYYY'
//     }
// });

/*select*/
let form = document.forms.assign;
let type = form.elements.testTypeId;
type.onchange = changeOption;

async function changeOption() {

    let typeId = type.value;

    let response = await fetch("http://localhost:8080/test-system/ajax?command=get_tests&typeId=" + typeId, {
        method: 'GET',
    });


    if (response.ok) {
        let json = await response.json();
        document.getElementById("testTitle").insertAdjacentHTML('beforeend', generateOptionSelect(json));
    }
}

function generateOptionSelect(json) {
    let options = "";
    for (let key in json.tests) {
        options = options + "<option value=" + json.tests[key].id + ">" + json.tests[key].title + "</option>";
    }
    return options;
}

async function assignUser() {

    if (document.getElementById("alert").style.visibility === 'visible') {
        document.getElementById("existsAssignment").innerHTML = '';
        document.getElementById("alert").style.visibility = 'hidden';
    }

    if (document.getElementById("success").style.visibility === 'visible') {
        document.getElementById("successMessage").innerHTML = '';
        document.getElementById("success").style.visibility = 'hidden';
    }

    let startTest = document.getElementById("assign");
    let response = await fetch("http://localhost:8080/test-system/ajax", {
        method: 'POST',
        body: new FormData(startTest),

    });

    if (response.ok) {
        console.log("ok");
        let json = await response.json();
        if (json.existsAssignment.length != 0) {
            document.getElementById("alert").style.visibility = 'visible';
            document.getElementById("existsAssignment").insertAdjacentHTML('afterbegin', generateAssignmentResultMessage(json.existsAssignment));
        }
        if (json.successAssignment.length != 0) {
            document.getElementById("success").style.visibility = 'visible';
            document.getElementById("successMessage").insertAdjacentHTML('afterbegin', generateAssignmentResultMessage(json.successAssignment));
        }

    }

}

function generateAssignmentResultMessage(users) {
    let message = "";
    for (let key in users) {
        message = message + "<span>" + users[key].firstName + " " + users[key].lastName + "</span></br>";
    }
    return message;


}
