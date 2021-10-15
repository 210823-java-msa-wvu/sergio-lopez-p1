let baseUrl= 'http://localhost:8083/Project1/employee_manage';

function uploadScore(){
    let reimburse_id = document.getElementById("reimbursement_id").value;
    let grade = document.getElementById("score").value;
    //4 steps to build an AJAX call:
    //create XMLHttpRequest object
    let xhr = new XMLHttpRequest();
    
    //set a callback function for the readystatechange event
    xhr.onreadystatechange = callback;
    //open the request
    xhr.open("put",baseUrl+`/upscore/${reimburse_id}`);
    //sent the request
    xhr.send(grade);

    function callback(){
        if(xhr.readyState==4&&xhr.status==403){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-danger"><strong>Not Allowed!</strong>You are not allowed to upload grade for that reimbursement.</div>';
        }
        else if(xhr.readyState==4&&xhr.status==400){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-danger"><strong>Bad Request!</strong>Unable to find the reimbursement</div>';
        }
        else if(xhr.readyState==4&&xhr.status==200){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-success"><strong>Success!</strong></div>';
        }
    }
}

function uploadPresentation(){
    let reimburse_id = document.getElementById("reimbursement_id").value;
    //4 steps to build an AJAX call:
    //create XMLHttpRequest object
    let xhr = new XMLHttpRequest();
    
    //set a callback function for the readystatechange event
    xhr.onreadystatechange = callback;
    //open the request
    xhr.open("put",baseUrl+`/uppresentation/${reimburse_id}`);
    //set up body
    xhr.setRequestHeader("Content-Type", "application/json");
    //sent the request
    xhr.send();

    function callback(){
        if(xhr.readyState==4&&xhr.status==403){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-danger"><strong>Not Allowed!</strong>You are not allowed to upload presentation for that reimbursement.</div>';
        }
        else if(xhr.readyState==4&&xhr.status==400){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-danger"><strong>Bad Request!</strong>Unable to find the reimbursement</div>';
        }
        else if(xhr.readyState==4&&xhr.status==200){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-success"><strong>Success!</strong></div>';
        }
    }
}

function uploadInfo(){
    let reimburse_id = document.getElementById("reimbursement_id").value;
    //4 steps to build an AJAX call:
    //create XMLHttpRequest object
    let xhr = new XMLHttpRequest();
    
    //set a callback function for the readystatechange event
    xhr.onreadystatechange = callback;
    //open the request
    xhr.open("put",baseUrl+`/upinfo/${reimburse_id}`);
    //sent the request
    xhr.send();

    function callback(){
        if(xhr.readyState==4&&xhr.status==403){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-danger"><strong>Not Allowed!</strong>You are not allowed to upload info for that reimbursement.</div>';
        }
        else if(xhr.readyState==4&&xhr.status==400){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-danger"><strong>Bad Request!</strong>Unable to find the reimbursement</div>';
        }
        else if(xhr.readyState==4&&xhr.status==200){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-success"><strong>Success!</strong></div>';
        }
    }
}

function revokeReimbursement(){
    let reimburse_id = document.getElementById("reimbursement_id").value;
    console.log(typeof reimburse_id);
    console.log(reimburse_id);
    //4 steps to build an AJAX call:
    //create XMLHttpRequest object
    let xhr = new XMLHttpRequest();
    
    //set a callback function for the readystatechange event
    xhr.onreadystatechange = callback;
    //open the request
    xhr.open("put",baseUrl+`/revoke/${reimburse_id}`);
    //sent the request
    xhr.send();

    function callback(){
        if(xhr.readyState==4&&xhr.status==403){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-danger"><strong>Not Allowed!</strong>You are not allowed to revoke that reimbursement.</div>';
        }
        else if(xhr.readyState==4&&xhr.status==400){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-danger"><strong>Bad Request!</strong>Unable to find the reimbursement</div>';
        }
        else if(xhr.readyState==4&&xhr.status==200){
            let warn = document.getElementById("warning");
            warn.innerHTML='<div class="alert alert-success"><strong>Success!</strong></div>';
        }
    }
}

function loadEmployee(){
    let emp = JSON.parse(JSON.parse(getCookie("employeeinfo")));
    //console.log(emp);
    let dataSection = document.getElementById('display');
    dataSection.textContent = '';
    let name = document.createElement('h3');
    name.innerHTML = "Hello," + " " + emp.first + " "+emp.last;
    dataSection.appendChild(name);
    let section = document.getElementById("tables");
    section.innerHTML='<div class="spinner-border text-primary"></div>';
    //4 steps to build an AJAX call:
    //create XMLHttpRequest object
    let xhr = new XMLHttpRequest();
    //set a callback function for the readystatechange event
    xhr.onreadystatechange = receiveData;
    //open the request
    xhr.open("get",baseUrl);
    //sent the request
    xhr.send();


    function receiveData(){
        /**
         * Different ready states of XMLHttpRequest Object:
         * 0 unsent
         * 1 opened but not sent
         * 2 headers received
         * 3 loading
         * 4 done
         */
        //before we parse the response and populate data
        //we empty out what's inside data section
        //let section = document.getElementById('display');
        //let data = document.createElement('div');
        //section.appendChild(data);

        //check if readyState is 'done' and HTTP status is 'ok'
        if(xhr.readyState==4&&xhr.status==200){
            let r = xhr.responseText;
            r=JSON.parse(r);
            //console.log(r);

            populateData(r);
        }
    }
}


//resp is a list of json
function populateData(resp){
    // this is where we do dom manipulation
    let section = document.getElementById("tables");
    section.innerHTML = '';
    //create a paragraph element
    let table = document.createElement("TABLE");
    table.className = "table-primary";
    section.appendChild(table);
    table.style="width:100%";
    let head = document.createElement("tr");
    head.innerHTML = "<thead><th>Id</th><th>Applicant</th><th>amount</th><th>Grade Method</th><th>Pass grade</th><th>Grade</th><th>Direct Manager</th><th>Depart Head</th><th>BENCO</th><th>Location</th>"+
    "<th>Start Date</th><th>Submit Date</th><th>Last Sign Date</th><th>Status</th><th>Note</th></thead><tbody>";
    table.appendChild(head);
    for(let i=0;i<resp.length;++i){
        //for each json entry
        //console.log(resp[i]);
        let grade_scheme = resp[i].grade_scheme==5? "Grade":"Presentation";
        let direct_manager = resp[i].level1==-1? "Not signed":resp[i].level1;
        let depart_head = resp[i].level2==-1? "Not signed":resp[i].level2;
        let benco = resp[i].level3==-1? "Not signed":resp[i].level3;
        let start_date = resp[i].start_date;
        let submit_date = resp[i].submit_date;
        let sign_date = resp[i].sign_date == null? "Not signed":resp[i].sign_date;
        let score = resp[i].score==-1? "No score":resp[i].score;
        let status = resp[i].status;
        let entry = document.createElement("tr");
        entry.innerHTML += `<td>${resp[i].id}</td>`;
        entry.innerHTML += `<td>${resp[i].applicant}</td>`;
        entry.innerHTML += `<td>${resp[i].amount}</td>`;
        entry.innerHTML += `<td>${grade_scheme}</td>`;
        entry.innerHTML += `<td>${resp[i].pass_grade}</td>`;
        entry.innerHTML += `<td>${score}</td>`;
        entry.innerHTML += `<td>${direct_manager}</td>`;
        entry.innerHTML += `<td>${depart_head}</td>`;
        entry.innerHTML += `<td>${benco}</td>`;
        entry.innerHTML += `<td>${resp[i].location}</td>`;
        entry.innerHTML += `<td>${start_date.year}-${start_date.monthValue}-${start_date.dayOfMonth}</td>`;
        entry.innerHTML += `<td>${submit_date.year}-${submit_date.monthValue}-${submit_date.dayOfMonth}</td>`;
        if(sign_date=="Not signed") entry.innerHTML += `<td>${sign_date}</td>`;
        else entry.innerHTML += `<td>${sign_date.year}-${sign_date.monthValue}-${sign_date.dayOfMonth}</td>`;
        if(status==0) entry.innerHTML += `<td>pending</td>`;
        else if(status==1) entry.innerHTML += `<td>need additional info from applicant</td>`;
        else if(status==2) entry.innerHTML += `<td>amount changed</td>`;
        else if(status==3) entry.innerHTML += `<td>starting within 14 days</td>`;
        else if(status==4) entry.innerHTML += `<td>declined</td>`;
        else if(status==5) entry.innerHTML += `<td>granted</td>`;
        else if(status==6) entry.innerHTML += `<td>need additional info from manager</td>`;
        else if(status==7) entry.innerHTML += `<td>waiting for grade/presentation upload</td>`;
        else if(status==8) entry.innerHTML += `<td>waiting for final confirmation</td>`;
        entry.innerHTML += `<td>${resp[i].note}</td></tbody>`;
        table.appendChild(entry);
    }
    
}

function redirectBack(){
    window.open("http://localhost:8083/Project1/employee.html","_self");
}

//Cookie Helper functions

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}