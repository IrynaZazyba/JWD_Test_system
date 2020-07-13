/**
 * Created by ladyi on 01.03.2020.
 */
"use strict"


document.addEventListener("DOMContentLoaded", addChartPie);


function addChartPie() {
    let rightCount = document.getElementById("countRight").value;
    let allCount = document.getElementById("countAll").value;

    let rightCountLocaleName = document.getElementById("countRight").name;
    let wrongCountLocalName = document.getElementById("countAll").name;




    new Chart(document.getElementById("resultChart"),
        {
            "type": "pie", "data": {
                "labels": [rightCountLocaleName, wrongCountLocalName],
                "datasets": [{
                    "label": "Result dataset", "data": [rightCount, allCount-rightCount],
                    "backgroundColor": ["rgb(59, 192, 191)", "rgb(253, 58, 92)"]
                }]
            }
        });
}

