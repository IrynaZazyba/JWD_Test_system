/**
 * Created by ladyi on 01.03.2020.
 */
"use strict"


$('a').on('click', function (e) {
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




