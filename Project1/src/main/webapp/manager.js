function redirectManagerManage(){
    window.open("http://localhost:8083/Project1/manager_manage.html","_self");
}

function loadManager() {
    let m = JSON.parse(JSON.parse(getCookie("managerinfo")));
    console.log(m);
    let dataSection = document.getElementById('display');
    
    let name = document.createElement('h3');
    name.innerHTML = "Hello," + " " + m.first + " "+ m.last;
    dataSection.appendChild(name);
    
    let details = document.createElement('div');
    details.id = "manager_details";
    details.innerHTML = `manager id: ${m.id}<br>
    management level: ${m.rank}<br>
    reporting to:${getCookie("report_to")}`
    dataSection.appendChild(details);
}

function logManagerOut(){
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