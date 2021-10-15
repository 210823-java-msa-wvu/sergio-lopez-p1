function getBack(){
    if(getCookie("employeeinfo")!=""){
        window.open("http://localhost:8083/Project1/employee.html","_self");
    }
    else if(getCookie("managerinfo")!=""){
        window.open("http://localhost:8083/Project1/manager.html","_self");
    }
    else{
        window.open("http://localhost:8083/Project1/login.html","_self");
    }
}

function badLogin(){
    setTimeout(function () {
        window.open("http://localhost:8083/Project1/login.html","_self");
    }, 3333);
}

function loadLogin(){
    if(getCookie("employeeinfo")!=""){
        window.open("http://localhost:8083/Project1/employee.html","_self");
    }
    else if(getCookie("managerinfo")!=""){
        window.open("http://localhost:8083/Project1/manager.html","_self");
    }
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