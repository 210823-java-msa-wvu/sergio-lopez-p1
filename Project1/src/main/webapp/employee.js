function loadEmployee() {
    let emp = JSON.parse(JSON.parse(getCookie("employeeinfo")));
    //console.log(emp);
    let dataSection = document.getElementById('display');
    
    let name = document.createElement('h3');
    name.innerHTML = "Welcome," + " " + emp.first + " "+emp.last;
    dataSection.appendChild(name);
    let details = document.createElement('div');
    details.id = "emp_details";
    details.innerHTML = ` ID: ${emp.id}<br>
    Pending Reimbursement Amount: ${emp.pending}<br>
    Granted Reimbursement Amount:${emp.awarded}<br>
    Reporting to:${getCookie("report_to")}`
    dataSection.appendChild(details);
}

function reloadEmployee(){
    let details = document.getElementById("emp_details");
    let emp = JSON.parse(JSON.parse(getCookie("employeeinfo")));
    details.innerHTML = `user id: ${emp.id}<br>
    pending amount: ${emp.pending}<br>
    awarded amount:${emp.awarded}<br>
    reporting to:${getCookie("report_to")}`;
}

function redirectEmployeeManage(){
    window.open("http://localhost:8083/Project1/manage_reimbursement.html","_self");
}

function redirectEmployeeSubmit(){
    window.open("http://localhost:8083/Project1/submit_reimbursement.html","_self");
}

function logEmployeeOut(){
    deleteAllCookies();
    window.open("http://localhost:8083/Project1/login.html","_self");
}

//Cookie Helper functions
function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}

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