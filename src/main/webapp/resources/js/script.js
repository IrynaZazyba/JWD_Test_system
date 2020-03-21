/**
 * Created by ladyi on 01.03.2020.
 */
"use strict"
formElem.onsubmit = async (e) => {
    e.preventDefault();

    console.log(formElem);

    let response = await fetch("http://localhost:8080/test-system/ajax", {
        method: 'POST',
        body: new FormData(formElem)
    });

    if (response.ok) {
        let json = await response.json();
        document.getElementById('message').innerHTML = generateMessageDiv(json);

        // setTimeout(function request() {
        //     document.getElementById('edit_user_answer').remove();
        // }, 2500);
    } else {
        alert("Ошибка HTTP: " + response.status);
    }


}


function generateMessageDiv(json) {
    let htmlCode;
    if (json.status === 'ok') {
        htmlCode = "<div class=\"alert alert-success\" id=\"edit_user_answer\" role=\"alert\">" + json.message + "</div>";

    } else {
        htmlCode = "<div class=\"alert alert-danger\" id=\"edit_user_answer\" role=\"alert\">" + json.message + "</div>";

    }
    return htmlCode;
}