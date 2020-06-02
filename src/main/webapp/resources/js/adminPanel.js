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
        document.getElementById('invalidDeleteMessage').style.display='block';
    }
}
