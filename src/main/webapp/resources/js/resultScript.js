/**
 * Created by ladyi on 01.03.2020.
 */
"use strict"

let form = document.forms.assign;
let type = form.elements.testTypeId;
type.onchange = changeOption;

async function changeOption() {

    let typeId = type.value;

    let jsOptions = document.querySelectorAll("#testTitle > .js");
    if (jsOptions.length > 0) {
        jsOptions.forEach(element => element.remove());
    }

    let response = await fetch("http://localhost:8080/test-system/ajax?command=get_tests&typeId=" + typeId, {
        method: 'GET',
    });


    if (response.ok) {
        let json = await response.json();
        document.getElementById("testTitle").insertAdjacentHTML('beforeend', generateOptionSelect(json));
    } else {
        //todo сообщение
    }
}

function generateOptionSelect(json) {
    let options = "";
    for (let key in json.tests) {
        options = options + "<option class=\"js\" value=" + json.tests[key].id + ">" + json.tests[key].title + "</option>";
    }
    return options;
}

async function assignUser() {

    if (document.getElementById("alert").style.display === 'block') {
        document.getElementById("existsAssignment").innerHTML = '';
        document.getElementById("alert").style.display = 'none';
    }

    if (document.getElementById("success").style.display === 'block') {
        document.getElementById("successMessage").innerHTML = '';
        document.getElementById("success").style.display = 'none';
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
            document.getElementById("alert").style.display = 'block';
            document.getElementById("existsAssignment").insertAdjacentHTML('afterbegin', generateAssignmentResultMessage(json.existsAssignment));
        }
        if (json.successAssignment.length != 0) {
            document.getElementById("success").style.display = 'block';
            document.getElementById("successMessage").insertAdjacentHTML('afterbegin', generateAssignmentResultMessage(json.successAssignment));
        }
    } else {
        console.log("409");
        document.getElementById('date').classList.add('is-invalid');
    }

}

function generateAssignmentResultMessage(users) {
    let message = "";
    for (let key in users) {
        message = message + "<span>" + users[key].firstName + " " + users[key].lastName + "</span></br>";
    }
    return message;
}


async function showUsersAssignedToTest() {
    let formData = document.getElementById('displayUsers');
    let form = new FormData(formData);

    let response = await fetch("http://localhost:8080/test-system/ajax?command=get_assigned_users", {
        method: 'POST',
        body: form,
    });


    if (response.ok) {
        if (document.getElementById('usersAssignment').style.display === 'none') {
            document.getElementById('usersAssignment').style.display = 'block';
        }
        document.getElementById("jsData").innerHTML = "";

        let json = await response.json();
        document.getElementById("jsData").insertAdjacentHTML('afterbegin', generateUsersAssignmentTable(json.setUsers));
    } else {
        //todo сообщение
    }

}

function generateUsersAssignmentTable(users) {
    let html = "";
    let num = 1;
    console.log(generateDate(users[0].assignment[0].asgmtDate));

    users.forEach(user => {
        user.assignment.forEach(assign => html = html + test(user, assign, num++)
        )
    });

    return html;
}


let formDisplayType = document.forms.displayUsers;
let testType = formDisplayType.elements.type;
testType.onchange = changeOptionFormDisplay;


async function changeOptionFormDisplay() {

    let typeId = testType.value;

    let jsOptions = document.querySelectorAll("#test > .js");
    if (jsOptions.length > 0) {
        jsOptions.forEach(element => element.remove());
    }

    let response = await fetch("http://localhost:8080/test-system/ajax?command=get_tests&typeId=" + typeId, {
        method: 'GET',
    });


    if (response.ok) {
        let json = await response.json();
        document.getElementById("test").insertAdjacentHTML('beforeend', generateOptionSelect(json));
    } else {
        //todo сообщение
    }
}


function generateDate(objDate) {
    return " " + objDate.day + "." + objDate.month + "." + objDate.year;
}

function generateActionButtonEdit(isCompleted, id) {
    let form = "";
    if (!isCompleted) {
        form = "<form id=editAssign-" + id + " onsubmit='editAssignment("+id+");return false;'>" +
            "<input type='hidden' name='assignId' value='" + id + "'>" +
            "<button class=\"btn btn-outline-primary card-btn\">Delete</button></form>"
    } else {
        form = "<button class=\"btn btn-outline-primary card-btn\" disabled >Delete</button>"
    }
    return form;
}

function test(user, assign, num) {
    let html = "";
    html = "<tr id='" + assign.id + "'><th scope = \"row\" >" + num + "</th>" +
        "<td>" + user.firstName + "</td>" +
        "<td>" + user.lastName + "</td>" +
        "<td>" + generateDate(assign.asgmtDate) + "</td>" +
        "<td>" + generateDate(assign.deadline) + "</td>" +
        "<td>" + generateActionButtonEdit(assign.isComplete, assign.id) + "</td>" +
        "</tr>";
    return html;
}

async function editAssignment(id) {

    let form = document.getElementById("editAssign-"+id);
    let editAssign = new FormData(form);
    let assignId = editAssign.get('assignId');

    let response = await fetch("http://localhost:8080/test-system/ajax?command=delete_assignment", {
        method: 'POST',
        body: editAssign,
    });

    if (response.ok) {
        //form.querySelector('button').setAttribute('disabled', 'disabled');
        form.insertAdjacentText('beforebegin','DELETED');
        form.remove();



        document.getElementById(assignId).setAttribute('class', 'table-danger');

    } else {
        //todo сообщение
    }


}
